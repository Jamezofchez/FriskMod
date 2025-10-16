package friskmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import friskmod.powers.PreventLVLoss;

public class AfterXPConsumedAction extends AbstractGameAction {
    public AfterXPConsumedAction() {
        this.duration = Settings.ACTION_DUR_FAST;
    }

    @Override
    public void update() {
        publishOnXPConsumed();
        isDone = true;
    }
    private void publishOnXPConsumed() {
        for (AbstractPower p : AbstractDungeon.player.powers) {
            if (p instanceof PreventLVLoss)
                ((PreventLVLoss) p).afterXPConsumed();
        }
    }
}
