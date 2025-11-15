package friskmod.cards.choosecard;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import friskmod.cards.ToughBranch;
import friskmod.powers.AbstractCountdownPower;
import friskmod.powers.CountdownAttack;
import friskmod.powers.CountdownDefend;

import static friskmod.FriskMod.makeID;
import static friskmod.helper.DraftManager.setDraftTarget;

public class ToughBranchAttack extends AbstractChooseCard{
    public static final String ID = makeID(ToughBranchAttack.class.getSimpleName());

    public ToughBranchAttack() {
        super(ID);
        baseDamage = ToughBranch.DAMAGE;
    }

    @Override
    public void chooseOption() {
        AbstractPlayer p = AbstractDungeon.player;
//        addToBot(new DamageAction(getTarget(), new DamageInfo(p, baseDamage, DamageInfo.DamageType.NORMAL)));
        AbstractCountdownPower countdown = new CountdownDefend(p, ToughBranch.BLOCK, ToughBranch.WAIT_TIMER, ToughBranch.UPG_BLOCK);
        if (upgraded) {
            countdown.upgrade();
        }
        addToBot(new ApplyPowerAction(p, p, countdown, ToughBranch.BLOCK));
        setDraftTarget(null);
    }

    @Override
    public void upp() {

    }
}
