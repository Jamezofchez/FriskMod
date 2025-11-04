package friskmod.actions;

import com.megacrit.cardcrawl.actions.*;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.*;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.powers.AbstractPower;
import friskmod.powers.Karma;
import friskmod.powers.LV_Enemy;
import friskmod.util.Wiz;

public class ConvertLVKarmaAction extends AbstractGameAction
{
    private final AbstractCreature c;

    private final AbstractPlayer p = AbstractDungeon.player;

    private final int additionalLV;

    public ConvertLVKarmaAction(AbstractCreature c, int additionalLV) {
        this.c = c;
        this.actionType = ActionType.SPECIAL;
        this.additionalLV = additionalLV;
    }

    @Override
    public void update() {
        if (additionalLV > 0) {
            Wiz.att(new ApplyPowerAction(c, p, new LV_Enemy(c, additionalLV), additionalLV));
            Wiz.att(new ConsumeLVEnemyAgami(c));
        }
        this.isDone = true;
    }
}

