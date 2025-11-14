package friskmod.helper;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import basemod.interfaces.CloneablePowerInterface;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BooleanSupplier;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;

import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.vfx.ThoughtBubble;
import friskmod.FriskMod;
import friskmod.patches.InherentPowerTagFields;
import friskmod.powers.Determination;
import friskmod.powers.LVRitual;
import friskmod.powers.LV_Enemy;
import friskmod.powers.LV_Hero;
import friskmod.util.Wiz;

import static friskmod.FriskMod.makeID;
import static friskmod.helper.SharedFunctions.isInvincible;

public class StealableWhitelist {

    public class StringDictionary {
        private Map<String, ArrayList<String>> dictionary;

        public StringDictionary() {
            dictionary = new HashMap<>();
        }

        public void addValue(String key, String value) {
            dictionary.putIfAbsent(key, new ArrayList<>());
            dictionary.get(key).add(value);
        }

        public boolean containsValue(String key, String testValue) {
            if (!dictionary.containsKey(key)) {
                return false;
            }
            return dictionary.get(key).contains(testValue);
        }
    }

    private class StealSettings {
        public boolean steal;
        public Predicate<AbstractPower> globalPostProcess;
        public StealSettings(boolean steal, Predicate<AbstractPower> globalPostProcess){
            this.steal = steal;
            this.globalPostProcess = globalPostProcess;
        }
    }
    public static final String ID = makeID(StealableWhitelist.class.getSimpleName());
    private static final UIStrings UI_STRINGS = CardCrawlGame.languagePack.getUIString(ID);

    public static StealableWhitelist INSTANCE = null;

    public StringDictionary synonymDict = new StringDictionary();

    public static StealableWhitelist getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new StealableWhitelist();
            INSTANCE.initializeWhitelist();
        }
        return INSTANCE;
    }

    // Maps power ID to a function that gets an appropriate clone of that power
    private final Map<String, BiConsumer<AbstractPower, StealSettings>> whiteList = new HashMap<>();

    public void attachClonePowerToPlayer(AbstractPower pow, boolean steal, Predicate<AbstractPower> globalPostProcess){
        BiConsumer<AbstractPower, StealSettings> consumer = whiteList.get(pow.ID);
        StealSettings settings = new StealSettings(steal, globalPostProcess);
        if (consumer != null) {
            consumer.accept(pow, settings);
        } else {
            FriskMod.logger.warn("{}: attempted to clone unregistered power {}", FriskMod.modID, pow.ID);
        }
    }

//    private static void ritualPostProcess(AbstractPower pow) {
//        ReflectionHacks.setPrivate(pow, RitualPower.class, "onPlayer", true);
//        pow.amount = 1;
//    }
    private static void intangiblePostProcess(AbstractPower pow) {
        pow.amount += 1;
    }
    private static int inherentPostProcess(boolean steal, AbstractPower enemyPower) {
        if (steal && InherentPowerTagFields.inherentPowerFields.inherentPower.get(enemyPower)){
            int inherentPowerAmount = InherentPowerTagFields.inherentPowerFields.inherentPowerAmount.get(enemyPower);
            if (inherentPowerAmount >= enemyPower.amount){
                FriskMod.logger.warn("{}: Somehow we are trying to steal an inherent power", FriskMod.modID);
                return 0;
            } else{
                return inherentPowerAmount;
            }
        }
        return 0;
    }


    private static boolean strengthPreProcess() {
        if (AbstractDungeon.getMonsters().monsters.stream().anyMatch(m -> isInvincible(m) && m.hasPower(StrengthPower.POWER_ID) && m.getPower(StrengthPower.POWER_ID).amount > 10)) {
            AbstractDungeon.effectList.add(new ThoughtBubble(AbstractDungeon.player.dialogX, AbstractDungeon.player.dialogY, 3.0F, UI_STRINGS.TEXT[1], true));
            return false;
        }
        return true;
    }

    private boolean inherentPreProcess(AbstractPower enemyPower, boolean tried, Predicate<AbstractPower> globalPostProcess, boolean steal) {
        if (globalPostProcess != null){
            if (!globalPostProcess.test(enemyPower)) {
//                if (tried) {
//                    AbstractDungeon.effectList.add(new ThoughtBubble(AbstractDungeon.player.dialogX, AbstractDungeon.player.dialogY, 3.0F, String.format(UI_STRINGS.TEXT[3], enemyPower.name), true));
//                }
                return false;
            }
        }
        if (enemyPower instanceof CurlUpPower){
            return true;
        }
//        if (enemyPower instanceof RegenerateMonsterPower){
//            return true;
//        }
        if (steal && InherentPowerTagFields.inherentPowerFields.inherentPower.get(enemyPower) && InherentPowerTagFields.inherentPowerFields.inherentPowerAmount.get(enemyPower) >= enemyPower.amount){
            if (tried){
                AbstractDungeon.effectList.add(new ThoughtBubble(AbstractDungeon.player.dialogX, AbstractDungeon.player.dialogY, 3.0F, String.format(UI_STRINGS.TEXT[2], enemyPower.name), true));
            }
            return false;
        }
        return true;
    }
    public boolean checkPreProcess(AbstractPower enemyPower, boolean steal){
        if (enemyPower instanceof StrengthPower){
            if (!strengthPreProcess()){
                return false;
            }
        }
        return inherentPreProcess(enemyPower, false, (pow) -> true, steal);
    }

    private void initializeWhitelist() {
        //Only steal powers that are in a sense "non-permanent?"
        //Normal enemy and player buffs
        //YAGNI... sigh

        (new applyPowerBuilder(StrengthPower.POWER_ID)).addPreProcess(StealableWhitelist::strengthPreProcess).addSynonym(LV_Hero.class, LV_Hero.POWER_ID).build();
        (new applyPowerBuilder(DexterityPower.POWER_ID)).build();
        (new applyPowerBuilder(ArtifactPower.POWER_ID)).build();
        (new applyPowerBuilder(PlatedArmorPower.POWER_ID)).build();
        (new applyPowerBuilder(BlurPower.POWER_ID)).build();
        (new applyPowerBuilder(BufferPower.POWER_ID)).build();
        (new applyPowerBuilder(ThornsPower.POWER_ID)).build();

        (new applyPowerBuilder(Determination.POWER_ID)).build();
        (new applyPowerBuilder(EvolvePower.POWER_ID)).build();


        //Modified enemy powers
        (new applyPowerBuilder(CurlUpPower.POWER_ID)).build();
        (new applyPowerBuilder(FlightPower.POWER_ID)).build(); //hopefully only on regain?
        //Other enemy powers
        (new applyPowerBuilder(RitualPower.POWER_ID)).addSynonym(LVRitual.class, LVRitual.POWER_ID).build();

        //Synonym powers
        (new applyPowerBuilder(SharpHidePower.POWER_ID)).addSynonym(ThornsPower.class, ThornsPower.POWER_ID).build();
        (new applyPowerBuilder(IntangiblePower.POWER_ID)).addPostProcess(StealableWhitelist::intangiblePostProcess).addSynonym(IntangiblePlayerPower.class, IntangiblePlayerPower.POWER_ID).build();
        (new applyPowerBuilder(LV_Enemy.POWER_ID)).addSynonym(LV_Hero.class, LV_Hero.POWER_ID).build();
        (new applyPowerBuilder(RegenerateMonsterPower.POWER_ID)).addSynonym(RegenPower.class, RegenPower.POWER_ID).build();
    }

    public Set<String> getWhitelist() {
        return whiteList.keySet();
    }

    private class applyPowerBuilder{
        private BooleanSupplier preProcess = null;
        private Consumer<AbstractPower> postProcess = null;
        private final String enemyPowerID;
        private Class<? extends AbstractPower> playerSynonym = null;
        applyPowerBuilder(String enemyPowerID){
            this.enemyPowerID = enemyPowerID;
        }
        applyPowerBuilder addPreProcess(BooleanSupplier preProcess){
            this.preProcess = preProcess;
            return this;
        }
        applyPowerBuilder addPostProcess(Consumer<AbstractPower> postProcess){
            this.postProcess = postProcess;
            return this;
        }
        applyPowerBuilder addSynonym(Class<? extends AbstractPower> playerSynonym, String playerSynonymID){
            this.playerSynonym = playerSynonym;
            getInstance().synonymDict.addValue(this.enemyPowerID, playerSynonymID);
            return this;
        }
        public void build(){
            if (playerSynonym == null){
                whiteList.put(enemyPowerID,applyCloneFactory());
            } else{
                whiteList.put(enemyPowerID,applySynonymCloneFactory());
            }
        }
        private BiConsumer<AbstractPower, StealSettings> applySynonymCloneFactory() {
            return (enemyPower, settings) -> {
                // Allow caller to check if valid
                if (!preProcess(enemyPower, settings.steal, settings.globalPostProcess)) return;
                int amount = enemyPower.amount;
                try {
                    Constructor<? extends AbstractPower> ctor = playerSynonym.getConstructor(AbstractCreature.class, int.class);
                    AbstractPower copy = ctor.newInstance(AbstractDungeon.player, amount);
                    // Allow caller to customize before applying
                    postProcess(enemyPower, settings.steal, copy);

                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException |
                         InstantiationException e) {

                    FriskMod.logger.warn("{}: synonym cloning failed", FriskMod.modID);
                    AbstractDungeon.effectList.add(new ThoughtBubble(AbstractDungeon.player.dialogX, AbstractDungeon.player.dialogY, 3.0F, String.format(UI_STRINGS.TEXT[0], enemyPower.name), true));
                }
            };
        }

        private BiConsumer<AbstractPower, StealSettings> applyCloneFactory() {
            return (enemyPower, settings) -> {
                // Allow caller to check if valid
                if (!preProcess(enemyPower, settings.steal, settings.globalPostProcess)) return;
                if (enemyPower instanceof CloneablePowerInterface) {
                    AbstractPower copy = ((CloneablePowerInterface) enemyPower).makeCopy();
                    // Allow caller to customize before applying
                    postProcess(enemyPower, settings.steal, copy);
                } else {
                    FriskMod.logger.warn("{}: normal cloning failed", FriskMod.modID);
                    AbstractDungeon.effectList.add(new ThoughtBubble(AbstractDungeon.player.dialogX, AbstractDungeon.player.dialogY, 3.0F, String.format(UI_STRINGS.TEXT[0], enemyPower.name), true));
                }
            };
        }

        private boolean preProcess(AbstractPower enemyPower, Boolean steal, Predicate<AbstractPower> globalPostProcess) {
            if (preProcess != null){
                if (!preProcess.getAsBoolean()) {
                    return false;
                }
            }
            if (!inherentPreProcess(enemyPower, true, globalPostProcess, steal)){
                return false;
            }
            return true;
        }

        private void postProcess(AbstractPower enemyPower, Boolean steal, AbstractPower copy) {
            if (postProcess != null) {
                postProcess.accept(copy);
            }
            int inherentPowerAmount = inherentPostProcess(steal, enemyPower);
            copy.owner = AbstractDungeon.player;
            if (inherentPowerAmount == 0) {
                Wiz.att(new ApplyPowerAction(copy.owner, enemyPower.owner, copy, copy.amount));
                if (steal) {
                    AbstractCreature enemy = enemyPower.owner;
                    Wiz.att(new RemoveSpecificPowerAction(enemy, copy.owner, enemyPower));
                }
            } else{
                copy.amount = copy.amount - inherentPowerAmount;
                Wiz.att(new ApplyPowerAction(copy.owner, enemyPower.owner, copy, copy.amount));
                if (steal) {
                    AbstractCreature enemy = enemyPower.owner;
                    Wiz.att(new ReducePowerAction(enemy, copy.owner, enemyPower, inherentPowerAmount));
                }
            }
            Wiz.att(Wiz.actionify(copy::updateDescription)); //incase desc depends on owner
        }
    }

//    private static void initializeNormal(String powerID, BiConsumer<AbstractPower, StealSettings> postProcess) {
//        BiConsumer<AbstractPower, StealSettings> applyCopy = applyCloneFactory(postProcess);
//        whiteList.put(powerID, applyCopy);
//    }
//    private static void initializeNormal(String powerID) {
//        initializeNormal(powerID,null);
//    }
//
//    private static void initializeSynonym(String enemyPowerID, Class<? extends AbstractPower> playerPowerClass) {
//        BiConsumer<AbstractPower, StealSettings> applyCopy = applySynonymCloneFactory(playerPowerClass);
//        whiteList.put(enemyPowerID, applyCopy);
//    }
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