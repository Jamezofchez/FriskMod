package friskmod.cards;

import basemod.patches.com.megacrit.cardcrawl.cards.AbstractCard.MultiCardPreview;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.screens.CardRewardScreen;
import friskmod.actions.SelectCardsCenteredAction;
import friskmod.character.Frisk;
import friskmod.util.CardStats;
import friskmod.util.FriskTags;


import java.util.ArrayList;

import static friskmod.FriskMod.makeID;

public class ScalesOfJustice extends AbstractEasyCard {
    public static final String ID = makeID(ScalesOfJustice.class.getSimpleName()); //makeID adds the mod ID, so the final ID will be something like "modID:MyCard"
    //These will be used in the constructor. Technically you can just use the values directly,
    //but constants at the top of the file are easy to adjust.
    private ArrayList<AbstractCard> cardsList = new ArrayList<>();

    private static final CardStats info = new CardStats(
            Frisk.Meta.Enums.CARD_COLOR, //The card color. If you're making your own character, it'll look something like this. Otherwise, it'll be CardColor.RED or similar for a basegame character color.
            CardType.SKILL, //The type. ATTACK/SKILL/POWER/CURSE/STATUS
            CardRarity.RARE, //Rarity. BASIC is for starting cards, then there's COMMON/UNCOMMON/RARE, and then SPECIAL and CURSE. SPECIAL is for cards you only get from events. Curse is for curses, except for special curses like Curse of the Bell and Necronomicurse.
            CardTarget.SELF, //The target. Single target is ENEMY, all enemies is ALL_ENEMY. Look at cards similar to what you want to see what to use.
            0 //The card's base cost. -1 is X cost, -2 is no cost for unplayable cards like curses, or Reflex.
    );

    public ScalesOfJustice() {
        super(ID, info); //Pass the required information to the BaseCard constructor.
        tags.add(FriskTags.JUSTICE);
        cardsList.add(new LeftScale());
        cardsList.add(new RightScale());
        MultiCardPreview.add(this, new LeftScale(), new RightScale());
    }
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (!upgraded) {
            addToBot(new SelectCardsCenteredAction(cardsList, 1, CardRewardScreen.TEXT[6], (cards) -> addToTop(new MakeTempCardInHandAction(cards.get(0).makeCopy(), true))));
        } else{
            addToBot(new MakeTempCardInHandAction(new LeftScale()));
            addToBot(new MakeTempCardInHandAction(new RightScale()));
        }
    }

    @Override
    public void upp() {

    }
}
