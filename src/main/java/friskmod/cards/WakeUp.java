package friskmod.cards;

import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import friskmod.actions.ScryBlockExhaustAction;
import friskmod.character.Frisk;
import friskmod.util.CardStats;
import friskmod.util.FriskTags;
import friskmod.util.interfaces.AfterCardPlayedInterface;


import static friskmod.FriskMod.makeID;

public class WakeUp extends AbstractEasyCard implements AfterCardPlayedInterface {
    public static final String ID = makeID(WakeUp.class.getSimpleName()); //makeID adds the mod ID, so the final ID will be something like "modID:MyCard"
    //These will be used in the constructor. Technically you can just use the values directly,
    //but constants at the top of the file are easy to adjust.

    private static final CardStats info = new CardStats(
            Frisk.Meta.Enums.CARD_COLOR, //The card color. If you're making your own character, it'll look something like this. Otherwise, it'll be CardColor.RED or similar for a basegame character color.
            CardType.SKILL, //The type. ATTACK/SKILL/POWER/CURSE/STATUS
            CardRarity.UNCOMMON, //Rarity. BASIC is for starting cards, then there's COMMON/UNCOMMON/RARE, and then SPECIAL and CURSE. SPECIAL is for cards you only get from events. Curse is for curses, except for special curses like Curse of the Bell and Necronomicurse.
            CardTarget.SELF, //The target. Single target is ENEMY, all enemies is ALL_ENEMY. Look at cards similar to what you want to see what to use.
            1 //The card's base cost. -1 is X cost, -2 is no cost for unplayable cards like curses, or Reflex.
    );
    private static final int SCRY_AMOUNT = 5;
    private static final int BLOCK = 3;
//    private static final int UPG_SCRY_AMOUNT = 2;
    private static final int UPG_BLOCK_AMOUNT = 2;




//    private int cardsPlayedThisTurn;

    public WakeUp() {
        super(ID, info); //Pass the required information to the BaseCard constructor.
        baseMagicNumber = magicNumber = BLOCK;
        baseSecondMagic = secondMagic = SCRY_AMOUNT;
        tags.add(FriskTags.PERSEVERANCE);
//        this.cardsPlayedThisTurn = 0;
    }
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ScryBlockExhaustAction(secondMagic, magicNumber));
    }

    @Override
    public void upp() {
        upgradeMagicNumber(UPG_BLOCK_AMOUNT);
    }

    public void afterCardPlayed(AbstractCard card) {
//        ++this.cardsPlayedThisTurn;
//        if (this.cardsPlayedThisTurn < magicNumber){
//            addToBot(new ExhaustSpecificCardAction(this, AbstractDungeon.player.hand));
//        }
//        addToBot(new ExhaustSpecificCardAction(this, AbstractDungeon.player.hand));
        this.setCostForTurn(this.costForTurn + 1);
    }



}
