//package friskmod.patches;
//import basemod.ReflectionHacks;
//import com.evacipated.cardcrawl.modthespire.lib.*;
//import com.megacrit.cardcrawl.actions.AbstractGameAction;
//import com.megacrit.cardcrawl.actions.common.DamageAction;
//import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
//import com.megacrit.cardcrawl.cards.AbstractCard;
//import com.megacrit.cardcrawl.cards.DamageInfo;
//import com.megacrit.cardcrawl.core.AbstractCreature;
//import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
//import com.megacrit.cardcrawl.monsters.AbstractMonster;
//import com.megacrit.cardcrawl.powers.AbstractPower;
//import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
//import friskmod.FriskMod;
//import friskmod.actions.SwapXPAction;
//import friskmod.powers.LV_Enemy;
//import friskmod.powers.LV_Hero;
//import friskmod.powers.PreventLVLoss;
//import friskmod.util.Wiz;
//
//
//import java.util.Objects;
//
//
//public class BackupDamageActionLikePatch {
//    @SpirePatch2(clz = DamageAction.class, method = SpirePatch.CLASS)
//    public static class DamageActionFields {
//        public static SpireField<AbstractCard> sourceCard = new SpireField<>(() -> null);
//    }
//    @SpirePatch2(clz = DamageAction.class, method = "update")
//    public static class DamageActionUpdatePost {
//        @SpirePostfixPatch
//        public static void  Postfix(DamageAction __instance) {
//            AbstractCard card = DamageActionFields.sourceCard.get(__instance);
//            if (card == null) return;
//            AbstractCreature target = __instance.target;
//            if (target == null) return;
//
//            int lastDamageTaken = target.lastDamageTaken;
//            if (lastDamageTaken <= 0) {
//                // Fully blocked or no HP loss — don’t swap XP
//                return;
//            }
//
//        }
//    }
//    @SpirePatch2(clz = DamageAction.class, method = "update")
//    public static class DamageActionUpdate {
//        @SpirePrefixPatch
//        public static void Prefix(DamageAction __instance) {
//            int numCards = AbstractDungeon.actionManager.cardsPlayedThisTurn.size();
//            if (numCards == 0) {
//                return;
//            }
//            float duration = ReflectionHacks.getPrivate(__instance, AbstractGameAction.class, "duration");
//            if (duration == 0.1F) { //On start
//                DamageInfo info = ReflectionHacks.getPrivate(__instance, DamageAction.class, "info"); //ReflectionHacks needed as damageType isn't accurate
//                AbstractCreature source = info.owner;
//                if (source == AbstractDungeon.player) {
//                    AbstractCard card = AbstractDungeon.actionManager.cardsPlayedThisTurn.get(numCards - 1);
//                    if (card != null) {
//                        DamageInfo.DamageType damageType = info.type;
//                        if (damageType == DamageInfo.DamageType.NORMAL) {
//                            DamageActionFields.sourceCard.set(__instance,card);
//                        }
//                    }
//                }
//            }
//        }
//    }
//}
//
