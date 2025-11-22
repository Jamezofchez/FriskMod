package friskmod.powers;

import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import friskmod.FriskMod;
import friskmod.util.interfaces.WastedEnergyInterface;

public class RecyclePower extends BasePower implements WastedEnergyInterface {
    public static final String POWER_ID = FriskMod.makeID(RecyclePower.class.getSimpleName());
    private static final PowerType TYPE = PowerType.BUFF;
    private static final boolean TURN_BASED = false;


    //The only thing TURN_BASED controls is the color of the number on the power icon.
    //Turn based powers are white, non-turn based powers are red or green depending on if their amount is positive or negative.
    //For a power to actually decrease/go away on its own they do it themselves.
    //Look at powers that do this like VulnerablePower and DoubleTapPower.
    public RecyclePower(AbstractCreature owner, int amount) {
        super(POWER_ID, TYPE, TURN_BASED, owner, amount);
        this.priority = 0; //hopefully gives it a higher priority than the other powers
        updateDescription();
    }


    @Override
    public AbstractPower makeCopy() {
        return new RecyclePower(owner, amount);
    }

    public void updateDescription() {
        this.description = String.format(DESCRIPTIONS[0], amount);
    }

    @Override
    public void WasteEnergyAction(int wasted) {
        this.flash();
        for (int i = 0; i < wasted; i++) {
//            addToBot(new ApplyPowerAction(this.owner, this.owner, new LV_Hero(this.owner, LVGain), LVGain));
            addToBot(new AddTemporaryHPAction(this.owner, this.owner, amount));
        }

        addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this));
    }
}