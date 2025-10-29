package friskmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import friskmod.util.Wiz;

import java.util.List;

public class BalletShoesAction extends AbstractGameAction {
    AbstractMonster m;
    int numTimes;
    public BalletShoesAction(AbstractMonster m, int numTimes){
        this.actionType = AbstractGameAction.ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_MED;
        this.m = m;
        this.numTimes = numTimes;
    }

    @Override
    public void update() {
        double hand_size = (AbstractDungeon.player.hand.size())-1; //hasn't updated yet?
        if (hand_size % 2 == 0) {
            --hand_size;
        }
        int card_pos = (int) (hand_size-1)/2;
        List<AbstractCard> afterHand = AbstractDungeon.player.hand.group;
        AbstractCard chosenCard = afterHand.get(card_pos);
        if (chosenCard != null) {
            for (int i = 0; i < numTimes; ++i) {
                if (m == null || m.isDeadOrEscaped()) {
                    m = AbstractDungeon.getRandomMonster();
                }
                Wiz.att(new CardPlayAction(chosenCard, m));
                Wiz.att(new CustomSFXAction("snd_punchstrong"));
                Wiz.att(new WaitAction(0.25f));
            }
            Wiz.att(new CustomSFXAction("mus_sfx_voice_triple"));
        }
        AbstractDungeon.player.hand.moveToExhaustPile(chosenCard);
    }

}
