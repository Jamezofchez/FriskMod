package friskmod.cards.choosecard;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import friskmod.powers.Determination;

import static friskmod.FriskMod.makeID;

public class DreamOfDetermination extends AbstractDreamCard{
    public static final String ID = makeID(DreamOfDetermination.class.getSimpleName()); //makeID adds the mod ID, so the final ID will be something like "modID:MyCard"
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(makeID(NightmareOfMercied.class.getSimpleName()));
    private static final String[] TEXT = uiStrings.TEXT;
    private static final int POWER_AMOUNT = 1;

    public DreamOfDetermination() {
        super(ID);
        setDisplayRarity(CardRarity.RARE);
        this.name = TEXT[0];
    }

    @Override
    protected AbstractPower getPower() {
        return new Determination(getTarget(), POWER_AMOUNT);
    }

}
