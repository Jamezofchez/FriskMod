package friskmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.List;

public class WebStrikeAction extends AbstractGameAction {
    AbstractMonster m;

    public WebStrikeAction(AbstractMonster m ) {
        this.m = m;
    }

    @Override
    public void update() {
        this.isDone = true;
        AbstractPlayer p = AbstractDungeon.player;
        List<AbstractCard> afterHand = p.hand.group;
        if (!afterHand.isEmpty()) {
            AbstractCard rightmost = afterHand.get(afterHand.size()-1);
            addToTop(new PersevereCardAction(rightmost, m));
        }
    }
}

