package friskmod.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import friskmod.FriskMod;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;

public class Pleaded extends BasePower {
    public static final String POWER_ID = FriskMod.makeID(Pleaded.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private static final AbstractPower.PowerType TYPE = AbstractPower.PowerType.DEBUFF;
    private static final boolean TURN_BASED = true;

    //The only thing TURN_BASED controls is the color of the number on the power icon.
    //Turn based powers are white, non-turn based powers are red or green depending on if their amount is positive or negative.
    //For a power to actually decrease/go away on its own they do it themselves.
    //Look at powers that do this like VulnerablePower and DoubleTapPower.
    public Pleaded(AbstractCreature owner, int amount) {
        super(POWER_ID, TYPE, TURN_BASED, owner, amount);
    }

    public void atEndOfRound() {
//        if (this.amount == 0) {
            addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this.ID));
//        } else {
//            addToBot(new ReducePowerAction(this.owner, this.owner, this.ID, 1));
//        }
    }

    @Override
    public AbstractPower makeCopy() {
        return new Pleaded(owner, amount);
    }

    public void updateDescription() {
        int LV_enemy = 0;
        if (owner != null) {
            AbstractPower lvPower = owner.getPower(LV_Enemy.POWER_ID);
            if (lvPower != null) {
                LV_enemy = lvPower.amount;
            }
        }
        String baseDescription = DESCRIPTIONS[0];
        this.description = String.format(baseDescription, LV_enemy*amount);
    }

}