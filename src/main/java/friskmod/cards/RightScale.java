package friskmod.cards;

import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;
import com.evacipated.cardcrawl.mod.stslib.patches.BindingPatches;
import com.evacipated.cardcrawl.mod.stslib.patches.core.AbstractCreature.TempHPField;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.SearingBlowEffect;
import friskmod.character.Frisk;
import friskmod.util.CardStats;
import friskmod.util.FriskTags;

import static friskmod.FriskMod.makeID;

public class RightScale extends AbstractEasyCard {
    public static final String ID = makeID(RightScale.class.getSimpleName()); //makeID adds the mod ID, so the final ID will be something like "modID:MyCard"
    //These will be used in the constructor. Technically you can just use the values directly,
    //but constants at the top of the file are easy to adjust.
    private static final CardStats info = new CardStats(
            CardColor.COLORLESS, //The card color. If you're making your own character, it'll look something like this. Otherwise, it'll be CardColor.RED or similar for a basegame character color.
            CardType.ATTACK, //The type. ATTACK/SKILL/POWER/CURSE/STATUS
            CardRarity.SPECIAL, //Rarity. BASIC is for starting cards, then there's COMMON/UNCOMMON/RARE, and then SPECIAL and CURSE. SPECIAL is for cards you only get from events. Curse is for curses, except for special curses like Curse of the Bell and Necronomicurse.
            CardTarget.ENEMY, //The target. Single target is ENEMY, all enemies is ALL_ENEMY. Look at cards similar to what you want to see what to use.
            2 //The card's base cost. -1 is X cost, -2 is no cost for unplayable cards like curses, or Reflex.
    );
    private static final int DAMAGE = 0;
    private static final int TEMP_HP = 4;
    private static final int UPG_TEMP_HP = 3;


    private int getBaseDamage() {
        return TempHPField.tempHp.get(AbstractDungeon.player);
    }

    public RightScale() {
        super(ID, info); //Pass the required information to the BaseCard constructor.
        baseDamage = DAMAGE;
        magicNumber = baseMagicNumber = TEMP_HP;
        tags.add(FriskTags.JUSTICE);
        this.exhaust = true;
    }
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.baseDamage = getBaseDamage();
        this.calculateCardDamage(m);
        addToBot(new AddTemporaryHPAction(p, p, magicNumber));
        if (m != null) {
            addToBot(new VFXAction(new SearingBlowEffect(m.hb.cX, m.hb.cY, effectScale()), 0.2F));
        }
        dmg(m, AbstractGameAction.AttackEffect.BLUNT_HEAVY);
        initializeDescription();
    }

    public void applyPowers() {
        this.baseDamage = getBaseDamage();
        super.applyPowers();
        this.rawDescription = cardStrings.DESCRIPTION;
        this.rawDescription += cardStrings.EXTENDED_DESCRIPTION[0];
        this.initializeDescription();
    }

    public void onMoveToDiscard() {
        initializeDescription();
    }

    public void calculateCardDamage(AbstractMonster mo) {
        super.calculateCardDamage(mo);
        this.rawDescription = cardStrings.DESCRIPTION;
        this.rawDescription += cardStrings.EXTENDED_DESCRIPTION[0];
        initializeDescription();
    }

    private int effectScale() {
        //damage <-> 12 + 4 + times upgraded
        //(damage - 12) // 4 <-> times upgraded
        return Math.max(damage-12,0) / 4;

    }

    @Override
    public void upp() {
        upgradeMagicNumber(UPG_TEMP_HP);
    }
}
