//package friskmod.patches;
//
//import basemod.ReflectionHacks;
//import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
//import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
//import com.megacrit.cardcrawl.actions.common.DamageAction;
//import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
//import com.megacrit.cardcrawl.cards.DamageInfo;
//import com.megacrit.cardcrawl.core.AbstractCreature;
//import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
//import com.megacrit.cardcrawl.powers.AbstractPower;
//import friskmod.powers.AbsolutePowerPower;
//import friskmod.powers.PreventLVLoss;
//import friskmod.util.Wiz;
//
//
//import java.util.Objects;
//
//public class AbsolutePowerPowerPatch
//{
//    @SpirePatch2(clz = DamageAction.class, method = "update")
//    public static class DamageActionUpdate {
//        @SpirePostfixPatch
//        public static void Postfix(DamageAction __instance) {
//            if (__instance.isDone) {
//                DamageInfo info = ReflectionHacks.getPrivate(__instance, DamageAction.class, "info"); //ReflectionHacks needed as damageType isn't accurate
//                AbstractCreature source = info.owner;
//                DamageInfo.DamageType damageType = info.type;
//                if (source == AbstractDungeon.player && damageType == DamageInfo.DamageType.NORMAL) {
//                    AbstractPower pow = AbstractDungeon.player.getPower(AbsolutePowerPower.POWER_ID);
//                    if (pow != null) {
//                        int energyGain = pow.amount;
//                        int damageDealt = __instance.amount;
//                        if (damageDealt % 10 == 9){
//                            Wiz.atb(new GainEnergyAction(energyGain));
//                            pow.flash();
//                        }
//                    }
//
//                }
//            }
//        }
//    }
//}
