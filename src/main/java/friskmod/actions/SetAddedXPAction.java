package friskmod.actions; // adjust this to your mod's package

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import basemod.helpers.CardModifierManager;
import friskmod.cardmods.XPMod;
import friskmod.patches.CardXPFields; // adjust if needed

public class SetAddedXPAction extends AbstractGameAction {
    private final AbstractCard card;
    private final int amount;

    public SetAddedXPAction(AbstractCard card, int amount) {
        this.card = card;
        this.amount = amount;
    }

    @Override
    public void update() {
        CardXPFields.flashXPColor(card);
        CardXPFields.XPFields.addedXP.set(card, amount);
        CardModifierManager.addModifier(card, new XPMod());  //why?
        card.initializeDescription();
        isDone = true;
    }
}
