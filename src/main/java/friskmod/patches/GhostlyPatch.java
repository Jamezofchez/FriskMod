package friskmod.patches;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import friskmod.powers.NonAttackPower;

public class GhostlyPatch {
    @SpirePatch2(clz = AbstractCard.class, method = SpirePatch.CLASS)
    public static class GhostlyCardFields {
        public static SpireField<Boolean> isGhostly = new SpireField<>(() -> false);
    }
    @SpirePatch2(clz = DamageInfo.DamageType.class, method = SpirePatch.CLASS)
    public static class GhostlyDamageTypeFields {
        public static SpireField<Boolean> isGhostly = new SpireField<>(() -> false);
    }
    @SpirePatch(clz = AbstractCard.class, method = "calculateCardDamage")
    public static class AbstractCardCalculateCardDamagePatch {
        @SpirePrefixPatch
        public static void Prefix(AbstractCard __instance) {
            SetDamageTypeThorns(__instance);
        }
    }
    @SpirePatch(clz = AbstractCard.class, method = "applyPowers")
    public static class AbstractCardApplyPowersPatch {
        @SpirePrefixPatch
        public static void Prefix(AbstractCard __instance) {
            SetDamageTypeThorns(__instance);
        }
    }
    @SpirePatch(clz = AbstractCard.class, method = "applyPowersToBlock")
    public static class AbstractCardApplyPowersToBlockPatch {
        @SpirePostfixPatch
        public static void Postfix(AbstractCard __instance) {
            AbstractPower posspow = AbstractDungeon.player.getPower(NonAttackPower.POWER_ID);
            if (posspow != null){
                __instance.isBlockModified = false;
                __instance.block = __instance.baseBlock;
            }
        }

    }

    private static void SetDamageTypeThorns(AbstractCard __instance) {
        if (AbstractDungeon.player != null && __instance.type == AbstractCard.CardType.ATTACK){
            if(ReflectionHacks.getPrivate(__instance, AbstractCard.class, "damageType") == DamageInfo.DamageType.NORMAL || __instance.damageTypeForTurn == DamageInfo.DamageType.NORMAL) {
                AbstractPower posspow = AbstractDungeon.player.getPower(NonAttackPower.POWER_ID);
                if (posspow != null){
                    if (!__instance.dontTriggerOnUseCard) {
                        ReflectionHacks.setPrivate(__instance, AbstractCard.class, "damageType", DamageInfo.DamageType.THORNS);
                        __instance.damageTypeForTurn = DamageInfo.DamageType.THORNS;
                        GhostlyDamageTypeFields.isGhostly.set(__instance.damageTypeForTurn, true);
                        __instance.dontTriggerOnUseCard = true;
                        GhostlyCardFields.isGhostly.set(__instance, true);
                    }
                }
            }
        }
    }
}
