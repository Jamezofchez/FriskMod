package friskmod.patches;

import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.actions.common.SpawnMonsterAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.city.TheCollector;
import com.megacrit.cardcrawl.monsters.city.TorchHead;
import basemod.ReflectionHacks;

import java.util.HashMap;
import java.util.Map;

public class RewindEnemyFixes {

    private static class theCollector{
        @SpirePatch(
                clz = TheCollector.class,
                method = "takeTurn"
        )
        public static class CollectorFirstMovePatch {
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

}
