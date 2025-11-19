package friskmod.monsters;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.actions.unique.GainBlockRandomMonsterAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import friskmod.BetterSpriterAnimation;
import friskmod.FriskMod;
import friskmod.powers.SoulBound;
import friskmod.util.Wiz;

public class GladDummy extends AbstractDummy {
    public static final String ID = FriskMod.makeID(GladDummy.class.getSimpleName());

    public int amount;

    AbstractCreature boundTarget;

    public GladDummy(float x, float y, int hp, int amount, AbstractCreature boundTarget) {
        super(ID, ID, x, y, hp);
        this.amount = amount;
        this.boundTarget = boundTarget;
        this.animation = new BetterSpriterAnimation(FriskMod.monsterPath("GladDummy/Spriter/GladDummy.scml"));
        addMove((byte)0, Intent.DEFEND);
    }

    public void usePreBattleAction() {
        super.usePreBattleAction();
        applyToSelf(new SoulBound(this, this.boundTarget));
        applyToSelf(new friskmod.powers.GladDummy(this, this.boundTarget));
    }

    @Override
    public void takeTurn() {
        super.takeTurn();
        switch (this.nextMove) {
            case 0:
                if (Wiz.getMonsters().stream().allMatch(m -> m instanceof AbstractDummy)){
                    die();
                    return;
                }
                attackAnimation((AbstractCreature) Wiz.adp());
                this.info.applyPowers(this, AbstractDungeon.player);
                addToBot(new GainBlockAction(AbstractDungeon.player, this, 2));
//                addToBot(new GainBlockRandomMonsterAction(this, 3));
//                for (AbstractMonster m : Wiz.getMonsters()) {
//                    this.info.applyPowers(this, m);
//                    addToBot(new GainBlockAction(m, this, 3));
//                }
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
        animationAction("Attack", "snd_heal_c", enemy, (AbstractCreature)this);
    }

}


/* Location:              C:\Program Files (x86)\Steam\steamapps\workshop\content\646570\3100352485\TheLocksmith.jar!\theAnime\monsters\BlockDummy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */