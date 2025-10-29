package friskmod.actions;

import basemod.ReflectionHacks;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import friskmod.powers.NonAttackPower;

public class ResetDamageTypeAttack extends AbstractGameAction {
    private NonAttackPower power;

    public ResetDamageTypeAttack(NonAttackPower power) {
        this.power = power;
    }
    @Override
    public void update() {
        this.isDone = true;
        power.onSpecificTrigger();
    }
}
