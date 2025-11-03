package friskmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.combat.LightningEffect;
import friskmod.util.Wiz;

public class NEOHitAction extends AbstractGameAction {
    private int blockAmount;
    public NEOHitAction(AbstractCreature target, AbstractCreature source, int amount, int blockAmount) {
        this.target = target;
        this.source = source;
        this.amount = amount;
        this.blockAmount = blockAmount;
    }

    public void update() {
        this.isDone = true;
        if (this.source == null){
            this.source = AbstractDungeon.player;
        }
        int damage = this.amount*blockAmount;
        Wiz.att(new DamageAction(this.target, new DamageInfo(this.target, damage, DamageInfo.DamageType.HP_LOSS), AbstractGameAction.AttackEffect.NONE, true));
        float speedTime = 0.1F;
        Wiz.att(new VFXAction(new LightningEffect(this.target.drawX, this.target.drawY), speedTime));
        Wiz.att(new CustomSFXAction("snd_shock"));
    }
}