package friskmod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.combat.WeightyImpactEffect;
import friskmod.actions.CustomSFXAction;
import friskmod.character.Frisk;
import friskmod.patches.WastedEnergyPatch;
import friskmod.util.CardStats;
import friskmod.util.FriskTags;
import friskmod.util.Wiz;


import static friskmod.FriskMod.makeID;

public class Chill extends AbstractEasyCard {
    public static final String ID = makeID(Chill.class.getSimpleName()); //makeID adds the mod ID, so the final ID will be something like "modID:MyCard"
    //These will be used in the constructor. Technically you can just use the values directly,
    //but constants at the top of the file are easy to adjust.

    private static final int BASE_COST = 4;

    private int newCost = BASE_COST;


    private static final CardStats info = new CardStats(
            Frisk.Meta.Enums.CARD_COLOR, //The card color. If you're making your own character, it'll look something like this. Otherwise, it'll be CardColor.RED or similar for a basegame character color.
            CardType.ATTACK, //The type. ATTACK/SKILL/POWER/CURSE/STATUS
            CardRarity.RARE, //Rarity. BASIC is for starting cards, then there's COMMON/UNCOMMON/RARE, and then SPECIAL and CURSE. SPECIAL is for cards you only get from events. Curse is for curses, except for special curses like Curse of the Bell and Necronomicurse.
            CardTarget.ENEMY, //The target. Single target is ENEMY, all enemies is ALL_ENEMY. Look at cards similar to what you want to see what to use.
            BASE_COST //The card's base cost. -1 is X cost, -2 is no cost for unplayable cards like curses, or Reflex.
    );

    private static final int DAMAGE = 32;
    private static final int UPG_DAMAGE = 10;


    public Chill() {
        super(ID, info); //Pass the required information to the BaseCard constructor.
        baseDamage = DAMAGE;
        tags.add(FriskTags.PERSEVERANCE);
        this.selfRetain = true;
        setWastedCostForTurn();
    }

    private int getReducedBaseCost() {
        return WastedEnergyPatch.previous_e_list.get(WastedEnergyPatch.previous_e_index);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (m != null)
            addToBot((AbstractGameAction)new VFXAction((AbstractGameEffect)new WeightyImpactEffect(m.hb.cX, m.hb.cY)));
        addToBot((AbstractGameAction)new WaitAction(0.8F));
        addToBot(new CustomSFXAction("mus_sfx_star"));
        dmg(m, AbstractGameAction.AttackEffect.NONE);
    }

    @Override
    public void resetAttributes(){
        super.resetAttributes();
        this.newCost = BASE_COST;
    }

    public void setWastedCostForTurn() {
        this.resetAttributes();
        this.newCost -= getReducedBaseCost();
        this.setCostForTurn(this.newCost);
    }



    @Override
    public void upp() {
        upgradeDamage(UPG_DAMAGE);
    }
}
