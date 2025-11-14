package friskmod.monsters;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import friskmod.BetterSpriterAnimation;
import friskmod.FriskMod;
import friskmod.powers.SoulBound;
import friskmod.util.Wiz;

import java.util.ArrayList;

public class MadDummy extends AbstractDummy {
    public static final String ID = FriskMod.makeID(MadDummy.class.getSimpleName());

    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);

    public static final String NAME = monsterStrings.NAME;

    public static final String[] MOVES = monsterStrings.MOVES;

    public static final String[] DIALOG = monsterStrings.DIALOG;

    public int amount;

    AbstractCreature boundTarget;


    public MadDummy(float x, float y, int hp, int amount, AbstractCreature boundTarget) {
        super(ID, ID, x, y, hp);
        this.amount = amount;
        this.boundTarget = boundTarget;
        this.animation = new BetterSpriterAnimation(FriskMod.monsterPath("MadDummy/Spriter/MadDummy.scml"));
        addMove((byte)0, Intent.ATTACK, 3);
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
                AbstractCreature randomEnemy = getRandomEnemy();
                if (randomEnemy != null) {
                    attackAnimation(randomEnemy);
                    Wiz.atb(new DamageAction(randomEnemy, this.info));
                    resetIdle(0.25F);
                    waitAnimation(0.25F);
                }
                break;
        }
        Wiz.atb(new RollMoveAction(this));
    }

    private AbstractCreature getRandomEnemy() {
        ArrayList<AbstractMonster> tmp = new ArrayList();

        for(AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
            if (!m.halfDead && !m.isDying && !m.isEscaping && !(m instanceof AbstractDummy)) {
                tmp.add(m);
            }
        }

        if (tmp.size() == 0) {
            return null;
        } else {
            return tmp.get(AbstractDungeon.cardRandomRng.random(0, tmp.size() - 1));
        }
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