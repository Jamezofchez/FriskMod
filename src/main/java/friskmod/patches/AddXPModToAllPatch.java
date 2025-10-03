package friskmod.patches;
import com.evacipated.cardcrawl.mod.stslib.damagemods.AbstractDamageModifier;
import com.evacipated.cardcrawl.mod.stslib.damagemods.DamageModifierManager;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import friskmod.damagemods.XPModifierAll;


import java.util.ArrayList;
import java.util.List;


public class AddXPModToAllPatch {
    @SpirePatch2(
            clz = DamageModifierManager.class,
            method = "modifiers"
    )
    public static class DamageModifierManagerModifiersPatch {
        @SpirePostfixPatch
        public static List<AbstractDamageModifier> Postfix(List<AbstractDamageModifier> __result, AbstractCard card) {
            if (card == null) {
                return __result;
            }

            if (card.type != AbstractCard.CardType.ATTACK){
                return __result;
            }
            if (__result == null) {
                __result = new ArrayList<>();
            }
            boolean hasXPModifier = __result.stream().anyMatch(m -> m instanceof XPModifierAll);
            if (!hasXPModifier) {
                __result.add(0,new XPModifierAll(card));
            }
            return __result;
        }
    }

}

