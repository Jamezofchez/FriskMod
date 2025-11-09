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
        String powerName = getPowerName();
        if (this instanceof AbstractDreamCard){
            String basename = TEXT[3];
            String descriptor = TEXT[4];
            return String.format(basename, descriptor, powerName);
        } else{
            String basename = TEXT[5];
            return String.format(basename, powerName);
        }
    }

    private String getPowerName() {
        AbstractPower tmp = getPower();
        String powerName;
        if (tmp instanceof CountdownAttack){
            powerName = TEXT[6];
        } else if (tmp == null){
            powerName = TEXT[7];
        } else{
            powerName = tmp.name;
        }
        return powerName;
    }

    protected AbstractPower getPower(){
        return null;
    };

    @Override
    public void chooseOption() {
        addToTop(new ApplyPowerAction(getTarget(), AbstractDungeon.player, getPower()));
    }

    @Override
    public void initializeDescription(){
        String basename = TEXT[0];
        AbstractPower tmp = getPower();
        String powerName = getPowerName();
        AbstractCreature tmpTarget = getTarget();
        String targetName;
        if (tmpTarget instanceof AbstractPlayer){
            targetName = TEXT[1];
        } else{
            targetName = TEXT[2];
        }
        String powerAmount;
        if (tmp != null){
            powerAmount = tmp.amount + "";
        } else{
            powerAmount = TEXT[8];
        }
        this.rawDescription = String.format(basename, powerAmount, powerName, targetName);
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
