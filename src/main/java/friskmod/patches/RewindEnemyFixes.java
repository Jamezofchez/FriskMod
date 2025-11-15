package friskmod.patches;

import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ChangeStateAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.actions.common.SpawnMonsterAction;
import com.megacrit.cardcrawl.actions.unique.SummonGremlinAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.city.*;
import basemod.ReflectionHacks;
import com.megacrit.cardcrawl.monsters.exordium.*;

import java.util.HashMap;
import java.util.Map;

public class RewindEnemyFixes {

    private static class theCollectorPatches{
        @SpirePatch(
                clz = TheCollector.class,
                method = "takeTurn"
        )
        public static class theCollectorFirstMovePatch {
            @SpirePrefixPatch
            public static SpireReturn<Void> Prefix(TheCollector __instance, @ByRef int[] ___spawnX, @ByRef HashMap<Integer, AbstractMonster>[] ___enemySlots, @ByRef boolean[] ___initialSpawn, @ByRef int[] ___turnsTaken) {
                byte move = __instance.nextMove;
                if (move != 1) {
                    return SpireReturn.Continue();
                }

                // Count current living TorchHeads
                int living = 0;
                for (AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
                    if (m instanceof TorchHead && !m.isDeadOrEscaped()) {
                        living++;
                    }
                }
                int i = 1;
                while (i < 3 && living < 2) {
                    AbstractMonster m = new TorchHead(___spawnX[0] + -185.0F * i, MathUtils.random(-5.0F, 25.0F));
                    AbstractDungeon.actionManager.addToBottom(new SFXAction("MONSTER_COLLECTOR_SUMMON"));
                    AbstractDungeon.actionManager.addToBottom(new SpawnMonsterAction(m, true));
                    ___enemySlots[0].put(Integer.valueOf(i), m);
                    living++;
                    i++;
                }
                //revive as well
                for (Map.Entry<Integer, AbstractMonster> m : ___enemySlots[0].entrySet()) {
                    if (((AbstractMonster)m.getValue()).isDying) {
                        AbstractMonster newMonster = new TorchHead(___spawnX[0] + -185.0F * ((Integer)m.getKey()).intValue(), MathUtils.random(-5.0F, 25.0F));
                        int key = ((Integer)m.getKey()).intValue();
                        ___enemySlots[0].put(Integer.valueOf(key), newMonster);
                        AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new SpawnMonsterAction(newMonster, true));
                    }
                }
                ___turnsTaken[0]++;
                ___initialSpawn[0] = false;
                AbstractDungeon.actionManager.addToBottom(new RollMoveAction(__instance));
                return SpireReturn.Return();
            }
        }
    }
    private static class GremlinLeaderPatches{
        @SpirePatch(
                clz = GremlinLeader.class,
                method = "takeTurn"
        )
        public static class GremlinLeaderSummonPatch {
            @SpirePrefixPatch
            public static SpireReturn<Void> Prefix(GremlinLeader __instance) {
                byte move = __instance.nextMove;
                if (move != 2) {
                    return SpireReturn.Continue();
                }

                // Count current living Gremlins
                int living = 0;
                /*
                    pool.add("GremlinWarrior");
                    pool.add("GremlinWarrior");
                    pool.add("GremlinThief");
                    pool.add("GremlinThief");
                    pool.add("GremlinFat");
                    pool.add("GremlinFat");
                    pool.add("GremlinTsundere");
                    pool.add("GremlinWizard");
                 */
                for (AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
                    if ((
                    m instanceof GremlinWarrior
                    || m instanceof GremlinThief
                    || m instanceof GremlinFat
                    || m instanceof GremlinTsundere
                    || m instanceof GremlinWizard
                    || (m.id.contains("Gremlin") && !(m instanceof GremlinLeader))
                    )
                    && !m.isDeadOrEscaped()) {
                        living++;
                    }
                }
                AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(__instance, "CALL"));
                if (living < 2) {
                    AbstractDungeon.actionManager.addToBottom(new SummonGremlinAction(__instance.gremlins));
                }
                if (living < 1) {
                    AbstractDungeon.actionManager.addToBottom(new SummonGremlinAction(__instance.gremlins));
                }
                AbstractDungeon.actionManager.addToBottom(new RollMoveAction(__instance));
                return SpireReturn.Return();
            }
        }
    }
    private static class BronzeAutomationPatches{
        @SpirePatch(
                clz = BronzeAutomaton.class,
                method = "takeTurn"
        )
        public static class BronzeAutomatonSummonPatch {
            @SpirePrefixPatch
            public static SpireReturn<Void> Prefix(BronzeAutomaton __instance) {
                byte move = __instance.nextMove;
                if (move != 4) {
                    return SpireReturn.Continue();
                }

                // Count current living Bronze Orbs
                int living = 0;
                for (AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
                    if (m instanceof BronzeOrb && !m.isDeadOrEscaped()) {
                        living++;
                    }
                }
                if (living < 2) {
                    if (MathUtils.randomBoolean()) {
                        AbstractDungeon.actionManager.addToBottom(new SFXAction("AUTOMATON_ORB_SPAWN", MathUtils.random(-0.1F, 0.1F)));
                    } else {
                        AbstractDungeon.actionManager.addToBottom(new SFXAction("MONSTER_AUTOMATON_SUMMON", MathUtils.random(-0.1F, 0.1F)));
                    }

                    AbstractDungeon.actionManager.addToBottom(new SpawnMonsterAction(new BronzeOrb(-300.0F, 200.0F, 0), true));
                }
                if (living < 1) {
                    if (MathUtils.randomBoolean()) {
                        AbstractDungeon.actionManager.addToBottom(new SFXAction("AUTOMATON_ORB_SPAWN", MathUtils.random(-0.1F, 0.1F)));
                    } else {
                        AbstractDungeon.actionManager.addToBottom(new SFXAction("MONSTER_AUTOMATON_SUMMON", MathUtils.random(-0.1F, 0.1F)));
                    }

                    AbstractDungeon.actionManager.addToBottom(new SpawnMonsterAction(new BronzeOrb(200.0F, 130.0F, 1), true));
                }
                AbstractDungeon.actionManager.addToBottom(new RollMoveAction(__instance));
                return SpireReturn.Return();
            }
        }
    }

}
