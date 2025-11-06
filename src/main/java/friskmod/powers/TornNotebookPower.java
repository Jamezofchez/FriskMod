package friskmod.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import friskmod.FriskMod;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import friskmod.util.interfaces.LosePlayerHPInterface;

public class TornNotebookPower extends BasePower implements LosePlayerHPInterface {
    public static final String POWER_ID = FriskMod.makeID(TornNotebookPower.class.getSimpleName());
    private static final AbstractPower.PowerType TYPE = AbstractPower.PowerType.BUFF;
    private static final boolean TURN_BASED = false;

    //The only thing TURN_BASED controls is the color of the number on the power icon.
    //Turn based powers are white, non-turn based powers are red or green depending on if their amount is positive or negative.
    //For a power to actually decrease/go away on its own they do it themselves.
    //Look at powers that do this like VulnerablePower and DoubleTapPower.
    public TornNotebookPower(AbstractCreature owner, int amount) {
        super(POWER_ID, TYPE, TURN_BASED, owner, amount);
    }

    @Override
    public AbstractPower makeCopy() {
        return new TornNotebookPower(owner, amount);
    }

//    @Override
//    public int onLoseHp(int damageAmount) {
//
//    }

    public void updateDescription() {
        this.description = String.format(DESCRIPTIONS[0], amount);
    }

    @Override
    public void onPlayerLoseHP(int amount, boolean tempHP, boolean overflow) {
        if (tempHP && overflow){
            return;
        }
        if (amount > 0) {
            addToTop(new ReducePowerAction(this.owner, this.owner, this.ID, 1));
            addToTop(new ApplyPowerAction(this.owner, this.owner, new BarrierPower(this.owner, 1), 1));
        }
    }
}