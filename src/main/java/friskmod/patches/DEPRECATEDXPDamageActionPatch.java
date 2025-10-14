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
//public class DEPRECIATEDXPDamageActionPatch {
//    @SpirePatch2(clz = DamageAction.class, method = SpirePatch.CLASS)
//    public static class DamageActionFields {
//        public static SpireField<Boolean> isLVRecalc = new SpireField<>(() -> false);
//        public static SpireField<SwapXPInfo> swapXPafter = new SpireField<>(() -> null);
//    }
//    @SpirePatch2(clz = DamageAction.class, method = "update")
//    public static class DamageActionUpdatePost {
//        @SpirePostfixPatch
//        public static void  Postfix(DamageAction __instance) {
//            SwapXPInfo info = DamageActionFields.swapXPafter.get(__instance);
//            if (info == null) return;
//
//            AbstractCreature target = __instance.target;
//            if (target == null) return;
//
//            int unblocked = target.lastDamageTaken;
//            if (unblocked <= 0) {
//                // Fully blocked or no HP loss — don’t swap XP
//                return;
//            }
//            AbstractCard card = info.card;
//            AbstractCreature player = info.player;
//            int lvTransferFrom = info.lvTransferFrom;
//            int LV_transfer_to = info.lvTransferTo;
//            Wiz.att(new SwapXPAction(card, player, target, LV_transfer_to, lvTransferFrom));
//            DamageActionFields.swapXPafter.set(__instance, null);
//        }
//    }
//    @SpirePatch2(clz = DamageAction.class, method = "update")
//    public static class DamageActionUpdate {
//        @SpirePrefixPatch
//        public static SpireReturn<Void> Prefix(DamageAction __instance) {
//            // If this DamageAction has already been marked done, skip its logic entirely
//            if (__instance.isDone) {
//                return SpireReturn.Return(null);
//            }
//            float duration = ReflectionHacks.getPrivate(__instance, AbstractGameAction.class, "duration");
//            if (duration == 0.1F) {
//                DamageInfo info = ReflectionHacks.getPrivate(__instance, DamageAction.class, "info"); //ReflectionHacks needed as damageType isn't accurate
//                AbstractCreature source = info.owner;
//                if (source == AbstractDungeon.player) {
//                    int numCards = AbstractDungeon.actionManager.cardsPlayedThisTurn.size();
//                    if (numCards == 0) {
//                        return SpireReturn.Continue();
//                    }
//                    AbstractCard card = AbstractDungeon.actionManager.cardsPlayedThisTurn.get(numCards - 1);
//                    if (card != null) {
//                        DamageInfo.DamageType damageType = info.type;
//                        if (damageType == DamageInfo.DamageType.NORMAL) {
//                            //RECOMPUTE DAMAGE BASED ON CURRENT XP - MAY HAVE CHANGED ON MULTI-HIT
//                            card.applyPowers();
//                            if (__instance.target instanceof AbstractMonster) {
//                                card.calculateCardDamage((AbstractMonster)__instance.target);
//                            }
//                            card.initializeDescription();
//                            DamageInfo newInfo = new DamageInfo(AbstractDungeon.player, card.damage, info.type);
//                            ReflectionHacks.setPrivate(__instance, DamageAction.class, "info", newInfo);
//                            boolean continueAction = preDamage(card, __instance);
//                            if (!continueAction){
//                                return SpireReturn.Return(null);
//                            }
//                        }
//                    }
//                }
//            }
//
//            // Continue normally
//            return SpireReturn.Continue();
//        }
//    }
//    public static class SwapXPInfo{
//        final public AbstractCard card;
//        final public AbstractCreature player;
//        final public AbstractCreature target;
//        final public int lvTransferFrom;
//        final public int lvTransferTo;
//        public SwapXPInfo(AbstractCard card, AbstractCreature player, AbstractCreature target, int lvTransferFrom, int lvTransferTo) {
//            this.card = card;
//            this.player = player;
//            this.target = target;
//            this.lvTransferFrom = lvTransferFrom;
//            this.lvTransferTo = lvTransferTo;
//        }
//    }
//    private static Runnable queueSwapXP(DamageAction damageAction, AbstractCard card, AbstractCreature player, AbstractCreature target, int lvTransferFrom) {
//        return () -> {
//            int xp = CardXPFields.getCardXP(card);
//            int lvTransferTo = 0;
//            if (xp > 0) {
//                lvTransferTo = xp - 1;
//            }
//            if (lvTransferTo == 0 && lvTransferFrom == 0) return;
//            DamageActionFields.swapXPafter.set(damageAction, new SwapXPInfo(card,player,target,lvTransferFrom,lvTransferTo));
//        };
//    }
//    private static boolean preDamage(AbstractCard card, DamageAction damageAction) {
//        if (card.type == AbstractCard.CardType.ATTACK) {
//            AbstractCreature target = damageAction.target;
//            if (target == null) return false;
//            AbstractCreature p = AbstractDungeon.player;
//            AbstractPower LV_enemy_power = target.getPower(LV_Enemy.POWER_ID);
//            int LV_transfer_from = 0;
//            if (LV_enemy_power != null) {
//                int LV_Enemy = LV_enemy_power.amount;
//                LV_transfer_from = LV_Enemy - 1;
//            }
//            final int final_LV_transfer_from = LV_transfer_from; //cache problems
//            final AbstractCard finalCard = card;
//            final AbstractCreature finalp = p;
//            final AbstractCreature finalTarget = target;
//            int XP_from_LV_Hero = 0;
//            if (!DamageActionFields.isLVRecalc.get(damageAction)){
//                AbstractPower LV_Hero_Power = p.getPower(LV_Hero.POWER_ID);
//                if (LV_Hero_Power != null) {
//                    XP_from_LV_Hero = LV_Hero_Power.amount;
//                    boolean hasLoveSaver = false;
//                    AbstractPower targetPower = null;
//                    for (AbstractPower pow : p.powers) {
//                        if (Objects.equals(pow.ID, PreventLVLoss.POWER_ID)) {
//                            hasLoveSaver = true;
//                            targetPower = pow;
//                            break;
//                        }
//                    }
//                    if (hasLoveSaver) {
//                        targetPower.flash();
//                        targetPower.amount--;
//                        if (targetPower.amount <= 0) {
//                            Wiz.atb(new RemoveSpecificPowerAction(p, p, PreventLVLoss.POWER_ID));
//                        }
//                    } else {
//                        Wiz.atb(new RemoveSpecificPowerAction(p, p, LV_Hero.POWER_ID));
//                    }
//                }
//            }
//            if (DamageActionFields.isLVRecalc.get(damageAction)) {
//                Wiz.att(Wiz.actionify(queueSwapXP(damageAction, finalCard, finalp, finalTarget, final_LV_transfer_from)));
//            } else {
//                if (XP_from_LV_Hero > 0) {
//                    // Remove the current action from the queue
//                    DamageInfo info = ReflectionHacks.getPrivate(damageAction, DamageAction.class, "info");
//                    final AbstractGameAction.AttackEffect finalAttackEffect = damageAction.attackEffect;
//                    damageAction.isDone = true;
//                    Wiz.att(new AbstractGameAction() {
//                        @Override
//                        public void update() {
//                            finalCard.applyPowers();
//                            if (finalTarget instanceof AbstractMonster) {
//                                finalCard.calculateCardDamage((AbstractMonster)finalTarget);
//                            }
//                            card.initializeDescription();
//                            DamageInfo newInfo = new DamageInfo(finalp, finalCard.damage, info.type);
//                            DamageAction newAction = new DamageAction(finalTarget, newInfo, finalAttackEffect);
//                            DamageActionFields.isLVRecalc.set(newAction, true);
//                            Wiz.att(newAction);
//                            this.isDone = true;
//                        }
//                    });
//                    // Add XP to the card
//                    CardXPFields.addXP(card, XP_from_LV_Hero);
//                    return false;
//
//                } else {
//                    queueSwapXP(damageAction, finalCard, finalp, finalTarget, final_LV_transfer_from).run();
//                }
//                return true;
//            }
//
//        } else{
//            FriskMod.logger.warn("{}: Attempted card map to pre damageAction is not an attack", FriskMod.modID);
//        }
//        return true;
//    }
//}
//
