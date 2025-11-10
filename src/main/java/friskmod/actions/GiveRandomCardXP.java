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

public class GiveRandomCardXP extends AbstractGameAction {

    private AbstractPlayer p;

    public int XPamount;

    public GiveRandomCardXP(int XPamount) {
        this.p = AbstractDungeon.player;
        this.XPamount = XPamount;
        this.duration = this.startDuration = Settings.ACTION_DUR_FAST;
        this.actionType = ActionType.CARD_MANIPULATION;
    }

    public void update() {
        if (this.duration == this.startDuration) {
            if (this.p.hand.isEmpty()) {
                this.isDone = true;
                return;
            }
            AbstractCard c;
            c = this.p.hand.getRandomCard(true);
            if (XPamount > 0) {
                CardXPFields.addXP(c, XPamount);
            }
        }
        tickDuration();
    }
}

/* Location:              C:\Program Files (x86)\Steam\steamapps\common\SlayTheSpire\desktop-1.0.jar!\com\megacrit\cardcrawl\actions\common\GiveRandomCardXP.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */