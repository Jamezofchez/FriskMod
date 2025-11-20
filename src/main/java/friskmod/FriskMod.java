package friskmod;

import basemod.AutoAdd;
import basemod.BaseMod;
import basemod.abstracts.CustomSavable;
import basemod.abstracts.DynamicVariable;
import basemod.helpers.CardBorderGlowManager;
import basemod.interfaces.*;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import friskmod.actions.AfterCardUseAction;
import friskmod.actions.OnBattleStartAction;
import friskmod.cards.*;
import friskmod.cards.cardvars.AbstractEasyDynamicVariable;
import friskmod.character.Frisk;
import friskmod.helper.SharedFunctions;
import friskmod.helper.StealableWhitelist;
import friskmod.patches.GhostlyPatch;
import friskmod.patches.perseverance.PerseveranceFields;
import friskmod.patches.perseverance.PerseverancePatch;
import friskmod.potions.AbstractEasyPotion;
import friskmod.potions.MercyPotion;
import friskmod.powers.NonAttackPower;
import friskmod.relics.BaseRelic;
import friskmod.util.*;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglFileHandle;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.evacipated.cardcrawl.modthespire.ModInfo;
import com.evacipated.cardcrawl.modthespire.Patcher;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.localization.*;
import friskmod.util.interfaces.AfterCardPlayedInterface;
import org.scannotation.AnnotationDB;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.util.*;

@SpireInitializer
public class FriskMod implements
        StartGameSubscriber,
        EditCharactersSubscriber,
        EditCardsSubscriber,
        EditRelicsSubscriber,
        EditStringsSubscriber,
        EditKeywordsSubscriber,
        AddAudioSubscriber,
        PostInitializeSubscriber,
        OnCardUseSubscriber,
        OnStartBattleSubscriber,
        PostBattleSubscriber{
    public static ModInfo info;
    public static String modID; //Edit your pom.xml to change this
    static { loadModInfo(); }
    private static final String resourcesFolder = checkResourcesPath();
    public static final Logger logger = LogManager.getLogger(modID); //Used to output to the console.

    //This is used to prefix the IDs of various objects like cards and relics,
    //to avoid conflicts between different mods using the same name for things.
    public static String makeID(String id) {
        return modID + ":" + id;
    }

    //This will be called by ModTheSpire because of the @SpireInitializer annotation at the top of the class.
    public static void initialize() {
        new FriskMod();
        Frisk.Meta.registerColor();
    }

    public FriskMod() {
        BaseMod.subscribe(this); //This will make BaseMod trigger all the subscribers at their appropriate times.
        logger.info("{} subscribed to BaseMod.", modID);
    }

    @Override
    public void receivePostInitialize() {
        //This loads the image used as an icon in the in-game mods menu.
        Texture badgeTexture = TextureLoader.getTexture(imagePath("badge.png"));
        //Set up the mod information displayed in the in-game mods menu.
        //The information used is taken from your pom.xml file.

        //If you want to set up a config panel, that will be done here.
        //You can find information about this on the BaseMod wiki page "Mod Config and Panel".
        BaseMod.registerModBadge(badgeTexture, info.Name, GeneralUtils.arrToString(info.Authors), info.Description, null);
//        CustomIntent.add((CustomIntent)new MassAttackIntent());
        PerseverancePatch.testPlayability();

        final Color perseveranceGlow = CardHelper.getColor(88.0f, 26.0f, 150.0f);
        CardBorderGlowManager.addGlowInfo(new CardBorderGlowManager.GlowInfo() {
            @Override
            public boolean test(AbstractCard card) {
                return PerseveranceFields.overcomePlayed.get(card);
            }

            @Override
            public Color getColor(AbstractCard card) {
                return perseveranceGlow.cpy();
            }

            @Override
            public String glowID() {
                return makeID("PerseveranceGlow");
            }
        });
        StealableWhitelist.getInstance();
        initializeSavedData();

    }

    /*----------Localization----------*/

    //This is used to load the appropriate localization files based on language.
    private static String getLangString()
    {
        return Settings.language.name().toLowerCase();
    }
    private static final String defaultLanguage = "eng";

    public static final Map<String, KeywordInfo> keywords = new HashMap<>();

    @Override
    public void receiveEditStrings() {
        /*
            First, load the default localization.
            Then, if the current language is different, attempt to load localization for that language.
            This results in the default localization being used for anything that might be missing.
            The same process is used to load keywords slightly below.
        */
        loadLocalization(defaultLanguage); //no exception catching for default localization; you better have at least one that works.
        if (!defaultLanguage.equals(getLangString())) {
            try {
                loadLocalization(getLangString());
            }
            catch (GdxRuntimeException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadLocalization(String lang) {
        //While this does load every type of localization, most of these files are just outlines so that you can see how they're formatted.
        //Feel free to comment out/delete any that you don't end up using.
        BaseMod.loadCustomStringsFile(CardStrings.class,
                localizationPath(lang, "CardStrings.json"));
        BaseMod.loadCustomStringsFile(CharacterStrings.class,
                localizationPath(lang, "CharacterStrings.json"));
        BaseMod.loadCustomStringsFile(EventStrings.class,
                localizationPath(lang, "EventStrings.json"));
        BaseMod.loadCustomStringsFile(MonsterStrings.class,
                localizationPath(lang, "MonsterStrings.json"));
        BaseMod.loadCustomStringsFile(OrbStrings.class,
                localizationPath(lang, "OrbStrings.json"));
        BaseMod.loadCustomStringsFile(PotionStrings.class,
                localizationPath(lang, "PotionStrings.json"));
        BaseMod.loadCustomStringsFile(PowerStrings.class,
                localizationPath(lang, "PowerStrings.json"));
        BaseMod.loadCustomStringsFile(RelicStrings.class,
                localizationPath(lang, "RelicStrings.json"));
        BaseMod.loadCustomStringsFile(UIStrings.class,
                localizationPath(lang, "UIStrings.json"));
    }

    @Override
    public void receiveEditKeywords()
    {
        Gson gson = new Gson();
        String json = Gdx.files.internal(localizationPath(defaultLanguage, "Keywords.json")).readString(String.valueOf(StandardCharsets.UTF_8));
        KeywordInfo[] keywords = gson.fromJson(json, KeywordInfo[].class);
        for (KeywordInfo keyword : keywords) {
            keyword.prep();
            registerKeyword(keyword);
        }

        if (!defaultLanguage.equals(getLangString())) {
            try
            {
                json = Gdx.files.internal(localizationPath(getLangString(), "Keywords.json")).readString(String.valueOf(StandardCharsets.UTF_8));
                keywords = gson.fromJson(json, KeywordInfo[].class);
                for (KeywordInfo keyword : keywords) {
                    keyword.prep();
                    registerKeyword(keyword);
                }
            }
            catch (Exception e)
            {
                logger.warn("{} does not support {} keywords.", modID, getLangString());
            }
        }
    }

    private void registerKeyword(KeywordInfo info) {
        BaseMod.addKeyword(modID.toLowerCase(), info.PROPER_NAME, info.NAMES, info.DESCRIPTION, info.COLOR);
        if (!info.ID.isEmpty())
        {
            keywords.put(info.ID, info);
        }
    }

    private void addAudio(String key){
        String path = audioPath(key + ".wav");
        BaseMod.addAudio(key, path);
    }
    private void addAudioOgg(String key){
        String path = audioPath(key + ".ogg");
        BaseMod.addAudio(key, path);
    }

    @Override
    public void receiveAddAudio() {
        //loadAudio(Sounds.class);
        //BaseMod.addAudio("STRIKE", makeAudioPath("SFX_Strike.wav"));
        addAudio("ding");
        addAudio("deltarune_explosion");
        addAudio("mus_create");
        addAudio("mus_drumcuica");
        addAudio("mus_drumcuica2");
        addAudio("mus_explosion");
        addAudio("mus_f_glock");
        addAudio("mus_f_noise");
        addAudio("mus_sfx_abreak");
        addAudio("mus_sfx_a_lithit2");
        addAudio("mus_sfx_a_target");
        addAudio("mus_sfx_bookspin");
        addAudio("mus_sfx_eyeflash");
        addAudio("mus_sfx_frypan");
        addAudio("mus_sfx_gigapunch");
        addAudio("mus_sfx_gunshot");
        addAudio("mus_sfx_rainbowbeam_1");
        addAudio("mus_sfx_segapower");
        addAudio("mus_sfx_segapower2");
        addAudio("mus_sfx_spellcast");
        addAudio("mus_sfx_star");
        addAudio("mus_sfx_voice_triple");
        addAudio("snd_arrow");
        addAudio("snd_b");
        addAudio("snd_bell");
        addAudio("snd_bombsplosion");
        addAudio("snd_break1");
        addAudio("snd_break1_c");
        addAudio("snd_buyitem");
        addAudio("snd_catsalad");
        addAudio("snd_damage");
        addAudio("snd_dumbvictory");
        addAudio("snd_escaped");
        addAudio("snd_grab");
        addAudio("snd_heal_c");
        addAudio("snd_heartshot");
        addAudio("snd_hurt1");
        addAudio("snd_impact");
        addAudio("snd_item");
        addAudio("snd_laz");
        addAudio("snd_levelup");
        addAudio("snd_mysterygo");
        addAudio("snd_phone");
        addAudio("snd_power");
        addAudio("snd_punchstrong");
        addAudio("snd_saber3");
        addAudio("snd_save");
        addAudio("snd_select");
        addAudio("snd_shock");
        addAudio("snd_spearrise");
        addAudio("snd_tearcard");
        addAudio("snd_vaporized");
        addAudio("snd_wrongvictory");
        addAudio("spamton");
        addAudio("spamton2");
        addAudio("test");
    }

    private static final String[] AUDIO_EXTENSIONS = { ".ogg", ".wav", ".mp3" }; //There are more valid types, but not really worth checking them all here
    private void loadAudio(Class<?> cls) {
        try {
            Field[] fields = cls.getDeclaredFields();
            outer:
            for (Field f : fields) {
                int modifiers = f.getModifiers();
                if (Modifier.isStatic(modifiers) && Modifier.isPublic(modifiers) && f.getType().equals(String.class)) {
                    String s = (String) f.get(null);
                    if (s == null) { //If no defined value, determine path using field name
                        s = audioPath(f.getName());

                        for (String ext : AUDIO_EXTENSIONS) {
                            String testPath = s + ext;
                            if (Gdx.files.internal(testPath).exists()) {
                                s = testPath;
                                BaseMod.addAudio(s, s);
                                f.set(null, s);
                                continue outer;
                            }
                        }
                        throw new Exception("Failed to find an audio file \"" + f.getName() + "\" in " + resourcesFolder + "/audio; check to ensure the capitalization and filename are correct.");
                    }
                    else { //Otherwise, load defined path
                        if (Gdx.files.internal(s).exists()) {
                            BaseMod.addAudio(s, s);
                        }
                        else {
                            throw new Exception("Failed to find audio file \"" + s + "\"; check to ensure this is the correct filepath.");
                        }
                    }
                }
            }
        }
        catch (Exception e) {
            logger.error("Exception occurred in loadAudio: ", e);
        }
    }

    //These methods are used to generate the correct filepaths to various parts of the resources folder.
    public static String localizationPath(String lang, String file) {
        return resourcesFolder + "/localization/" + lang + "/" + file;
    }

    public static String audioPath(String file) {
        return resourcesFolder + "/audio/" + file;
    }
    public static String musicPath(String file) {
        return resourcesFolder + "/audio/music/" + file;
    }
    public static String imagePath(String file) {
        return resourcesFolder + "/images/" + file;
    }
    public static String characterPath(String file) {
        return resourcesFolder + "/images/character/" + file;
    }
    public static String powerPath(String file) {
        return resourcesFolder + "/images/powers/" + file;
    }
    public static String relicPath(String file) {
        return resourcesFolder + "/images/relics/" + file;
    }
    public static String monsterPath(String file) {
        return resourcesFolder + "/images/monsters/" + file;
    }
    public static String UIPath(String file) {
        return resourcesFolder + "/images/monsters/" + file;
    }

    /**
     * Checks the expected resources path based on the package name.
     */
    private static String checkResourcesPath() {
        String name = FriskMod.class.getName(); //getPackage can be iffy with patching, so class name is used instead.
        int separator = name.indexOf('.');
        if (separator > 0)
            name = name.substring(0, separator);

        FileHandle resources = new LwjglFileHandle(name, Files.FileType.Internal);

        if (!resources.exists()) {
            throw new RuntimeException("\n\tFailed to find resources folder; expected it to be at  \"resources/" + name + "\"." +
                    " Either make sure the folder under resources has the same name as your mod's package, or change the line\n" +
                    "\t\"private static final String resourcesFolder = checkResourcesPath();\"\n" +
                    "\tat the top of the " + FriskMod.class.getSimpleName() + " java file.");
        }
        if (!resources.child("images").exists()) {
            throw new RuntimeException("\n\tFailed to find the 'images' folder in the mod's 'resources/" + name + "' folder; Make sure the " +
                    "images folder is in the correct location.");
        }
        if (!resources.child("localization").exists()) {
            throw new RuntimeException("\n\tFailed to find the 'localization' folder in the mod's 'resources/" + name + "' folder; Make sure the " +
                    "localization folder is in the correct location.");
        }

        return name;
    }

    /**
     * This determines the mod's ID based on information stored by ModTheSpire.
     */
    private static void loadModInfo() {
        Optional<ModInfo> infos = Arrays.stream(Loader.MODINFOS).filter((modInfo)->{
            AnnotationDB annotationDB = Patcher.annotationDBMap.get(modInfo.jarURL);
            if (annotationDB == null)
                return false;
            Set<String> initializers = annotationDB.getAnnotationIndex().getOrDefault(SpireInitializer.class.getName(), Collections.emptySet());
            return initializers.contains(FriskMod.class.getName());
        }).findFirst();
        if (infos.isPresent()) {
            info = infos.get();
            modID = info.ID;
        }
        else {
            throw new RuntimeException("Failed to determine mod info/ID based on initializer.");
        }
    }

    @Override
    public void receiveEditCharacters() {
        Frisk.Meta.registerCharacter();
        new AutoAdd(modID)
                .packageFilter(AbstractEasyPotion.class)
                .any(AbstractEasyPotion.class, (info, potion) -> {
                    if (potion.pool == null)
                        BaseMod.addPotion(potion.getClass(), potion.liquidColor, potion.hybridColor, potion.spotsColor, potion.ID);
                    else
                        BaseMod.addPotion(potion.getClass(), potion.liquidColor, potion.hybridColor, potion.spotsColor, potion.ID, potion.pool);
                });
    }


    @Override
    public void receiveEditCards() {
        new AutoAdd(modID)
                .packageFilter(AbstractEasyDynamicVariable.class)
                .any(DynamicVariable.class, (info, var) ->
                        BaseMod.addDynamicVariable(var));
        new AutoAdd(modID) //Loads files from this mod
                .packageFilter(AbstractEasyCard.class) //In the same package as this class
                .setDefaultSeen(true) //And marks them as seen in the compendium
                .cards(); //Adds the cards
    }
    @Override
    public void receiveEditRelics() {
        new AutoAdd(modID) //Loads files from this mod
                .packageFilter(BaseRelic.class) //In the same package as this class
                .any(BaseRelic.class, (info, relic) -> { //Run this code for any classes that extend this class
                    if (relic.pool != null)
                        BaseMod.addRelicToCustomPool(relic, relic.pool); //Register a custom character specific relic
                    else
                        BaseMod.addRelic(relic, relic.relicType); //Register a shared or base game character specific relic

//                    //If the class is annotated with @AutoAdd.Seen, it will be marked as seen, making it visible in the relic library.
//                    //If you want all your relics to be visible by default, just remove this if statement.
//                    if (info.seen)
//                        UnlockTracker.markRelicAsSeen(relic.relicId);
                });
    }
    @Override
    public void receiveStartGame() {
        if (!CardCrawlGame.loadingSave) {
            SharedFunctions.setSpecialDealBonus(0);
        }
    }


    private void initializeSavedData() {
        BaseMod.addSaveField("SpecialDealCycle", new CustomSavable<Integer>() {
            @Override
            public Integer onSave() {
                return SharedFunctions.getSpecialDealBonus();
            }

            @Override
            public void onLoad(Integer s) {
                if (s != null) {
                    // Override Bonus.
                    SharedFunctions.setSpecialDealBonus(s);
                }
            }
        });
    }
    @Override
    public void receiveCardUsed(AbstractCard card) {
        //XPModifierAll.setActiveCard(abstractCard);
        if (!card.dontTriggerOnUseCard) {
            Wiz.atb(new AfterCardUseAction(card));
        }
        if (!GhostlyPatch.GhostlyCardFields.isGhostly.get(card)) {
            for (AbstractPower p : AbstractDungeon.player.powers) {
                if (p instanceof NonAttackPower)
                    ((NonAttackPower) p).ghostlyCardPlayed();
            }
        }
    }

    public void receivePostBattle(AbstractRoom abstractRoom) {

    }


    @Override
    public void receiveOnBattleStart(AbstractRoom abstractRoom) {
        Wiz.atb(new OnBattleStartAction(abstractRoom));
    }
}
