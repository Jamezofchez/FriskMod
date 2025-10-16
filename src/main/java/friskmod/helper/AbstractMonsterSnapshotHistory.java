package friskmod.helper;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;
import com.megacrit.cardcrawl.vfx.ThoughtBubble;
import com.megacrit.cardcrawl.vfx.TintEffect;

import java.lang.reflect.*;
import java.util.*;

import static friskmod.FriskMod.makeID;


public class AbstractMonsterSnapshotHistory {
    public static final String ID = makeID(AbstractMonsterSnapshotHistory.class.getSimpleName());
    private static final UIStrings UI_STRINGS = CardCrawlGame.languagePack.getUIString(ID);
    
    private final List<AbstractMonsterSnapshot> snapshots = new LinkedList<>();

    public void store(AbstractMonster m, EnemyMoveInfo lastMove, String moveName) {
        AbstractMonsterSnapshot currentSnapshot = new AbstractMonsterSnapshot();
        currentSnapshot.store(m, lastMove, moveName);
        snapshots.add(currentSnapshot);
        
    }
    
    public void restore(AbstractMonster m) {
        if (snapshots.isEmpty()){
            AbstractDungeon.effectList.add(new ThoughtBubble(AbstractDungeon.player.dialogX, AbstractDungeon.player.dialogY, 3.0F, (UI_STRINGS.TEXT[0]), true));
        } else{
            snapshots.get(snapshots.size()-1).restore(m);
            snapshots.remove(snapshots.size()-1);
        }
    }

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
