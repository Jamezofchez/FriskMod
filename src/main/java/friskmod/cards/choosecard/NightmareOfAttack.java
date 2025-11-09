package friskmod.cards.choosecard;

import com.megacrit.cardcrawl.powers.AbstractPower;
import friskmod.cards.ToughBranch;
import friskmod.powers.CountdownAttack;

import static friskmod.FriskMod.makeID;

public class NightmareOfAttack extends AbstractNightmareCard{
    public static final String ID = makeID(NightmareOfAttack.class.getSimpleName()); //makeID adds the mod ID, so the final ID will be something like "modID:MyCard"

//    private static final int POWER_AMOUNT = 1;

    public NightmareOfAttack() {
        super(ID);
        setDisplayRarity(CardRarity.UNCOMMON);
    }

    @Override
    AbstractPower getPower() {
        CountdownAttack attack = new CountdownAttack(getTarget(), ToughBranch.DAMAGE, ToughBranch.WAIT_TIMER, ToughBranch.UPG_DAMAGE);
        attack.upgrade();
        return attack;
    }

}
