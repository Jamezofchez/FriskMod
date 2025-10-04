package friskmod.actions;

import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.mod.stslib.damagemods.BindingHelper;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.PoisonPower;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.combat.FlyingDaggerEffect;
import com.megacrit.cardcrawl.vfx.combat.PotionBounceEffect;
import friskmod.cards.AbstractEasyCard;
import friskmod.util.Wiz;
import friskmod.vfx.FlurryOfKnivesVFX;

public class FlurryOfKnivesAction extends AbstractGameAction {
    private static final float POST_ATTACK_WAIT_DUR = 0.4F;

    private int numTimes;
    private int amount;
    private AbstractCard sourceCard;
    public FlurryOfKnivesAction(AbstractCreature target, int amount, int numTimes, AbstractCard sourceCard) {
        this.target = target;
        this.actionType = ActionType.DAMAGE;
        this.duration = 0.01F;
        this.numTimes = numTimes;
        this.amount = amount;
        this.sourceCard = sourceCard;
    }

    public void update() {
        // Early exit if no monsters remain at all
        if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
            this.isDone = true;
            AbstractDungeon.actionManager.clearPostCombatActions();
            return;
        }

        // If there are remaining hits
        if (this.numTimes > 0) {
            AbstractMonster randomMonster = AbstractDungeon.getMonsters().getRandomMonster(null, true, AbstractDungeon.cardRandomRng);

            if (randomMonster != null) {
                // Only decrement numTimes if we’re actually going to damage a living target
                if (randomMonster.currentHealth > 0) {
                    this.numTimes--;
                    DamageAction action = new DamageAction(randomMonster, new DamageInfo(AbstractDungeon.player, this.amount, DamageInfo.DamageType.NORMAL), AttackEffect.SLASH_HORIZONTAL);
                    BindingHelper.bindAction(this.sourceCard, action);
                    addToBot(action);
                    throwDaggerEffect();
                }
                addToBot(new WaitAction(POST_ATTACK_WAIT_DUR));
                FlurryOfKnivesAction action = new FlurryOfKnivesAction(randomMonster, this.amount, this.numTimes, this.sourceCard);
                BindingHelper.bindAction(this.sourceCard, action);
                addToBot(action);
            }
        }

        this.isDone = true;
    }
    private void throwDaggerEffect(){
        boolean flipX = AbstractDungeon.getMonsters().shouldFlipVfx();
        float playerX = AbstractDungeon.player.hb.cX;
        float playerY = AbstractDungeon.player.hb.cY;

        // Random vertical spread around player
        //float offsetY = MathUtils.random(-100.0F, 100.0F) * Settings.scale;
        // Forward-biased horizontal spread
        //float offsetX = MathUtils.random(200.0F, 400.0F) * Settings.scale;
        float offsetX = 0;
        float offsetY = 0;
        // Mirror depending on which way we’re facing
        float x = flipX ? playerX - offsetX : playerX + offsetX;
        float y = playerY + offsetY;

        // Random small rotation variation (-15° to +15°)
        final float ANGLE_DEVIATION = 5.0F;
        float rotation = MathUtils.random(-ANGLE_DEVIATION, ANGLE_DEVIATION);
        AbstractDungeon.effectsQueue.add(
                new FlyingDaggerEffect(x, y, rotation, flipX)
        );
    }
}


/* Location:              C:\Program Files (x86)\Steam\steamapps\common\SlayTheSpire\desktop-1.0.jar!\com\megacrit\cardcrawl\action\\unique\FlurryOfKnivesAction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */