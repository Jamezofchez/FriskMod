package friskmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;

import static friskmod.helper.DraftManager.setDraftTarget;
import static friskmod.helper.DraftManager.setLastChoices;

public class ResetDraftAction extends AbstractGameAction {
    @Override
    public void update() {
        this.isDone = true;
        setDraftTarget(null);
        setLastChoices(null);
    }
}
