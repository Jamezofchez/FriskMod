package friskmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import friskmod.patches.perseverance.PerseveranceFields;
import friskmod.patches.perseverance.PerseverancePatch;

public class PersevereCardAction extends AbstractGameAction {
    private AbstractCard card;
    public PersevereCardAction(AbstractCard card, AbstractCreature target) {
        this.card = card;
        this.target = target;
    }
    @Override
    public void update() {
        if (PerseverancePatch.isRealUnplayable(card) || !card.hasEnoughEnergy()) {
            PerseveranceFields.setIsPerseverable(card, true);
            AbstractDungeon.actionManager.cardQueue.add(new CardQueueItem(this.card, (AbstractMonster) this.target, EnergyPanel.getCurrentEnergy(), false, false));
        }
        this.isDone = true;
    }
}
