package friskmod.cards.choosecard;

import com.megacrit.cardcrawl.powers.AbstractPower;
import friskmod.cards.ToughBranch;
import friskmod.powers.CountdownAttack;
import friskmod.powers.CountdownDefend;

import static friskmod.FriskMod.makeID;

public class DreamOfDefend extends AbstractDreamCard{
    public static final String ID = makeID(DreamOfDefend.class.getSimpleName()); //makeID adds the mod ID, so the final ID will be something like "modID:MyCard"

//    private static final int POWER_AMOUNT = 1;

    public DreamOfDefend() {
        super(ID);
        setDisplayRarity(CardRarity.UNCOMMON);
    }

    @Override
    protected AbstractPower getPower() {
        CountdownDefend defend = new CountdownDefend(getTarget(), ToughBranch.BLOCK, ToughBranch.WAIT_TIMER, ToughBranch.UPG_BLOCK);
        defend.upgrade();
        return defend;
    }

}
