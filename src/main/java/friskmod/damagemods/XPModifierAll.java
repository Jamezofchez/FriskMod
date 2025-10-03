package friskmod.damagemods;

import com.evacipated.cardcrawl.mod.stslib.damagemods.AbstractDamageModifier;
import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import friskmod.actions.SwapXPAction;
import friskmod.patches.CardXPFields;
import friskmod.powers.LV_Enemy;
import friskmod.powers.LV_Hero;
import friskmod.powers.PreventLVLoss;
import friskmod.util.Wiz;
import basemod.ReflectionHacks;

import java.util.Objects;


public class XPModifierAll extends AbstractDamageModifier {
//    @SpirePatch2(clz = AbstractCard.class, method = "use")
//    public static class AbstractCardUsePatch {
//        @SpirePrefixPatch
//        public static void Prefix(AbstractCard __instance, AbstractPlayer p, AbstractMonster m) {
//            AoETrackingField.accumulatedEnemyLV.set(__instance, 0);
//            AoETrackingField.enemiesProcessed.set(__instance, 0);
//            AoETrackingField.totalEnemies.set(__instance, 0);
//        }
//    }
    @SpirePatch2(clz = AbstractCard.class, method = SpirePatch.CLASS)
    public static class AoETrackingField {
        public static SpireField<Integer> accumulatedEnemyLV = new SpireField<>(() -> 0);
        public static SpireField<Integer> enemiesProcessed = new SpireField<>(() -> 0);
        public static SpireField<Integer> totalEnemies = new SpireField<>(() -> 0);
    }

    private final AbstractCard sourceCard;
    private final AbstractCreature p = AbstractDungeon.player;

    public XPModifierAll(AbstractCard sourceCard) {
        this.sourceCard = sourceCard;
    }

    @Override
    public float atDamageGive(float damage, DamageInfo.DamageType type, AbstractCreature target, AbstractCard card) {
        if (sourceCard == null) {
            return damage;
        }
        if (sourceCard.type != AbstractCard.CardType.ATTACK){
            return damage;
        }
        if (type != DamageInfo.DamageType.NORMAL){
            return damage;
        }
        return damage + getResultantXP();
    }

    private int getResultantXP() {
        int xp = CardXPFields.getCardXP(sourceCard);
        AbstractPower LV_Hero_Power = p.getPower(LV_Hero.POWER_ID);
        int XP_from_LV_Hero = 0;
        if (LV_Hero_Power != null) {
            XP_from_LV_Hero = LV_Hero_Power.amount;
        }
        return xp + XP_from_LV_Hero;
    }
    @Override
    public void onLastDamageTakenUpdate(DamageInfo info, int lastDamageTaken, int overkillAmount, AbstractCreature target) {
        if (info.type != DamageInfo.DamageType.NORMAL){
            return;
        }
        if (target != null) {
            if (target.isDying || target.isDead) {
                return;
            }
        }
        if (lastDamageTaken <= 0){
            return;
        }

        int LV_transfer_from = 0;
        if (target != null) {
            LV_transfer_from = getLVFromTarget(target);
        }
        final int final_LV_transfer_from = LV_transfer_from;
        Wiz.att(new AbstractGameAction() {
            @Override
            public void update() {
                int newXP = 0;
                if (sourceCard != null) {
                    newXP = CardXPFields.getCardXP(sourceCard);
                }
                int LV_transfer_to = 0;
                if (newXP > 0) {
                    LV_transfer_to = newXP - 1;
                }
                if (LV_transfer_to == 0 && final_LV_transfer_from == 0) return;
                boolean isMultiDamage = ReflectionHacks.getPrivate(sourceCard, AbstractCard.class, "isMultiDamage");
                if (isMultiDamage){
                    handleAOEDamage(target, LV_transfer_to, final_LV_transfer_from);
                } else{
                    handleSingleTargetDamage(target, LV_transfer_to, final_LV_transfer_from);
                }
                this.isDone = true;

            }
        });
        // Add XP to the card
        if (sourceCard != null) {
            CardXPFields.addXP(sourceCard, consumeLVHeroForXP());
        }
    }

    private void handleAOEDamage(AbstractCreature target, int LV_transfer_to, int final_LV_transfer_from) {
        int LV_from_target = getLVFromTarget(target);
        int cumulativeLV = AoETrackingField.accumulatedEnemyLV.get(sourceCard);
        AoETrackingField.accumulatedEnemyLV.set(sourceCard, cumulativeLV + LV_from_target);

        if (AoETrackingField.totalEnemies.get(sourceCard) == 0) {
            int total = (int) AbstractDungeon.getMonsters().monsters.stream()
                    .filter(m -> !m.isDeadOrEscaped())
                    .count();
            AoETrackingField.totalEnemies.set(sourceCard, total);
        }
        
        int processed = AoETrackingField.enemiesProcessed.get(sourceCard) + 1;
        AoETrackingField.enemiesProcessed.set(sourceCard, processed);
        if (processed >= AoETrackingField.totalEnemies.get(sourceCard)) {
            finalizeAOEXPSwap();
        }
    }

    private void finalizeAOEXPSwap() {
        int totalLV = AoETrackingField.accumulatedEnemyLV.get(sourceCard);
        int cardXP = CardXPFields.getCardXP(sourceCard);

        AoETrackingField.accumulatedEnemyLV.set(sourceCard, 0);
        AoETrackingField.enemiesProcessed.set(sourceCard, 0);
        AoETrackingField.totalEnemies.set(sourceCard, 0);

        Wiz.atb(new AbstractGameAction() {
            @Override
            public void update() {
                // Player receives accumulated LV from all enemies
                // Each enemy gets the card’s current XP value
                for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                    if (!m.isDeadOrEscaped()) {
                        boolean resetCardXP = false;
                        Wiz.atb(new SwapXPAction(sourceCard, p, m, cardXP, totalLV, resetCardXP));
                    }
                }
                this.isDone = true;
            }
        });
    }

    private void handleSingleTargetDamage(AbstractCreature target, int LV_transfer_to, int final_LV_transfer_from) {
        Wiz.att(new SwapXPAction(sourceCard, p, target, LV_transfer_to, final_LV_transfer_from));
    }

    private int getLVFromTarget(AbstractCreature target) {
        int LV_transfer_from = 0;
        AbstractPower LV_enemy_power = target.getPower(LV_Enemy.POWER_ID);
        if (LV_enemy_power != null) {
            int LV_Enemy = LV_enemy_power.amount;
            LV_transfer_from = LV_Enemy - 1;
        }
        return LV_transfer_from;
    }

    private int consumeLVHeroForXP() {
        AbstractPower LV_Hero_Power = p.getPower(LV_Hero.POWER_ID);
        int XP_from_LV_Hero = 0;
        if (LV_Hero_Power != null) {
            XP_from_LV_Hero = LV_Hero_Power.amount;
            AbstractPower targetPower = p.getPower(PreventLVLoss.POWER_ID);
            if (targetPower != null) {
                targetPower.flash();
                targetPower.amount--;
                if (targetPower.amount <= 0) {
                    Wiz.att(new RemoveSpecificPowerAction(p, p, PreventLVLoss.POWER_ID));
                }
            } else {
                Wiz.att(new RemoveSpecificPowerAction(p, p, LV_Hero.POWER_ID));
            }
        }
        return XP_from_LV_Hero;
    }

    @Override
    public AbstractDamageModifier makeCopy() {
        return new XPModifierAll(sourceCard);
    }

    //Overriding this to true tells us that this damage mod is considered part of the card and not just something added on to the card later.
    //If you ever add a damage modifier during the initialization of a card, it should be inherent.
    public boolean isInherent() {
        return true;
    }

}

