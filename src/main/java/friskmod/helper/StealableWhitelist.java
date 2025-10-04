package friskmod.helper;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import basemod.interfaces.CloneablePowerInterface;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import com.megacrit.cardcrawl.powers.*;
import friskmod.FriskMod;
import friskmod.powers.LV_Enemy;
import friskmod.powers.LV_Hero;
import friskmod.util.Wiz;
import basemod.ReflectionHacks;

public class StealableWhitelist {
    // Maps power ID to a function that gets an appropriate clone of that power
    public static final Map<String, Consumer<AbstractPower>> whiteList = new HashMap<>();

    public static class PowerSynonymsManager {

        // Inner class to hold the data
        public static class PowerSynonym {
            public final String playerID;
            public final Class<? extends AbstractPower> playerClass;

            public PowerSynonym(String playerID, Class<? extends AbstractPower> playerClass) {
                this.playerID = playerID;
                this.playerClass = playerClass;
            }
        }

        public final Map<String, PowerSynonym> synonymMap = new HashMap<>();

        private void addSynonym(String enemyID, String playerID, Class<? extends AbstractPower> playerClass) {
            synonymMap.put(enemyID, new PowerSynonym(playerID, playerClass));
        }
        public String lookupPlayerID(String enemyID) {
            PowerSynonym syn = synonymMap.get(enemyID);
            if (syn == null) return null;
            return syn.playerID;
        }
        public Class<? extends AbstractPower> lookupPlayerClass(String enemyID) {
            PowerSynonym syn = synonymMap.get(enemyID);
            if (syn == null) return null;
            return syn.playerClass;
        }

        private PowerSynonymsManager() {
            addSynonym(SharpHidePower.POWER_ID, ThornsPower.POWER_ID, ThornsPower.class);
            addSynonym(IntangiblePower.POWER_ID, IntangiblePlayerPower.POWER_ID, IntangiblePlayerPower.class);
            addSynonym(LV_Enemy.POWER_ID, LV_Hero.POWER_ID, LV_Hero.class);
            addSynonym(RegenerateMonsterPower.POWER_ID, RegenPower.POWER_ID, RegenPower.class); //permanent but non-permanent for player
        }
    }
    public static final PowerSynonymsManager powerSynonyms = new PowerSynonymsManager();

    public static void attachClonePowerToPlayer(AbstractPower pow){
        Consumer<AbstractPower> consumer = whiteList.get(pow.ID);
        if (consumer != null) {
            consumer.accept(pow);
        } else {
            FriskMod.logger.warn("{}: attempted to clone unregistered power {}", FriskMod.modID, pow.ID);
        }
    }

    public static void initialize() {
        initializeNormals();
        initializeSynonyms();
        initializeOddballs();
    }
    private static void initializeOddballs() {
    }
    private static void ritualPostProcess(AbstractPower pow) {
        ReflectionHacks.setPrivate(pow, RitualPower.class, "isPlayer", true);
        pow.amount = 1;
    }
    private static void initializeSynonyms() {
        for (Map.Entry<String, PowerSynonymsManager.PowerSynonym> entry : powerSynonyms.synonymMap.entrySet()) {
            String key = entry.getKey();
            PowerSynonymsManager.PowerSynonym value = entry.getValue();
            initializeSynonym(key, value.playerClass);
        }
    }

    private static void initializeNormals() {
        //Only steal powers that are in a sense "non-permanent?"
        initializeNormal(StrengthPower.POWER_ID);
        initializeNormal(DexterityPower.POWER_ID);
        initializeNormal(ArtifactPower.POWER_ID);
        initializeNormal(PlatedArmorPower.POWER_ID);
        initializeNormal(BlurPower.POWER_ID);
        initializeNormal(BufferPower.POWER_ID);
        initializeNormal(RegenPower.POWER_ID);

//      initializeNormal(FlightPower.POWER_ID);
        initializeNormal(CurlUpPower.POWER_ID);
//      initializeNormal(AngryPower.POWER_ID);
//      initializeNormalInherent(AngerPower.POWER_ID);
//      initializeNormal(ThornsPower.POWER_ID);
//      initializeNormal(MetallicizePower.POWER_ID);

        initializeNormal(RitualPower.POWER_ID, StealableWhitelist::ritualPostProcess);

    }

    private static void initializeNormal(String powerID, Consumer<AbstractPower> postProcess) {
        Consumer<AbstractPower> applyCopy = applyCloneFactory(postProcess);
        whiteList.put(powerID, applyCopy);
    }
    private static void initializeNormal(String powerID) {
        initializeNormal(powerID,null);
    }

    private static void initializeSynonym(String enemyPowerID, Class<? extends AbstractPower> playerPowerClass) {
        Consumer<AbstractPower> applyCopy = applySynonymCloneFactory(playerPowerClass);
        whiteList.put(enemyPowerID, applyCopy);
    }

    private static Consumer<AbstractPower> applySynonymCloneFactory(Class<? extends AbstractPower> playerPowerClass) {
        return (enemyPower) -> {
            int amount = enemyPower.amount;
            try {
                Constructor<? extends AbstractPower> ctor = playerPowerClass.getConstructor(AbstractCreature.class, int.class);
                AbstractPower copy = ctor.newInstance(AbstractDungeon.player, amount);
                Wiz.att(new ApplyPowerAction(copy.owner, enemyPower.owner, copy, copy.amount));
                Wiz.att(Wiz.actionify(copy::updateDescription));
                AbstractCreature enemy = enemyPower.owner;
                Wiz.att(new RemoveSpecificPowerAction(enemy,copy.owner, enemyPower));
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException |
                     InstantiationException e) {
                FriskMod.logger.warn("{}: synonym cloning failed", FriskMod.modID);
            }
        };
    }

    private static Consumer<AbstractPower> applyCloneFactory(Consumer<AbstractPower> postProcess) {
        return (enemyPower) -> {
            if (enemyPower instanceof CloneablePowerInterface) {
                AbstractPower copy = ((CloneablePowerInterface) enemyPower).makeCopy();
                // Allow caller to customize before applying
                if (postProcess != null) {
                    postProcess.accept(copy);
                }
                copy.owner = AbstractDungeon.player;
                Wiz.att(new ApplyPowerAction(copy.owner, enemyPower.owner, copy, copy.amount));
                Wiz.att(Wiz.actionify(copy::updateDescription));
                AbstractCreature enemy = enemyPower.owner;
                Wiz.att(new RemoveSpecificPowerAction(enemy, copy.owner, enemyPower));
            } else {
                FriskMod.logger.warn("{}: normal cloning failed", FriskMod.modID);
            }
        };
    }
//    private static void applyCloneKeep(AbstractPower enemyPower) {
//        if (enemyPower instanceof CloneablePowerInterface) {
//            CloneablePowerInterface cloneable = (CloneablePowerInterface) enemyPower;
//            AbstractPower copy = cloneable.makeCopy();
//            copy.owner = AbstractDungeon.player;
//            Wiz.att(new ApplyPowerAction(copy.owner, copy.owner, copy, copy.amount));
//        } else {
//            //FriskMod.logger.warn("{}: normal cloning failed", FriskMod.modID);
//        }
//    }
}