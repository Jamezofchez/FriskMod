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

public class FreakBullet extends AbstractEasyCard{
    public static final String ID = makeID(FreakBullet.class.getSimpleName()); //makeID adds the mod ID, so the final ID will be something like "modID:MyCard"
    private static final CardStats info = new CardStats(
            Frisk.Meta.Enums.CARD_COLOR, //The card color. If you're making your own character, it'll look something like this. Otherwise, it'll be CardColor.RED or similar for a basegame character color.
            CardType.ATTACK, //The type. ATTACK/SKILL/POWER/CURSE/STATUS
            CardRarity.SPECIAL, //Rarity. BASIC is for starting cards, then there's COMMON/UNCOMMON/RARE, and then SPECIAL and CURSE. SPECIAL is for cards you only get from events. Curse is for curses, except for special curses like Curse of the Bell and Necronomicurse.
            CardTarget.ENEMY, //The target. Single target is ENEMY, all enemies is ALL_ENEMY. Look at cards similar to what you want to see what to use.
            0 //The card's base cost. -1 is X cost, -2 is no cost for unplayable cards like curses, or Reflex.
    );
    private static final int DAMAGE = 4;
    private static final int INHERENT_XP = 5;
    private static final int UPG_XP = 2;
    public FreakBullet() {
        super(ID, info); //Pass the required information to the BaseCard constructor.
        baseMagicNumber = INHERENT_XP;
        magicNumber = baseMagicNumber;
        baseDamage = DAMAGE;
        CardXPFields.setInherentXP(this, magicNumber);
    }
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new CustomSFXAction("mus_f_noise"));
        dmg(m, AbstractGameAction.AttackEffect.SLASH_DIAGONAL);

    }

    @Override
    public void upp() {
        upgradeMagicNumber(UPG_XP);
        CardXPFields.setInherentXP(this, magicNumber);
    }
}
