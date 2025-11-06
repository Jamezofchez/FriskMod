package friskmod.patches;

import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import friskmod.powers.GladDummy;
import friskmod.powers.MadDummy;
import javassist.CtBehavior;

public class DummyPowerPatch {
    @SpirePatch2(clz = ApplyPowerAction.class, method = SpirePatch.CLASS)
    public static class GladDummyPowerTrigger {
        public static SpireField<Boolean> triggeredGladDummy = new SpireField<>(() -> Boolean.FALSE);
    }

    @SpirePatch2(clz = GainBlockAction.class, method = SpirePatch.CLASS)
    public static class GladDummyBlockTrigger {
        public static SpireField<Boolean> triggeredGladDummy = new SpireField<>(() -> Boolean.FALSE);
    }

    @SpirePatch2(clz = AddTemporaryHPAction.class, method = SpirePatch.CLASS)
    public static class GladDummyTempHPTrigger {
        public static SpireField<Boolean> triggeredGladDummy = new SpireField<>(() -> Boolean.FALSE);
    }

    @SpirePatch2(
            clz = GainBlockAction.class,
            method = "update"
    )
    public static class ApplyBlockActionUpdatePatch {
        @SpirePrefixPatch
        public static void Prefix(GainBlockAction __instance) {
            if (!FrailOnEnemiesPatch.FrailTrigger.triggeredFrail.get(__instance)) {
                FrailOnEnemiesPatch.ApplyBlockActionUpdatePatch.Prefix(__instance);
            }
            if (!GladDummyBlockTrigger.triggeredGladDummy.get(__instance)) {
                GladDummyBlockTrigger.triggeredGladDummy.set(__instance, true);
                if (__instance.target instanceof AbstractMonster) {
                    if (__instance.amount > 0) {
                        for (AbstractPower p : __instance.target.powers) {
                            if (p instanceof GladDummy)
                                ((GladDummy) p).onGainBlock(__instance.amount);
                        }
                    }
                }
            }
        }
    }
    @SpirePatch2(
            clz = AddTemporaryHPAction.class,
            method = "update"
    )
    public static class ApplyTempHPActionUpdatePatch {
        @SpirePrefixPatch
        public static void Prefix(AddTemporaryHPAction __instance){
            if (GladDummyTempHPTrigger.triggeredGladDummy.get(__instance)){
                GladDummyTempHPTrigger.triggeredGladDummy.set(__instance, true);
                if (__instance.target instanceof AbstractMonster) {
                    if (__instance.amount > 0) {
                        for (AbstractPower p : __instance.target.powers) {
                            if (p instanceof GladDummy)
                                ((GladDummy) p).onGainTempHP(__instance.amount);
                        }                    }
                }
            }
        }
    }



    @SpirePatch2(
            clz = ApplyPowerAction.class,
            method = "update"
    )
    public static class ApplyPowerActionUpdatePatch {
        @SpirePrefixPatch
        public static void Prefix(ApplyPowerAction __instance, AbstractPower ___powerToApply){
            if (!GladDummyPowerTrigger.triggeredGladDummy.get(__instance)){
                GladDummyPowerTrigger.triggeredGladDummy.set(__instance, true);
                if (__instance.target instanceof AbstractMonster) {
                    if (__instance.source == AbstractDungeon.player){
                        for (AbstractPower p : __instance.target.powers) {
                            if (p instanceof GladDummy)
                                ((GladDummy) p).onPlayerApplyPower(___powerToApply);
                        }
                    }
                }
            }
        }
    }

    @SpirePatch(clz = AbstractMonster.class, method = "damage")
    public static class OnLoseHPMonsterPatch {
        @SpireInsertPatch(locator = Locator.class)
        public static void Insert(AbstractMonster m, DamageInfo info) {
            for (AbstractPower p : m.powers) {
                if (p instanceof MadDummy)
                    ((MadDummy) p).onLoseHpMonster(m.lastDamageTaken);
            }
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher.FieldAccessMatcher fieldAccessMatcher = new Matcher.FieldAccessMatcher(AbstractMonster.class, "currentHealth");
                return new int[]{LineFinder.findAllInOrder(ctMethodToPatch, (Matcher) fieldAccessMatcher)[2]};
            }
        }
    }
}


/* Location:              C:\Program Files (x86)\Steam\steamapps\workshop\content\646570\3100352485\TheLocksmith.jar!\theAnime\patches\OnLoseHPMonsterPatch.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */