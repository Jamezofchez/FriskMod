package friskmod.patches.perseverance.occult;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.UIStrings;

import static friskmod.FriskMod.makeID;

@SpirePatch(
        clz = AbstractCard.class,
        method = "initializeDescription"
)
public class PerseverableDescription {
    public static UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(makeID("Perseverable"));
    public static String[] perseverable = uiStrings.TEXT;

    @SpirePrefixPatch
    public static void betterHaveTheKeyword(AbstractCard __instance)
    {
        if (PerseveranceFields.isPerseverable.get(__instance) && (!__instance.rawDescription.startsWith(perseverable[0]) && !__instance.rawDescription.contains(perseverable[1])))
        {
            __instance.rawDescription = perseverable[0] + __instance.rawDescription;
        }
    }
}
