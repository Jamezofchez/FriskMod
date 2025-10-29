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

    private final int DETONATION_AMOUNT;

    public ConvertLVKarmaAction(AbstractCreature c, int DETONATION_AMOUNT) {
        this.c = c;
        this.actionType = ActionType.SPECIAL;
        this.DETONATION_AMOUNT = DETONATION_AMOUNT;
    }

    @Override
    public void update() {
        int KR_Amount = 0;
        AbstractPower posspow = c.getPower(LV_Enemy.POWER_ID);
        if (posspow != null){
            KR_Amount = posspow.amount;
        }
        if (DETONATION_AMOUNT > 0) {
            for (int i = 0; i < DETONATION_AMOUNT; i++) {
                AbstractDungeon.actionManager.addToTop(new DetonateKarmaAction(c));
            }
        }
        AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(c, p, new Karma(c, KR_Amount), KR_Amount));
        AbstractDungeon.actionManager.addToTop(new RemoveSpecificPowerAction(c, p, LV_Enemy.POWER_ID));
        this.isDone = true;
    }
}

