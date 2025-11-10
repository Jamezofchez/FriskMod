//package friskmod.patches;
//
//import com.evacipated.cardcrawl.modthespire.lib.SpireField;
//import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
//import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
//import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
//import com.megacrit.cardcrawl.actions.common.GainBlockAction;
//import com.megacrit.cardcrawl.monsters.AbstractMonster;
//import com.megacrit.cardcrawl.powers.AbstractPower;
//import com.megacrit.cardcrawl.powers.FrailPower;
//
//public class FrailOnEnemiesPatch {
//    @SpirePatch2(clz = GainBlockAction.class, method = SpirePatch.CLASS)
//    public static class FrailTrigger {
//        public static SpireField<Boolean> triggeredFrail = new SpireField<>(() -> Boolean.FALSE);
//    }
//    public static class ApplyBlockActionUpdatePatch {
//        @SpirePrefixPatch
//        public static void Prefix(GainBlockAction __instance) {
//            if (!FrailTrigger.triggeredFrail.get(__instance)) {
//                FrailTrigger.triggeredFrail.set(__instance, true);
//                if (__instance.target instanceof AbstractMonster) {
//                    if (__instance.amount > 0) {
//                        AbstractPower posspow = __instance.target.getPower(FrailPower.POWER_ID);
//                        if (posspow != null) {
//                            applyFrail(__instance);
//                        }
//                    }
//                }
//            }
//        }
//    }
//
//    private static void applyFrail(GainBlockAction __instance) {
//        __instance.amount = (int) (__instance.amount * 0.75F);
//    }
//
//}
