package friskmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import static friskmod.helper.GrillbysHelper.isCardCostAllowed;

public class LowCostUpgradeAction extends AbstractGameAction {

    public LowCostUpgradeAction() {
        this.actionType = AbstractGameAction.ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_FAST;
    }

    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            for (AbstractCard c : AbstractDungeon.player.hand.group) {
                if (c.canUpgrade()) {
                    if (isCardCostAllowed(c)) {
                        c.upgrade();
                        c.superFlash();
                        c.applyPowers();
                    }
                }
            }
            this.isDone = true;
        }
    }
}