package friskmod.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import friskmod.character.Frisk;
import friskmod.powers.*;
import friskmod.util.CardStats;
import friskmod.util.FriskTags;


import static friskmod.FriskMod.makeID;

public class Pounce extends AbstractEasyCard {
    public static final String ID = makeID(Pounce.class.getSimpleName()); //makeID adds the mod ID, so the final ID will be something like "modID:MyCard"
    //These will be used in the constructor. Technically you can just use the values directly,
    //but constants at the top of the file are easy to adjust.

    private static final CardStats info = new CardStats(
            Frisk.Meta.Enums.CARD_COLOR, //The card color. If you're making your own character, it'll look something like this. Otherwise, it'll be CardColor.RED or similar for a basegame character color.
            CardType.SKILL, //The type. ATTACK/SKILL/POWER/CURSE/STATUS
            CardRarity.COMMON, //Rarity. BASIC is for starting cards, then there's COMMON/UNCOMMON/RARE, and then SPECIAL and CURSE. SPECIAL is for cards you only get from events. Curse is for curses, except for special curses like Curse of the Bell and Necronomicurse.
            CardTarget.ENEMY, //The target. Single target is ENEMY, all enemies is ALL_ENEMY. Look at cards similar to what you want to see what to use.
            1 //The card's base cost. -1 is X cost, -2 is no cost for unplayable cards like curses, or RePounce.
    );
    public static final int DAMAGE = 10;
    public static final int UPG_DAMAGE = 4;
    public static final int WAIT_TIMER = 5;


    public Pounce() {
        super(ID, info); //Pass the required information to the BaseCard constructor.
        baseMagicNumber = magicNumber = DAMAGE;
        baseSecondMagic = secondMagic = WAIT_TIMER;
        tags.add(FriskTags.PATIENCE);
    }
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractCountdownPower countdown = new CountdownPounce(m, DAMAGE, secondMagic, UPG_DAMAGE);
        addToBot(new ApplyPowerAction(m, p, countdown, DAMAGE));
        if (upgraded) {
            countdown.upgrade();
        }
    }

    @Override
    public void upp() {
        upgradeMagicNumber(UPG_DAMAGE); //only for card rendering
    }
}
