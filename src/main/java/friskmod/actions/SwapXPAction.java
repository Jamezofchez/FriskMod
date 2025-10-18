package friskmod.actions; // adjust this to your mod's package

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon; //debug
import com.megacrit.cardcrawl.powers.AbstractPower;
import friskmod.helper.playBGM;
import friskmod.patches.CardXPFields; // adjust if needed
import friskmod.powers.Betrayed;
import friskmod.powers.LV_Enemy;
import friskmod.powers.Spared;
import friskmod.util.Wiz;

public class SwapXPAction extends AbstractGameAction {
    AbstractCard card;
    AbstractCreature source;
    AbstractCreature target;
    int LV_transfer_to;
    int LV_transfer_from;
    boolean resetCardXP;
    public SwapXPAction(AbstractCard card, AbstractCreature source, AbstractCreature target, int LV_transfer_to, int LV_transfer_from){
        this(card, source, target, LV_transfer_to, LV_transfer_from, true);
    }
    public SwapXPAction(AbstractCard card, AbstractCreature source, AbstractCreature target, int LV_transfer_to, int LV_transfer_from, boolean resetCardXP){
        this.card = card;
        this.source = source;
        this.target = target;
        this.LV_transfer_to = LV_transfer_to;
        this.LV_transfer_from = LV_transfer_from;
        this.resetCardXP = resetCardXP;

    }
    @Override
    public void update() {
        //remove xp from card
        //remove lv from enemy
        //add lv to enemy
        //add xp to card
        if (card != null) {
            if (LV_transfer_from >= 1) {
                CardXPFields.addXP(card, LV_transfer_from);
            }
        }
        if (target != null) {
            if (LV_transfer_to >= 1) {
                AbstractPower posspow = target.getPower(Betrayed.POWER_ID);
                int actualLV_transfer_to = LV_transfer_to;
                if (posspow != null) {
                    actualLV_transfer_to *= 2;
                }
                Wiz.att(new ApplyPowerAction(target, source, new LV_Enemy(target, actualLV_transfer_to), actualLV_transfer_to));
            }
        }
        if (target != null) {
            Wiz.att(new RemoveSpecificPowerAction(target, source, LV_Enemy.POWER_ID));
        }
        if (card != null && resetCardXP) {
            CardXPFields.setAddedXP(card, 0);
        }

        isDone = true;
    }
}
