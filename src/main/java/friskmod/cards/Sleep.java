package friskmod.cards;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import friskmod.character.Frisk;
import friskmod.powers.AbstractCountdownPower;
import friskmod.util.CardStats;
import friskmod.util.FriskTags;


import static friskmod.FriskMod.makeID;

public class Sleep extends AbstractEasyCard {
    public static final String ID = makeID(Sleep.class.getSimpleName()); //makeID adds the mod ID, so the final ID will be something like "modID:MyCard"
    //These will be used in the constructor. Technically you can just use the values directly,
    //but constants at the top of the file are easy to adjust.

    private static final CardStats info = new CardStats(
            Frisk.Meta.Enums.CARD_COLOR, //The card color. If you're making your own character, it'll look something like this. Otherwise, it'll be CardColor.RED or similar for a basegame character color.
            CardType.SKILL, //The type. ATTACK/SKILL/POWER/CURSE/STATUS
            CardRarity.UNCOMMON, //Rarity. BASIC is for starting cards, then there's COMMON/UNCOMMON/RARE, and then SPECIAL and CURSE. SPECIAL is for cards you only get from events. Curse is for curses, except for special curses like Curse of the Bell and Necronomicurse.
            CardTarget.SELF, //The target. Single target is ENEMY, all enemies is ALL_ENEMY. Look at cards similar to what you want to see what to use.
            0 //The card's base cost. -1 is X cost, -2 is no cost for unplayable cards like curses, or Reflex.
    );
    private static final int INCREASE_COUNTDOWNS = 4;

    public Sleep() {
        super(ID, info); //Pass the required information to the BaseCard constructor.
        baseMagicNumber = magicNumber = INCREASE_COUNTDOWNS;
        tags.add(FriskTags.PATIENCE);
        this.exhaust = true;
    }
    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        for (AbstractCreature m : (AbstractDungeon.getMonsters()).monsters) {
            for (AbstractPower p : m.powers) {
                if (p instanceof AbstractCountdownPower) {
                    ((AbstractCountdownPower) p).addCountdown(magicNumber);
                    ((AbstractCountdownPower) p).upgrade();
                }
            }
        }
        for (AbstractPower p : player.powers) {
            if (p instanceof AbstractCountdownPower) {
                ((AbstractCountdownPower) p).addCountdown(magicNumber);
                ((AbstractCountdownPower) p).upgrade();
            }
        }
    }

    @Override
    public void upp() {
        this.exhaust = false;
    }
}
