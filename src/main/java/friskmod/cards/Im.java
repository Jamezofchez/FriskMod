package friskmod.cards;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.unique.LoseEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import friskmod.character.Frisk;
import friskmod.util.CardStats;
import friskmod.util.FriskTags;
import basemod.ReflectionHacks;


import static friskmod.FriskMod.makeID;

public class Im extends AbstractEasyCard {
    public static final String ID = makeID(Im.class.getSimpleName()); //makeID adds the mod ID, so the final ID will be something like "modID:MyCard"
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(ID);
    public static final String[] TEXT = uiStrings.TEXT;
    private int cardsPlayed = 0;
    //These will be used in the constructor. Technically you can just use the values directly,
    //but constants at the top of the file are easy to adjust.

    private static final CardStats info = new CardStats(
            CardColor.COLORLESS, //The card color. If you're making your own character, it'll look something like this. Otherwise, it'll be CardColor.RED or similar for a basegame character color.
            CardType.STATUS, //The type. ATTACK/SKILL/POWER/CURSE/STATUS
            CardRarity.COMMON, //Rarity. BASIC is for starting cards, then there's COMMON/UNCOMMON/RARE, and then SPECIAL and CURSE. SPECIAL is for cards you only get from events. Curse is for curses, except for special curses like Curse of the Bell and Necronomicurse.
            CardTarget.NONE, //The target. Single target is ENEMY, all enemies is ALL_ENEMY. Look at cards similar to what you want to see what to use.
            -2 //The card's base cost. -1 is X cost, -2 is no cost for unplayable cards like curses, or Reflex.
    );
    public Im() {
        super(ID, info); //Pass the required information to the BaseCard constructor.
        this.isEthereal = true;
    }
    @Override
    public void resetAttributes() {
        super.resetAttributes();
        cardsPlayed = 0;
    }

    @Override
    public void onPlayCard(AbstractCard card, AbstractMonster m) {
        ++cardsPlayed;
    }


    @Override
    public boolean canPlay(AbstractCard card) {
        if (cardsPlayed >= 1) {
            card.cantUseMessage = TEXT[0];
            return false;
        }
        return true;
    }
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
    }

    @Override
    public void upp() {
        --cardsPlayed;
    }
}
