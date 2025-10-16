package friskmod.helper;

import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;

import java.lang.reflect.*;
import java.util.*;


public AbstractMonsterSnapshotHistory {
    public class AbstractMonsterSnapshot {
        private class Snapshot {
            public final Map<Field, Object> snapshotFields = new HashMap<>();
            public EnemyMoveInfo lastMoveInfo;
            public String moveName;
        }

        private final Snapshot snapshot = new Snapshot();

        /**
         * Stores the values of all non-inherited, non-final, non-static fields of the given object.
         */
        public void store(AbstractMonster m, EnemyMoveInfo lastMove, String moveName) {
            if (m == null) return;

            Field[] fields = m.getClass().getDeclaredFields();
            snapshot.lastMoveInfo = lastMove;
            snapshot.moveName = moveName;
            for (Field field : fields) {
                int modifiers = field.getModifiers();
                if (Modifier.isFinal(modifiers) || Modifier.isStatic(modifiers)) {
                    continue;
                }
                try {
                    field.setAccessible(true); //force private access
                    snapshot.snapshotFields.put(field, field.get(m)); //ig you're just fucked if you have a list??
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

        /**
         * Restores previously stored field values for the given object.
         * Does nothing if no snapshot exists.
         */
        public void restore(AbstractMonster m) {
            if (m == null) return;
            for (Map.Entry<Field, Object> entry : snapshot.snapshotFields.entrySet()) {
                Field field = entry.getKey();
                Object savedValue = entry.getValue();
                try {
                    field.setAccessible(true);
                    field.set(m, savedValue);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            EnemyMoveInfo currentMove = snapshot.lastMoveInfo;
            byte nextMove = currentMove.nextMove;
            AbstractMonster.Intent intent = currentMove.intent;
            int baseDamage = currentMove.baseDamage;
            int multiplier = currentMove.multiplier;
            boolean isMultiDamage = currentMove.isMultiDamage;
            String moveName = snapshot.moveName;
            m.setMove(moveName, nextMove, intent, baseDamage, multiplier, isMultiDamage);
            m.applyPowers();
            m.createIntent();
            //Maybe reset tint??
        }
    }
}
