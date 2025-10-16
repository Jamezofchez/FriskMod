package friskmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import friskmod.powers.AfterCardPlayedInterface;

public class AfterCardUseAction extends AbstractGameAction {
    private final AbstractCard card;

    public AfterCardUseAction(AbstractCard abstractCard) {
        this.card = abstractCard;
        this.duration = Settings.ACTION_DUR_FAST;
    }

    @Override
    public void update() {
        publishOnUseCard();
        isDone = true;
    }
    private void publishOnUseCard() {
        for (AbstractCreature m : (AbstractDungeon.getMonsters()).monsters) {
            for (AbstractPower p : m.powers) {
                if (p instanceof AfterCardPlayedInterface)
                    ((AfterCardPlayedInterface) p).afterCardPlayed(card);
            }
        }
        for (AbstractPower p : AbstractDungeon.player.powers) {
            if (p instanceof AfterCardPlayedInterface)
                ((AfterCardPlayedInterface) p).afterCardPlayed(card);
        }
    }
}
