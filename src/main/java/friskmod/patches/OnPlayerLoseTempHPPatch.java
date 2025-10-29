package friskmod.patches;

import com.evacipated.cardcrawl.mod.stslib.patches.tempHp.PlayerDamage;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import friskmod.actions.PlayerLoseHPAction;
import friskmod.util.Wiz;
import javassist.CtBehavior;

import java.util.ArrayList;

@SpirePatch2(
        clz = PlayerDamage.class,
        method = "Insert",
        paramtypez = {AbstractCreature.class, DamageInfo.class, int[].class, boolean[].class}
        //AbstractCreature __instance, DamageInfo info, @ByRef int[] damageAmount, @ByRef boolean[] hadBlock
)
public class OnPlayerLoseTempHPPatch {
    @SpireInsertPatch(
            locator= OnPlayerLoseTempHPPatch.TempHPLocator.class,
            localvars={"damageAmount", "temporaryHealth"}
    )
    public static void Insert(Object[] __args, int[] damageAmount, int temporaryHealth, DamageInfo info) {
        AbstractCreature creature = (AbstractCreature) __args[0];
        int tempHPLost = Math.min(temporaryHealth, damageAmount[0]);
        boolean overflow = temporaryHealth < damageAmount[0];
        if (creature == AbstractDungeon.player){
            Wiz.att(new PlayerLoseHPAction(tempHPLost, info, true, overflow));
        }
    }
    private static class TempHPLocator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            Matcher matcher = new Matcher.FieldAccessMatcher(PlayerDamage.class, "hadTempHP");
            int[] allMatches = LineFinder.findAllInOrder(ctMethodToPatch, new ArrayList<>(), matcher);
            // Safety check — make sure at least two matches exist
            if (allMatches.length < 2) {
                throw new RuntimeException("Expected at least two hadTempHP accesses in PlayerDamage.Insert()");
            }
            // We want just before the second one (the one setting it to true)
            return new int[] { allMatches[1] };
        }
    }
}
