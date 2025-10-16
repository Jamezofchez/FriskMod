package friskmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ChangeStateAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;
import friskmod.util.Wiz;
import basemod.ReflectionHacks;


public class EnemyPowerStealPatch {
    @SpirePatch2(
            clz = CurlUpPower.class,
            method = "onAttacked"
    )
    public static class CurlUpPowerOnAttackedPatch {
        @SpirePrefixPatch
        public static SpireReturn<Integer> Prefix(CurlUpPower __instance, DamageInfo info, int damageAmount) {
            // If this power is on a monster, continue normally
            if (__instance.owner instanceof AbstractMonster) {
                return SpireReturn.Continue();
            }
            if (!(boolean) ReflectionHacks.getPrivate(__instance, CurlUpPower.class, "triggered") && damageAmount < __instance.owner.currentHealth && damageAmount > 0 && info.owner != null && info.type == DamageInfo.DamageType.NORMAL) {

                __instance.flash();
                ReflectionHacks.setPrivate(__instance, CurlUpPower.class, "triggered", true);
                Wiz.atb((AbstractGameAction) new GainBlockAction(__instance.owner, __instance.owner, __instance.amount));
                Wiz.atb((AbstractGameAction) new RemoveSpecificPowerAction(__instance.owner, __instance.owner, "Curl Up"));
            }
            return SpireReturn.Return(damageAmount);
        }
    }
    @SpirePatch2(
            clz = FlightPower.class,
            method = "onRemove"
    )
    public static class FlightPowerOnRemovePatch {
        @SpirePrefixPatch
        public static SpireReturn<Void> Prefix(FlightPower __instance) {
            if (__instance.owner instanceof AbstractMonster) {
                return SpireReturn.Continue();
            }
            return SpireReturn.Return();
        }
    }
}
