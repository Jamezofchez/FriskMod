package friskmod.actions;

import com.megacrit.cardcrawl.actions.*;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.*;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.powers.AbstractPower;
import friskmod.powers.Karma;
import friskmod.powers.LV_Enemy;

public class ConvertLVKarmaAction extends AbstractGameAction
{
    private final AbstractCreature c;

    private final AbstractPlayer p = AbstractDungeon.player;

    private final int extraLV;

    public ConvertLVKarmaAction(AbstractCreature c, int extraLV) {
        this.c = c;
        this.actionType = ActionType.WAIT;
        this.extraLV = extraLV;
    }

    @Override
    public void update() {
        int KR_Amount = 0;
        AbstractPower posspow = c.getPower(LV_Enemy.POWER_ID);
        if (posspow != null){
            KR_Amount = posspow.amount;
        }
        AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(c, p, new Karma(c, KR_Amount), KR_Amount));
        if (extraLV > 0) {
            AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(c, p, new LV_Enemy(c, extraLV), extraLV));
        }
        AbstractDungeon.actionManager.addToTop(new RemoveSpecificPowerAction(c, p, LV_Enemy.POWER_ID));
        this.isDone = true;
    }
}

