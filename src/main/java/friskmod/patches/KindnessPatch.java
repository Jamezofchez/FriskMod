package friskmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import friskmod.powers.KillWithKindnessPower;
import friskmod.powers.RememberKindnessPower;
import friskmod.util.Wiz;

public class KindnessPatch {
    @SpirePatch2(clz = ApplyPowerAction.class, method = SpirePatch.CLASS)
    public static class KillWithKindnessTrigger {
        public static SpireField<Boolean> triggeredKillWithKindness = new SpireField<>(() -> Boolean.FALSE);
    }

    @SpirePatch2(clz = GainBlockAction.class, method = SpirePatch.CLASS)
    public static class RememberKindnessTrigger {
        public static SpireField<Boolean> triggeredRememberKindness = new SpireField<>(() -> Boolean.FALSE);
    }

    @SpirePatch2(
            clz = ApplyPowerAction.class,
            method = "update"
    )
    public static class ApplyPowerActionUpdatePatch {
        @SpirePrefixPatch
        public static void Prefix(ApplyPowerAction __instance, AbstractPower ___powerToApply){
            if (!KillWithKindnessTrigger.triggeredKillWithKindness.get(__instance)){
                KillWithKindnessTrigger.triggeredKillWithKindness.set(__instance, true);
                if (__instance.target instanceof AbstractMonster) {
                    if (___powerToApply.type == AbstractPower.PowerType.BUFF) {
                        if (__instance.source == AbstractDungeon.player){
                            handleKillWithKindness(__instance.target);
                        }
                    }
                }
            }
        }
    }
    @SpirePatch2(
            clz = GainBlockAction.class,
            method = "update"
    )
    public static class ApplyBlockActionUpdatePatch {
        @SpirePrefixPatch
        public static void Prefix(GainBlockAction __instance){
            if (!RememberKindnessTrigger.triggeredRememberKindness.get(__instance)){
                RememberKindnessTrigger.triggeredRememberKindness.set(__instance, true);
                if (__instance.target instanceof AbstractMonster) {
                    if (__instance.amount > 0) {
                        handleRememberKindness(__instance.target);
                    }
                }
            }
        }

        private static void handleRememberKindness(AbstractCreature target) {
            AbstractPower posspow = AbstractDungeon.player.getPower(RememberKindnessPower.POWER_ID);
            if (posspow != null) {
                posspow.flash();
                int debuff_amount = posspow.amount;
                Wiz.atb(new ApplyPowerAction(target, AbstractDungeon.player, new VulnerablePower(target, debuff_amount, false), debuff_amount));
            }
        }

    }

    private static void handleKillWithKindness(AbstractCreature target) {
        AbstractPower posspow = AbstractDungeon.player.getPower(KillWithKindnessPower.POWER_ID);
        if (posspow != null) {
            posspow.flash();
            int debuff_amount = posspow.amount;
            Wiz.atb(new ApplyPowerAction(target, AbstractDungeon.player, new VulnerablePower(target, debuff_amount, false), debuff_amount));
        }
    }
}
