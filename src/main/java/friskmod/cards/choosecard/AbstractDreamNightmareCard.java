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
import friskmod.powers.CountdownAttack;

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
        String powerName = getPowerName();
        return String.format(basename, descriptor, powerName);
    }

    private String getPowerName() {
        AbstractPower tmp = getPower();
        String powerName = tmp.name;
        if (tmp instanceof CountdownAttack){
            powerName = TEXT[6];
        }
        return powerName;
    }

    abstract AbstractPower getPower();

    @Override
    public void chooseOption() {
        addToTop(new ApplyPowerAction(getTarget(), AbstractDungeon.player, getPower()));
    }

    @Override
    public void initializeDescription(){
        String basename = TEXT[0];
        AbstractPower tmp = getPower();
        String powerName = tmp.name;
        if (tmp instanceof CountdownAttack){
            powerName = TEXT[6];
        }
        AbstractCreature tmpTarget = getTarget();
        String targetName;
        if (tmpTarget instanceof AbstractPlayer){
            targetName = TEXT[1];
        } else{
            targetName = TEXT[2];
        }
        this.rawDescription = String.format(basename, tmp.amount, powerName, targetName);
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
