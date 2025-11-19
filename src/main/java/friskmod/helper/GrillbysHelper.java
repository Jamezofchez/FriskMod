package friskmod.helper;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import friskmod.cards.drinks.*;
import friskmod.util.Wiz;

public class GrillbysHelper {
    private static int limitCardCost = 9999;
    private static int timesAttemptedToLimit = 0;

    public static AbstractCard getPotionCard(AbstractPotion potion) {
        AbstractCard card;
        card = returnUnique(potion);
        if (card == null){
            if (isGeneric(potion)){
                card = new GenericDrinkCard(potion);
            } else{
                card = new Ketchup(potion);
            }
        }
        card.applyPowers();
        return card;
    }

    public static AbstractCard returnUnique(AbstractPotion potion) {
        switch (potion.ID){
            case "EntropicBrew":
                return new EntropicCocktail(potion);
            case "FairyPotion":
                return new FairyCocktail(potion);
            case "DuplicationPotion":
                return new DuplicationCocktail(potion);
            case "Ancient Potion": //why??
                return new AncientCocktail(potion);
            case "DistilledChaos":
                return new ChaoticCocktail(potion);
            case "CultistCocktail":
                return new CultistCocktail(potion);
            default:
                return null;
        }
    }

    public static void setLimitCardCost(int newLimit){
        limitCardCost = newLimit;
        timesAttemptedToLimit = 0;
    }

    public static boolean isCardCostAllowed(AbstractCard c){
        boolean result = Wiz.getLogicalCardCost(c) <= limitCardCost;
        if (!result){
            ++timesAttemptedToLimit;
            if (timesAttemptedToLimit > 100){
                result = true;
            }
        }
        return result;
    }

    public static boolean isGeneric(AbstractPotion potion) {
        if (checkPotency(potion) || checkLowCost(potion) || checkChance(potion)){
            return true;
        }
        return false;
    }

    public static boolean checkPotency(AbstractPotion potion) {
        if (potion == null){
            return false;
        }
        int potency = potion.getPotency();
        if (potency <= 1){
            return false;
        }
        return true;
    }

    public static boolean checkLowCost(AbstractPotion potion) {
        if (potion == null){
            return false;
        }
        switch (potion.ID){
            case "AttackPotion":
            case "PowerPotion":
            case "SkillPotion":
            case "LiquidMemories":
            case "ColorlessPotion":
            case "GamblersBrew":
            case "ElixirPotion":
            case "DistilledChaos":
            case "BlessingOfTheForge":
                return true;
            default:
                return false;
        }
    }

    public static boolean checkChance(AbstractPotion potion) {
        if (potion == null){
            return false;
        }
        switch (potion.ID){
            case "GhostInAJar":
            case "SmokeBomb":
            case "Ambrosia":
            case "StancePotion":
                return true;
            default:
                return false;
        }
    }
}
