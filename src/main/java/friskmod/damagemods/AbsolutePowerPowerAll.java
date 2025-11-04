package friskmod.damagemods;

import com.evacipated.cardcrawl.mod.stslib.damagemods.AbstractDamageModifier;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import friskmod.FriskMod;
import friskmod.actions.CustomSFXAction;
import friskmod.helper.SharedFunctions;
import friskmod.patches.GhostlyPatch;
import friskmod.powers.*;
import friskmod.util.Wiz;

import static friskmod.FriskMod.logger;

public class AbsolutePowerPowerAll extends AbstractDamageModifier {

    public AbsolutePowerPowerAll() {
        this.priority = 999;
    }

    @Override
    public int onAttackToChangeDamage(DamageInfo info, int damageAmount, AbstractCreature target) {
        int newDamageAmount = damageAmount;
        AbstractCreature source = info.owner;
        DamageInfo.DamageType damageType = info.type;
        if (source == AbstractDungeon.player && (damageType == DamageInfo.DamageType.NORMAL || GhostlyPatch.GhostlyDamageTypeFields.isGhostly.get(damageType))) { //hopefully plays well with blooky?
            int damageDealt = info.output;
            AbstractPower possFavourite = AbstractDungeon.player.getPower(FavouriteNumberPower.POWER_ID);
            if (possFavourite != null) {
                int LV_power = 1;
                AbstractPower possMercied = AbstractDungeon.player.getPower(Mercied.POWER_ID);
                if (possMercied != null){
                    LV_power = -possMercied.amount;
                }
                newDamageAmount = determineFavouriteNumberLV(damageAmount, LV_power, target);
                possFavourite.onSpecificTrigger();
            }
            AbstractPower possAbsolute = AbstractDungeon.player.getPower(AbsolutePowerPower.POWER_ID);
            if (possAbsolute != null) {
                int energyGain = possAbsolute.amount;
                if (damageDealt % 10 == 9){ //damage *mod* hehe
                    Wiz.atb(new GainEnergyAction(energyGain));
                    possAbsolute.flash();
                }
            }
        }
        return newDamageAmount;
    }

    private int determineFavouriteNumberLV(int damageAmount, int LV_power, AbstractCreature target) {
        if (damageAmount < 0){ //hopefully not???
            return damageAmount;
        }
        boolean healFlag = false;
        int remainder = damageAmount % 10;
        int diff = 0;
        if (LV_power > 0){
            diff = 9-remainder;
        } else if (LV_power < 0){
            if (damageAmount >= 9) {
                diff = (remainder + 1) % 10;
            }else{
                diff = remainder + 9; //for "-9"
                healFlag = true;
            }

        } else{
            Wiz.att(new CustomSFXAction("snd_wrongvictory")); // can't use LV to change damage
            return damageAmount;
        }
        int extraLV = diff/LV_power;
        int remainderLV = diff%LV_power;
        if (remainderLV > 0){
            extraLV++;
        }
        if (extraLV > 0) {
            LV_Hero LV = new LV_Hero(AbstractDungeon.player, extraLV);
            LV.setFromFavouriteNumber(extraLV);
            int index = 0;
            for (AbstractGameAction action : AbstractDungeon.actionManager.actions) {
                if (action instanceof RemoveSpecificPowerAction){
                    if (SharedFunctions.MarkLVConsumedRemoveSpecificPowerActionPatch.isLVConsumed.get(action)){
                        AbstractDungeon.actionManager.actions.add(index+1, new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, LV, extraLV));
                        break;
                    }
                }
                ++index;
            }
            if (index == AbstractDungeon.actionManager.actions.size()){ //Couldn't find it??
                Wiz.att(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, LV, extraLV));
            }
            if (healFlag) {
                Wiz.att(new CustomSFXAction("snd_wrongvictory"));
                Wiz.att(new HealAction(target, AbstractDungeon.player, 9));
                return 0;
            }
        }
        int newDamageAmount = damageAmount + (extraLV*LV_power);
        if (newDamageAmount % 10 != 9) { //overshot
            if (LV_power > 0) {
                if (newDamageAmount < 9){
                    logger.warn("{}: Small damage values did not exceed 9 for FavouriteNumber", FriskMod.modID);
                } else {
                    newDamageAmount = ((newDamageAmount / 10) * 10) - 1; //round down
                }
            } else{
                newDamageAmount = ((newDamageAmount / 10) * 10) + 9 ; // round up
            }
        }
        return newDamageAmount;
    }

    @Override
    public AbstractDamageModifier makeCopy() {
        return new AbsolutePowerPowerAll();
    }

    //Overriding this to true tells us that this damage mod is considered part of the card and not just something added on to the card later.
    //If you ever add a damage modifier during the initialization of a card, it should be inherent.
    public boolean isInherent() {
        return true;
    }
}
