package friskmod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.defect.SunderAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.combat.LightningEffect;
import com.megacrit.cardcrawl.vfx.combat.WeightyImpactEffect;
import friskmod.actions.CustomSFXAction;
import friskmod.actions.ThunderingRageAction;
import friskmod.character.Frisk;
import friskmod.util.CardStats;
import friskmod.util.FriskTags;
import friskmod.util.Wiz;


import java.util.List;

import static friskmod.FriskMod.makeID;

public class ThunderingRage extends AbstractEasyCard {
    public static final String ID = makeID(ThunderingRage.class.getSimpleName()); //makeID adds the mod ID, so the final ID will be something like "modID:MyCard"
    //These will be used in the constructor. Technically you can just use the values directly,
    //but constants at the top of the file are easy to adjust.
    private static final CardStats info = new CardStats(
            Frisk.Meta.Enums.CARD_COLOR, //The card color. If you're making your own character, it'll look something like this. Otherwise, it'll be CardColor.RED or similar for a basegame character color.
            CardType.ATTACK, //The type. ATTACK/SKILL/POWER/CURSE/STATUS
            CardRarity.RARE, //Rarity. BASIC is for starting cards, then there's COMMON/UNCOMMON/RARE, and then SPECIAL and CURSE. SPECIAL is for cards you only get from events. Curse is for curses, except for special curses like Curse of the Bell and Necronomicurse.
            CardTarget.ALL_ENEMY, //The target. Single target is ENEMY, all enemies is ALL_ENEMY. Look at cards similar to what you want to see what to use.
            3 //The card's base cost. -1 is X cost, -2 is no cost for unplayable cards like curses, or Reflex.
    );
    private static final int DAMAGE = 23;
    private static final int UPG_DAMAGE = 4;
    private static final int PERSEVERE_AMOUNT = 1;
    private static final int UPG_DAMAGE_PERSEVERE_AMOUNT = 1;

    public ThunderingRage() {
        super(ID, info); //Pass the required information to the BaseCard constructor.
        baseDamage = DAMAGE;
        baseMagicNumber = magicNumber = PERSEVERE_AMOUNT;
        tags.add(FriskTags.PERSEVERANCE);
        this.isMultiDamage = true;
    }
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        List<AbstractMonster> monsters = Wiz.getMonsters();
        Wiz.att(new CustomSFXAction("snd_shock"));
        final float speedTime = 0.1F;
        for (AbstractMonster monster : monsters) {
            Wiz.att(new VFXAction(new LightningEffect(monster.drawX, monster.drawY), speedTime));
        }
        int aliveAmount = monsters.size();
        addToBot(new DamageAllEnemiesAction(p, this.multiDamage, DamageInfo.DamageType.NORMAL, AbstractGameAction.AttackEffect.NONE));
        addToBot(new ThunderingRageAction(aliveAmount, magicNumber));
    }

    @Override
    public void upp() {
        upgradeDamage(UPG_DAMAGE);
//        upgradeMagicNumber(UPG_DAMAGE_PERSEVERE_AMOUNT);
    }
}
