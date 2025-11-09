package friskmod.cards.drinks;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.mod.stslib.actions.common.SelectCardsInHandAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.powers.AbstractPower;
import friskmod.FriskMod;
import friskmod.actions.*;
import friskmod.helper.GrillbysHelper;
import friskmod.patches.CardXPFields;
import friskmod.powers.LesserDuplication;
import friskmod.util.Wiz;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static friskmod.FriskMod.makeID;
import static friskmod.helper.GrillbysHelper.isCardCostAllowed;

public class GenericDrinkCard extends AbstractDrinkCard {
    public static final String ID = makeID(GenericDrinkCard.class.getSimpleName());
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(makeID(GenericDrinkCard.class.getSimpleName()));
    private static final String[] TEXT = uiStrings.TEXT;

    public GenericDrinkCard(){
        this(null);
    }
    public GenericDrinkCard(AbstractPotion potion) {
        super(ID, potion);
        if (potion != null) {
            initialiseDrinkType();
        }
    }

    private void initialiseDrinkType() {
        if (GrillbysHelper.checkPotency(potion)) {
            drinkType = DrinkType.POTENCY;
            baseMagicNumber = magicNumber = getNewPotency();
        }
        else if (GrillbysHelper.checkLowCost(potion)){
            drinkType = DrinkType.LOWCOST;
            baseMagicNumber = magicNumber = getNewHighestCost();
        }
        else if (GrillbysHelper.checkChance(potion)){
            drinkType = DrinkType.CHANCE;
            baseMagicNumber = magicNumber = getNewChance();
        }
    }

    enum DrinkType {POTENCY, LOWCOST, CHANCE}
    private DrinkType drinkType;

    @Override
    public void applyPowers(){
        setDesc();
        super.applyPowers();
        initializeDescription();
    }

    private void setDesc() {
        if (drinkType == null){
            this.rawDescription = TEXT[4];
        } else {
            switch (drinkType) {
                case POTENCY:
                    this.rawDescription = cocktailifyDesc(getPotencyDescription());
                    return;
                case LOWCOST:
                    this.rawDescription = cocktailifyDesc(getLowCostDescription());
                    return;
                case CHANCE:
                    this.rawDescription = cocktailifyDesc((getChanceDescription()));
                    return;
            }
        }
        return;
    }

    public String cocktailifyDesc(String description) {
        String newDesc = description + TEXT[0];
        newDesc = newDesc.replaceAll("#b", "");
        newDesc = newDesc.replaceAll("#y", "");

        return newDesc;
    }


    private String getLowCostDescription() {
        String rawDescription;
        if (potion != null){
            rawDescription = potion.description;
        } else{
            rawDescription = TEXT[4];
        }
        String costDescriptor;
        if (!upgraded){
            costDescriptor = TEXT[1];
        } else{
            costDescriptor = TEXT[2];
        }
        return rawDescription.replaceAll("(?<!dis|Dis)(cards?)", Matcher.quoteReplacement(costDescriptor) + " $1");
    }

    private String getChanceDescription() {
        String rawDescription;
        if (potion != null){
            rawDescription = potion.description;
        } else{
            rawDescription = TEXT[4];
        }
        String chanceDescriptor = String.format(TEXT[3], magicNumber);
        return chanceDescriptor + rawDescription;

    }

    private String getPotencyDescription() {
        String rawDescription;
        if (potion != null) {
            rawDescription = potion.description;
        } else {
            rawDescription = TEXT[4];
        }

        // Find all numbers in the string
        Matcher matcher = Pattern.compile("\\d+").matcher(rawDescription);
        List<String> numbers = new ArrayList<>();

        while (matcher.find()) {
            numbers.add(matcher.group());
        }

        if (numbers.isEmpty()) {
            return rawDescription; // no numbers found
        }

        // Check if all numbers are the same
        boolean allSame = numbers.stream().distinct().count() == 1;

        if (allSame) {
            // Replace all if all numbers are the same
            return rawDescription.replaceAll("\\d+", "!M!");
        } else {
            // Replace only the first occurrence otherwise
            return rawDescription.replaceFirst("\\d+", "!M!"); //guess??
        }
    }


    @Override
    public void usePotion(AbstractMonster m) {
        if (potion == null){
            return;
        }
        AbstractPotion tmpPotion = potion.makeCopy();
        switch (drinkType) {
            case POTENCY:
                ReflectionHacks.setPrivate(tmpPotion, AbstractPotion.class, "potency", magicNumber);
                tryUsePotion(tmpPotion, m);
                return;
            case LOWCOST:
                GrillbysHelper.setLimitCardCost(magicNumber);
                Wiz.atb(new ResetCardCostLimit());
                tryUsePotion(tmpPotion, m);
                return;
            case CHANCE:
                int sample = AbstractDungeon.cardRng.random(100);
                if (sample < magicNumber){
                    tryUsePotion(tmpPotion, m);
                }
                return;
        }
    }

    private void tryUsePotion(AbstractPotion tmpPotion, AbstractMonster m) {
        try{
            if (specialCases(tmpPotion)){
                return;
            }
            tmpPotion.use(m);
        } catch (Exception ignored) {
            FriskMod.logger.warn("{}: Failed to sip potion {}", FriskMod.modID, tmpPotion.name);
        }
    }

    private boolean specialCases(AbstractPotion tmpPotion) {
        switch (tmpPotion.ID) {
            case "DuplicationPotion":
                duplicationPotionUse();
                return true;
            case "ElixirPotion":
                elixirPotionUse();
                return true;
            case "BlessingOfTheForge":
                blessingPotionUse();
                return true;
            case "GamblersBrew":
                gamblersBrewUse();
                return true;
            case "LiquidMemories":
                liquidMemoriesUse();
            default:
                return false;
        }
    }

    @Override
    public void upp() {
        switch (drinkType) {
            case POTENCY:
                setUpgradeMagicNumber(getNewPotency());
                return;
            case LOWCOST:
                setUpgradeMagicNumber(getNewHighestCost());
                return;
            case CHANCE:
                setUpgradeMagicNumber(getNewChance());
                return;
        }
    }

    private void duplicationPotionUse() {
        AbstractPlayer p = AbstractDungeon.player;
        AbstractPower posspow = p.getPower(LesserDuplication.POWER_ID);
        if (posspow != null) {
            ((LesserDuplication) posspow).amount2 = Math.min(((LesserDuplication) posspow).amount2, secondMagic);
        }
        addToBot(new ApplyPowerAction(p, p, new LesserDuplication(p, magicNumber, secondMagic), magicNumber));
    }

    private void elixirPotionUse() {
        addToBot(new LowCostExhaustAction());
    }

    private void blessingPotionUse() {
        addToBot(new LowCostUpgradeAction());
    }
    private void gamblersBrewUse() {
        addToBot(new LowCostGambleAction());
    }
    private void liquidMemoriesUse(){
        addToBot(new LowCostMemoriesAction());
    }


}
