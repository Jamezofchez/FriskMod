//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package friskmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.AbstractCard.CardType;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import friskmod.util.Wiz;

public class ExhaustAllNonAttackForXPAction extends AbstractGameAction {
    private float startingDuration;
    int XP_amount;

    public ExhaustAllNonAttackForXPAction(int XP_amount) {
        this.actionType = ActionType.WAIT;
        this.startingDuration = Settings.ACTION_DUR_FAST;
        this.duration = this.startingDuration;
        this.XP_amount = XP_amount;
    }

    public void update() {
        if (this.duration == this.startingDuration) {
            int exhaustCount = 0;
            for(AbstractCard c : AbstractDungeon.player.hand.group) {
                if (c.type != CardType.ATTACK) {
                    this.addToTop(new ExhaustSpecificCardAction(c, AbstractDungeon.player.hand));
                    ++exhaustCount;
                }
            }

            this.isDone = true;
            if (AbstractDungeon.player.exhaustPile.size() >= 20) {
                UnlockTracker.unlockAchievement("THE_PACT");
            }
            Wiz.atb(new GiveAllCardsInHandXPAction(exhaustCount*XP_amount));
        }

    }
}
