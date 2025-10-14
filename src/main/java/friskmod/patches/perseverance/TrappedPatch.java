//package friskmod.patches.perseverance;
//
//import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
//import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
//import com.megacrit.cardcrawl.cards.AbstractCard;
//import com.megacrit.cardcrawl.core.CardCrawlGame;
//import com.megacrit.cardcrawl.localization.UIStrings;
//
//import static friskmod.FriskMod.makeID;
//
//@SpirePatch(
//        clz = AbstractCard.class,
//        method = "initializeDescription"
//)
//public class PerseverableDescription {
//    private static UIStrings uiStrings = null;
//    private static String[] perseverable = null;
//
//    private static void initializeStrings() {
//        if (uiStrings == null) {
//            uiStrings = CardCrawlGame.languagePack.getUIString(makeID("Perseverable"));
//            perseverable = uiStrings.TEXT;
//        }
//    }
//    @SpirePrefixPatch
//    public static void betterHaveTheKeyword(AbstractCard __instance)
//    {
//        initializeStrings();
//        if (PerseveranceFields.isPerseverable.get(__instance) && (!__instance.rawDescription.startsWith(perseverable[0]) && !__instance.rawDescription.contains(perseverable[1])))
//        {
//            __instance.rawDescription = perseverable[0] + __instance.rawDescription;
//        }
//    }
//}
