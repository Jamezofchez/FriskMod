package friskmod.actions;

import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class StealEnemyHP extends AbstractGameAction {
    private AbstractMonster monster;
    public StealEnemyHP(int amount, AbstractMonster monster) {
        this.amount = amount;
        this.monster = monster;
    }
    @Override
    public void update() {
        this.isDone = true;
        AbstractPlayer p = AbstractDungeon.player;
        addToTop(new AddTemporaryHPAction(p, p, amount));
        addToTop(new DamageAction(monster, new DamageInfo(p, amount, DamageInfo.DamageType.HP_LOSS), AbstractGameAction.AttackEffect.NONE, true));

    }
}
