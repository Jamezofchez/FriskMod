package friskmod.cards.choosecard;

import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

import static friskmod.FriskMod.makeID;

public class DreamOfDexterity extends AbstractDreamCard{
    public static final String ID = makeID(DreamOfDexterity.class.getSimpleName()); //makeID adds the mod ID, so the final ID will be something like "modID:MyCard"

    private static final int POWER_AMOUNT = 2;

    public DreamOfDexterity() {
        super(ID);
        setDisplayRarity(CardRarity.COMMON);
    }

    @Override
    protected AbstractPower getPower() {
        return new DexterityPower(getTarget(), POWER_AMOUNT);
    }

}
