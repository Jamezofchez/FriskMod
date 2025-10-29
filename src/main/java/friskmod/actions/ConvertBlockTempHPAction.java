package friskmod.actions;

import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;
import com.megacrit.cardcrawl.actions.*;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.*;
import com.megacrit.cardcrawl.actions.common.*;

public class ConvertBlockTempHPAction extends AbstractGameAction
{
    private final AbstractCreature c;

    private final int sparedBlock;

    public ConvertBlockTempHPAction(AbstractCreature c, int sparedBlock) {
        this.c = c;
        this.actionType = ActionType.SPECIAL;
        this.sparedBlock = sparedBlock;
    }

    @Override
    public void update() {
        int amount = c.currentBlock;
        int reservedBlock = Math.min(amount, sparedBlock);
        AbstractDungeon.actionManager.addToTop(new AddTemporaryHPAction(c,c,amount));
        AbstractDungeon.actionManager.addToTop(new GainBlockAction(c, c, reservedBlock));
        AbstractDungeon.actionManager.addToTop(new RemoveAllBlockAction(c,c));
        this.isDone = true;
    }
}

