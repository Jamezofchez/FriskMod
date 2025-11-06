package friskmod.actions;

import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;
import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.RemoveAllTemporaryHPAction;
import com.evacipated.cardcrawl.mod.stslib.patches.core.AbstractCreature.TempHPField;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.utility.LoseBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.city.SphericGuardian;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.BarricadePower;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import friskmod.util.Wiz;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class StealAllBlockAction extends AbstractGameAction {
    public float t;

    public final List<AbstractMonster> targetMonsters;

    public static StealAllBlockAction activatedInstance = null;

    public boolean stealMatching;

    // Primary constructor — for multiple monsters
    public StealAllBlockAction(List<AbstractMonster> monsters) {
        this(monsters, false);
    }
    public StealAllBlockAction(List<AbstractMonster> monsters, boolean stealMatching) {
        this.amount = 0;
        this.duration = 0.5F;
        this.stealMatching = stealMatching;
        actionType = ActionType.BLOCK;
        startDuration = duration = Settings.ACTION_DUR_MED;
        targetMonsters = monsters;
    }

    // Overload for a single monster
    public StealAllBlockAction(AbstractMonster m) {
        this(toList(m));
    }
    public StealAllBlockAction(AbstractMonster m, boolean stealMatching) {
        this(toList(m), stealMatching);
    }

    // Helper method to create a list with a single monster (if non-null)
    private static List<AbstractMonster> toList(AbstractMonster m) {
        List<AbstractMonster> list = new ArrayList<>();
        if (m != null) {
            list.add(m);
        }
        return list;
    }
    public void calcPosition(float[] x, float[] y) {
        float targetX = AbstractDungeon.player.hb.cX - AbstractDungeon.player.hb.width / 2.0F;
        float targetY = AbstractDungeon.player.hb.cY - AbstractDungeon.player.hb.height / 2.0F;
        x[0] = MathUtils.lerp(x[0], targetX, t);
        y[0] = MathUtils.lerp(y[0], targetY, t);
    }

    public void update() {
        if (duration == startDuration) {
            boolean foundTarget = false;
            for (AbstractMonster target : targetMonsters){
                boolean haspow = target.hasPower(BarricadePower.POWER_ID);
                if (!target.isDying && !target.isDead && !haspow) {
                    if (stealMatching){
                        if (!((target.currentBlock > 0 && AbstractDungeon.player.currentBlock > 0) || (TempHPField.tempHp.get(target) > 0 && TempHPField.tempHp.get(AbstractDungeon.player) > 0))){
                            continue;
                        }
                    } else{
                        if (!(target.currentBlock > 0 || TempHPField.tempHp.get(target) > 0)){
                            continue;
                        }
                    }
                    activatedInstance = this;
                    t = 0;
                    foundTarget = true;
                    break;
                }
            }
            if (!foundTarget) {
                isDone = true;
                activatedInstance = null;
                return;
            }
        }

        tickDuration();

        if (isDone) {
            t = 1;
            for (AbstractMonster target : targetMonsters) {
                if (target != null) {
                    AbstractCreature source = AbstractDungeon.player;
                    int tempHpAmount = TempHPField.tempHp.get(target);
                    if (tempHpAmount > 0) {
                        if (stealMatching) {
                            if (!(TempHPField.tempHp.get(source) > 0)){
                                continue;
                            }
                        }
                        Wiz.att(new AddTemporaryHPAction(source, source, tempHpAmount));
                        Wiz.att(new RemoveAllTemporaryHPAction(target, source));
                    }
                }
            }
            for (AbstractMonster target : targetMonsters) {
                AbstractCreature source = AbstractDungeon.player;
                AbstractDungeon.effectList.add(new FlashAtkImgEffect(source.hb.cX, source.hb.cY, AttackEffect.SHIELD));
                if (target != null) {
                    int blockAmount = target.currentBlock;
                    if (blockAmount > 0) {
                        if (stealMatching) {
                            if (!(source.currentBlock > 0)) {
                                continue;
                            }
                        }
                        Wiz.att(new GainBlockAction(source, source, blockAmount));
                        Wiz.att(new LoseBlockAction(target, source, blockAmount));
                    }
                }
            }

            activatedInstance = null;
        } else {
            t = (startDuration - duration) / startDuration;
        }
    }
}
