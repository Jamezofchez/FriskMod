package friskmod.patches;

import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import friskmod.powers.Vitality;

public class VitalityPatch {
    @SpirePatch2(
            clz = AddTemporaryHPAction.class,
            method = "update"
    )
    public static class AddTemporaryHPActionUpdatePatch {
        @SpirePrefixPatch
        public static SpireReturn<Void> Prefix(AbstractGameAction __instance, float ___duration) {
            AbstractCreature target = __instance.target;
            if (___duration != 0.5F){
                return SpireReturn.Continue();
            }
            if (target == null){
                return SpireReturn.Continue();
            }
            AbstractPower posspow = target.getPower(Vitality.POWER_ID);
            if (posspow == null){
                return SpireReturn.Continue();
            }
            if (posspow.amount <= 0){
                return SpireReturn.Continue();
            }
            __instance.amount = posspow.amount + __instance.amount;
            return SpireReturn.Continue();
        }
    }
}
