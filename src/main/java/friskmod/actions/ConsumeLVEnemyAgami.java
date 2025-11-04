package friskmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import friskmod.powers.Karma;
import friskmod.powers.LV_Enemy;
import friskmod.util.Wiz;

public class ConsumeLVEnemyAgami extends AbstractGameAction {
    AbstractCreature c;
    public ConsumeLVEnemyAgami(AbstractCreature c){
        this.c = c;
    }
    @Override
    public void update() {
        this.isDone = true;
        int KR_Amount = 0;
        AbstractPower posspow = c.getPower(LV_Enemy.POWER_ID);
        if (posspow != null){
            KR_Amount = posspow.amount;
        }
        AbstractPlayer p = AbstractDungeon.player;
        Wiz.att(new ApplyPowerAction(c, p, new Karma(c, KR_Amount), KR_Amount));
        Wiz.att(new RemoveSpecificPowerAction(c, p, LV_Enemy.POWER_ID));
    }
}
