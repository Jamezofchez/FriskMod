package friskmod.cards.choosecard;

import com.megacrit.cardcrawl.powers.AbstractPower;
import friskmod.powers.Hurt;
import friskmod.powers.Karma;

import static friskmod.FriskMod.makeID;

public class NightmareOfHurt extends AbstractDreamCard{
    public static final String ID = makeID(NightmareOfHurt.class.getSimpleName()); //makeID adds the mod ID, so the final ID will be something like "modID:MyCard"

    private static final int POWER_AMOUNT = 5;

    public NightmareOfHurt() {
        super(ID);
        setDisplayRarity(CardRarity.UNCOMMON);
    }

    @Override
    protected AbstractPower getPower() {
        return new Hurt(getTarget(), POWER_AMOUNT);
    }

}
