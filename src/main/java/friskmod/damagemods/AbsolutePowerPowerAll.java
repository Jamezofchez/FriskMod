package friskmod.damagemods;

import com.evacipated.cardcrawl.mod.stslib.damagemods.AbstractDamageModifier;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import friskmod.powers.AbsolutePowerPower;
import friskmod.util.Wiz;

public class AbsolutePowerPowerAll extends AbstractDamageModifier {

    public AbsolutePowerPowerAll() {
        this.priority = 999;
    }

    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        AbstractCreature source = info.owner;
        DamageInfo.DamageType damageType = info.type;
        if (source == AbstractDungeon.player && damageType == DamageInfo.DamageType.NORMAL || damageType == DamageInfo.DamageType.THORNS) { //hopefully plays well with blooky?
            AbstractPower pow = AbstractDungeon.player.getPower(AbsolutePowerPower.POWER_ID);
            if (pow != null) {
                int energyGain = pow.amount;
                int damageDealt = info.output;
                if (damageDealt % 10 == 9){ //damage *mod* hehe
                    Wiz.atb(new GainEnergyAction(energyGain));
                    pow.flash();
                }
            }
        }
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
