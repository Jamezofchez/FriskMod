package friskmod.cardmods;

import basemod.abstracts.AbstractCardModifier;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import friskmod.cards.AbstractCriticalCard;
import friskmod.patches.MonochromePatch;
import friskmod.patches.perseverance.PerseveranceFields;

import static friskmod.FriskMod.makeID;

public class CriticalCheckerMod extends AbstractCardModifier {
    public static String ID = makeID(MonochromeMod.class.getSimpleName());

    public void onUse(AbstractCard card, AbstractCreature target, UseCardAction action) {
        if (AbstractCriticalCard.isCriticalPos(card) || PerseveranceFields.perseverePlayed.get(card)) {
            MonochromePatch.MonochromeFields.wasCriticalPlayed.set(card, true);
        } else{
            MonochromePatch.MonochromeFields.wasCriticalPlayed.set(card, false);
        }
    }
    @Override
    public AbstractCardModifier makeCopy() {
        return new CriticalCheckerMod();
    }
    @Override
    public boolean isInherent(AbstractCard card) {
        return true;
    }
}
