package friskmod.powers;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import friskmod.FriskMod;

public class CountdownDrawPower extends AbstractCountdownPower {
    public static final String POWER_ID = FriskMod.makeID(CountdownDrawPower.class.getSimpleName());
    private static final PowerType TYPE = PowerType.BUFF;
    private static final boolean TURN_BASED = false;

    public CountdownDrawPower(AbstractCreature owner, int amount, int countdown) {
        super(POWER_ID, TYPE, TURN_BASED, owner, amount, countdown);
    }

    @Override
    public void onCountdownTrigger(boolean expire) {
        addToBot(new DrawCardAction(owner, amount));
        super.onCountdownTrigger(expire);
    }

    @Override
    public AbstractPower makeCopy() {
        return new CountdownDrawPower(owner, amount, amount2);
    }

    @Override
    public void updateDescription() {
        String baseDescription = DESCRIPTIONS[1];
        if (amount == 1){
            baseDescription = DESCRIPTIONS[0];
        }
        this.description = String.format(baseDescription, amount);
    }
}
