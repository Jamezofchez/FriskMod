package friskmod.cards;

import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;
import com.evacipated.cardcrawl.mod.stslib.cards.interfaces.OnObtainCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ExhaustAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import friskmod.character.Frisk;
import friskmod.util.CardStats;
import friskmod.util.FriskTags;

import static friskmod.FriskMod.makeID;

public class Bandage extends AbstractCriticalCard implements OnObtainCard {
    public static final String ID = makeID(Bandage.class.getSimpleName()); //makeID adds the mod ID, so the final ID will be something like "modID:MyCard"
    //These will be used in the constructor. Technically you can just use the values directly,
    //but constants at the top of the file are easy to adjust.

    private static final CardStats info = new CardStats(
            Frisk.Meta.Enums.CARD_COLOR, //The card color. If you're making your own character, it'll look something like this. Otherwise, it'll be CardColor.RED or similar for a basegame character color.
            CardType.SKILL, //The type. ATTACK/SKILL/POWER/CURSE/STATUS
            CardRarity.COMMON, //Rarity. BASIC is for starting cards, then there's COMMON/UNCOMMON/RARE, and then SPECIAL and CURSE. SPECIAL is for cards you only get from events. Curse is for curses, except for special curses like Curse of the Bell and Necronomicurse.
            CardTarget.SELF, //The target. Single target is ENEMY, all enemies is ALL_ENEMY. Look at cards similar to what you want to see what to use.
            1 //The card's base cost. -1 is X cost, -2 is no cost for unplayable cards like curses, or Reflex.
    );
    private static final int TEMP_HP = 4;
    private static final int UPG_TEMP_HP = 2;
    private static final int PICKUP_HP = 4;
    private static final int UPG_PICKUP_HP = 2;

    public Bandage() {
        super(ID, info); //Pass the required information to the BaseCard constructor.
        baseMagicNumber = magicNumber = TEMP_HP;
        baseSecondMagic = secondMagic = PICKUP_HP;
        tags.add(FriskTags.INTEGRITY);
    }
    @Override
    public void CriticalEffect(AbstractPlayer p, AbstractMonster m) {
        addToTop(new AddTemporaryHPAction(p, p, magicNumber));
        addToBot(new ExhaustAction(1, true));
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (isCritical()){
            TriggerCriticalEffect(p, m);
        }
        super.use(p, m);
    }

    @Override
    public void onObtainCard() {
        AbstractDungeon.player.heal(secondMagic);
    }

    @Override
    public void upp() {
        upgradeMagicNumber(UPG_TEMP_HP);
        upgradeSecondMagic(UPG_PICKUP_HP);
    }
}
