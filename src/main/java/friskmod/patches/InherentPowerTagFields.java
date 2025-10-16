package friskmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class InherentPowerTagFields {
    @SpirePatch2(clz = AbstractPower.class, method = SpirePatch.CLASS)
    public static class inherentPowerFields {
        public static SpireField<Boolean> inherentPower = new SpireField<>(() -> false);
        public static SpireField<Integer> inherentPowerAmount = new SpireField<>(() -> 0);
    }
    @SpirePatch2(clz = ReducePowerAction.class, method = SpirePatch.CLASS)
    private static class PowerReduceInherent {
        public static SpireField<Boolean> triggeredPowerReduceInherent = new SpireField<>(() -> Boolean.FALSE);
    }
    @SpirePatch2(
            clz = ReducePowerAction.class,
            method = "update"
    )
    private static class ApplyPowerActionUpdatePatch {
        @SpirePrefixPatch
        public static void Prefix(ReducePowerAction __instance, AbstractPower ___powerInstance, String ___powerID){
            if (!InherentPowerTagFields.PowerReduceInherent.triggeredPowerReduceInherent.get(__instance)){
                InherentPowerTagFields.PowerReduceInherent.triggeredPowerReduceInherent.set(__instance, true);
                AbstractPower reduceMe = null;
                if (___powerID != null) {
                    reduceMe = __instance.target.getPower(___powerID);
                } else if (___powerInstance != null) {
                    reduceMe = ___powerInstance;
                }

                if (reduceMe != null) {
                    int inherentPowerAmount = inherentPowerFields.inherentPowerAmount.get(reduceMe);
                    if (inherentPowerAmount > 0) {
                        int amountToSet = Math.max(0, inherentPowerAmount - __instance.amount);
                        inherentPowerFields.inherentPowerAmount.set(reduceMe, amountToSet);
                        if (amountToSet == 0) {
                            inherentPowerFields.inherentPower.set(reduceMe, false);
                        }
                    }
                }
            }
        }

    }
    //I mean its getting removed... shouldnt need a patch
//    @SpirePatch2(clz = RemoveSpecificPowerAction.class, method = SpirePatch.CLASS)
//    private static class PowerRemoveInherent {
//        private static SpireField<Boolean> triggeredPowerRemoveInherent = new SpireField<>(() -> Boolean.FALSE);
//    }
//    @SpirePatch2(
//            clz = RemoveSpecificPowerAction.class,
//            method = "update"
//    )
//    private static class RemoveSpecificPowerActionUpdatePatch {
//        @SpirePrefixPatch
//        public static void Prefix(RemoveSpecificPowerAction __instance, String ___powerToRemove, AbstractPower ___powerInstance){
//            if (!PowerRemoveInherent.triggeredPowerRemoveInherent.get(__instance)){
//                PowerRemoveInherent.triggeredPowerRemoveInherent.set(__instance, true);
//                AbstractPower removeme = ___powerInstance;
//                inherentPowerFields.inherentPowerAmount.set(removeme, 0);
//                inherentPowerFields.inherentPower.set(removeme, false);
//            }
//        }
//
//    }
}
