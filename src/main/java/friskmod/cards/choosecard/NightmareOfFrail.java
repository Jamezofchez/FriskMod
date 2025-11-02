package friskmod.cards.choosecard;

import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.FrailPower;
import com.megacrit.cardcrawl.powers.WeakPower;

import static friskmod.FriskMod.makeID;

public class NightmareOfFrail extends AbstractNightmareCard{
    public static final String ID = makeID(NightmareOfFrail.class.getSimpleName()); //makeID adds the mod ID, so the final ID will be something like "modID:MyCard"

    private static final int POWER_AMOUNT = 2;

    public NightmareOfFrail() {
        super(ID);
        setDisplayRarity(CardRarity.COMMON);
    }

    @Override
    AbstractPower getPower() {
        return new FrailPower(getTarget(), POWER_AMOUNT, false);
    }

}
