package friskmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.screens.*;
import com.megacrit.cardcrawl.screens.select.GridCardSelectScreen;
import com.megacrit.cardcrawl.screens.select.HandCardSelectScreen;
import friskmod.helper.GrillbysHelper;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Consumer;

import static friskmod.helper.GrillbysHelper.isCardCostAllowed;

public class LimitCardCostPatch {
    @SpirePatch2(clz = AbstractCard.class, method = "hasTag")
    public static class AvoidAddingHighCostPatch {
        public static boolean Postfix(AbstractCard __instance, boolean __result, AbstractCard.CardTags ___tagToCheck){
            if (___tagToCheck == AbstractCard.CardTags.HEALING) {
                return __result || !isCardCostAllowed(__instance);
            }
            return __result;
        }
    }
}
