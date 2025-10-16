package friskmod.damagemods;

import com.evacipated.cardcrawl.mod.stslib.damagemods.AbstractDamageModifier;
import com.evacipated.cardcrawl.mod.stslib.damagemods.BindingHelper;
import com.evacipated.cardcrawl.mod.stslib.damagemods.DamageModContainer;
import com.evacipated.cardcrawl.mod.stslib.damagemods.DamageModifierManager;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import friskmod.actions.*;
import friskmod.patches.CardXPFields;
import friskmod.powers.LV_Enemy;
import friskmod.powers.LV_Hero;
import friskmod.powers.PreventLVLoss;
import friskmod.powers.RememberBraveryPower;
import friskmod.util.Wiz;
import basemod.ReflectionHacks;


public class XPModifierAll extends AbstractDamageModifier {
//    @SpirePatch2(clz = AbstractCard.class, method = "use")
//    public static class AbstractCardUsePatch {
//        @SpirePrefixPatch
//        public static void Prefix(AbstractCard __instance, AbstractPlayer p, AbstractMonster m) {
//            ExtraXPInfo.accumulatedEnemyLV.set(__instance, 0);
//            ExtraXPInfo.enemiesProcessed.set(__instance, 0);
//            ExtraXPInfo.totalEnemies.set(__instance, 0);
//        }
//    }
    public static AbstractCard activeCard = null;

    public static void setActiveCard(AbstractCard card) {
//        activeCard = card;
    }

//    // Patch DamageAction constructor
//    @SpirePatch2(clz = DamageAction.class,
//            method = SpirePatch.CONSTRUCTOR,
//            paramtypez = {AbstractCreature.class, DamageInfo.class, AbstractGameAction.AttackEffect.class}
//    )
//    public static class DamageActionPatch {
//        @SpirePostfixPatch
//        public static void Postfix(DamageAction __instance, DamageInfo info) {
//            if (info.owner == AbstractDungeon.player && activeCard != null && info.type == DamageInfo.DamageType.NORMAL && activeCard.type == AbstractCard.CardType.ATTACK) {
//                boolean hasXPMod = DamageModifierManager.getDamageMods(info).stream().anyMatch(d -> d instanceof XPModifierAll);
//                if (!hasXPMod){
//                    DamageModContainer XPContainer = new DamageModContainer(AbstractDungeon.player, new XPModifierAll(activeCard));
//                    BindingHelper.bindAction(XPContainer, __instance);
//                }
//            }
//        }
//    }
//
//    // Patch DamageAllEnemiesAction constructor
//    @SpirePatch2(clz = DamageAllEnemiesAction.class,
//            method = SpirePatch.CONSTRUCTOR,
//            paramtypez = {
//                    AbstractCreature.class,
//                    int[].class,
//                    DamageInfo.DamageType.class,
//                    AbstractGameAction.AttackEffect.class
//            }
//    )
//    public static class DamageAllEnemiesActionPatch {
//        @SpirePostfixPatch
//        public static void Postfix(DamageAllEnemiesAction __instance, DamageInfo.DamageType type) {
//            if (__instance.source == AbstractDungeon.player && activeCard != null && type == DamageInfo.DamageType.NORMAL && activeCard.type == AbstractCard.CardType.ATTACK) {
//                DamageModContainer XPContainer = new DamageModContainer(AbstractDungeon.player, new XPModifierAll(activeCard));
//                BindingHelper.bindAction(XPContainer, __instance);
//            }
//        }
//    }

    @SpirePatch2(clz = AbstractCard.class, method = SpirePatch.CLASS, paramtypez = {AbstractPlayer.class, AbstractMonster.class})
    public static class ExtraXPInfo {
        public static SpireField<Integer> originalCardXP = new SpireField<>(() -> -1);
        public static SpireField<Integer> enemiesProcessed = new SpireField<>(() -> 0);
        public static SpireField<Integer> totalEnemies = new SpireField<>(() -> -1);
    }

    public final AbstractCard sourceCard;
    private final AbstractCreature p = AbstractDungeon.player;

    public XPModifierAll(AbstractCard sourceCard) {
        this.priority = -999;
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
    public int onAttackToChangeDamage(DamageInfo info, int damageAmount, AbstractCreature target) {
        if (target != null) {
            if (target.isDying || target.isDead) {
                return damageAmount;
            }
        } else{
            return damageAmount;
        }
        if (damageAmount <= 0){ //blocked damage
            return damageAmount;
        }
        if (info.type != DamageInfo.DamageType.NORMAL){
            return damageAmount;
        }
        //RECOMPUTE DAMAGE BASED ON CURRENT XP - MAY HAVE CHANGED ON MULTI-HIT
        sourceCard.applyPowers();
        if (target instanceof AbstractMonster) {
            sourceCard.calculateCardDamage((AbstractMonster) target);
        }
        sourceCard.initializeDescription();
        int newDamageAmount = sourceCard.damage;
        int LV_transfer_from = getLVFromTarget(target);
        Wiz.att(new AfterLVHeroConsumedAction(this, target, LV_transfer_from));
        // Add XP to the card
        if (sourceCard != null) {
            CardXPFields.addXP(sourceCard, consumeLVHeroForXP());
        }
        Wiz.atb(new AfterXPConsumedAction());
        return newDamageAmount;
    }
//    @Override
//    public void onLastDamageTakenUpdate(DamageInfo info, int lastDamageTaken, int overkillAmount, AbstractCreature target) {
        //        if (info.type != DamageInfo.DamageType.NORMAL){
//            return;
//        }
//        if (target != null) {
//            if (target.isDying || target.isDead) {
//                return;
//            }
//        }
//        if (lastDamageTaken <= 0){
//            return;
//        }
//
//        int LV_transfer_from = 0;
//        if (target != null) {
//            LV_transfer_from = getLVFromTarget(target);
//        }
//        final int fLV_transfer_from = LV_transfer_from;
//        final AbstractCreature ftarget = target;
//        Wiz.att(new AbstractGameAction() {
//            @Override
//            public void update() {
//                this.isDone = true;
//                int newXP = 0;
//                if (sourceCard != null) {
//                    newXP = CardXPFields.getCardXP(sourceCard);
//                }
//                int LV_transfer_to = 0;
//                if (newXP > 0) {
//                    LV_transfer_to = newXP - 1;
//                }
//                boolean isMultiDamage = ReflectionHacks.getPrivate(sourceCard, AbstractCard.class, "isMultiDamage");
//                if (isMultiDamage){
//                    handleAOEDamage(ftarget, LV_transfer_to, fLV_transfer_from);
//                } else{
//                    if (LV_transfer_to == 0 && fLV_transfer_from == 0) return;
//                    handleSingleTargetDamage(ftarget, LV_transfer_to, fLV_transfer_from);
//                }
//
//            }
//        });
//        // Add XP to the card
//        if (sourceCard != null) {
//            CardXPFields.addXP(sourceCard, consumeLVHeroForXP());
//        }
//    }

    public void handleAOEDamage(AbstractCreature target, int LV_transfer_to, int final_LV_transfer_from) {
        if (ExtraXPInfo.originalCardXP.get(sourceCard) == -1) {
            int originalCardXP = CardXPFields.getCardXP(sourceCard);
            ExtraXPInfo.originalCardXP.set(sourceCard, originalCardXP);
        }
        if (ExtraXPInfo.totalEnemies.get(sourceCard) == -1) {
            int total = (int) AbstractDungeon.getMonsters().monsters.stream()
                    .filter(m -> !m.isDeadOrEscaped())
                    .count();
            ExtraXPInfo.totalEnemies.set(sourceCard, total);
        }

        int processed = ExtraXPInfo.enemiesProcessed.get(sourceCard) + 1;
        ExtraXPInfo.enemiesProcessed.set(sourceCard, processed);
        if (processed >= ExtraXPInfo.totalEnemies.get(sourceCard)) {
            finalizeAOEXPSwap();
        }
    }

    private void finalizeAOEXPSwap() {
        int originalCardXP = ExtraXPInfo.originalCardXP.get(sourceCard);

        ExtraXPInfo.originalCardXP.set(sourceCard, -1);
        ExtraXPInfo.enemiesProcessed.set(sourceCard, 0);
        ExtraXPInfo.totalEnemies.set(sourceCard, -1);
        CardXPFields.XPFields.addedXP.set(sourceCard, 0); //clear stale
        for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
//            if (!m.isDeadOrEscaped()) {
                Wiz.att(new SwapXPAction(sourceCard, p, m, Math.max(0,originalCardXP-1), getLVFromTarget(m), false));
//            }
        }
    }

    public void handleSingleTargetDamage(AbstractCreature target, int LV_transfer_to, int final_LV_transfer_from) {
        CardXPFields.XPFields.addedXP.set(sourceCard, 0); //clear stale
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
            AbstractPower targetPower;
            targetPower = p.getPower(RememberBraveryPower.POWER_ID);
            if (targetPower != null) {
                targetPower.flash();
                for (int i = 0; i < targetPower.amount; i++) {
                    Wiz.att(new GiveRandomCardXP(XP_from_LV_Hero));
                }
            }
            targetPower = p.getPower(PreventLVLoss.POWER_ID);
            if (targetPower != null) {
                targetPower.flash();
//                targetPower.amount--;
//                if (targetPower.amount <= 0) {
//                    Wiz.att(new RemoveSpecificPowerAction(p, p, PreventLVLoss.POWER_ID));
//                }
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

