package friskmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import friskmod.actions.CustomSFXAction;
import friskmod.powers.RememberKindnessPower;
import friskmod.util.Wiz;

import java.util.Locale;

public class RememberKindnessPatch {
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
                        handleKindness(__instance.target, true);
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

    private static void handleKindness(AbstractCreature target, boolean fromBuff) {
        AbstractPower posspow = AbstractDungeon.player.getPower(RememberKindnessPower.POWER_ID);
        if (posspow != null) {
            posspow.flash();
            int debuff_amount = posspow.amount;
            if (fromBuff) {
                Wiz.atb(new ApplyPowerAction(target, AbstractDungeon.player, new WeakPower(target, debuff_amount, false), debuff_amount));
            }
//            if (!fromBuff && ((RememberKindnessPower) posspow).upgraded) {
//                Wiz.atb(new ApplyPowerAction(target, AbstractDungeon.player, new VulnerablePower(target, debuff_amount, false), debuff_amount));
//            }
        }
    }
}
