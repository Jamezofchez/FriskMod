package friskmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;

import static friskmod.helper.GrillbysHelper.setLimitCardCost;

public class ResetCardCostLimit extends AbstractGameAction {
    @Override
    public void update() {
        this.isDone = true;
        setLimitCardCost(9999);
    }
}
