package friskmod.patches;

import basemod.helpers.CardModifierManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.UIStrings;
import friskmod.cardmods.MonochromeMod;
import friskmod.cardmods.CriticalCheckerMod;
import friskmod.util.CardArtRoller;
import friskmod.util.Wiz;
import friskmod.util.WizArt;

import java.util.regex.Pattern;

import static friskmod.FriskMod.makeID;

public class MonochromePatch {
    @SpirePatch(
            clz = AbstractCard.class,
            method = SpirePatch.CLASS
            //AbstractCard(String id, String name, String imgUrl, int cost, String rawDescription, CardType type, CardColor color, CardRarity rarity, CardTarget target, DamageInfo.DamageType dType) {
    )
    public static class MonochromeFields {
        public static SpireField<Boolean> isMonochrome = new SpireField<>(()->false);
        public static SpireField<Boolean> wasCriticalPlayed = new SpireField<>(()->false);
    }
    @SpirePatch2(
            clz = AbstractCard.class,
            method = SpirePatch.CONSTRUCTOR,
            paramtypez = { String.class, String.class, String.class, int.class, String.class, AbstractCard.CardType.class, AbstractCard.CardColor.class, AbstractCard.CardRarity.class, AbstractCard.CardTarget.class, DamageInfo.DamageType.class}
    )
    public static class AddCriticalCheckerMod {
        @SpirePrefixPatch
        public static void Prefix(AbstractCard __instance) {
            if (Wiz.isInCombat()) {
                if (!CardModifierManager.hasModifier(__instance, CriticalCheckerMod.ID)) {
                    Wiz.att(Wiz.actionify(() -> {
                            CardModifierManager.addModifier(__instance, new CriticalCheckerMod());
                    }));

                }
            }
        }
    }

    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(makeID(MonochromePatch.class.getSimpleName()));
    private static final String[] Monochrome = uiStrings.TEXT;
    @SpirePatch(
            clz = AbstractCard.class,
            method = "initializeDescription"
    )
    public static class MonochromeDescription {
        @SpirePrefixPatch
        public static void Prefix(AbstractCard __instance) {
            //???
            if (__instance == null){
                return;
            }
            if (__instance.rawDescription == null){
                return;
            }
            if (Monochrome == null){
                return;
            }
            if (Monochrome[0] == null){
                return;
            }
            if (Monochrome[1] == null){
                return;
            }
            if (MonochromeFields.isMonochrome.get(__instance)) {
                if ((!__instance.rawDescription.startsWith(Monochrome[0]) && !__instance.rawDescription.contains(Monochrome[1]))) {
                    __instance.rawDescription = Monochrome[0] + __instance.rawDescription;
                }
            } else{
                /*
                    "${modID}:Monochrome. NL ",
                    "NL ${modID}:Monochrome. NL",
                 */
                __instance.rawDescription = __instance.rawDescription.replaceFirst(Pattern.quote(Monochrome[0]), "");

            }
        }
    }
    public static void makeMonochromeCard(AbstractCard card){
        AbstractCard c = card.makeStatEquivalentCopy();
        CardModifierManager.addModifier(c, new MonochromeMod());
        MonochromeFields.isMonochrome.set(c, true);
        Wiz.atb(new MakeTempCardInHandAction(c));
    }

    @SpirePatch(
            clz = AbstractCard.class,
            method = "makeStatEquivalentCopy"
    )
    public static class CopyMonochrome {
        @SpirePostfixPatch
        public static AbstractCard transferIt(AbstractCard __result, AbstractCard __instance)
        {
            MonochromeFields.isMonochrome.set(__result, MonochromeFields.isMonochrome.get(__instance));
            __result.initializeDescription();
            return __result;
        }
    }


    // Render card portraits in black and white when isMonochrome is set
    @SpirePatch2(
            clz = AbstractCard.class,
            method = "renderPortrait"
    )
    public static class MonochromeRenderPortrait {
        private static final ThreadLocal<Boolean> applied = ThreadLocal.withInitial(() -> false);
        @SpirePrefixPatch
        public static void Prefix(AbstractCard __instance, SpriteBatch sb) {
            if (__instance != null && MonochromeFields.isMonochrome.get(__instance)) {
                applied.set(true);
                WizArt.saveState(sb);
                sb.setShader(CardArtRoller.getGrayscaleShader());
                sb.setColor(Color.WHITE);
            } else {
                applied.set(false);
            }
        }
        @SpirePostfixPatch
        public static void Postfix(AbstractCard __instance, SpriteBatch sb) {
            if (applied.get()) {
                WizArt.loadState(sb);
                applied.set(false);
            }
        }
    }
//    @SpirePatch(
//            clz = AbstractPlayer.class,
//            method = "useCard",
//            paramtypez = { AbstractCard.class, AbstractMonster.class, int.class }
//    )
//    public static class UseCopyCardPatch {
//        // Replace the direct call to c.use(p, m) with a guarded call
//        public static ExprEditor Instrument() {
//            return new ExprEditor() {
//                @Override
//                public void edit(MethodCall m) throws CannotCompileException {
//                    if (m.getClassName().equals(AbstractCard.class.getName()) && m.getMethodName().equals("use")) {
//                        m.replace(
//                                "{" +
//                                        "  if (" + (MonochromePatch.class.getName() + "$UseCopyCardPatch.isCopy($0)") + ") {" +
//                                        "    " + (MonochromePatch.class.getName() + "$UseCopyCardPatch$.handleCopyPlayed($0)") + ";" +
//                                        "  } " +
//                                        " $proceed($$);" +
//                                        "}"
//                        );
//                    }
//                }
//            };
//        }
//        public static boolean isCopy(AbstractCard c) {
//            if (c == null) {
//                return false;
//            }
//            if (MonochromePatch.MonochromeFields.isCopied.get(c)) {
//                return true;
//            }
//            return false;
//        }
//        public static void handleCopyPlayed(AbstractCard c) {
//            if (c == null) {
//                return;
//            }
//            c.exhaustOnUseOnce = true;
//            Wiz.atb(new GainEnergyAction(1));
//        }
//    }
}
