package friskmod.cardmods;

import basemod.abstracts.AbstractCardModifier;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import friskmod.util.Wiz;

import static friskmod.FriskMod.makeID;

public class MonochromeMod extends AbstractCardModifier {
    public static String ID = makeID(MonochromeMod.class.getSimpleName());

    public void onUse(AbstractCard card, AbstractCreature target, UseCardAction action) {
        Wiz.atb(new GainEnergyAction(1));
    }

    public AbstractCardModifier makeCopy() {
        return new MonochromeMod();
    }
    public boolean shouldApply(AbstractCard card) {
        return !card.isEthereal;
    }

    public void onInitialApplication(AbstractCard card) {
        card.exhaust = true;
        card.isEthereal = true;
    }

    public void onRemove(AbstractCard card) {
        card.exhaust = false;
        card.isEthereal = false;
    }
}
