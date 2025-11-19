package friskmod.patches;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import friskmod.cards.VineBloom;
import friskmod.powers.NonAttackPower;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

public class GhostlyPatch {
    @SpirePatch2(clz = AbstractCard.class, method = SpirePatch.CLASS)
    public static class GhostlyCardFields {
        public static SpireField<Boolean> isGhostly = new SpireField<>(() -> false);
    }
    @SpirePatch2(clz = DamageInfo.DamageType.class, method = SpirePatch.CLASS)
    public static class GhostlyDamageTypeFields {
        public static SpireField<Boolean> isGhostly = new SpireField<>(() -> false);
    }
//    @SpirePatch(clz = AbstractCard.class, method = "calculateCardDamage")
//    public static class AbstractCardCalculateCardDamagePatch {
//        @SpirePrefixPatch
//        public static void Prefix(AbstractCard __instance) {
//            SetDamageTypeThorns(__instance);
//        }
//    }
    @SpirePatch(clz = AbstractCard.class, method = "applyPowers")
    public static class AbstractCardApplyPowersPatch {
        @SpirePrefixPatch
        public static void Prefix(AbstractCard __instance) {
            SetDamageTypeThorns(__instance);
        }
    }
    @SpirePatch(clz = AbstractCard.class, method = "applyPowersToBlock")
    public static class AbstractCardApplyPowersToBlockPatch {
        @SpirePostfixPatch
        public static void Postfix(AbstractCard __instance) {
            AbstractPower posspow = AbstractDungeon.player.getPower(NonAttackPower.POWER_ID);
            if (posspow != null){
                __instance.isBlockModified = false;
                __instance.block = __instance.baseBlock;
            }
        }

    }

    private static void SetDamageTypeThorns(AbstractCard __instance) {
        if (AbstractDungeon.player != null && __instance.type == AbstractCard.CardType.ATTACK){
            if(ReflectionHacks.getPrivate(__instance, AbstractCard.class, "damageType") == DamageInfo.DamageType.NORMAL || __instance.damageTypeForTurn == DamageInfo.DamageType.NORMAL) {
//                if (__instance instanceof VineBloom){
//                    makeGhostly(__instance);
//                } else {
                    AbstractPower posspow = AbstractDungeon.player.getPower(NonAttackPower.POWER_ID);
                    if (posspow != null) {
                        makeGhostly(__instance);
                        GhostlyDamageTypeFields.isGhostly.set(__instance.damageTypeForTurn, true);
                    }
//                }
            }
        }
    }

    private static void makeGhostly(AbstractCard __instance) {
        ReflectionHacks.setPrivate(__instance, AbstractCard.class, "damageType", DamageInfo.DamageType.THORNS);
        __instance.damageTypeForTurn = DamageInfo.DamageType.THORNS;
    }

    @SpirePatch(
            clz = AbstractPlayer.class,
            method = "useCard",
            paramtypez = { AbstractCard.class, AbstractMonster.class, int.class }
    )
    public static class UseGhostlyCardPatch {
        // Replace the direct call to c.use(p, m) with a guarded call
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                @Override
                public void edit(MethodCall m) throws CannotCompileException {
                    if (m.getClassName().equals(AbstractCard.class.getName()) && m.getMethodName().equals("use")) {
                        m.replace(
                                "{" +
                                        "    " + (GhostlyPatch.class.getName() + "$UseGhostlyCardPatch.handleGhostly($0)") + ";" +
                                        "    $proceed($$);" +
                                        "}"
                        );
                    }
                }
            };
        }

        public static void handleGhostly(AbstractCard c) {
            if (GhostlyCardFields.isGhostly.get(c)) {
                for (AbstractPower p : AbstractDungeon.player.powers) {
                    if (p instanceof NonAttackPower)
                        ((NonAttackPower) p).ghostlyCardPlayed();
                }
            }
        }
    }
}
