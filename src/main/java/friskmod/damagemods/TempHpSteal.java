package friskmod.damagemods;

import basemod.helpers.TooltipInfo;
import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;
import com.evacipated.cardcrawl.mod.stslib.damagemods.AbstractDamageModifier;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import friskmod.util.Wiz;


public class TempHpSteal extends AbstractDamageModifier {
//    TooltipInfo leechTooltip = null;

    public TempHpSteal() {}

    //This hook grabs the lastDamageTaken once it is updated upon attacking the monster.
    //This lets us heal the attacker equal to the damage that was actually dealt to the target
  @Override
    public void onLastDamageTakenUpdate(DamageInfo info, int lastDamageTaken, int overkillAmount, AbstractCreature targetHit) {
        if (lastDamageTaken > 0) {
            Wiz.atb(new AddTemporaryHPAction(info.owner, info.owner, lastDamageTaken));
        }
    }
//    @Override
//    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) { //should only heal unblocked dmg
//        if (damageAmount > 0) {
//            Wiz.atb(new AddTemporaryHPAction(info.owner, info.owner, damageAmount));
//        }
//    }

    @Override
    public AbstractDamageModifier makeCopy() {
        return new TempHpSteal();
    }

    //Overriding this to true tells us that this damage mod is considered part of the card and not just something added on to the card later.
    //If you ever add a damage modifier during the initialization of a card, it should be inherent.
    public boolean isInherent() {
        return true;
    }

}

