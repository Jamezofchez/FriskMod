package friskmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import friskmod.powers.WastedEnergyInterface;
import friskmod.util.Wiz;

public class OnWasteEnergyPatch {
    public static int previous_e = 0;

    @SpirePatch(
            clz = AbstractPlayer.class,
            method = "loseEnergy"
    )
    public static class AbstractPlayerLoseEnergyPatch {

        public static void Postfix(AbstractPlayer __instance, int e) {
            checkWastedEnergy(e);
        }
    }

    @SpirePatch(clz = AbstractRoom.class, method = "endTurn")
    public static class AbstractRoomEndTurnPatch {
        @SpirePrefixPatch
        public static void Prefix(AbstractRoom __instance) {
            if (Wiz.isInCombat()) {
                previous_e = 0;
                int e = EnergyPanel.getCurrentEnergy();
                checkWastedEnergy(e);
            }
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
}