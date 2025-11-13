//package friskmod.cards;
//
//import com.evacipated.cardcrawl.mod.stslib.damagemods.BindingHelper;
//import com.megacrit.cardcrawl.characters.AbstractPlayer;
//import com.megacrit.cardcrawl.core.CardCrawlGame;
//import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
//import com.megacrit.cardcrawl.localization.CardStrings;
//import com.megacrit.cardcrawl.monsters.AbstractMonster;
//import friskmod.actions.FlurryOfKnivesAction;
//import friskmod.character.Frisk;
//import friskmod.util.CardStats;
//import friskmod.util.FriskTags;
//
//
//import static friskmod.FriskMod.makeID;
//
//public class EXTRAFlurryOfKnives extends AbstractEasyCard {
//    public static final String ID = makeID(EXTRAFlurryOfKnives.class.getSimpleName()); //makeID adds the mod ID, so the final ID will be something like "modID:MyCard"
//    public static final String NAME;
//    private static final CardStrings cardStrings;
//    static {
//        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
//        NAME = cardStrings.NAME;
//    }
//    //These will be used in the constructor. Technically you can just use the values directly,
//    //but constants at the top of the file are easy to adjust.
//    private static final CardStats info = new CardStats(
//            Frisk.Meta.Enums.CARD_COLOR, //The card color. If you're making your own character, it'll look something like this. Otherwise, it'll be CardColor.RED or similar for a basegame character color.
//            CardType.ATTACK, //The type. ATTACK/SKILL/POWER/CURSE/STATUS
//            CardRarity.UNCOMMON, //Rarity. BASIC is for starting cards, then there's COMMON/UNCOMMON/RARE, and then SPECIAL and CURSE. SPECIAL is for cards you only get from events. Curse is for curses, except for special curses like Curse of the Bell and Necronomicurse.
//            CardTarget.ALL_ENEMY, //The target. Single target is ENEMY, all enemies is ALL_ENEMY. Look at cards similar to what you want to see what to use.
//            3 //The card's base cost. -1 is X cost, -2 is no cost for unplayable cards like curses, or Reflex.
//    );
//    private static final int DAMAGE = 9;
//    private static final int NUM_KNIVES = 2;
//    private static final int UPG_NUM_KNIVES = 1;
//
//
//
//    public EXTRAFlurryOfKnives() {
//        super(ID, info); //Pass the required information to the BaseCard constructor.
//        baseDamage = DAMAGE;
//        baseMagicNumber = magicNumber = NUM_KNIVES;
//        tags.add(FriskTags.YOU);
//    }
//
//    @Override
//    public void use(AbstractPlayer p, AbstractMonster m) {
//        AbstractMonster randomMonster = AbstractDungeon.getMonsters().getRandomMonster(null, true, AbstractDungeon.cardRandomRng);
//        FlurryOfKnivesAction action = new FlurryOfKnivesAction(randomMonster, damage, magicNumber, this);
//        BindingHelper.bindAction(this, action);
//        addToBot(action);
//    }
//    @Override
//    public void upgrade() {
//        upgradeMagicNumber(UPG_NUM_KNIVES);
//        ++this.timesUpgraded;
//        this.upgraded = true;
//        this.name = NAME + "+" + this.timesUpgraded;
//        this.initializeTitle();
//    }
//
//    public boolean canUpgrade() {
//        return true;
//    }
//    @Override
//    public void upp() {};
//}
