package friskmod.powers;

import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import friskmod.FriskMod;

public class FavouriteNumberPower extends BasePower {
    public static final String POWER_ID = FriskMod.makeID(FavouriteNumberPower.class.getSimpleName());
    private static final PowerType TYPE = PowerType.BUFF;
    private static final boolean TURN_BASED = false;


    //The only thing TURN_BASED controls is the color of the number on the power icon.
    //Turn based powers are white, non-turn based powers are red or green depending on if their amount is positive or negative.
    //For a power to actually decrease/go away on its own they do it themselves.
    //Look at powers that do this like VulnerablePower and DoubleTapPower.
    public FavouriteNumberPower(AbstractCreature owner, int amount) {
        super(POWER_ID, TYPE, TURN_BASED, owner, amount);
    }

    @Override
    public void atEndOfRound() {
//       if (this.amount == 0) {
        addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this.ID));
//       } else {
//            addToBot(new ReducePowerAction(this.owner, this.owner, this.ID, 1));
//       }
    }
    @Override
    public void onSpecificTrigger(){
        addToBot(new ReducePowerAction(this.owner, this.owner, this.ID, 1));
    }

    @Override
    public AbstractPower makeCopy() {
        return new FavouriteNumberPower(owner, amount);
    }

    public void updateDescription() {
        this.description = String.format(DESCRIPTIONS[0], amount);
    }


}