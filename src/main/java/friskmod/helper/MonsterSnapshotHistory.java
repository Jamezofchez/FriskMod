package friskmod.helper;

import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;
import com.megacrit.cardcrawl.monsters.exordium.Hexaghost;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.ThoughtBubble;
import friskmod.FriskMod;
import friskmod.patches.RewindPower;
import friskmod.powers.AbstractCountdownPower;
import friskmod.util.Wiz;

import java.lang.reflect.*;
import java.util.*;
import java.util.stream.Collectors;

import static friskmod.FriskMod.makeID;


public class MonsterSnapshotHistory {
    public static final String ID = makeID(MonsterSnapshotHistory.class.getSimpleName());
    private static final UIStrings UI_STRINGS = CardCrawlGame.languagePack.getUIString(ID);
    
    private final List<MonsterSnapshot> snapshots = new LinkedList<>();

    private final HashMap<Integer, ArrayList<AbstractPower>> powersMap = new HashMap<>();


    public void store(AbstractMonster m, EnemyMoveInfo lastMove, String moveName) {
        MonsterSnapshot currentSnapshot = new MonsterSnapshot();
        currentSnapshot.store(m, lastMove, moveName);
        snapshots.add(currentSnapshot);
        
    }
    
    public void restore(AbstractMonster m) {
        if (snapshots.isEmpty() || snapshots.size() == 1 ){
            AbstractDungeon.effectList.add(new ThoughtBubble(AbstractDungeon.player.dialogX, AbstractDungeon.player.dialogY, 3.0F, (UI_STRINGS.TEXT[0]), true));
        } else{
            snapshots.get(snapshots.size()-2).restore(m);
            snapshots.remove(snapshots.size()-1);
            snapshots.remove(snapshots.size()-1);
        }
    }

    public void storePowers(AbstractMonster instance, int turnCounter) {
        powersMap.put(turnCounter, new ArrayList<>());
        for (AbstractPower p : instance.powers){
//            if (p.type == AbstractPower.PowerType.DEBUFF){
            if (p instanceof CloneablePowerInterface) {
                if (p instanceof AbstractCountdownPower){
                    powersMap.get(turnCounter).add(p);
                } else {
                    AbstractPower copy = ((CloneablePowerInterface) p).makeCopy();
                    powersMap.get(turnCounter).add(copy);
                }
            }
        }
    }
    public void restorePowers(AbstractMonster instance, int turnCounter) {
        List<AbstractPower> restoredPowers = powersMap.get(turnCounter);
        if (restoredPowers != null){
            for (AbstractPower restoredP : restoredPowers){
                if (restoredP instanceof AbstractCountdownPower) {
                    Integer possAmount = ((AbstractCountdownPower) restoredP).getCountdown(turnCounter);
                    if (possAmount != null) {
                        ((AbstractCountdownPower) restoredP).amount2 = possAmount;
                        restoredP.updateDescription();
                    }
                } else {
                    AbstractPower possCurrent = instance.getPower(restoredP.ID);
                    int possCurrentAmount = 0;
                    if (possCurrent != null) {
                        possCurrentAmount = possCurrent.amount;
                    }
                    if (restoredP.amount > possCurrentAmount) {
                        int diff = restoredP.amount - possCurrentAmount;
                        Wiz.atb(new ApplyPowerAction(instance, AbstractDungeon.player, restoredP, diff));
                    }
                }
            }
        }
    }

    public class MonsterSnapshot {
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
                    FriskMod.logger.warn("{}: failed to store MonsterSnapshot", FriskMod.modID);
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
                if (savedValue == null){
                    continue;
                }
                try {
                    field.setAccessible(true);
                    field.set(m, savedValue);
                } catch (IllegalAccessException e) {
                    FriskMod.logger.warn("{}: failed to access field to restore MonsterSnapshot", FriskMod.modID);
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
            //hooray for shitty code
            if (m instanceof Hexaghost){
                if (nextMove == 1){
                    int d = AbstractDungeon.player.currentHealth / 12 + 1;
                    ((DamageInfo)m.damage.get(2)).base = d;
                }
            }
            try {
                Field field = m.getClass().getDeclaredField("firstTurn");
                field.setAccessible(true);
                field.setBoolean(m, false);
            } catch (NoSuchFieldException ignored) {
                // Monster doesn't have firstMove — do nothing
            } catch (Exception e) {
                FriskMod.logger.warn("{}: Setting firstTurn failed", FriskMod.modID);
            }
            m.applyPowers();
            m.createIntent();

            //Maybe reset tint??
        }
    }
}
