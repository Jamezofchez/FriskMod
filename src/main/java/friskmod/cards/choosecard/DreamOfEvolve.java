package friskmod.cards.choosecard;

import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.BlurPower;
import friskmod.powers.EvolvePower;

import static friskmod.FriskMod.makeID;

public class DreamOfEvolve extends AbstractDreamCard{
    public static final String ID = makeID(DreamOfEvolve.class.getSimpleName()); //makeID adds the mod ID, so the final ID will be something like "modID:MyCard"

    private static final int POWER_AMOUNT = 1;

    public DreamOfEvolve() {
        super(ID);
        setDisplayRarity(CardRarity.UNCOMMON);
    }

    @Override
    AbstractPower getPower() {
        return new EvolvePower(getTarget(), POWER_AMOUNT);
    }

}
