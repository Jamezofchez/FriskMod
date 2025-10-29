package friskmod.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import friskmod.FriskMod;

public class KillWithKindnessPower extends BasePower {
    public static final String POWER_ID = FriskMod.makeID(KillWithKindnessPower.class.getSimpleName());
    private static final AbstractPower.PowerType TYPE = AbstractPower.PowerType.BUFF;
    private static final boolean TURN_BASED = false;

//    public boolean upgraded;

    //The only thing TURN_BASED controls is the color of the number on the power icon.
    //Turn based powers are white, non-turn based powers are red or green depending on if their amount is positive or negative.
    //For a power to actually decrease/go away on its own they do it themselves.
    //Look at powers that do this like VulnerablePower and DoubleTapPower.
    public KillWithKindnessPower(AbstractCreature owner, int amount) {
        super(POWER_ID, TYPE, TURN_BASED, owner, amount);
//        this.upgraded = upgraded; //currently unused
    }

    @Override
    public AbstractPower makeCopy() {
        return new KillWithKindnessPower(owner, amount);
    }

    public void updateDescription() {
        this.description = String.format(DESCRIPTIONS[0], amount);
    }

}