package friskmod.cards.choosecard;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import friskmod.powers.Mercied;

import static friskmod.FriskMod.makeID;

public class NightmareOfMercied extends AbstractNightmareCard{
    public static final String ID = makeID(NightmareOfMercied.class.getSimpleName()); //makeID adds the mod ID, so the final ID will be something like "modID:MyCard"
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(makeID(DreamOfDetermination.class.getSimpleName()));
    private static final String[] TEXT = uiStrings.TEXT;
    private static final int POWER_AMOUNT = 2;

    public NightmareOfMercied() {
        super(ID);
        setDisplayRarity(CardRarity.RARE);
        this.name = TEXT[0];
    }

    @Override
    AbstractPower getPower() {
        return new Mercied(getTarget(), POWER_AMOUNT);
    }

}
