package friskmod.cards.choosecard;

import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.FrailPower;
import friskmod.cards.Pounce;
import friskmod.powers.AbstractCountdownPower;
import friskmod.powers.CountdownPounce;

import static friskmod.FriskMod.makeID;

public class NightmareOfPounce extends AbstractNightmareCard{
    public static final String ID = makeID(NightmareOfPounce.class.getSimpleName()); //makeID adds the mod ID, so the final ID will be something like "modID:MyCard"

//    private static final int POWER_AMOUNT = 1;

    public NightmareOfPounce() {
        super(ID);
        setDisplayRarity(CardRarity.UNCOMMON);
    }

    @Override
    AbstractPower getPower() {
        return new CountdownPounce(getTarget(), Pounce.DAMAGE, Pounce.WAIT_TIMER, Pounce.UPG_DAMAGE);
    }

}
