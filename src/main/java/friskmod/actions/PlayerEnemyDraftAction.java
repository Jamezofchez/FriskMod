package friskmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import friskmod.util.Wiz;

import static friskmod.helper.DraftManager.setDraftTarget;
import static friskmod.helper.DraftManager.setLastChoices;

public class PlayerEnemyDraftAction extends AbstractGameAction {

    private AbstractPlayer p;
    private AbstractMonster m;
    private OpenDraftAction.DreamType type;
    private int selectionAmount;

    public PlayerEnemyDraftAction(AbstractPlayer p, AbstractMonster m, OpenDraftAction.DreamType type, int selectionAmount){
        this.p = p;
        this.m = m;
        this.type = type;
        this.selectionAmount = selectionAmount;
    }

    @Override
    public void update() {
        this.isDone = true;
        Wiz.att(new ResetDraftAction());
        --selectionAmount;
        Wiz.att(new OpenDraftAction(m, type, selectionAmount));
        ++selectionAmount;
        Wiz.att(new OpenDraftAction(p, type, selectionAmount));
    }
}
