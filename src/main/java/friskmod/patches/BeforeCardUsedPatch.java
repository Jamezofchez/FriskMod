//package friskmod.patches;
//
//import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
//import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
//import com.megacrit.cardcrawl.actions.utility.UseCardAction;
//import com.megacrit.cardcrawl.cards.AbstractCard;
//import com.megacrit.cardcrawl.core.AbstractCreature;
//import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
//import com.megacrit.cardcrawl.powers.AbstractPower;
//import friskmod.FriskMod;
//import friskmod.actions.DecreaseNonAttackPower;
//import friskmod.cards.AbstractCriticalCard;
//import friskmod.cards.AnnoyingDog;
//import friskmod.cards.BreakFree;
//import friskmod.patches.perseverance.PerseveranceFields;
//import friskmod.powers.NonAttackPower;
//import friskmod.util.Wiz;
//
//import java.util.List;
//
//import static friskmod.FriskMod.logger;
//
//public class BeforeCardUsedPatch {
//    @SpirePatch(
//            clz = UseCardAction.class,
//            method = SpirePatch.CONSTRUCTOR,
//            paramtypez = { AbstractCard.class, AbstractCreature.class }
//    )
//    public static class OnCardUsed {
//        @SpirePostfixPatch
//        public static void Postfix(UseCardAction __instance, AbstractCard card, AbstractCreature target) {
////            if (!card.dontTriggerOnUseCard) {
////                AbstractPower possFavouriteNumber = AbstractDungeon.player.getPower(FavouriteNumberPower.POWER_ID);
////                if (possFavouriteNumber != null) {
////                    possFavouriteNumber.onSpecificTrigger();
////                }
////            }
//        }
//    }
//}
