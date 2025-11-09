package friskmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import java.util.ArrayList;

import static friskmod.helper.GrillbysHelper.isCardCostAllowed;

public class LowCostMemoriesAction extends AbstractGameAction {
    public static final String[] TEXT = (CardCrawlGame.languagePack.getUIString("BetterToHandAction")).TEXT;

    private AbstractPlayer player;

    private int numberOfCards = 1;

    private int newCost = 0;

    private boolean setCost;

    public LowCostMemoriesAction() {
        this.actionType = AbstractGameAction.ActionType.CARD_MANIPULATION;
        this.duration = this.startDuration = Settings.ACTION_DUR_FAST;
        this.player = AbstractDungeon.player;
        this.setCost = true;
    }

    public void update() {
        if (this.duration == this.startDuration) {
            if (this.player.discardPile.isEmpty() || this.numberOfCards <= 0) {
                this.isDone = true;
                return;
            }
            if (this.player.discardPile.size() <= this.numberOfCards) {
                ArrayList<AbstractCard> cardsToMove = new ArrayList<>();
                for (AbstractCard c : this.player.discardPile.group)
                    cardsToMove.add(c);
                for (AbstractCard c : cardsToMove) {
                    if (isCardCostAllowed(c)) {
                        if (this.player.hand.size() < 10) {
                            this.player.hand.addToHand(c);
                            if (this.setCost)
                                c.setCostForTurn(this.newCost);
                            this.player.discardPile.removeCard(c);
                        }
                        c.lighten(false);
                        c.applyPowers();
                    }
                }
                this.isDone = true;
                return;
            }
            if (this.numberOfCards == 1) {
                AbstractDungeon.gridSelectScreen.open(this.player.discardPile, this.numberOfCards, TEXT[0], false);
            } else {
                AbstractDungeon.gridSelectScreen.open(this.player.discardPile, this.numberOfCards, TEXT[1] + this.numberOfCards + TEXT[2], false);
            }
            tickDuration();
            return;
        }
        if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            for (AbstractCard c : AbstractDungeon.gridSelectScreen.selectedCards) {
                if (isCardCostAllowed(c)) {
                    if (this.player.hand.size() < 10) {
                        this.player.hand.addToHand(c);
                        if (this.setCost)
                            c.setCostForTurn(this.newCost);
                        this.player.discardPile.removeCard(c);
                    }
                    c.lighten(false);
                    c.unhover();
                    c.applyPowers();
                }
            }
            for (AbstractCard c : this.player.discardPile.group) {
                c.unhover();
                c.target_x = CardGroup.DISCARD_PILE_X;
                c.target_y = 0.0F;
            }
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
            AbstractDungeon.player.hand.refreshHandLayout();
        }
        tickDuration();
        if (this.isDone)
            for (AbstractCard c : this.player.hand.group)
                c.applyPowers();
    }
}


/* Location:              C:\Program Files (x86)\Steam\steamapps\common\SlayTheSpire\desktop-1.0.jar!\com\megacrit\cardcrawl\actions\common\LowCostMemoriesAction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */