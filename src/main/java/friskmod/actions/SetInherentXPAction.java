package friskmod.actions; // adjust this to your mod's package

import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import friskmod.cardmods.XPMod;
import friskmod.patches.CardXPFields;

public class SetInherentXPAction extends AbstractGameAction {
    private final AbstractCard card;
    private final int amount;

    public SetInherentXPAction(AbstractCard card, int amount) {
        this.card = card;
        this.amount = amount;
    }

    @Override
    public void update() {
        CardXPFields.flashXPColor(card);
        CardXPFields.XPFields.inherentXP.set(card, amount);
        CardModifierManager.addModifier(card, new XPMod());
        card.initializeDescription();
        isDone = true;
    }
}
