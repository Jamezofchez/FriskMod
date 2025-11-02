package friskmod.cards.choosecard;

import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;

import static friskmod.FriskMod.makeID;

public class NightmareOfVulnerable extends AbstractNightmareCard{
    public static final String ID = makeID(NightmareOfVulnerable.class.getSimpleName()); //makeID adds the mod ID, so the final ID will be something like "modID:MyCard"

    private static final int POWER_AMOUNT = 2;

    public NightmareOfVulnerable() {
        super(ID);
        setDisplayRarity(CardRarity.COMMON);
    }

    @Override
    AbstractPower getPower() {
        return new VulnerablePower(getTarget(), POWER_AMOUNT, false);
    }

}
