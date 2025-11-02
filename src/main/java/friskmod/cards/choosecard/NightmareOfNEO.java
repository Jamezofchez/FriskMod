package friskmod.cards.choosecard;

import com.megacrit.cardcrawl.powers.AbstractPower;
import friskmod.powers.Karma;
import friskmod.powers.NEO;
import friskmod.powers.Pleaded;

import static friskmod.FriskMod.makeID;

public class NightmareOfNEO extends AbstractNightmareCard{
    public static final String ID = makeID(NightmareOfNEO.class.getSimpleName()); //makeID adds the mod ID, so the final ID will be something like "modID:MyCard"

    private static final int POWER_AMOUNT = 2;

    public NightmareOfNEO() {
        super(ID);
        setDisplayRarity(CardRarity.UNCOMMON);
    }

    @Override
    AbstractPower getPower() {
        return new NEO(getTarget(), POWER_AMOUNT);
    }

}
