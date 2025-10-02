package friskmod.powers;

import com.megacrit.cardcrawl.powers.AbstractPower;
import friskmod.FriskMod;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.cards.DamageInfo;

import java.util.Objects;

public class LV_Enemy extends BasePower {
    public static final String POWER_ID = FriskMod.makeID(LV_Enemy.class.getSimpleName());
    private static final AbstractPower.PowerType TYPE = AbstractPower.PowerType.BUFF;
    private static final boolean TURN_BASED = false;
    //The only thing TURN_BASED controls is the color of the number on the power icon.
    //Turn based powers are white, non-turn based powers are red or green depending on if their amount is positive or negative.
    //For a power to actually decrease/go away on its own they do it themselves.
    //Look at powers that do this like VulnerablePower and DoubleTapPower.
    public LV_Enemy(AbstractCreature owner, int amount) {
        super(POWER_ID, TYPE, TURN_BASED, owner, amount);
    }

    public float atDamageGive(float damage, DamageInfo.DamageType type) {
        //If NORMAL (attack) damage, modify damage by this power's amount
        if (type != DamageInfo.DamageType.NORMAL) {
            return damage;
        }
        for (AbstractPower pow : this.owner.powers) {
            if (Objects.equals(pow.ID, PleadedPower.POWER_ID)) {
                pow.flash();
                return damage - this.amount;
            }
        }
        return damage + this.amount;
    }

    @Override
    public AbstractPower makeCopy() {
        return new LV_Enemy(owner, amount);
    }

    public void updateDescription() {
        this.description = String.format(DESCRIPTIONS[0], amount);
    }

}