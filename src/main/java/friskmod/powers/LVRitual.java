package friskmod.powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import friskmod.FriskMod;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;

public class LVRitual extends BasePower {
    public static final String POWER_ID = FriskMod.makeID(LVRitual.class.getSimpleName());
    private static final AbstractPower.PowerType TYPE = AbstractPower.PowerType.BUFF;
    private static final boolean TURN_BASED = false;


    //The only thing TURN_BASED controls is the color of the number on the power icon.
    //Turn based powers are white, non-turn based powers are red or green depending on if their amount is positive or negative.
    //For a power to actually decrease/go away on its own they do it themselves.
    //Look at powers that do this like VulnerablePower and DoubleTapPower.
    public LVRitual(AbstractCreature owner, int amount) {
        super(POWER_ID, TYPE, TURN_BASED, owner, amount);
    }

    @Override
    public AbstractPower makeCopy() {
        return new LVRitual(owner, amount);
    }

    public void atEndOfTurn(boolean isPlayer) {
        if (isPlayer) {
            flash();
            addToBot((AbstractGameAction)new ApplyPowerAction(this.owner, this.owner, new LV_Hero(this.owner, this.amount), this.amount));
        }
    }

    public void updateDescription() {
        this.description = String.format(DESCRIPTIONS[0], amount);
    }

}