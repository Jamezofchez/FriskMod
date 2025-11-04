package friskmod.patches;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.LineFinder;
import com.evacipated.cardcrawl.modthespire.lib.Matcher;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertLocator;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.ShowCardAndPoofAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.screens.SingleCardViewPopup;
import java.lang.reflect.Method;

import friskmod.FriskMod;
import friskmod.cards.EchoFlower;
import javassist.CannotCompileException;
import javassist.CtBehavior;

public class EchoFlowerPatch {
    @SpirePatch(clz = AbstractCard.class, method = "renderEnergy")
    public static class PatchPearlescenceCostColor {
        @SpireInsertPatch(locator = Locator.class, localvars = {"costColor"})
        public static void Insert(AbstractCard __instance, SpriteBatch _, Color ___ENERGY_COST_RESTRICTED_COLOR, @ByRef Color[] costColor) {
            if (__instance.price == -100 && AbstractDungeon.player != null && !__instance.hasEnoughEnergy())
                costColor[0] = ___ENERGY_COST_RESTRICTED_COLOR;
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher.FieldAccessMatcher fieldAccessMatcher = new Matcher.FieldAccessMatcher(AbstractCard.class, "transparency");
                return LineFinder.findInOrder(ctMethodToPatch, (Matcher)fieldAccessMatcher);
            }
        }
    }

//    @SpirePatch(clz = UseCardAction.class, method = "update")
//    public static class PatchPearlescencePurge {
//        @SpireInsertPatch(locator = Locator.class)
//        public static SpireReturn Insert(UseCardAction __instance, AbstractCard ___targetCard) {
//            if (___targetCard instanceof EchoFlower) {
//                AbstractDungeon.actionManager.addToTop((AbstractGameAction)new ShowCardAndPoofAction(___targetCard));
//                __instance.isDone = true;
//                AbstractDungeon.player.cardInUse = null;
//                return SpireReturn.Return();
//            }
//            return SpireReturn.Continue();
//        }
//
//        private static class Locator extends SpireInsertLocator {
//            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
//                Matcher.FieldAccessMatcher fieldAccessMatcher = new Matcher.FieldAccessMatcher(AbstractCard.class, "purgeOnUse");
//                return LineFinder.findInOrder(ctMethodToPatch, (Matcher)fieldAccessMatcher);
//            }
//        }
//    }

    @SpirePatch(clz = SingleCardViewPopup.class, method = "render")
    public static class PatchPearlescencePopup {
        @SpireInsertPatch(locator = Locator.class)
        public static SpireReturn Insert(SingleCardViewPopup __instance, SpriteBatch sb, AbstractCard ___card) {
            if (___card instanceof EchoFlower) {
                ___card.current_x = Settings.WIDTH / 2.0F;
                ___card.current_y = Settings.HEIGHT / 2.0F;
                ___card.drawScale = 2.0F;
                ___card.render(sb);
                try {
                    Method method = SingleCardViewPopup.class.getDeclaredMethod("renderArrows", new Class[] { SpriteBatch.class });
                    method.setAccessible(true);
                    method.invoke(__instance, new Object[] { sb });
                    method = SingleCardViewPopup.class.getDeclaredMethod("renderTips", new Class[] { SpriteBatch.class });
                    method.setAccessible(true);
                    method.invoke(__instance, new Object[] { sb });
                } catch (Exception e) {
                    FriskMod.logger.warn("{}: reflection failed in PatchPearlescencePopup: {}", FriskMod.modID, String.valueOf(e));
                }
                return SpireReturn.Return();
            }
            return SpireReturn.Continue();
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher.MethodCallMatcher methodCallMatcher = new Matcher.MethodCallMatcher(SingleCardViewPopup.class, "renderCardBack");
                return LineFinder.findInOrder(ctMethodToPatch, (Matcher)methodCallMatcher);
            }
        }
    }
}


/* Location:              C:\Program Files (x86)\Steam\steamapps\workshop\content\646570\2371967829\Reliquary.jar!\patches\PatchPearlescence.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */