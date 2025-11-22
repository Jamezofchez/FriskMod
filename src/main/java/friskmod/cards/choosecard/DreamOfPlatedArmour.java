package friskmod.cards.choosecard;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.PlatedArmorPower;
import friskmod.cards.ToughBranch;
import friskmod.powers.CountdownDefend;

import static friskmod.FriskMod.makeID;

public class DreamOfPlatedArmour extends AbstractDreamCard{
    public static final String ID = makeID(DreamOfPlatedArmour.class.getSimpleName()); //makeID adds the mod ID, so the final ID will be something like "modID:MyCard"

    private static final int POWER_AMOUNT = 3;

    public DreamOfPlatedArmour() {
        super(ID);
        setDisplayRarity(CardRarity.UNCOMMON);
    }

    @Override
    protected AbstractPower getPower() {
        return new PlatedArmorPower(getTarget(), POWER_AMOUNT);
    }

}
