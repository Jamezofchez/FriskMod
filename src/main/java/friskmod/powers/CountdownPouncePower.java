package friskmod.powers;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import friskmod.FriskMod;

public class CountdownOffguardPower extends AbstractCountdownPower {
    public static final String POWER_ID = FriskMod.makeID(CountdownOffguardPower.class.getSimpleName());
    private static final PowerType TYPE = PowerType.BUFF;
    private static final boolean TURN_BASED = false;

    public CountdownOffguardPower(AbstractCreature owner, int amount, int countdown) {
        super(POWER_ID, TYPE, TURN_BASED, owner, amount, countdown);
    }

    @Override
    public void onCountdownTrigger() {
        addToBot(new DrawCardAction(owner, amount));
        addToBot(new RemoveSpecificPowerAction(owner, owner, this));
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
