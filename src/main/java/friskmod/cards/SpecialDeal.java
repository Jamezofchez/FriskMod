package friskmod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import friskmod.actions.CustomSFXAction;
import friskmod.actions.SpecialDealAction;
import friskmod.character.Frisk;
import friskmod.util.CardStats;
import friskmod.util.FriskTags;
import friskmod.util.Wiz;


import static friskmod.FriskMod.makeID;

public class SpecialDeal extends AbstractEasyCard {
    public static final String ID = makeID(SpecialDeal.class.getSimpleName()); //makeID adds the mod ID, so the final ID will be something like "modID:MyCard"
    //These will be used in the constructor. Technically you can just use the values directly,
    //but constants at the top of the file are easy to adjust.

    public static final int NUM_CYCLES = 3;

    public static final int DEFAULT_GOLD = 19;

    public static final int JACKPOT_GOLD = 97;

    public static final int PLEADED_AND_ARTIFACT = 2;

    public static final int UPG_COST = 1;


    private static final CardStats info = new CardStats(
            Frisk.Meta.Enums.CARD_COLOR, //The card color. If you're making your own character, it'll look something like this. Otherwise, it'll be CardColor.RED or similar for a basegame character color.
            CardType.POWER, //The type. ATTACK/SKILL/POWER/CURSE/STATUS
            CardRarity.UNCOMMON, //Rarity. BASIC is for starting cards, then there's COMMON/UNCOMMON/RARE, and then SPECIAL and CURSE. SPECIAL is for cards you only get from events. Curse is for curses, except for special curses like Curse of the Bell and Necronomicurse.
            CardTarget.SELF, //The target. Single target is ENEMY, all enemies is ALL_ENEMY. Look at cards similar to what you want to see what to use.
            2 //The card's base cost. -1 is X cost, -2 is no cost for unplayable cards like curses, or Reflex.
    );

    public SpecialDeal() {
        super(ID, info); //Pass the required information to the BaseCard constructor.
        this.baseMagicNumber = magicNumber = PLEADED_AND_ARTIFACT;
        this.isEthereal = true;
        tags.add(CardTags.HEALING);
        tags.add(FriskTags.KINDNESS);
    }
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new SpecialDealAction(magicNumber)); //45 gold avg
    }

    @Override
    public void upp() {
        upgradeBaseCost(UPG_COST);
    }

}
