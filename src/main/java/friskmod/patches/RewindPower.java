package friskmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import friskmod.powers.AbstractCountdownPower;
import friskmod.util.Wiz;

public class RewindPower {
    public static int turnCounter = 0;
    @SpirePatch2(
            clz = AbstractPlayer.class,
            method = "applyStartOfCombatLogic"
    )
    public static class AbstractPlayerResetTurnCounterPatch {
        @SpirePrefixPatch
        public static void Prefix() {
            turnCounter = 0;
        }
    }
    @SpirePatch2(
            clz = AbstractPlayer.class,
            method = "applyStartOfTurnRelics"
    )
    public static class AbstractPlayerIncreaseTurnCounterPatch {
        @SpirePrefixPatch
        public static void Prefix() {
            ++turnCounter;
        }
    }
    @SpirePatch2(
            clz = AbstractRoom.class,
            method = "endTurn"
    )
    public static class AbstractRoomStoreEnemyPowersPatch {
        @SpirePrefixPatch
        public static void Prefix() {
            for (AbstractMonster m : Wiz.getMonsters()) {
                MonsterLastMovePatch.AbstractMonsterSnapshotFields.snapshot.get(m).storePowers(m, turnCounter);
                for (AbstractPower p : m.powers) {
                    if (p instanceof AbstractCountdownPower){
                        ((AbstractCountdownPower) p).storeCountdown(turnCounter);
                    }
                }
            }
            for (AbstractPower p : AbstractDungeon.player.powers) {
                if (p instanceof AbstractCountdownPower){
                    ((AbstractCountdownPower) p).storeCountdown(turnCounter);
                }
            }
        }
    }
}
