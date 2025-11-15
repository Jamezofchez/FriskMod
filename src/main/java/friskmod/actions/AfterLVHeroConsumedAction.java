package friskmod.actions;

import basemod.ReflectionHacks;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import friskmod.damagemods.XPModifierAll;
import friskmod.helper.SharedFunctions;
import friskmod.patches.CardXPFields;

public class AfterLVHeroConsumedAction extends AbstractGameAction {
    XPModifierAll XPModifierAll;
    AbstractCard sourceCard;
    AbstractCreature target;
    int LV_transfer_from;
    boolean wasBlocked;
    public AfterLVHeroConsumedAction(XPModifierAll XPModifierAll, AbstractCreature target, int LV_transfer_from, boolean wasBlocked){
        this.XPModifierAll = XPModifierAll;
        this.sourceCard = XPModifierAll.sourceCard;
        this.target = target;
        this.LV_transfer_from = LV_transfer_from;
        this.duration = Settings.ACTION_DUR_FAST;
        this.wasBlocked = wasBlocked;
    }
    public void update() {
        this.isDone = true;
        int LV_transfer_to = 0;
        if (sourceCard != null) {
            LV_transfer_to = CardXPFields.getCardXP(sourceCard);
        }
        LV_transfer_to += SharedFunctions.consumeLVHeroForXP(true); //should generally be zero unless FavouriteNumber
        if (LV_transfer_to > 0) {
            LV_transfer_to = LV_transfer_to - 1;
        }
        boolean isMultiDamage = ReflectionHacks.getPrivate(sourceCard, AbstractCard.class, "isMultiDamage");
        if (isMultiDamage){
            XPModifierAll.handleAOEDamage(target, LV_transfer_to, LV_transfer_from, wasBlocked);
        } else{
//            if (LV_transfer_to == 0 && LV_transfer_from == 0) return; //still may need to handle removal
            if (!wasBlocked) {
                XPModifierAll.handleSingleTargetDamage(target, LV_transfer_to, LV_transfer_from);
            }
        }

    }
}
