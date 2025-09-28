package friskmod.powers;

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import friskmod.patches.CardXPFields;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;

import com.megacrit.cardcrawl.powers.AbstractPower;
import friskmod.FriskMod;

public class FireballPower extends BasePower {
    public static final String POWER_ID = FriskMod.makeID("LV_Enemy");
    private static final AbstractPower.PowerType TYPE = AbstractPower.PowerType.BUFF;
    private static final boolean TURN_BASED = false;
    //The only thing TURN_BASED controls is the color of the number on the power icon.
    //Turn based powers are white, non-turn based powers are red or green depending on if their amount is positive or negative.
    //For a power to actually decrease/go away on its own they do it themselves.
    //Look at powers that do this like VulnerablePower and DoubleTapPower.

    public FireballPower(AbstractCreature owner, int amount) {
        super(POWER_ID, TYPE, TURN_BASED, owner, amount);
    }


    public void onUseCard(AbstractCard card, UseCardAction action) {
        flash();
        CardXPFields.addXP(card, amount);
        addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, "FireballPower"));
    }

    public AbstractPower makeCopy() {
        return new FireballPower(owner, amount);
    }
}


/* Location:              C:\Program Files (x86)\Steam\steamapps\workshop\content\646570\3390561252\Snowpunk.jar!\Snowpunk\powers\FireballPower.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */