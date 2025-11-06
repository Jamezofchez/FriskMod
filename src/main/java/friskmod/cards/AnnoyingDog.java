package friskmod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import friskmod.FriskMod;
import friskmod.actions.CustomSFXAction;
import friskmod.character.Frisk;
import friskmod.patches.perseverance.PerseveranceFields;
import friskmod.util.CardStats;
import friskmod.util.FriskTags;
import friskmod.util.Wiz;
import friskmod.util.interfaces.AfterCardPlayedInterface;


import java.util.ArrayList;

import static friskmod.FriskMod.makeID;

public class AnnoyingDog extends AbstractCriticalCard implements AfterCardPlayedInterface {
    public static final String ID = makeID(AnnoyingDog.class.getSimpleName()); //makeID adds the mod ID, so the final ID will be something like "modID:MyCard"
    //These will be used in the constructor. Technically you can just use the values directly,
    //but constants at the top of the file are easy to adjust.
    private static final CardStats info = new CardStats(
            Frisk.Meta.Enums.CARD_COLOR, //The card color. If you're making your own character, it'll look something like this. Otherwise, it'll be CardColor.RED or similar for a basegame character color.
            CardType.ATTACK, //The type. ATTACK/SKILL/POWER/CURSE/STATUS
            CardRarity.UNCOMMON, //Rarity. BASIC is for starting cards, then there's COMMON/UNCOMMON/RARE, and then SPECIAL and CURSE. SPECIAL is for cards you only get from events. Curse is for curses, except for special curses like Curse of the Bell and Necronomicurse.
            CardTarget.ENEMY, //The target. Single target is ENEMY, all enemies is ALL_ENEMY. Look at cards similar to what you want to see what to use.
            0 //The card's base cost. -1 is X cost, -2 is no cost for unplayable cards like curses, or Reflex.
    );
    private static final int DAMAGE_AND_BLOCK = 10;
    private static final int UPG_DAMAGE_AND_BLOCK = 3;
    public AnnoyingDog() {
        super(ID, info); //Pass the required information to the BaseCard constructor.
        baseDamage = DAMAGE_AND_BLOCK;
        baseBlock = DAMAGE_AND_BLOCK;
        tags.add(FriskTags.PERSEVERANCE);
        PerseveranceFields.trapped.set(this, true);
        initializeDescription();
    }

    @Override
    public void triggerOnEndOfPlayerTurn() {
        super.triggerOnEndOfPlayerTurn();
        PerseveranceFields.trapped.set(this, true);
        initializeDescription();
    }


    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        blck();
        dmg(m, AbstractGameAction.AttackEffect.SLASH_VERTICAL);
    }

    @Override
    public void upp() {
        upgradeDamage(UPG_DAMAGE_AND_BLOCK);
        upgradeBlock(UPG_DAMAGE_AND_BLOCK);
    }

    @Override
    public void CriticalEffect(AbstractPlayer p, AbstractMonster m) {

    }

    @Override
    public void afterCardPlayed(AbstractCard cardPlayed) {
        untrapOnCritical();
        if (cardPlayed.cardID.equals(BreakFree.ID)){
            PerseveranceFields.trapped.set(this, false);
        }
        initializeDescription();
    }

    public void triggerWhenDrawn() {
        Wiz.actB(this::untrapOnCritical);
    }

    private void untrapOnCritical() {
        PerseveranceFields.trapped.set(this, true);
        if (isCritical()) {
            PerseveranceFields.trapped.set(this, false);
        }
    }
}
