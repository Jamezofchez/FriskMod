package friskmod.actions;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.utility.LoseBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import friskmod.util.Wiz;

import java.util.ArrayList;
import java.util.List;

public class StealAllBlockAction extends AbstractGameAction {
    public float t;

    public final List<AbstractMonster> targetMonsters;

    public static StealAllBlockAction activatedInstance = null;

    // Primary constructor — for multiple monsters
    public StealAllBlockAction(List<AbstractMonster> monsters) {
        this.amount = 0;
        this.duration = 0.5F;
        actionType = ActionType.BLOCK;
        startDuration = duration = Settings.ACTION_DUR_MED;
        targetMonsters = monsters;
    }

    // Overload for a single monster
    public StealAllBlockAction(AbstractMonster m) {
        this(toList(m));
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
                if (!target.isDying && !target.isDead && target.currentBlock > 0) {
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
                    AbstractDungeon.effectList.add(new FlashAtkImgEffect(source.hb.cX, source.hb.cY, AttackEffect.SHIELD));
                    int amount = target.currentBlock;
                    if (amount > 0) {
                        AbstractPower posspow = target.getPower(ArtifactPower.POWER_ID);
                        if (posspow != null) {
                            posspow.flash();
                            posspow.onSpecificTrigger();
                            continue;
                        }
                        Wiz.att(new GainBlockAction(source, source, amount));
                        Wiz.att(new LoseBlockAction(target, source, amount));
                    }
                }
            }

            activatedInstance = null;
        } else {
            t = (startDuration - duration) / startDuration;
        }
    }
}
