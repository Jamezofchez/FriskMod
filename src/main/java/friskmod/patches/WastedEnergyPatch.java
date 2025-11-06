package friskmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.EnergyManager;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import friskmod.powers.RecyclePower;
import friskmod.util.interfaces.WastedEnergyInterface;
import friskmod.util.Wiz;
import javassist.CtBehavior;

import java.util.ArrayList;

public class WastedEnergyPatch {
    @SpirePatch2(clz = EnergyManager.class, method = SpirePatch.CLASS)
    public static class WastedEnergyEndOfTurnFields {
        public static SpireField<Boolean> wastedEnergy = new SpireField<>(() -> Boolean.TRUE);
        public static SpireField<Integer> wastedEnergyAmount = new SpireField<>(() -> 0);

    }
    public static int previous_e = 0;

    @SpirePatch(
            clz = AbstractPlayer.class,
            method = "loseEnergy"
    )
    public static class AbstractPlayerLoseEnergyPatch {
        @SpirePrefixPatch
        public static SpireReturn<Void> Prefix() {
            AbstractPower posspow = AbstractDungeon.player.getPower(RecyclePower.POWER_ID);
            if (posspow != null){
                return SpireReturn.Return();
            }
            return SpireReturn.Continue();
        }
        @SpirePostfixPatch
        public static void Postfix(AbstractPlayer __instance, int e) {
            checkWastedEnergy(e);
        }
    }

    @SpirePatch(clz = AbstractRoom.class, method = "endBattle")
    public static class AbstractRoomEndBattlePatch {
        @SpirePrefixPatch
        public static void Prefix(AbstractRoom __instance) {
            previous_e = 0;
        }
    }

    private static void checkWastedEnergy(int e) {
        if (e <= 0){
            return;
        }
        previous_e += e;
        for (AbstractPower p : AbstractDungeon.player.powers) {
            if (p instanceof WastedEnergyInterface) {
                ((WastedEnergyInterface) p).WasteEnergyAction(e);
            }
        }
    }

    @SpirePatch(clz = EnergyManager.class, method = "recharge")
    public static class EnergyManagerRechargePatch {
        @SpirePrefixPatch
        public static void Prefix(EnergyManager __instance) {
            WastedEnergyEndOfTurnFields.wastedEnergy.set(__instance, Boolean.TRUE);
            WastedEnergyEndOfTurnFields.wastedEnergyAmount.set(__instance, EnergyPanel.getCurrentEnergy());

        }

        @SpireInsertPatch(locator = Locator.class)
        public static void Insert(EnergyManager __instance) {
            AbstractPlayer p = AbstractDungeon.player;
            if (p.hasPower(RecyclePower.POWER_ID)) {
                justInCase(__instance);
            }
        }

        private static void justInCase(EnergyManager __instance) {
            if (__instance != null && __instance.energy > 0) {
                EnergyPanel.addEnergy(__instance.energy);
            }
        }

        @SpirePrefixPatch
        public static void Postfix(EnergyManager __instance) {
            if (WastedEnergyEndOfTurnFields.wastedEnergy.get(__instance)) {
                checkWastedEnergy(WastedEnergyEndOfTurnFields.wastedEnergyAmount.get(__instance));
            }
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher matcher = new Matcher.MethodCallMatcher(AbstractPlayer.class, "hasRelic");
                return LineFinder.findInOrder(ctMethodToPatch, matcher);
            }
        }
    }
}