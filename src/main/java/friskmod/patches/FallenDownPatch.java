package friskmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.potions.ExplosivePotion;
import com.megacrit.cardcrawl.powers.AbstractPower;
import friskmod.powers.FallenDown;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.NewExpr;

public class FallenDownPatch {
    @SpirePatch(clz = AbstractMonster.class, method = "damage")
    public static class AbstractMonsterDamagePatch {
        public static void Prefix(AbstractMonster m, DamageInfo info) {
            if (AbstractDungeon.player != null && (info.owner == null || info.owner == AbstractDungeon.player) &&
                    !m.hasPower("Intangible") && !m.hasPower("IntangiblePlayer") &&
                    info.type != DamageInfo.DamageType.NORMAL) {
                AbstractPower fallenDown = m.getPower(FallenDown.POWER_ID);
                if (fallenDown != null)
                    info.output = (int)(info.output * ((FallenDown)fallenDown).mult());
            }
        }
    }
    @SpirePatch(clz = ExplosivePotion.class, method = "use")
    public static class ExplosivePotionUsesThornsDamage {
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                public void edit(NewExpr e) throws CannotCompileException {
                    if (e.getClassName().equals(DamageAllEnemiesAction.class.getName()))
                        e.replace("$_ = new " + DamageAllEnemiesAction.class.getName() + "($1, $2, " + DamageInfo.DamageType.class.getName() + ".THORNS, $4);");
                }
            };
        }
    }
}
