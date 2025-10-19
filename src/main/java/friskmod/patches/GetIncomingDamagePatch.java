package friskmod.patches;

import basemod.ReflectionHacks;
import basemod.abstracts.CustomMonster;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.LineFinder;
import com.evacipated.cardcrawl.modthespire.lib.Matcher;
import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertLocator;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import javassist.CannotCompileException;
import javassist.CtBehavior;

public class GetIncomingDamagePatch {
    @SpirePatch(clz = AbstractDungeon.class, method = "render")
    public static class TIDHook {
        @SpireInsertPatch(locator = Locator.class)
        public static void hook(AbstractDungeon __instance, SpriteBatch sb) {
            if (AbstractDungeon.player != null && AbstractDungeon.currMapNode != null && (AbstractDungeon.getCurrRoom()).phase == AbstractRoom.RoomPhase.COMBAT) {
                int c = 0, dmg = 0, tmp = 0;
                if (AbstractDungeon.getMonsters() != null) {
                    for (AbstractMonster m : (AbstractDungeon.getMonsters()).monsters) {
                        if (!m.isDeadOrEscaped() && isAttacking(m)) {
                            c++;
                            int multiAmt = 0;
                            multiAmt = (Integer) ReflectionHacks.getPrivate(m, AbstractMonster.class, "intentMultiAmt");
                            tmp = m.getIntentDmg();
                            if (multiAmt > 1)
                                tmp *= multiAmt;
                            if (tmp > 0)
                                dmg += tmp;
                        }
                    }
                    if (c > 0 && dmg > 0) {

                    }
                }
            }
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher.MethodCallMatcher methodCallMatcher = new Matcher.MethodCallMatcher(AbstractDungeon.class, "getCurrRoom");
                return LineFinder.findInOrder(ctMethodToPatch, (Matcher) methodCallMatcher);
            }
        }
    }

    public static boolean isAttacking(AbstractMonster m) {
        return (m.getIntentBaseDmg() >= 0);
    }
}
