package friskmod.patches;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.CurlUpPower;
import friskmod.actions.CustomSFXAction;
import friskmod.util.Wiz;

public class AudioToActionPatch{
    @SpirePatch2(
            clz = HealAction.class,
            method = "update"
    )
    public static class HealActionUpdatePatch {
        @SpirePrefixPatch
        public static void Prefix(HealAction __instance) {
            {
                float duration = ReflectionHacks.getPrivate(__instance, AbstractGameAction.class, "duration");
                float startDuration = ReflectionHacks.getPrivate(__instance, AbstractGameAction.class, "startDuration");
                if (duration == startDuration) {
                    if (__instance.target == AbstractDungeon.player) {
                        Wiz.atb(new CustomSFXAction("snd_heal_c"));
                    }
                }
            }
        }
    }
    @SpirePatch2(
            clz = AddTemporaryHPAction.class,
            method = "update"
    )
    public static class AddTemporaryHPActionUpdatePatch {
        @SpirePrefixPatch
        public static void Prefix(AbstractGameAction __instance) {
            {
                float duration = ReflectionHacks.getPrivate(__instance, AbstractGameAction.class, "duration");
                float startDuration = ReflectionHacks.getPrivate(__instance, AbstractGameAction.class, "startDuration");
                if (duration == startDuration) {
                    if (__instance.target == AbstractDungeon.player) {
                        Wiz.atb(new CustomSFXAction("snd_heal_c"));
                    }
                }
            }
        }
    }
    @SpirePatch2(
            clz = AbstractMonster.class,
            method = "die",
            paramtypez = {boolean.class}
    )
    public static class AbstractMonsterDie {
        @SpirePrefixPatch
        public static void Prefix(AbstractMonster __instance) {
            {
                Wiz.atb(new CustomSFXAction("snd_vaporized"));
            }
        }
    }
    //    public LoseHPAction(AbstractCreature target, AbstractCreature source, int amount, AbstractGameAction.AttackEffect effect) {
    @SpirePatch2(
            clz = LoseHPAction.class,
            method = "update"
    )
    public static class LoseHPActionUpdatePatch {
        @SpirePrefixPatch
        public static void Prefix(LoseHPAction __instance) {
            {
                float duration = ReflectionHacks.getPrivate(__instance, AbstractGameAction.class, "duration");
                if (duration == 0.33F){
                    if (__instance.target == AbstractDungeon.player && __instance.source == AbstractDungeon.player) {
                        Wiz.atb(new CustomSFXAction("snd_hurt1"));
                    }
                }
            }
        }
    }

}
