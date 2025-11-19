package friskmod.damagemods;

import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;
import com.evacipated.cardcrawl.mod.stslib.damagemods.AbstractDamageModifier;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import friskmod.powers.Karma;
import friskmod.util.Wiz;


public class KarmaApply extends AbstractDamageModifier {
//    TooltipInfo leechTooltip = null;

    public KarmaApply() {
        this.priority = 999;
    }

    //This hook grabs the lastDamageTaken once it is updated upon attacking the monster.
    //This lets us heal the attacker equal to the damage that was actually dealt to the target
  @Override
    public void onLastDamageTakenUpdate(DamageInfo info, int lastDamageTaken, int overkillAmount, AbstractCreature targetHit) {
        if (lastDamageTaken > 0) {
            Wiz.atb(new ApplyPowerAction(targetHit, info.owner, new Karma(targetHit, lastDamageTaken)));
        }
    }

    @Override
    public AbstractDamageModifier makeCopy() {
        return new KarmaApply();
    }

    //Overriding this to true tells us that this damage mod is considered part of the card and not just something added on to the card later.
    //If you ever add a damage modifier during the initialization of a card, it should be inherent.
    public boolean isInherent() {
        return true;
    }

}

