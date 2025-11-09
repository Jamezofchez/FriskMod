package friskmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import friskmod.cards.BalletShoes;
import friskmod.powers.NonAttackPower;
import friskmod.util.Wiz;
import friskmod.util.interfaces.AfterCardPlayedInterface;

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
        for (AbstractCard c : AbstractDungeon.player.hand.group) {
            if (c instanceof AfterCardPlayedInterface){
                ((AfterCardPlayedInterface) c).afterCardPlayed(this.card);
            }
        }
        for (AbstractMonster m : (AbstractDungeon.getMonsters()).monsters) {
            for (AbstractPower p : m.powers) {
                if (p instanceof AfterCardPlayedInterface) {
                    ((AfterCardPlayedInterface) p).afterCardPlayed(this.card);
                }
            }
        }
        for (AbstractPower p : AbstractDungeon.player.powers) {
            if (p instanceof AfterCardPlayedInterface)
                ((AfterCardPlayedInterface) p).afterCardPlayed(this.card);
        }
    }
}
