//package friskmod.cards;
//
//import com.megacrit.cardcrawl.actions.common.GainBlockAction;
//import com.megacrit.cardcrawl.actions.unique.LoseEnergyAction;
//import com.megacrit.cardcrawl.cards.AbstractCard;
//import com.megacrit.cardcrawl.characters.AbstractPlayer;
//import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
//import com.megacrit.cardcrawl.monsters.AbstractMonster;
//import friskmod.character.Frisk;
//import friskmod.util.CardStats;
//import friskmod.util.FriskTags;
//import basemod.ReflectionHacks;
//
//
//import static friskmod.FriskMod.makeID;
//
//public class Exhaustion extends AbstractEasyCard {
//    public static final String ID = makeID(Exhaustion.class.getSimpleName()); //makeID adds the mod ID, so the final ID will be something like "modID:MyCard"
//    //These will be used in the constructor. Technically you can just use the values directly,
//    //but constants at the top of the file are easy to adjust.
//
//    private static final CardStats info = new CardStats(
//            CardColor.COLORLESS, //The card color. If you're making your own character, it'll look something like this. Otherwise, it'll be CardColor.RED or similar for a basegame character color.
//            CardType.STATUS, //The type. ATTACK/SKILL/POWER/CURSE/STATUS
//            CardRarity.COMMON, //Rarity. BASIC is for starting cards, then there's COMMON/UNCOMMON/RARE, and then SPECIAL and CURSE. SPECIAL is for cards you only get from events. Curse is for curses, except for special curses like Curse of the Bell and Necronomicurse.
//            CardTarget.NONE, //The target. Single target is ENEMY, all enemies is ALL_ENEMY. Look at cards similar to what you want to see what to use.
//            0 //The card's base cost. -1 is X cost, -2 is no cost for unplayable cards like curses, or Reflex.
//    );
//    public Exhaustion() {
//        this(-2);
//    }
//    public Exhaustion(int energyLoss) {
//        super(ID, info); //Pass the required information to the BaseCard constructor.
//        baseMagicNumber = magicNumber = energyLoss;
//        ReflectionHacks.setPrivate(this, AbstractCard.class, "baseCost", energyLoss);
//        this.cost = energyLoss;
//        this.exhaust = true;
//    }
//    @Override
//    public void triggerWhenDrawn() {
//        this.isEthereal = true;
//        addToBot(new LoseEnergyAction(this.cost));
//    }
//    @Override
//    public void applyPowers() {
//        super.applyPowers();
//        this.rawDescription = cardStrings.DESCRIPTION;
//        this.rawDescription = this.rawDescription + cardStrings.EXTENDED_DESCRIPTION[0];
//        this.initializeDescription();
//    }
//    @Override
//    public void use(AbstractPlayer p, AbstractMonster m) {
//
//    }
//
//    @Override
//    public void upp() {
//        upgradeBaseCost(this.costForTurn-1);
//    }
//}
