package friskmod.cards.choosecard;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import friskmod.FriskMod;
import friskmod.helper.DraftManager;

import java.util.ArrayList;

import static friskmod.FriskMod.makeID;
import static java.util.stream.Collectors.toCollection;

public abstract class AbstractDreamNightmareCard extends AbstractChooseCard{
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(makeID(AbstractDreamNightmareCard.class.getSimpleName()));
    private static final String[] TEXT = uiStrings.TEXT;
    public AbstractDreamNightmareCard(String ID) {
        super(ID);
    }

    protected String getCardName() {
        String basename = TEXT[3];
        String descriptor = TEXT[4];
        if (this instanceof AbstractNightmareCard){
            descriptor = TEXT[5];
        }
        AbstractPower tmp = getPower();
        return String.format(basename, descriptor, tmp.name);
    }

    abstract AbstractPower getPower();

    @Override
    public void chooseOption() {
        addToTop(new ApplyPowerAction(getTarget(), AbstractDungeon.player, getPower()));
    }

    protected AbstractCreature getTarget() {
        AbstractCreature possTarget = DraftManager.currentTarget;
        if (possTarget != null) {
            return possTarget;
        }
        FriskMod.logger.warn("{}: Couldn't find a target for the choice", FriskMod.modID);
        return null;
    }

    @Override
    public void initializeDescription(){
        AbstractPower tmp = getPower();
        AbstractCreature tmpTarget = getTarget();
        String targetName;
        if (tmpTarget instanceof AbstractPlayer){
            targetName = TEXT[1];
        } else{
            targetName = TEXT[2];
        }
        this.rawDescription = String.format(TEXT[0], tmp.amount, tmp.name, targetName);
        super.initializeDescription();
    }

    @Override
    public void onChoseThisOption() {
        super.onChoseThisOption();
        if (DraftManager.lastChoices != null) {
            DraftManager.setLastChoices(DraftManager.lastChoices.stream().filter(x -> x != this).collect(toCollection(ArrayList::new)));
        }
    }

    @Override
    public void upp() {

    }
}
