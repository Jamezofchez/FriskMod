package friskmod.actions; // adjust this to your mod's package

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import friskmod.patches.CardXPFields; // adjust if needed
import friskmod.powers.LV_Enemy;
import friskmod.util.Wiz;

public class SwapXPAction extends AbstractGameAction {
    AbstractCard card;
    AbstractCreature source;
    AbstractCreature target;
    int LV_transfer_to;
    int LV_transfer_from;
    public SwapXPAction(AbstractCard card, AbstractCreature source, AbstractCreature target, int LV_transfer_to, int LV_transfer_from){
        this.card = card;
        this.source = source;
        this.target = target;
        this.LV_transfer_to = LV_transfer_to;
        this.LV_transfer_from = LV_transfer_from;
    }
    @Override
    public void update() {
        //remove xp from card
        //remove lv from enemy
        //add lv to enemy
        //add xp to card

        CardXPFields.setAddedXP(card, 0);


        Wiz.att(new RemoveSpecificPowerAction(target, source, LV_Enemy.POWER_ID));

        if (LV_transfer_to >= 1) {
            Wiz.att(new ApplyPowerAction(target, source, new LV_Enemy(target,LV_transfer_to),LV_transfer_to));
        }
        if (LV_transfer_from >= 1){
            CardXPFields.addXP(card, LV_transfer_from);
        }
        isDone = true;
    }
}
