package friskmod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import friskmod.actions.CustomSFXAction;
import friskmod.character.Frisk;
import friskmod.util.CardStats;
import friskmod.patches.CardXPFields;

import static friskmod.FriskMod.makeID;

public class SlackOff extends AbstractEasyCard{
    public static final String ID = makeID(SlackOff.class.getSimpleName()); //makeID adds the mod ID, so the final ID will be something like "modID:MyCard"
    private static final CardStats info = new CardStats(
            Frisk.Meta.Enums.CARD_COLOR, //The card color. If you're making your own character, it'll look something like this. Otherwise, it'll be CardColor.RED or similar for a basegame character color.
            CardType.ATTACK, //The type. ATTACK/SKILL/POWER/CURSE/STATUS
            CardRarity.UNCOMMON, //Rarity. BASIC is for starting cards, then there's COMMON/UNCOMMON/RARE, and then SPECIAL and CURSE. SPECIAL is for cards you only get from events. Curse is for curses, except for special curses like Curse of the Bell and Necronomicurse.
            CardTarget.ENEMY, //The target. Single target is ENEMY, all enemies is ALL_ENEMY. Look at cards similar to what you want to see what to use.
            2 //The card's base cost. -1 is X cost, -2 is no cost for unplayable cards like curses, or Reflex.
    );
    private static final int DAMAGE = 24;
    private static final int UPG_DAMAGE = 4;
    private static final int ADDED_XP = 10;
    private static final int UPG_ADDED_XP = 4;

    private boolean serious = false;

    public SlackOff() {
        super(ID, info); //Pass the required information to the BaseCard constructor.
        baseMagicNumber = magicNumber = ADDED_XP;
        baseDamage = DAMAGE;
        serious = false;
        CardXPFields.setAddedXP(this, magicNumber);
    }

    public void setSeriousDescription() {
        serious = true;
        initializeDescription();
    }

    public void setNormalDescription() {
        serious = false;
        initializeDescription();
    }

    public void initializeTitle() {
        if (cardStrings != null) {
            if (serious) {
                name = cardStrings.EXTENDED_DESCRIPTION[0];
            } else {
                name = cardStrings.NAME;
            }
        }
        super.initializeTitle();
    }
    public void initializeDescription() {
        if (cardStrings != null) {
            if (serious) {
                rawDescription = cardStrings.EXTENDED_DESCRIPTION[1];
            } else {
                rawDescription = cardStrings.DESCRIPTION;
            }
            initializeTitle();
        }
        super.initializeDescription();
    }


    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        dmg(m, AbstractGameAction.AttackEffect.BLUNT_HEAVY);
    }

    @Override
    public void upp() {
        upgradeMagicNumber(UPG_ADDED_XP);
        upgradeDamage(UPG_DAMAGE);
        CardXPFields.setAddedXP(this, magicNumber);
    }
}
