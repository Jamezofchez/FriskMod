package friskmod.helper;

import basemod.ReflectionHacks;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class ThreatenedCheck {
    private static boolean isAttacking(AbstractMonster m) {
        return (m.getIntentBaseDmg() >= 0);
    }
    public static boolean isThreatened() {
        int dmg = 0;
        for (AbstractMonster m : (AbstractDungeon.getMonsters()).monsters) {
            int tmp = 0;
            if (!m.isDeadOrEscaped() && isAttacking(m)) {
                int multiAmt = 0;
                multiAmt = (Integer) ReflectionHacks.getPrivate(m, AbstractMonster.class, "intentMultiAmt");
                tmp = m.getIntentDmg();
                if (multiAmt > 1)
                    tmp *= multiAmt;
                if (tmp > 0)
                    dmg += tmp;
            }
        }
        int playerHP = AbstractDungeon.player.currentHealth;
        return (dmg >= playerHP);
    }
}
