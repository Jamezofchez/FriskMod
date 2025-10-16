package friskmod.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.BufferPower;
import friskmod.actions.CustomSFXAction;
import friskmod.character.Frisk;
import friskmod.powers.TornNotebookPower;
import friskmod.util.CardStats;
import friskmod.util.FriskTags;


import static friskmod.FriskMod.makeID;

public class TornNotebook extends AbstractCriticalCard {
    public static final String ID = makeID(TornNotebook.class.getSimpleName()); //makeID adds the mod ID, so the final ID will be something like "modID:MyCard"
    //These will be used in the constructor. Technically you can just use the values directly,
    //but constants at the top of the file are easy to adjust.

    private static final CardStats info = new CardStats(
            Frisk.Meta.Enums.CARD_COLOR, //The card color. If you're making your own character, it'll look something like this. Otherwise, it'll be CardColor.RED or similar for a basegame character color.
            CardType.POWER, //The type. ATTACK/SKILL/POWER/CURSE/STATUS
            CardRarity.RARE, //Rarity. BASIC is for starting cards, then there's COMMON/UNCOMMON/RARE, and then SPECIAL and CURSE. SPECIAL is for cards you only get from events. Curse is for curses, except for special curses like Curse of the Bell and Necronomicurse.
            CardTarget.SELF, //The target. Single target is ENEMY, all enemies is ALL_ENEMY. Look at cards similar to what you want to see what to use.
            3 //The card's base cost. -1 is X cost, -2 is no cost for unplayable cards like curses, or Reflex.
    );
    private static final int REGAIN_BUFFER_AMOUNT = 2;
    private static final int CRITICAL_BUFFER_AMOUNT = 1;
    private static final int UPG_REGAIN_BUFFER_AMOUNT = 1;

    public TornNotebook() {
        super(ID, info); //Pass the required information to the BaseCard constructor.
        baseMagicNumber = magicNumber = REGAIN_BUFFER_AMOUNT;
        baseSecondMagic = secondMagic = CRITICAL_BUFFER_AMOUNT;
        tags.add(FriskTags.PERSEVERANCE);
    }
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new CustomSFXAction("mus_sfx_bookspin"));
        if (isCritical()) {
            TriggerCriticalEffect(p, m);
        }
        addToBot(new ApplyPowerAction(p, p, new TornNotebookPower(p, magicNumber), magicNumber));
    }
    @Override
    public void TriggerCriticalEffect(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new BufferPower(p, secondMagic), secondMagic));
    }

    @Override
    public void upp() {
        upgradeMagicNumber(UPG_REGAIN_BUFFER_AMOUNT);
    }
}
