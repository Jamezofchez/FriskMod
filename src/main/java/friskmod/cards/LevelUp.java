package friskmod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import friskmod.actions.CustomSFXAction;
import friskmod.actions.UpgradeWithXPAction;
import friskmod.character.Frisk;
import friskmod.patches.CardXPFields;
import friskmod.powers.LV_Hero;
import friskmod.util.CardStats;
import friskmod.util.FriskTags;

import static friskmod.FriskMod.makeID;
@SuppressWarnings("unused")
public class LevelUp extends AbstractEasyCard {
    public static final String ID = makeID(LevelUp.class.getSimpleName()); //makeID adds the mod ID, so the final ID will be something like "modID:MyCard"
    //These will be used in the constructor. Technically you can just use the values directly,
    //but constants at the top of the file are easy to adjust.
//    private static final int DAMAGE = 5;
//    private static final int UPG_DAMAGE = 3;
    private static final int BLOCK = 5;
    private static final int UPG_BLOCK = 3;

    private static final CardStats info = new CardStats(
            Frisk.Meta.Enums.CARD_COLOR, //The card color. If you're making your own character, it'll look something like this. Otherwise, it'll be CardColor.RED or similar for a basegame character color.
            CardType.SKILL, //The type. ATTACK/SKILL/POWER/CURSE/STATUS
            CardRarity.COMMON, //Rarity. BASIC is for starting cards, then there's COMMON/UNCOMMON/RARE, and then SPECIAL and CURSE. SPECIAL is for cards you only get from events. Curse is for curses, except for special curses like Curse of the Bell and Necronomicurse.
            CardTarget.SELF, //The target. Single target is ENEMY, all enemies is ALL_ENEMY. Look at cards similar to what you want to see what to use.
            1 //The card's base cost. -1 is X cost, -2 is no cost for unplayable cards like curses, or Reflex.
    );
    public LevelUp() {
        super(ID, info); //Pass the required information to the BaseCard constructor.
        baseBlock = BLOCK;
        tags.add(FriskTags.BRAVERY);
    }
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
//        if (!upgraded){ //allow upgrade if has XP?
//            if (CardXPFields.getCardXPBool(this)) {
//                upgrade();
//            } else{
//                AbstractPower posspow = p.getPower(LV_Hero.POWER_ID);
//                if (posspow != null) {
//                    if (posspow.amount > 0) {
//                        upgrade();
//                    }
//                }
//            }
//        }
        dmg(m, AbstractGameAction.AttackEffect.SLASH_DIAGONAL);
        addToBot(new UpgradeWithXPAction(upgraded));
        addToBot(new CustomSFXAction("snd_levelup"));
    }

    @Override
    public void upp() {
        upgradeBlock(UPG_BLOCK);
    }
}
