package friskmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.powers.PlatedArmorPower;

public class IsPlatedArmourReallyTheOnlyPowerToRequireAnOwnerPatch {
    @SpirePatch2(
            clz = PlatedArmorPower.class,
            method = "updateDescription"
    )
    public static class AbstractPowerRenderIconPatch {
        public static SpireReturn<Void> Prefix(PlatedArmorPower __instance) {
            if (__instance.owner == null) {
                __instance.description = PlatedArmorPower.DESCRIPTIONS[0] + __instance.amount + PlatedArmorPower.DESCRIPTIONS[1];
                return SpireReturn.Return(null);
            }
            return SpireReturn.Continue();
        }
    }
}
