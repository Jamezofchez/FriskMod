package friskmod.helper;

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import friskmod.actions.GiveRandomCardXP;
import friskmod.powers.LV_Hero;
import friskmod.powers.PreventLVLoss;
import friskmod.powers.RememberBraveryPower;
import friskmod.util.Wiz;

public class SharedFunctions {
    public static int consumeLVHeroForXP() {
        AbstractPlayer p = AbstractDungeon.player;
        AbstractPower LV_Hero_Power = p.getPower(LV_Hero.POWER_ID);
        int XP_from_LV_Hero = 0;
        if (LV_Hero_Power != null) {
            XP_from_LV_Hero = LV_Hero_Power.amount;
            if (XP_from_LV_Hero > 0) {
                AbstractPower targetPower;
                targetPower = p.getPower(RememberBraveryPower.POWER_ID);
                if (targetPower != null) {
                    targetPower.flash();
                    for (int i = 0; i < targetPower.amount; i++) {
                        Wiz.att(new GiveRandomCardXP(XP_from_LV_Hero));
                    }
                }
                targetPower = p.getPower(PreventLVLoss.POWER_ID);
                if (targetPower != null) {
                    targetPower.flash();
                    //                targetPower.amount--;
                    //                if (targetPower.amount <= 0) {
                    //                    Wiz.att(new RemoveSpecificPowerAction(p, p, PreventLVLoss.POWER_ID));
                    //                }
                } else {
                    Wiz.att(new RemoveSpecificPowerAction(p, p, LV_Hero.POWER_ID));
                }
            }
        }
        return XP_from_LV_Hero;
    }
}
