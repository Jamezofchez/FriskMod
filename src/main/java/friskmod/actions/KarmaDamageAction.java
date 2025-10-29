package friskmod.actions;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import friskmod.powers.Karma;
import friskmod.util.Wiz;

public class KarmaDamageAction extends AbstractGameAction {
    @SpirePatch2(clz = DamageInfo.class, method = SpirePatch.CLASS)
    public static class KarmaDamageTypeFields {
        public static SpireField<Boolean> isNotKarma = new SpireField<>(() -> true);
    }

    private final Karma karma;

    public KarmaDamageAction(Karma karma) {
        this.karma = karma;

    }

    @Override
    public void update() {
        int targetHealth = karma.owner.currentHealth;
        DamageInfo info = new DamageInfo(AbstractDungeon.player, Math.min(targetHealth-1, karma.amount),DamageInfo.DamageType.HP_LOSS);
        KarmaDamageTypeFields.isNotKarma.set(info, false);
        karma.flashWithoutSound();
        Wiz.att(new DamageAction(karma.owner, info, AttackEffect.FIRE));
        this.isDone = true;
    }
}
