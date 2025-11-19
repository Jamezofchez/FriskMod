package friskmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;

import static friskmod.helper.GrillbysHelper.setLimitCardCost;

public class ResetCardCostLimit extends AbstractGameAction {

    private static final int MAX_REPEATS = 40;
    private static int repeatCount = 0;

    @Override
    public void update() {
        this.isDone = true;
        ArrayList<AbstractGameAction> actions = AbstractDungeon.actionManager.actions;
        ++repeatCount;
        if (repeatCount < MAX_REPEATS) {
            if (!actions.isEmpty()) {
                if (!(actions.get(0) instanceof ResetCardCostLimit)) {
                    AbstractDungeon.actionManager.addToBottom(new ResetCardCostLimit());
                    return;
                }
            }
        }
        repeatCount = 0;
        setLimitCardCost(9999);
    }
}
