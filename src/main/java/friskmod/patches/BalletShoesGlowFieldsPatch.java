package friskmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import basemod.ReflectionHacks;

public class BalletShoesGlowFieldsPatch {
    @SpirePatch2(clz = AbstractCard.class, method = SpirePatch.CLASS)
    public static class BalletShoesGlowFields {
        public static SpireField<Boolean> glowingBecauseBalletShoes = new SpireField<>(() -> false);
    }
    @SpirePatch2(clz = AbstractCard.class, method = "triggerOnGlowCheck")
    public static class AbstractCardTriggerOnGlowCheckPatch {
        @SpirePostfixPatch
        public static void Postfix(AbstractCard __instance) {
            com.badlogic.gdx.graphics.Color BLUE_GLOW = ReflectionHacks.getPrivateStatic(AbstractCard.class, "BLUE_BORDER_GLOW_COLOR");
            if (!__instance.glowColor.equals(BLUE_GLOW)){
                BalletShoesGlowFields.glowingBecauseBalletShoes.set(__instance, false);
            }
        }
    }
}
