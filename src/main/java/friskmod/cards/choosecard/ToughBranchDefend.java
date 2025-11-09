package friskmod.cards.choosecard;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import friskmod.cards.ToughBranch;
import friskmod.powers.AbstractCountdownPower;
import friskmod.powers.CountdownAttack;

import static friskmod.FriskMod.makeID;
import static friskmod.helper.DraftManager.setDraftTarget;

public class ToughBranchDefend extends AbstractChooseCard{
    public static final String ID = makeID(ToughBranchDefend.class.getSimpleName());

    public ToughBranchDefend() {
        super(ID);
        baseBlock = ToughBranch.BLOCK;
    }

    @Override
    public void chooseOption() {
        AbstractPlayer p = AbstractDungeon.player;
        addToBot(new GainBlockAction(p, p, baseBlock));
        AbstractCountdownPower countdown = new CountdownAttack(getTarget(), ToughBranch.DAMAGE, ToughBranch.WAIT_TIMER, ToughBranch.UPG_DAMAGE);
        if (upgraded) {
            countdown.upgrade();
        }
        addToBot(new ApplyPowerAction(getTarget(), p, countdown, ToughBranch.DAMAGE));
        setDraftTarget(null);
    }

    @Override
    public void upp() {

    }
}
