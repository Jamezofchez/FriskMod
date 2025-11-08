package friskmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;

import static friskmod.helper.GrillbysHelper.isCardCostAllowed;

public class LowCostGambleAction extends AbstractGameAction {
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("GamblingChipAction");

    public static final String[] TEXT = uiStrings.TEXT;

    public LowCostGambleAction() {
        setValues(AbstractDungeon.player, AbstractDungeon.player, -1);
        this.actionType = AbstractGameAction.ActionType.CARD_MANIPULATION;
    }
    public void update() {
        if (this.duration == 0.5F) {
            AbstractDungeon.handCardSelectScreen.open(TEXT[1], 99, true, true);
            addToBot((AbstractGameAction)new WaitAction(0.25F));
            tickDuration();
            return;
        }
        if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
            if (!AbstractDungeon.handCardSelectScreen.selectedCards.group.isEmpty()) {
                int sizeSub = AbstractDungeon.handCardSelectScreen.selectedCards.group.size();
                for (AbstractCard c : AbstractDungeon.handCardSelectScreen.selectedCards.group) {
                    if (isCardCostAllowed(c)) {
                        AbstractDungeon.player.hand.moveToDiscardPile(c);
                        GameActionManager.incrementDiscard(false);
                        c.triggerOnManualDiscard();
                    } else{
                        --sizeSub;
                    }
                }
                addToTop((AbstractGameAction)new DrawCardAction(AbstractDungeon.player, sizeSub));
            }
            AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
        }
        tickDuration();
    }
}
