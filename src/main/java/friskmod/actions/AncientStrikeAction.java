package friskmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import friskmod.util.Wiz;

public class AncientStrikeAction extends AbstractGameAction {
    public AncientStrikeAction(AbstractCreature target, int amount) {
        this.target = target;
        this.amount = amount;
    }

    public void update() {
        this.isDone = true;
        AbstractPower posspow = target.getPower(ArtifactPower.POWER_ID);
        if (posspow != null){
            int i;
            for (i = 0; i < posspow.amount; i++){
                Wiz.att(new DamageAction(target, new DamageInfo(AbstractDungeon.player, amount, DamageInfo.DamageType.NORMAL)));
            }
        }
    }
}
