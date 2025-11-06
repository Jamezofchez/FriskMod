package friskmod.cards.drinks;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.potions.FirePotion;
import friskmod.cards.AbstractEasyCard;
import friskmod.helper.GrillbysHelper;
import friskmod.util.CardStats;

import java.util.*;

import static friskmod.FriskMod.makeID;

public abstract class AbstractDrinkCard extends AbstractEasyCard {
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(makeID(AbstractDrinkCard.class.getSimpleName()));
    private static final String[] TEXT = uiStrings.TEXT;
    protected AbstractPotion potion;
    public AbstractDrinkCard(String ID, AbstractPotion potion){
        super(
                ID,
                new CardStats(
                        CardColor.COLORLESS,
                        CardType.SKILL,
                        CardRarity.SPECIAL,
                        getTarget(potion),
                        0
                )
        );
        if (potion == null) {
            this.potion = new FirePotion();
        } else{
            this.potion = potion;
        }
        if (GrillbysHelper.isGeneric(potion)) {
            this.name = getCardName();
        }
        this.exhaust = true;
        switch (this.potion.rarity) {
            case UNCOMMON:
                setDisplayRarity(CardRarity.UNCOMMON);
                break;
            case RARE:
                setDisplayRarity(CardRarity.RARE);
                break;
            default:
                setDisplayRarity(CardRarity.COMMON);
                break;
        }
    }

    private String getCardName() {
        String descriptor;
        if (potion == null){
            descriptor = TEXT[4];
        } else {
            String rawName = potion.name;
            List<String> words = new ArrayList<>(Arrays.asList(rawName.split(" ")));
            if (words.isEmpty()) {
                descriptor = TEXT[4];
            }  else {
                Collections.reverse(words); //second priority to last word
                words = words.stream().filter(w -> !isCommonName(w)).collect(java.util.stream.Collectors.toList());
                words.sort((a, b) -> {
                    boolean aShort = a.length() < 4;
                    boolean bShort = b.length() < 4;

                    if (aShort && bShort) {
                        // Both short — sort by length descending
                        return Integer.compare(b.length(), a.length());
                    } else {
                        // At least one is long — preserve reversed order
                        return 0;
                    }
                }); //first priority to longest word if greater than 4
                if (words.isEmpty()) {
                    descriptor = TEXT[4];
                } else {
                    descriptor = words.get(0);
                }
            }
        }
        String cocktail = TEXT[1];
        if (potion != null) {
            if (potion.isThrown) {
                if (potion.targetRequired) {
                    cocktail = TEXT[2];
                } else {
                    cocktail = TEXT[3];
                }
            }
        }
        String basename = TEXT[0];
        return String.format(basename, descriptor, cocktail);
    }

    private boolean isCommonName(String w) {
        if (w == null) return true;
        if (w.isEmpty()) return true;
        if (Character.isLowerCase(w.charAt(0))) return true;
        switch (w){
            case "Potion":
            case "Brew":
            case "Bottled":
            case "Bottle":
            case "Oil":
            case "Jar":
                return true;
            default:
                return false;
        }
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        usePotion(m);
    }

    protected static CardTarget getTarget(AbstractPotion potion) {
        if (potion == null){
            return CardTarget.NONE;
        }
        if (potion.targetRequired){
            return CardTarget.ENEMY;
        } else{
            return CardTarget.NONE;
        }

    }

    public abstract void usePotion(AbstractMonster m);

    @Override
    public AbstractCard makeCopy() {
        AbstractCard tmp;
        if (this instanceof Ketchup){
            tmp = new Ketchup();
        } else{
            tmp = GrillbysHelper.getPotionCard(potion);
        }
        return tmp;
    }
}