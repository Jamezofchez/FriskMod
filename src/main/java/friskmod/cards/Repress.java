package friskmod.cards;

import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import friskmod.character.Frisk;
import friskmod.powers.Karma;
import friskmod.util.CardStats;
import friskmod.util.FriskTags;
import friskmod.util.Wiz;


import static friskmod.FriskMod.makeID;

public class Repress extends AbstractEasyCard {
    public static final String ID = makeID(Repress.class.getSimpleName()); //makeID adds the mod ID, so the final ID will be something like "modID:MyCard"
    //These will be used in the constructor. Technically you can just use the values directly,
    //but constants at the top of the file are easy to adjust.

    private static final CardStats info = new CardStats(
            Frisk.Meta.Enums.CARD_COLOR, //The card color. If you're making your own character, it'll look something like this. Otherwise, it'll be CardColor.RED or similar for a basegame character color.
            CardType.SKILL, //The type. ATTACK/SKILL/POWER/CURSE/STATUS
            CardRarity.UNCOMMON, //Rarity. BASIC is for starting cards, then there's COMMON/UNCOMMON/RARE, and then SPECIAL and CURSE. SPECIAL is for cards you only get from events. Curse is for curses, except for special curses like Curse of the Bell and Necronomicurse.
            CardTarget.ALL, //The target. Single target is ENEMY, all enemies is ALL_ENEMY. Look at cards similar to what you want to see what to use.
            2 //The card's base cost. -1 is X cost, -2 is no cost for unplayable cards like curses, or ReSanchita.
    );
    private static final int BLOCK = 15;
    private static final int UPG_BLOCK = 2;
    private static final int TEMP_HP = 5;
    private static final int UPG_TEMP_HP = 2;
    private static final int KARMA_AMOUNT = 2;


    public Repress() {
        super(ID, info); //Pass the required information to the BaseCard constructor.
        baseMagicNumber = magicNumber = TEMP_HP;
        baseSecondMagic = secondMagic = KARMA_AMOUNT;
        baseBlock = BLOCK;
        tags.add(FriskTags.JUSTICE);
    }
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, p, block));
        for(AbstractMonster monster : Wiz.getMonsters()) {
            addToBot(new GainBlockAction(monster, p, block));
        }
        addToBot(new AddTemporaryHPAction(p, p, magicNumber));
        for(AbstractMonster monster : Wiz.getMonsters()) {
            addToBot(new AddTemporaryHPAction(monster, p, magicNumber));
        }
        addToBot(new ApplyPowerAction(p, p, new Karma(p, secondMagic), secondMagic));
        for(AbstractMonster monster : Wiz.getMonsters()) {
            addToBot(new ApplyPowerAction(monster, p, new Karma(monster, secondMagic), secondMagic));
        }

    }

    @Override
    public void upp() {
        upgradeBlock(UPG_BLOCK);
        upgradeMagicNumber(UPG_TEMP_HP);
    }
}
