package friskmod.actions;

import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import friskmod.patches.CardXPFields;

public class SeekHighestXPAction extends AbstractGameAction {

    private AbstractPlayer p;

    public SeekHighestXPAction() {
        this.p = AbstractDungeon.player;
        this.actionType = AbstractGameAction.ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_MED;
    }



    public void update() {
        CardGroup tmp = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        boolean fromDrawPile = true;
        for (AbstractCard c : this.p.drawPile.group) {
            if (addIfHighestXP(tmp, c)){
                fromDrawPile = true;
            }
        }
        for (AbstractCard c : this.p.discardPile.group) {
            if (addIfHighestXP(tmp, c)){
                fromDrawPile = false;
            }
        }
        if (tmp.isEmpty()) {
            this.isDone = true;
            return;
        }
        tmp.shuffle();
        AbstractCard card = tmp.getTopCard();
        if (this.p.hand.size() == BaseMod.MAX_HAND_SIZE) {
            this.p.drawPile.moveToDiscardPile(card);
            this.p.createHandIsFullDialog();
        } else {
            card.unhover();
            card.lighten(true);
            card.setAngle(0.0F);
            card.drawScale = 0.12F;
            card.targetDrawScale = 0.75F;
            card.current_x = CardGroup.DRAW_PILE_X;
            card.current_y = CardGroup.DRAW_PILE_Y;
            if (fromDrawPile) {
                this.p.drawPile.removeCard(card);
            } else{
                this.p.discardPile.removeCard(card);
            }
            AbstractDungeon.player.hand.addToTop(card);
            AbstractDungeon.player.hand.refreshHandLayout();
            AbstractDungeon.player.hand.applyPowers();
        }
        this.isDone = true;
    }

    private boolean addIfHighestXP(CardGroup tmp, AbstractCard c) {
        if (tmp.isEmpty()){
            tmp.addToTop(c);
            return true;
        }
        int highestXP = CardXPFields.getCardXP(tmp.getTopCard());
        if (highestXP < CardXPFields.getCardXP(c)) {
            tmp.clear();
            tmp.addToTop(c);
            return true;
        } else if (highestXP == CardXPFields.getCardXP(c)) {
            tmp.addToRandomSpot(c);
            return true;
        }
        return false;
    }
}


/* Location:              C:\Program Files (x86)\Steam\steamapps\common\SlayTheSpire\desktop-1.0.jar!\com\megacrit\cardcrawl\actions\defect\SeekHighestXPAction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */