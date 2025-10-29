package friskmod.helper;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import friskmod.actions.GiveRandomCardXP;
import friskmod.powers.LV_Hero;
import friskmod.powers.PreventLVLoss;
import friskmod.powers.RememberBraveryPower;
import friskmod.util.Wiz;

public class SharedFunctions {
    @SpirePatch2(clz = RemoveSpecificPowerAction.class, method = SpirePatch.CLASS)
    public static class MarkLVConsumedRemoveSpecificPowerActionPatch {
        public static SpireField<Boolean> isLVConsumed = new SpireField<>(() -> false);
    }

    public static int consumeLVHeroForXP() {
        return consumeLVHeroForXP(false);
    }
    public static int consumeLVHeroForXP(boolean secondConsume) {
        AbstractPlayer p = AbstractDungeon.player;
        AbstractPower LV_Hero_Power = p.getPower(LV_Hero.POWER_ID);
        int XP_from_LV_Hero;
        if (LV_Hero_Power != null) {
            if (secondConsume) {
                int favouriteNumberAmount = ((LV_Hero) LV_Hero_Power).getFavouriteNumberAmount();
                if (favouriteNumberAmount == 0){
                    return 0;
                }
                XP_from_LV_Hero = favouriteNumberAmount;
                ((LV_Hero) LV_Hero_Power).setFromFavouriteNumber(0);
            } else {
                XP_from_LV_Hero = LV_Hero_Power.amount;
            }
            if (XP_from_LV_Hero > 0) {
                AbstractPower targetPower;
                targetPower = p.getPower(RememberBraveryPower.POWER_ID);
                if (targetPower != null) {
                    if (!secondConsume){
                            targetPower.flash();
                        for (int i = 0; i < targetPower.amount; i++) {
                            Wiz.atb(new GiveRandomCardXP(XP_from_LV_Hero));
                        }
                    } else{
                        AbstractDungeon.actionManager.actions.stream().filter(action -> action instanceof GiveRandomCardXP).forEach(action -> {((GiveRandomCardXP) action).XPamount += XP_from_LV_Hero;});
                    }
                }
                targetPower = p.getPower(PreventLVLoss.POWER_ID);
                if (targetPower != null) {
                    if (!secondConsume) {
                        targetPower.flash();
                    }
                    //                targetPower.amount--;
                    //                if (targetPower.amount <= 0) {
                    //                    Wiz.att(new RemoveSpecificPowerAction(p, p, PreventLVLoss.POWER_ID));
                    //                }
                } else {
                    RemoveSpecificPowerAction removeAction = new RemoveSpecificPowerAction(p, p, LV_Hero.POWER_ID);
                    MarkLVConsumedRemoveSpecificPowerActionPatch.isLVConsumed.set(removeAction, true);
                    Wiz.att(removeAction);
                }
            }
        } else {
            XP_from_LV_Hero = 0;
        }
        return XP_from_LV_Hero;
    }
}
