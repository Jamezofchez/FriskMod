package friskmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import friskmod.powers.Overcome;

public class ThunderingRageAction extends AbstractGameAction {
    private int persevereAmt;

    private DamageInfo info;

    public ThunderingRageAction(AbstractCreature target, DamageInfo info, int energyAmt) {
        this.info = info;
        setValues(target, info);
        this.persevereAmt = energyAmt;
        this.actionType = AbstractGameAction.ActionType.DAMAGE;
        this.duration = Settings.ACTION_DUR_FASTER;
    }

    public void update() {
        if (this.duration == Settings.ACTION_DUR_FASTER &&
                this.target != null) {
            AbstractDungeon.effectList.add(new FlashAtkImgEffect(this.target.hb.cX, this.target.hb.cY, AbstractGameAction.AttackEffect.BLUNT_HEAVY));
            this.target.damage(this.info);
            if (((AbstractMonster) this.target).isDying || this.target.currentHealth <= 0) {
                addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new Overcome(AbstractDungeon.player, persevereAmt), persevereAmt));
            }
            if ((AbstractDungeon.getCurrRoom()).monsters.areMonstersBasicallyDead())
                AbstractDungeon.actionManager.clearPostCombatActions();
        }
        tickDuration();
    }
}


/* Location:              C:\Program Files (x86)\Steam\steamapps\common\SlayTheSpire\desktop-1.0.jar!\com\megacrit\cardcrawl\actions\defect\SunderAction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */