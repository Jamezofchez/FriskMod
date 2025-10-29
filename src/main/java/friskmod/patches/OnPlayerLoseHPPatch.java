package friskmod.patches;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.mod.stslib.patches.tempHp.PlayerDamage;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.cards.DamageInfo;
import friskmod.actions.PlayerLoseHPAction;
import friskmod.util.Wiz;
import javassist.CannotCompileException;
import javassist.CtBehavior;

import java.util.ArrayList;


@SpirePatch2(
        clz= AbstractPlayer.class,
        method="damage"
)
public class OnPlayerLoseHPPatch {

    @SpireInsertPatch(
            locator=HPLocator.class,
            localvars={"damageAmount"}
    )
    public static void Insert(AbstractPlayer __instance, int damageAmount, DamageInfo info) {
        if (damageAmount > 0) {
            Wiz.att(new PlayerLoseHPAction(damageAmount, info, false));
        }
    }

    // ModTheSpire searches for a nested class that extends SpireInsertLocator
    // This class will be the Locator for the @SpireInsertPatch
    // When a Locator is not specified, ModTheSpire uses the default behavior for the @SpireInsertPatch
    private static class HPLocator extends SpireInsertLocator {
        // This is the abstract method from SpireInsertLocator that will be used to find the line
        // numbers you want this patch inserted at
        public int[] Locate(CtBehavior ctMethodToPatch) throws PatchingException, CannotCompileException {
            // finalMatcher is the line that we want to insert our patch before -
            Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractPlayer.class, "currentHealth");

            // the `new ArrayList<Matcher>()` specifies the prerequisites before the line can be matched -
            // the LineFinder will search for all the prerequisites in order before it will match the finalMatcher -
            // since we didn't specify any prerequisites here, the LineFinder will simply find the first expression
            // that matches the finalMatcher.
            int[] lines = LineFinder.findInOrder(ctMethodToPatch, new ArrayList<Matcher>(), finalMatcher);
            for (int i = 0; i < lines.length; ++i)
                lines[i] += 1;
            return lines;
        }
    }
}
