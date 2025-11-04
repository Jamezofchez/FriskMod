package friskmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;
import friskmod.helper.MonsterSnapshotHistory;

public class MonsterLastMovePatch {
    @SpirePatch2(clz = AbstractMonster.class, method = SpirePatch.CLASS)
    public static class AbstractMonsterSnapshotFields {
        public static SpireField<MonsterSnapshotHistory> snapshot = new SpireField<>(() -> new MonsterSnapshotHistory());
    }
    @SpirePatch2(
            clz= AbstractMonster.class,
            method="setMove",
            paramtypez={String.class, byte.class, AbstractMonster.Intent.class, int.class, int.class, boolean.class}
    )
    //    public void setMove(String moveName, byte nextMove, Intent intent, int baseDamage, int multiplier, boolean isMultiDamage) {


    public static class AbstractMonsterSetMovePatch{
        @SpirePostfixPatch
        public static void Postfix(AbstractMonster __instance, EnemyMoveInfo ___move, String ___moveName){
            AbstractMonsterSnapshotFields.snapshot.get(__instance).store(__instance, ___move, ___moveName);
        }
    }
}
