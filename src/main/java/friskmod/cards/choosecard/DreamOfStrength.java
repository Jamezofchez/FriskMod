package friskmod.cards.choosecard;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import friskmod.powers.LV_Enemy;
import friskmod.powers.LV_Hero;

import static friskmod.FriskMod.makeID;

public class DreamOfStrength extends AbstractDreamCard{
    public static final String ID = makeID(DreamOfStrength.class.getSimpleName()); //makeID adds the mod ID, so the final ID will be something like "modID:MyCard"

    private static final int POWER_AMOUNT = 2;

    public DreamOfStrength() {
        super(ID);
        setDisplayRarity(CardRarity.COMMON);
    }

    @Override
    protected AbstractPower getPower() {
        return new StrengthPower(getTarget(), POWER_AMOUNT);
    }

}
