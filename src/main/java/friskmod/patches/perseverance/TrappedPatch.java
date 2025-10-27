package friskmod.patches.perseverance;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.actions.unique.LoseEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import friskmod.util.Wiz;

import static friskmod.FriskMod.makeID;

public class TrappedPatch {
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(makeID(TrappedPatch.class.getSimpleName()));
    private static final String[] Trapped = uiStrings.TEXT;
    @SpirePatch(
            clz = AbstractCard.class,
            method = "initializeDescription"
    )
    public static class TrappedDescription {
        @SpirePrefixPatch
        public static void Prefix(AbstractCard __instance) {
            if (PerseveranceFields.trapped.get(__instance) && (!__instance.rawDescription.startsWith(Trapped[0]) && !__instance.rawDescription.contains(Trapped[1]))) {
                __instance.rawDescription = Trapped[0] + __instance.rawDescription;
            }
        }
    }
    @SpirePatch(
            clz = AbstractCard.class,
            method = "triggerWhenDrawn"
    )
    public static class AbstractCardTriggerWhenDrawnPatch {
        @SpirePrefixPatch
        public static void Prefix(AbstractCard __instance) {
            if (PerseveranceFields.trapped.get(__instance)) {
//                int cardCost = __instance.costForTurn;
//                if (cardCost == -1) {
//                    Wiz.atb(new LoseEnergyAction(EnergyPanel.getCurrentEnergy()));
//                } else if (cardCost > 0) {
//                    Wiz.atb(new LoseEnergyAction(cardCost));
//                }
                Wiz.atb(new LoseEnergyAction(1));
            }
        }
    }
    @SpirePatch(
            clz = AbstractCard.class,
            method = "canUse"
    )
    public static class AbstractCardCanUsePatch {
        @SpirePostfixPatch
        public static boolean Postfix(boolean __result, AbstractCard __instance) {
            if (PerseveranceFields.trapped.get(__instance)) {
                __instance.cantUseMessage = Trapped[2];
                return false;
            }
            return __result;
        }
    }
    @SpirePatch(
            clz = AbstractCard.class,
            method = "hasEnoughEnergy"
    )
    public static class AbstractCardHasEnoughEnergyPatch {
        @SpirePostfixPatch
        public static boolean Postfix(boolean __result, AbstractCard __instance) {
            if (PerseveranceFields.trapped.get(__instance)) {
                __instance.cantUseMessage = Trapped[2];
                return false;
            }
            return __result;
        }
    }
}
