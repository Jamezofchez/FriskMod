package friskmod.cards.choosecard;

import com.megacrit.cardcrawl.powers.AbstractPower;
import friskmod.powers.Karma;
import friskmod.powers.Pleaded;

import static friskmod.FriskMod.makeID;

public class NightmareOfPleaded extends AbstractNightmareCard{
    public static final String ID = makeID(NightmareOfPleaded.class.getSimpleName()); //makeID adds the mod ID, so the final ID will be something like "modID:MyCard"

    private static final int POWER_AMOUNT = 1;

    public NightmareOfPleaded() {
        super(ID);
        setDisplayRarity(CardRarity.UNCOMMON);
    }

    @Override
    AbstractPower getPower() {
        return new Pleaded(getTarget(), POWER_AMOUNT);
    }

}
