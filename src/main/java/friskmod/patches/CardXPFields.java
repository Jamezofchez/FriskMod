package friskmod.patches;

import basemod.helpers.CardModifierManager;
import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import friskmod.FriskMod;
import friskmod.actions.SetAddedXPAction;
import friskmod.actions.SetInherentXPAction;
import friskmod.cardmods.XPMod;
import friskmod.util.Wiz;


public class CardXPFields {
    public static final Color XP_TINT = new Color(1.0F, 0.0F, 0.0F, 1.0F);
//    public static final Color STABLE_TINT = Color.WHITE.cpy();

    @SpirePatch2(clz = AbstractCard.class, method = SpirePatch.CLASS)
    public static class XPFields {
        public static SpireField<Integer> inherentXP = new SpireField<>(() -> 0);
        public static SpireField<Integer> addedXP = new SpireField<>(() -> 0);
        public static SpireField<Integer> triginherentXP = new SpireField<>(() -> 0);
        public static SpireField<Integer> trigaddedXP = new SpireField<>(() -> 0);

    }

    public static int getCardXP(AbstractCard card) {
        return XPFields.inherentXP.get(card) + XPFields.addedXP.get(card);
    }

    public static boolean getCardXPBool(AbstractCard card) {
        if (getCardXP(card) > 0) {
            return true;
        }
        return false;
    }

    public static int getCardInherentXP(AbstractCard card) {
        return XPFields.inherentXP.get(card);
    }

    public static int getCardAddedXP(AbstractCard card) {
        return XPFields.addedXP.get(card);
    }

    public static void setInherentXP(AbstractCard card, int amount) {
        if (amount <= 0) {
            amount = 0;
        }

//            FriskMod.logger.warn("{}: queue setting inherentXP failed", FriskMod.modID);
        XPFields.inherentXP.set(card, amount);
        CardModifierManager.addModifier(card, new XPMod());
        card.initializeDescription();
    }
    public static void setAddedXP(AbstractCard card, int amount) {
        int resultXP = getCardXP(card) + amount;
        if (resultXP <= 0) {
            amount = 0;
        }
        try {
            Wiz.att(new SetAddedXPAction(card, amount));
        } catch (Exception e) {
//            FriskMod.logger.warn("{}: queue setting addedXP failed", FriskMod.modID);
            XPFields.addedXP.set(card, amount);
            CardModifierManager.addModifier(card, new XPMod());
            card.initializeDescription();
        }
    }

    public static void addXP(AbstractCard card, int amount) {
        if (amount == 0) {
            return;
        }
        int previousCardXP = getCardAddedXP(card);
        int newXP = previousCardXP + amount;
        setAddedXP(card, newXP);
    }
    public static void addInherentXP(AbstractCard card, int amount) {
        if (amount == 0) {
            return;
        }
        int previousCardXP = getCardInherentXP(card);
        int newXP = previousCardXP + amount;
        setInherentXP(card, newXP);
    }

    public static void flashXPColor(AbstractCard card) {
        if (getCardXPBool(card)){
            card.superFlash(XP_TINT.cpy());
            return;
        }
//        card.superFlash(STABLE_TINT.cpy());
    }
    @SpirePatch(
            clz = AbstractCard.class,
            method = "makeStatEquivalentCopy"
    )
    public static class AbstractCardMakeStatEquivalentCopyPatch {
        @SpirePostfixPatch
        public static AbstractCard transferIt(AbstractCard __result, AbstractCard __instance)
        {
            int currentInherent = XPFields.inherentXP.get(__instance);
            int currentTrigInherent = XPFields.triginherentXP.get(__instance);
            if (currentTrigInherent == 0 && currentInherent > 0) {
                XPFields.triginherentXP.set(__instance, currentInherent);
            }
            int currentAdded = XPFields.addedXP.get(__instance);
            int currentTrigAdded = XPFields.trigaddedXP.get(__instance);
            if (currentTrigAdded == 0 && currentAdded > 0){
                XPFields.trigaddedXP.set(__instance, currentAdded);
            }
            XPFields.inherentXP.set(__result, XPFields.triginherentXP.get(__instance));
            XPFields.addedXP.set(__result, XPFields.trigaddedXP.get(__instance));
            __result.initializeDescription();
            return __result;
        }
    }
//    @SpirePatch(clz = AbstractCard.class, method = "makeStatEquivalentCopy")
//    public static class MakeStatEquivalentCopy {
//        public static AbstractCard Postfix(AbstractCard result, AbstractCard self) {
//            setAddedXP(result, XPFields.addedXP.get(self));
//            return result;
//        }
//    }

//    @SpirePatch2(clz = AbstractCard.class, method = "makeStatEquivalentCopy")
//    public static class AbstractCardMakeStatEquivalentCopy {
//        @SpirePostfixPatch
//        public static void Postfix(AbstractCard result, AbstractCard self) {
//            CardXPFields.setAddedXP(result, getCardAddedXP(self));
//        }
//    }
}


/* Location:              C:\Program Files (x86)\Steam\steamapps\workshop\content\646570\3390561252\Snowpunk.jar!\Snowpunk\patches\CardTemperatureFields.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */