package friskmod.actions;

import basemod.ReflectionHacks;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import friskmod.powers.NonAttackPower;

public class ResetDamageTypeAttack extends AbstractGameAction {
    private AbstractCard card;
    private NonAttackPower power;

    public ResetDamageTypeAttack(AbstractCard card, NonAttackPower power) {
        this.card = card;
        this.power = power;
        this.duration = this.startDuration = Settings.ACTION_DUR_FAST;
        this.actionType = ActionType.USE;
    }
    @Override
    public void update() {
        this.isDone = true;
        ReflectionHacks.setPrivate(card, AbstractCard.class, "damageType", DamageInfo.DamageType.NORMAL);
        card.damageTypeForTurn = DamageInfo.DamageType.NORMAL;
        power.onSpecificTrigger();
    }
}
