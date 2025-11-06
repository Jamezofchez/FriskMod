package friskmod.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.FrailPower;
import friskmod.character.Frisk;
import friskmod.powers.NEO;
import friskmod.util.CardStats;
import friskmod.util.FriskTags;
import friskmod.util.Wiz;

import static friskmod.FriskMod.makeID;

public class Tinker extends AbstractEasyCard {
    public static final String ID = makeID(Tinker.class.getSimpleName()); //makeID adds the mod ID, so the final ID will be something like "modID:MyCard"
    //These will be used in the constructor. Technically you can just use the values directly,
    //but constants at the top of the file are easy to adjust.
    private static final CardStats info = new CardStats(
            Frisk.Meta.Enums.CARD_COLOR, //The card color. If you're making your own character, it'll look something like this. Otherwise, it'll be CardColor.RED or similar for a basegame character color.
            CardType.SKILL, //The type. ATTACK/SKILL/POWER/CURSE/STATUS
            CardRarity.UNCOMMON, //Rarity. BASIC is for starting cards, then there's COMMON/UNCOMMON/RARE, and then SPECIAL and CURSE. SPECIAL is for cards you only get from events. Curse is for curses, except for special curses like Curse of the Bell and Necronomicurse.
            CardTarget.ENEMY, //The target. Single target is ENEMY, all enemies is ALL_ENEMY. Look at cards similar to what you want to see what to use.
            1 //The card's base cost. -1 is X cost, -2 is no cost for unplayable cards like curses, or Reflex.
    );
    private static final int BLOCK = 10;
    private static final int UPG_BLOCK = 5;
    private static final int NEO_AND_FRAIL = 1;

    public Tinker() {
        super(ID, info); //Pass the required information to the BaseCard constructor.
        baseBlock = BLOCK;
        baseMagicNumber = magicNumber = NEO_AND_FRAIL;
        tags.add(FriskTags.KINDNESS);
    }
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(m, p, new FrailPower(m, magicNumber, false), magicNumber));
        addToBot(new ApplyPowerAction(m, p, new NEO(m, magicNumber), magicNumber));
        addToBot(Wiz.actionify(() -> {
            m.applyPowers();
            addToBot(new GainBlockAction(m, p, block));
        }));
    }

    @Override
    public void upp() {
        upgradeBlock(UPG_BLOCK);
    }
}
