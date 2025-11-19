package friskmod.cards.choosecard;

import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.DexterityPower;

import static friskmod.FriskMod.makeID;

public class DreamOfArtifact extends AbstractDreamCard{
    public static final String ID = makeID(DreamOfArtifact.class.getSimpleName()); //makeID adds the mod ID, so the final ID will be something like "modID:MyCard"

    private static final int POWER_AMOUNT = 2;

    public DreamOfArtifact() {
        super(ID);
        setDisplayRarity(CardRarity.COMMON);
    }

    @Override
    protected AbstractPower getPower() {
        return new ArtifactPower(getTarget(), POWER_AMOUNT);
    }

}
