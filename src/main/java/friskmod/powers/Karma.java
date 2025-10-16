package friskmod.powers;

import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.HealthBarRenderPower;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import friskmod.FriskMod;

public class Karma extends BasePower implements HealthBarRenderPower, LosePlayerHPInterface {
    public static final String POWER_ID = FriskMod.makeID(Karma.class.getSimpleName());
    private static final AbstractPower.PowerType TYPE = AbstractPower.PowerType.DEBUFF;
    private static final boolean TURN_BASED = false;
    public Karma(AbstractCreature owner, int amount) {
        super(POWER_ID, TYPE, TURN_BASED, owner, amount);
    }
    @Override
    public int getHealthBarAmount() {
        return this.amount;
    }

    @Override
    public Color getColor() {
        return new Color(1.0f, 0.0f, 1.0f, 1.0f);
    }

    @Override
    public AbstractPower makeCopy() {
        return new Karma(owner, amount);
    }
//    @Override
//    public int onLoseHp(int damageAmount) {
//    }
    @Override
    public void onPlayerLoseHP(int dmgamount, boolean tempHP, boolean overflow) {
        if (tempHP && overflow){
            return;
        }
        if (dmgamount > 0) {
            flashWithoutSound();
            addToBot(new DamageAction(this.owner, new DamageInfo(AbstractDungeon.player, Math.min(this.owner.currentHealth-1, this.amount), DamageInfo.DamageType.HP_LOSS)));
        }
    }
    @Override
    public void updateDescription() {
        String baseDescription = DESCRIPTIONS[0];
        this.description = String.format(baseDescription, amount);
    }
}
