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

//    private final int additionalLV;
    private final int detAmount;


    public ConvertLVKarmaAction(AbstractCreature c, int detAmount) {
        this.c = c;
        this.actionType = ActionType.SPECIAL;
        this.detAmount = detAmount;
    }

    @Override
    public void update() {
        Wiz.att(new ConsumeLVEnemyAgami(c, detAmount));
        this.isDone = true;
    }
}

