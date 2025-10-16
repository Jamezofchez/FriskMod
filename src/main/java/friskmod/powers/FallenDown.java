package friskmod.powers;

import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import friskmod.FriskMod;

public class FallenDown extends BasePower {
    public static final String POWER_ID = FriskMod.makeID(FallenDown.class.getSimpleName());
    private static final AbstractPower.PowerType TYPE = PowerType.DEBUFF;
    private static final boolean TURN_BASED = false;
    private float multiplier;
    private static final float multbyamount = 0.5F;

    //The only thing TURN_BASED controls is the color of the number on the power icon.
    //Turn based powers are white, non-turn based powers are red or green depending on if their amount is positive or negative.
    //For a power to actually decrease/go away on its own they do it themselves.
    //Look at powers that do this like VulnerablePower and DoubleTapPower.
    public FallenDown(AbstractCreature owner, int amount) {
        super(POWER_ID, TYPE, TURN_BASED, owner, amount);
        multiplier = 1.0F + (amount * multbyamount);
        updateDescription();
    }

    public float mult(){
        return multiplier;
    }

    @Override
    public void stackPower(int stackAmount){
        super.stackPower(stackAmount);
        multiplier = multiplier + (stackAmount * multbyamount);
        updateDescription();
    }


    @Override
    public AbstractPower makeCopy() {
        return new FallenDown(owner, amount);
    }

    public void updateDescription() {
        int percentage = ((int) (multiplier * 100)) - 100;
        this.description = String.format(DESCRIPTIONS[0], percentage);
    }

    public float atDamageReceive(float damage, DamageInfo.DamageType type) {
        if (type != DamageInfo.DamageType.NORMAL) {
            return damage * multiplier;
        }
        return damage;
    }

}