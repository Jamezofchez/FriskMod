package friskmod.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import friskmod.FriskMod;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import friskmod.helper.playBGM;

public class Spared extends BasePower {
    public static final String POWER_ID = FriskMod.makeID(Spared.class.getSimpleName());
    private static final AbstractPower.PowerType TYPE = AbstractPower.PowerType.BUFF;
    private static final boolean TURN_BASED = false;

    private final CountdownFlee fleePower;

    //The only thing TURN_BASED controls is the color of the number on the power icon.
    //Turn based powers are white, non-turn based powers are red or green depending on if their amount is positive or negative.
    //For a power to actually decrease/go away on its own they do it themselves.
    //Look at powers that do this like VulnerablePower and DoubleTapPower.
    public Spared(AbstractCreature owner, CountdownFlee fleePower) {
        super(POWER_ID, TYPE, TURN_BASED, owner, -1);
        this.fleePower = fleePower;
    }

    @Override
    public int onAttackedToChangeDamage(DamageInfo info, int damageAmount) { //hopefully called before XP mod?
        if (info.type == DamageInfo.DamageType.NORMAL) {
            if (damageAmount > 0) {
                addToTop(new ApplyPowerAction(this.owner, AbstractDungeon.player, new Betrayed(this.owner, fleePower)));
                addToTop(new RemoveSpecificPowerAction(this.owner, AbstractDungeon.player, this.ID));
            }
        }
        return damageAmount;
    }

    @Override
    public void onRemove(){
        playBGM.playEnemyRetreating();
    }

    @Override
    public AbstractPower makeCopy() {
        return new Spared(owner, fleePower);
    }

    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }

}