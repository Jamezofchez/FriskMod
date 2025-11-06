package friskmod.cards.drinks;

import basemod.ReflectionHacks;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import friskmod.FriskMod;
import friskmod.actions.ResetCardCostLimit;
import friskmod.helper.GrillbysHelper;
import friskmod.util.Wiz;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static friskmod.FriskMod.makeID;

public class GenericDrinkCard extends AbstractDrinkCard {
    public static final String ID = makeID(GenericDrinkCard.class.getSimpleName());
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(makeID(GenericDrinkCard.class.getSimpleName()));
    private static final String[] TEXT = uiStrings.TEXT;

    private static final int PROBABILITY = 50;
    private static final int UPG_PROBABILITY = 75;

    private static final int HIGHEST_COST = 1;
    private static final int UPG_HIGHEST_COST = 2;

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
        return rawDescription.replaceFirst("(cards?)", Matcher.quoteReplacement(costDescriptor) + " $1");
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
            tmpPotion.use(m);
        } catch (Exception ignored) {
            FriskMod.logger.warn("{}: Failed to sip potion {}", FriskMod.modID, tmpPotion.name);
        }
    }

    private int getNewHighestCost() {
        if (upgraded) {
            return UPG_HIGHEST_COST;
        }
        return HIGHEST_COST;
    }

    private int getNewChance() {
        if (upgraded) {
            return UPG_PROBABILITY;
        }
        return PROBABILITY;
    }

    private int getNewPotency() {
        if (potion == null){
            return 0;
        }
        int potency = potion.getPotency();
        int newPotency = potency / 2;
        if (upgraded) {
            newPotency = (int) (newPotency * 1.5F);
        }
        return newPotency;
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
}
