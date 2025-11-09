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
import friskmod.util.Wiz;

public class ThunderingRageAction extends AbstractGameAction {
    private int persevereAmt;
    private int aliveAmount;

    public ThunderingRageAction(int aliveAmount, int energyAmt) {
        this.persevereAmt = energyAmt;
        this.aliveAmount = aliveAmount;
        this.duration = this.startDuration = Settings.ACTION_DUR_FASTER;
    }

    public void update() {
        if (this.duration == this.startDuration) {
            int newAliveAmount = Wiz.getMonsters().size();
            for (int i = 0; i < (aliveAmount-newAliveAmount); ++i) {
                Wiz.att(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new Overcome(AbstractDungeon.player, this.persevereAmt), this.persevereAmt));
            }
        }
        tickDuration();
    }
}


/* Location:              C:\Program Files (x86)\Steam\steamapps\common\SlayTheSpire\desktop-1.0.jar!\com\megacrit\cardcrawl\actions\defect\SunderAction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */