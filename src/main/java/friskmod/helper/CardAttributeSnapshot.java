package friskmod.helper;

import basemod.ReflectionHacks;
import basemod.helpers.CardModifierManager;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;
import com.megacrit.cardcrawl.vfx.ThoughtBubble;
import friskmod.FriskMod;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

public class CardAttributeSnapshot {
    public static final String[] BLACKLISTED_FIELDS_VALUES = new String[] {
            "purgeOnUse",
            "exhaust",
            "exhaustOnUseOnce",
            "exhaustOnUseOnceForTurn",
            "isEthereal",
            "angle",
            "targetAngle",
            "current_x",
            "current_y",
            "drawScale",
            "targetDrawScale",
            "target_x",
            "target_y",

    };
    public static final Set<String> BLACKLISTED_FIELDS = new HashSet<>(Arrays.asList(BLACKLISTED_FIELDS_VALUES));

    private final CardSnapshot snapshot = new CardSnapshot();

    public void store(AbstractCard storeCard) {
        snapshot.store(storeCard);
    }

    public void restore(AbstractCard restoreCard) {
        snapshot.restore(restoreCard);
        AbstractCard storeCard = snapshot.lastCard;
        if (storeCard != null){
            CardModifierManager.removeAllModifiers(restoreCard, true);
            CardModifierManager.copyModifiers(storeCard, restoreCard, true, false, false);
            storeCard.glowColor = Color.NAVY.cpy();
            storeCard.isGlowing = true;
            storeCard.isEthereal = true;
            storeCard.exhaust = true;
            storeCard.purgeOnUse = true;
        }
    }

    public class CardSnapshot {
        public final Map<Field, Object> snapshotFields = new HashMap<>();
        public AbstractCard lastCard;

        /**
         * Stores the values of all non-final, non-static fields of the given object.
         */
        public void store(AbstractCard storeCard) {
            lastCard = storeCard;
            if (storeCard == null) return;

            Field[] fields = storeCard.getClass().getFields();
            for (Field field : fields) {
                int modifiers = field.getModifiers();
                if (Modifier.isFinal(modifiers) || Modifier.isStatic(modifiers)) {
                    continue;
                }
                if (blacklistField(field)){
                    continue;
                }
                try {
                    field.setAccessible(true); //force private access
                    this.snapshotFields.put(field, field.get(storeCard)); //ig you're just fucked if you have a list??
                } catch (IllegalAccessException e) {
                    FriskMod.logger.warn("{}: failed to store CardSnapshot", FriskMod.modID);
                }
            }
        }

        private boolean blacklistField(Field field) {
            return BLACKLISTED_FIELDS.contains(field.getName());
        }

        /**
         * Restores previously stored field values for the given object.
         * Does nothing if no snapshot exists.
         */
        public void restore(AbstractCard restoreCard) {
            if (restoreCard == null) return;
            for (Map.Entry<Field, Object> entry : this.snapshotFields.entrySet()) {
                Field field = entry.getKey();
                Object savedValue = entry.getValue();
                try {
                    field.setAccessible(true);
                    field.set(restoreCard, savedValue);
                } catch (Exception e) {
                    FriskMod.logger.warn("{}: failed to restore CardSnapshot", FriskMod.modID);
                }
            }
        }
    }
}
