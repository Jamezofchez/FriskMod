package friskmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.EnergyManager;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import friskmod.cards.Chill;
import friskmod.powers.RecyclePower;
import friskmod.util.interfaces.WastedEnergyInterface;
import friskmod.util.Wiz;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

import java.util.ArrayList;

public class WastedEnergyPatch {
    @SpirePatch2(clz = EnergyManager.class, method = SpirePatch.CLASS)
    public static class WastedEnergyEndOfTurnFields {
        public static SpireField<Boolean> wastedEnergy = new SpireField<>(() -> Boolean.TRUE);
        public static SpireField<Integer> wastedEnergyAmount = new SpireField<>(() -> 0);

    }
    public static int previous_e_index;
    public static ArrayList<Integer> previous_e_list;
    static {
        previous_e_index = 0;
        previous_e_list = new ArrayList<>();
        previous_e_list.add(0);
    }


    public static boolean hasRecycle(AbstractPlayer __instance){
        if (__instance == null) return false;
        return __instance.hasPower(RecyclePower.POWER_ID);
    }

    public static void addEnergyHide(EnergyManager __instance) {
        if (__instance == null) return;
        if (__instance.energy > 0) {
            EnergyPanel.addEnergy(__instance.energy);
        }
    }

    public static void resetLastWasted() {
        for (AbstractCard c : Wiz.getAllCardsInCardGroups()) {
            if (c instanceof Chill){
                ((Chill) c).setWastedCostForTurn();
            }
        }
        ++previous_e_index;
        previous_e_list.add(0);
    }

    @SpirePatch(
            clz = AbstractPlayer.class,
            method = "loseEnergy"
    )
    public static class AbstractPlayerLoseEnergyPatch {
        @SpirePrefixPatch
        public static SpireReturn<Void> Prefix(AbstractPlayer __instance) {
            if (hasRecycle(__instance)){
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
            previous_e_index = 0;
            previous_e_list = new ArrayList<>();
            previous_e_list.add(0);
        }
    }

    private static void checkWastedEnergy(int e) {
        if (e <= 0){
            return;
        }
        previous_e_list.set(previous_e_index, previous_e_list.get(previous_e_index) + e);
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
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                @Override
                public void edit(MethodCall m) throws CannotCompileException {
                    if (m.getClassName().equals(AbstractPlayer.class.getName()) && m.getMethodName().equals("hasRelic")) {
                        m.replace(
                                "{" +
                                        "  if (" + (WastedEnergyPatch.class.getName() + ".hasRecycle($0)") + ") {" +
                                        "    " + (WastedEnergyPatch.class.getName() + ".addEnergyHide(this)") + ";" +
                                        "   " + AbstractDungeon.class.getName() + ".actionManager.updateEnergyGain(this.energy);" +
                                        "    return;" +
                                        "  } else {" +
                                        "    $_ = $proceed($$);" +
                                        "  }" +
                                        "}"
                        );
                    }
                }
            };
        }

        @SpireInsertPatch(locator = BeforeAddEnergyLocator.class)
        public static void Insert(EnergyManager __instance) {
            WastedEnergyEndOfTurnFields.wastedEnergy.set(__instance, Boolean.FALSE);
        }

        @SpirePostfixPatch
        public static void Postfix(EnergyManager __instance) {
            if (WastedEnergyEndOfTurnFields.wastedEnergy.get(__instance)) {
                checkWastedEnergy(WastedEnergyEndOfTurnFields.wastedEnergyAmount.get(__instance));
            }
            resetLastWasted();
        }

        private static class BeforeAddEnergyLocator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher matcher = new Matcher.MethodCallMatcher(EnergyPanel.class, "addEnergy");
                // Find *all* occurrences of addEnergy
                return LineFinder.findAllInOrder(ctMethodToPatch, new ArrayList<>(), matcher);
            }
        }
    }
}