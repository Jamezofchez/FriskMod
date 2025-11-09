package friskmod.cards.choosecard;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.powers.AbstractPower;
import friskmod.powers.LV_Enemy;
import friskmod.powers.LV_Hero;

import static friskmod.FriskMod.makeID;

public class DreamOfLV extends AbstractDreamCard{
    public static final String ID = makeID(DreamOfLV.class.getSimpleName()); //makeID adds the mod ID, so the final ID will be something like "modID:MyCard"

    private static final int POWER_AMOUNT = 5;

    public DreamOfLV() {
        super(ID);
        setDisplayRarity(CardRarity.COMMON);
    }

    @Override
    protected AbstractPower getPower() {
        if (getTarget() instanceof AbstractPlayer){
            return new LV_Hero(getTarget(), POWER_AMOUNT);
        } else {
            return new LV_Enemy(getTarget(), POWER_AMOUNT);
        }
    }

}
