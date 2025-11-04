package friskmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import friskmod.patches.perseverance.PerseveranceFields;
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
        this.isDone = true;
        AbstractPlayer p = AbstractDungeon.player;
        List<AbstractCard> afterHand = p.hand.group;
        double hand_size = afterHand.size();
        int card_pos = (int) (hand_size-1)/2;
        if (card_pos < 0) { //no cards in hand
            return;
        }
        AbstractCard chosenCard = afterHand.get(card_pos);
        if (chosenCard != null) {
            for (int i = 0; i < numTimes; ++i) {
//                Wiz.att(new CardPlayAction(chosenCard, m));
                PerseveranceFields.dontTrap.set(chosenCard, true);
                Wiz.atb(new PersevereCardAction(chosenCard.makeSameInstanceOf(), m, false));
                Wiz.atb(new CustomSFXAction("snd_punchstrong"));
                Wiz.atb(new WaitAction(0.25f));
            }
            if (numTimes == 3){
                Wiz.atb(new CustomSFXAction("mus_sfx_voice_triple"));
            }
            AbstractCard tmp = chosenCard.makeStatEquivalentCopy();
            PerseveranceFields.trapped.set(tmp, true);
            Wiz.atb(new MakeTempCardInHandAction(tmp));
            p.hand.moveToExhaustPile(chosenCard);
        }
    }
}
