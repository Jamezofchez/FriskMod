package friskmod.cards.choosecard;

import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.BlurPower;

import static friskmod.FriskMod.makeID;

public class DreamOfBlur extends AbstractDreamCard{
    public static final String ID = makeID(DreamOfBlur.class.getSimpleName()); //makeID adds the mod ID, so the final ID will be something like "modID:MyCard"

    private static final int POWER_AMOUNT = 1;

    public DreamOfBlur() {
        super(ID);
        setDisplayRarity(CardRarity.UNCOMMON);
    }

    @Override
    AbstractPower getPower() {
        return new BlurPower(getTarget(), POWER_AMOUNT);
    }

}
