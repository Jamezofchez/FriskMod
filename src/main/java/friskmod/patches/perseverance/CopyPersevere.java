package friskmod.patches.perseverance;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;

@SpirePatch(
        clz = AbstractCard.class,
        method = "makeStatEquivalentCopy"
)
public class CopyPersevere {
    @SpirePostfixPatch
    public static AbstractCard transferIt(AbstractCard __result, AbstractCard __instance)
    {
//        PerseveranceFields.isPerseverable.set(__result, PerseveranceFields.isPerseverable.get(__instance));
        PerseveranceFields.trapped.set(__result, PerseveranceFields.trapped.get(__instance));
        __result.initializeDescription();
        return __result;
    }
}
