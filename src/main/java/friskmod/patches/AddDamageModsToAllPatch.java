package friskmod.patches;
import com.evacipated.cardcrawl.mod.stslib.damagemods.AbstractDamageModifier;
import com.evacipated.cardcrawl.mod.stslib.damagemods.DamageModifierManager;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import friskmod.damagemods.AbsolutePowerPowerAll;
import friskmod.damagemods.XPModifierAll;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class AddDamageModsToAllPatch {
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
            __result.removeIf(m -> m instanceof XPModifierAll || m instanceof AbsolutePowerPowerAll);
            __result.add(0, new XPModifierAll(card));
            __result.add(new AbsolutePowerPowerAll());
            return __result;
        }
    }
//    @SpirePatch2(clz = DamageInfo.class, method = SpirePatch.CONSTRUCTOR,
//            paramtypez = {AbstractCreature.class, int.class, DamageInfo.DamageType.class})
//    public static class DamageInfoCopyPatch {
//        @SpirePrefixPatch
//        public static void onConstruct(DamageInfo __instance, AbstractCreature owner, int base, DamageInfo.DamageType type) {
//            if (AbstractDungeon.actionManager.currentAction instanceof com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction) {
//                AbstractCard card = ReflectionHacks.getPrivate(AbstractDungeon.actionManager, AbstractDungeon.class, "cardQueue").peek().card;
//                if (card != null) {
//                    DamageModifierManager.bindDamageMods(__instance, DamageModifierManager.modifiers(card));
//                }
//            }
//        }
//    }


}

