package friskmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class HackyToughBranchPatch {
    @SpirePatch2(clz = DamageAction.class, method = SpirePatch.CLASS)
    public static class damageActionFields {
        public static SpireField<Boolean> fromToughBranch = new SpireField<>(() -> false);
    }
}
