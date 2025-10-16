package friskmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import friskmod.powers.AbstractCountdownPower;
import friskmod.powers.LosePlayerHPInterface;

public class PlayerLoseHPAction extends AbstractGameAction {
    private final int damageAmount;
    private final boolean tempHP;
    private final boolean overflow;


    public PlayerLoseHPAction(int damageAmount, boolean tempHP) {
        this(damageAmount, tempHP, false);
    }
    public PlayerLoseHPAction(int damageAmount, boolean tempHP, boolean overflow) {
        this.damageAmount = damageAmount;
        this.tempHP = tempHP;
        this.overflow = overflow;
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
                    ((LosePlayerHPInterface) p).onPlayerLoseHP(damageAmount, tempHP, overflow);
            }
        }
        for (AbstractPower p : AbstractDungeon.player.powers) {
            if (p instanceof LosePlayerHPInterface)
                ((LosePlayerHPInterface) p).onPlayerLoseHP(damageAmount, tempHP, overflow);
        }
    }
}
