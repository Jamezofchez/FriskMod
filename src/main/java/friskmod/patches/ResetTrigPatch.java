package friskmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import friskmod.cards.AbstractCriticalCard;
import javassist.CtBehavior;

@SpirePatch(
        clz = CardGroup.class,
        method = "refreshHandLayout"
)
public class ResetTrigPatch {
    @SpireInsertPatch(
            locator = Locator.class
    )
    public static void Insert(CardGroup __instance) {
        for (AbstractCard c : AbstractDungeon.player.hand.group) {
            resetTrig(c);
        }
        for (AbstractCard c : AbstractDungeon.player.drawPile.group) {
            resetTrig(c);
        }
        for (AbstractCard c : AbstractDungeon.player.discardPile.group) {
            resetTrig(c);
        }
    }

    private static void resetTrig(AbstractCard c) {
        CardXPFields.XPFields.triginherentXP.set(c, 0);
        CardXPFields.XPFields.trigaddedXP.set(c, 0);
        if (c instanceof AbstractCriticalCard) {
            ((AbstractCriticalCard) c).trig_critical = false;
        }
    }

    private static class Locator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractPlayer.class, "relics");
            return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
        }
    }
}
