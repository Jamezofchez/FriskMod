package friskmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import friskmod.powers.KillWithKindnessPower;
import friskmod.util.Wiz;

public class KindnessPatch {
    @SpirePatch2(clz = ApplyPowerAction.class, method = SpirePatch.CLASS)
    public static class PowerKindessTrigger {
        public static SpireField<Boolean> triggeredKindness = new SpireField<>(() -> Boolean.FALSE);
    }
    @SpirePatch2(
            clz = ApplyPowerAction.class,
            method = "update"
    )
    public static class ApplyPowerActionUpdatePatch {
        @SpirePrefixPatch
        public static void Prefix(ApplyPowerAction __instance, AbstractPower ___powerToApply){
            if (!PowerKindessTrigger.triggeredKindness.get(__instance)){
                PowerKindessTrigger.triggeredKindness.set(__instance, true);
                if (__instance.target instanceof AbstractMonster) {
                    if (___powerToApply.type == AbstractPower.PowerType.BUFF) {
                        if (__instance.source == AbstractDungeon.player){
                            handleKillWithKindness(__instance.target, true);
                        }
                    }
                }
            }
        }
    }
//    @SpirePatch2(clz = GainBlockAction.class, method = SpirePatch.CLASS)
//    public static class BlockKindnessTrigger {
//        public static SpireField<Boolean> triggeredKindness = new SpireField<>(() -> Boolean.FALSE);
//    }
//    @SpirePatch2(
//            clz = GainBlockAction.class,
//            method = "update"
//    )
//    public static class ApplyBlockActionUpdatePatch {
//        @SpirePrefixPatch
//        public static void Prefix(GainBlockAction __instance){
//            if (!BlockKindnessTrigger.triggeredKindness.get(__instance)){
//                BlockKindnessTrigger.triggeredKindness.set(__instance, true);
//                if (__instance.target instanceof AbstractMonster) {
//                    if (__instance.amount > 0) {
//                        handleKindness(__instance.target, false);
//                    }
//                }
//            }
//        }
//
//    }

    private static void handleKillWithKindness(AbstractCreature target, boolean fromBuff) {
        AbstractPower posspow = AbstractDungeon.player.getPower(KillWithKindnessPower.POWER_ID);
        if (posspow != null) {
            posspow.flash();
            int debuff_amount = posspow.amount;
            if (fromBuff) {
                Wiz.atb(new ApplyPowerAction(target, AbstractDungeon.player, new VulnerablePower(target, debuff_amount, false), debuff_amount));
            }
//            if (!fromBuff && ((KillWithKindnessPower) posspow).upgraded) {
//                Wiz.atb(new ApplyPowerAction(target, AbstractDungeon.player, new WeakPower(target, debuff_amount, false), debuff_amount));
//            }
        }
    }
}
