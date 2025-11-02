package friskmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import friskmod.cards.Dream;
import friskmod.util.Wiz;

public class DraftDreamPerDebuff extends AbstractGameAction {
    private AbstractPlayer p;
    private AbstractMonster m;
    private OpenDraftAction.DreamType type;
    private int selectionAmount;

    public DraftDreamPerDebuff(AbstractPlayer p, AbstractMonster m, OpenDraftAction.DreamType type, int selectionAmount) {
        this.p = p;
        this.m = m;
        this.type = type;
        this.selectionAmount = selectionAmount;
    }
    @Override
    public void update() {
        this.isDone = true;
        for (AbstractPower q : m.powers) {
            if (q.type == AbstractPower.PowerType.DEBUFF) {
                Wiz.atb(new PlayerEnemyDraftAction(p, m, type, selectionAmount));
            }
        }
    }
}
