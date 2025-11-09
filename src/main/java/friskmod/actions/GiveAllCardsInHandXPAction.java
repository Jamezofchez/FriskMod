package friskmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import friskmod.patches.CardXPFields;
import friskmod.util.Wiz;

import java.util.ArrayList;

public class GiveAllCardsInHandXPAction extends AbstractGameAction {

    private AbstractPlayer p;

    public int XPamount;

    private static final int MAX_REPEATS = 40;
    private static int repeatCount = 0;

    public GiveAllCardsInHandXPAction(int XPamount) {
        this.p = AbstractDungeon.player;
        this.XPamount = XPamount;
        this.duration = this.startDuration = Settings.ACTION_DUR_FAST;
        this.actionType = ActionType.CARD_MANIPULATION;
    }

    public void update() {
        ArrayList<AbstractGameAction> actions = AbstractDungeon.actionManager.actions;
        ++repeatCount;
        if (repeatCount < MAX_REPEATS) {
            if (!actions.isEmpty()) {
                if (!(actions.get(0) instanceof GiveAllCardsInHandXPAction)) {
                    AbstractDungeon.actionManager.addToBottom(new GiveAllCardsInHandXPAction(XPamount));
                    this.isDone = true;
                    return;
                }
            }
        }
        if (this.duration == this.startDuration) {
            if (this.p.hand.isEmpty()) {
                this.isDone = true;
                return;
            }
            for (AbstractCard c : this.p.hand.group) {
                CardXPFields.addXP(c, XPamount);
            }
        }
        tickDuration();
    }
}

/* Location:              C:\Program Files (x86)\Steam\steamapps\common\SlayTheSpire\desktop-1.0.jar!\com\megacrit\cardcrawl\actions\common\GiveAllCardsInHandXPAction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */