package friskmod.monsters;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import friskmod.BetterSpriterAnimation;
import friskmod.customintent.IntentEnums;
import friskmod.FriskMod;
import friskmod.powers.SoulBound;
import friskmod.util.Wiz;

public class MadDummy extends AbstractDummy {
    public static final String ID = FriskMod.makeID(MadDummy.class.getSimpleName());

    public int amount;

    AbstractCreature boundTarget;

    public MadDummy(float x, float y, int hp, int amount, AbstractCreature boundTarget) {
        super(ID, ID, x, y, hp);
        this.amount = amount;
        this.boundTarget = boundTarget;
        this.animation = new BetterSpriterAnimation(FriskMod.monsterPath("MadDummy/Spriter/MadDummy.scml"));
        addMove((byte)0, IntentEnums.MASS_ATTACK, 3);
    }

    public void usePreBattleAction() {
        applyToSelf(new SoulBound(this, this.boundTarget));
        applyToSelf(new friskmod.powers.MadDummy(this, this.boundTarget));
    }

    @Override
    public void takeTurn() {
        super.takeTurn();
        switch (this.nextMove) {
            case 0:
                int damageArray[];
                attackAnimation((AbstractCreature) Wiz.adp());
                damageArray = calcMassAttack(this.info);
                Wiz.atb(new DamageAllEnemiesAction(this, damageArray, DamageInfo.DamageType.NORMAL, AbstractGameAction.AttackEffect.SLASH_VERTICAL));
                resetIdle(0.25F);
                waitAnimation(0.25F);
                break;
        }
        Wiz.atb(new RollMoveAction(this));
    }
    @Override
    protected void getMove(int num) {
        setMoveShortcut((byte)0);
    }
    private void attackAnimation(AbstractCreature enemy) {
        animationAction("Attack", "snd_laz", enemy, (AbstractCreature)this);
    }
}


/* Location:              C:\Program Files (x86)\Steam\steamapps\workshop\content\646570\3100352485\TheLocksmith.jar!\theAnime\monsters\BlockDummy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */