package friskmod.cards;

import com.evacipated.cardcrawl.mod.stslib.actions.common.SelectCardsInHandAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.ExhaustAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import friskmod.character.Frisk;
import friskmod.patches.CardXPFields;
import friskmod.util.CardStats;
import friskmod.util.FriskTags;


import java.util.List;
import java.util.function.Consumer;

import static friskmod.FriskMod.makeID;

public class DogShrine extends AbstractEasyCard {
    public static final String ID = makeID(DogShrine.class.getSimpleName()); //makeID adds the mod ID, so the final ID will be something like "modID:MyCard"
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(ID);
    public static final String[] TEXT = uiStrings.TEXT;

    //These will be used in the constructor. Technically you can just use the values directly,
    //but constants at the top of the file are easy to adjust.

    private static final CardStats info = new CardStats(
            Frisk.Meta.Enums.CARD_COLOR, //The card color. If you're making your own character, it'll look something like this. Otherwise, it'll be CardColor.RED or similar for a basegame character color.
            CardType.SKILL, //The type. ATTACK/SKILL/POWER/CURSE/STATUS
            CardRarity.UNCOMMON, //Rarity. BASIC is for starting cards, then there's COMMON/UNCOMMON/RARE, and then SPECIAL and CURSE. SPECIAL is for cards you only get from events. Curse is for curses, except for special curses like Curse of the Bell and Necronomicurse.
            CardTarget.NONE, //The target. Single target is ENEMY, all enemies is ALL_ENEMY. Look at cards similar to what you want to see what to use.
            1 //The card's base cost. -1 is X cost, -2 is no cost for unplayable cards like curses, or Reflex.
    );
    private static final int CARDS_TO_CHOOSE = 1;
    private static final int UPG_CARDS_TO_CHOOSE = 1;
    public DogShrine() {
        super(ID, info); //Pass the required information to the BaseCard constructor.
        tags.add(FriskTags.BRAVERY);
        baseMagicNumber = magicNumber = CARDS_TO_CHOOSE;
    }
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        String selectionText;
        if (magicNumber == 1) {
            selectionText = String.format(TEXT[0], magicNumber);
        } else{
            selectionText = String.format(TEXT[1], magicNumber);
        }
        addToTop(new SelectCardsInHandAction(magicNumber, selectionText, true, true, (x -> true), SacrificeCard()));
    }

    private Consumer<List<AbstractCard>> SacrificeCard(){
        return (List<AbstractCard> cardList) -> {
            for (AbstractCard c: cardList) {
                AbstractPlayer p = AbstractDungeon.player;
                int xp = CardXPFields.getCardXP(c);
                addToBot(new DrawCardAction(p, xp));
                p.hand.moveToExhaustPile(c);
            }
        };
    }



    @Override
    public void upp() {
        upgradeMagicNumber(UPG_CARDS_TO_CHOOSE);
    }
}
