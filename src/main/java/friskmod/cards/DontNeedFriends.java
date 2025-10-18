package friskmod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import friskmod.actions.CustomSFXAction;
import friskmod.actions.FlurryOfKnivesAction;
import friskmod.character.Frisk;
import friskmod.util.CardStats;
import friskmod.util.FriskTags;


import static friskmod.FriskMod.makeID;
import static friskmod.vfx.ThrowDaggerEffect.throwDaggerEffect;

public class DontNeedFriends extends AbstractEasyCard {
    public static final String ID = makeID(DontNeedFriends.class.getSimpleName()); //makeID adds the mod ID, so the final ID will be something like "modID:MyCard"
    //These will be used in the constructor. Technically you can just use the values directly,
    //but constants at the top of the file are easy to adjust.
    private static final CardStats info = new CardStats(
            Frisk.Meta.Enums.CARD_COLOR, //The card color. If you're making your own character, it'll look something like this. Otherwise, it'll be CardColor.RED or similar for a basegame character color.
            CardType.ATTACK, //The type. ATTACK/SKILL/POWER/CURSE/STATUS
            CardRarity.UNCOMMON, //Rarity. BASIC is for starting cards, then there's COMMON/UNCOMMON/RARE, and then SPECIAL and CURSE. SPECIAL is for cards you only get from events. Curse is for curses, except for special curses like Curse of the Bell and Necronomicurse.
            CardTarget.ENEMY, //The target. Single target is ENEMY, all enemies is ALL_ENEMY. Look at cards similar to what you want to see what to use.
            1 //The card's base cost. -1 is X cost, -2 is no cost for unplayable cards like curses, or Reflex.
    );
    private static final int DAMAGE = 12;
    private static final int UPG_DAMAGE = 3;
    private static final int DRAW = 2;
    private static final int CARD_GAIN = 1;


    public DontNeedFriends() {
        super(ID, info); //Pass the required information to the BaseCard constructor.
        baseDamage = DAMAGE;
        baseMagicNumber = magicNumber = DRAW;
        tags.add(FriskTags.JUSTICE);
        this.cardsToPreview = new Im();
    }
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        throwDaggerEffect(0.0F);
        dmg(m, AbstractGameAction.AttackEffect.SLASH_VERTICAL);
        addToBot(new DrawCardAction(p, magicNumber));
        addToBot(new MakeTempCardInHandAction(new Im(), CARD_GAIN));
    }

    @Override
    public void upp() {
        upgradeDamage(UPG_DAMAGE);
    }
}
