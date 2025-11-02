package friskmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
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
        Wiz.att(new WaitAction(0.2F));
        tickDuration();
        if (this.isDone) {
            Wiz.atb(new CustomSFXAction("snd_shock.wav"));
            Wiz.atb(new DamageAction(this.target, new DamageInfo(this.source, this.amount, DamageInfo.DamageType.HP_LOSS), AttackEffect.LIGHTNING, false, true));
        }
    }
}