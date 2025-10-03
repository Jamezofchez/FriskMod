package friskmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.core.AbstractCreature;
import friskmod.actions.StealAllBlockAction;
import friskmod.actions.StealPowerAction;

public class StealBlockPatch {
    @SpirePatch2(
            clz = AbstractCreature.class,
            method = "renderBlockIconAndValue"
    )
    public static class AbstractCreatureRenderBlockIconAndValue {
        @SpirePrefixPatch
        public static void Prefix(AbstractCreature __instance, @ByRef float[] x, @ByRef float[] y) {
            if (StealAllBlockAction.activatedInstance != null && StealAllBlockAction.activatedInstance.targetMonsters.contains(__instance)) {
                StealAllBlockAction.activatedInstance.calcPosition(x, y);
            }
        }
    }
}
