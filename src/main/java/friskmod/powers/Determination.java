package friskmod.powers;

import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import friskmod.FriskMod;

public class Determination extends BasePower {
    public static final String POWER_ID = FriskMod.makeID(Determination.class.getSimpleName());
    private static final AbstractPower.PowerType TYPE = AbstractPower.PowerType.BUFF;
    private static final boolean TURN_BASED = false;

    //The only thing TURN_BASED controls is the color of the number on the power icon.
    //Turn based powers are white, non-turn based powers are red or green depending on if their amount is positive or negative.
    //For a power to actually decrease/go away on its own they do it themselves.
    //Look at powers that do this like VulnerablePower and DoubleTapPower.
    public Determination(AbstractCreature owner, int amount) {
        super(POWER_ID, TYPE, TURN_BASED, owner, amount);
    }

    @Override
    public AbstractPower makeCopy() {
        return new Determination(owner, amount);
    }

    public void updateDescription() {
        String baseDescription = DESCRIPTIONS[1];
        if (amount == 1){
            baseDescription = DESCRIPTIONS[0];
        }
        this.description = String.format(baseDescription, amount);
    }


    public int onAttackedToChangeDamage(DamageInfo info, int damageAmount) {
        if (damageAmount > 0) {
            if (info.type == DamageInfo.DamageType.NORMAL) {
                addToTop(new ReducePowerAction(this.owner, this.owner, this.ID, 1));
            } else{
                return damageAmount;
            }
        }
        return 0;
    }

}