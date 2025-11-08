package friskmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;

import static friskmod.helper.GrillbysHelper.isCardCostAllowed;

public class LowCostExhaustAction extends AbstractGameAction {
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("ExhaustAction");

    public static final String[] TEXT = uiStrings.TEXT;

    private AbstractPlayer p;

    public static int numExhausted;

    public LowCostExhaustAction() {
        this.p = AbstractDungeon.player;
        this.amount = 99;
        this.duration = this.startDuration = Settings.ACTION_DUR_FAST;
        this.actionType = AbstractGameAction.ActionType.EXHAUST;
    }

    public void update() {
        if (this.duration == this.startDuration) {
            if (this.p.hand.size() == 0) {
                this.isDone = true;
                return;
            }
            numExhausted = this.amount;
            AbstractDungeon.handCardSelectScreen.open(TEXT[0], this.amount, true, true);
            tickDuration();
            return;
        }
        if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
            for (AbstractCard c : AbstractDungeon.handCardSelectScreen.selectedCards.group) {
                if (isCardCostAllowed(c)) {
                    this.p.hand.moveToExhaustPile(c);
                }
            }
            CardCrawlGame.dungeon.checkForPactAchievement();
            AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
        }
        tickDuration();
    }
}