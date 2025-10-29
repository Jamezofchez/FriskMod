package friskmod.actions;

import basemod.ReflectionHacks;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import friskmod.powers.Karma;
import friskmod.powers.NonAttackPower;

public class DetonateKarmaAction extends AbstractGameAction {
    private AbstractCreature c;

    public DetonateKarmaAction(AbstractCreature c) {
        this.c = c;
    }
    @Override
    public void update() {
        this.isDone = true;
        for (AbstractPower p : c.powers) {
            if (p.ID.equals(Karma.POWER_ID)){
                ((Karma) p ).triggerKarma();
            }
        }
    }
}
