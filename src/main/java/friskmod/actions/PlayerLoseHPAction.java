package friskmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import friskmod.powers.LosePlayerHPInterface;

public class PlayerLoseHPAction extends AbstractGameAction {
    private final int damageAmount;
    private final boolean tempHP;
    private final boolean overflow;
    private DamageInfo info;


    public PlayerLoseHPAction(int damageAmount, DamageInfo info, boolean tempHP) {
        this(damageAmount, info, tempHP, false);
    }
    public PlayerLoseHPAction(int damageAmount, DamageInfo info, boolean tempHP,  boolean overflow) {
        this.damageAmount = damageAmount;
        this.tempHP = tempHP;
        this.overflow = overflow;
        this.info = info;
    }

    @Override
    public void update() {
        publishOnPlayerLoseHP();
        isDone = true;
    }
    private void publishOnPlayerLoseHP() {
        for (AbstractCreature m : (AbstractDungeon.getMonsters()).monsters) {
            for (AbstractPower p : m.powers) {
                if (p instanceof LosePlayerHPInterface)
                    if (info != null && KarmaDamageAction.KarmaDamageTypeFields.isNotKarma.get(info)) {
                        ((LosePlayerHPInterface) p).onPlayerLoseHP(damageAmount, tempHP, overflow);
                    }
            }
        }
        for (AbstractPower p : AbstractDungeon.player.powers) {
            if (p instanceof LosePlayerHPInterface) {
                if (info != null && KarmaDamageAction.KarmaDamageTypeFields.isNotKarma.get(info)) {
                    ((LosePlayerHPInterface) p).onPlayerLoseHP(damageAmount, tempHP, overflow);
                }
            }
        }
    }
}
