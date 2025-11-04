package friskmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import friskmod.cards.BalletShoes;
import friskmod.powers.AfterCardPlayedInterface;
import friskmod.powers.CountdownPounce;

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
        for (AbstractCard card : AbstractDungeon.player.hand.group) {
            if (card instanceof BalletShoes){
                ((BalletShoes) card).afterCardPlayed();
            }
        }
        for (AbstractMonster m : (AbstractDungeon.getMonsters()).monsters) {
            for (AbstractPower p : m.powers) {
                if (p instanceof CountdownPounce){
                    if (card != null){
                        if (card.type == AbstractCard.CardType.ATTACK){
                            ((CountdownPounce) p).setPlayerNormalDamage(true);
                        }
                    }
                }
                if (p instanceof AfterCardPlayedInterface) {
                    ((AfterCardPlayedInterface) p).afterCardPlayed(card);
                }
            }
        }
        for (AbstractPower p : AbstractDungeon.player.powers) {
            if (p instanceof AfterCardPlayedInterface)
                ((AfterCardPlayedInterface) p).afterCardPlayed(card);
        }
    }
}
