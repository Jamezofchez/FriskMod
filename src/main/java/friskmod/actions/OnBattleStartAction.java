package friskmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import friskmod.patches.InherentPowerTagFields;
import friskmod.powers.AfterCardPlayedInterface;

public class OnBattleStartAction extends AbstractGameAction {
    private final AbstractRoom room;

    public OnBattleStartAction(AbstractRoom room) {
        this.room = room;
        this.duration = Settings.ACTION_DUR_FAST;
    }

    @Override
    public void update() {
        publishOnBattleStart();
        isDone = true;
    }
    private void publishOnBattleStart() {
        for (AbstractCreature m : (AbstractDungeon.getMonsters()).monsters) {
            for (AbstractPower p : m.powers){
                InherentPowerTagFields.inherentPowerFields.inherentPower.set(p, true);
                InherentPowerTagFields.inherentPowerFields.inherentPowerAmount.set(p, p.amount);
            }
        }
    }
}
