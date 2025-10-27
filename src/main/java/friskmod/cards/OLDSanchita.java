//package friskmod.cards;
//
//import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
//import com.megacrit.cardcrawl.cards.AbstractCard;
//import com.megacrit.cardcrawl.characters.AbstractPlayer;
//import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
//import com.megacrit.cardcrawl.monsters.AbstractMonster;
//import friskmod.character.Frisk;
//import friskmod.helper.ThreatenedCheck;
//import friskmod.patches.OnWasteEnergyPatch;
//import friskmod.powers.Karma;
//import friskmod.util.CardStats;
//import friskmod.util.FriskTags;
//
//
//import static friskmod.FriskMod.makeID;
//
//public class OLDSanchita extends AbstractEasyCard {
//    public static final String ID = makeID(OLDSanchita.class.getSimpleName()); //makeID adds the mod ID, so the final ID will be something like "modID:MyCard"
//    //These will be used in the constructor. Technically you can just use the values directly,
//    //but constants at the top of the file are easy to adjust.
//
//    private static final CardStats info = new CardStats(
//            Frisk.Meta.Enums.CARD_COLOR, //The card color. If you're making your own character, it'll look something like this. Otherwise, it'll be CardColor.RED or similar for a basegame character color.
//            CardType.SKILL, //The type. ATTACK/SKILL/POWER/CURSE/STATUS
//            CardRarity.UNCOMMON, //Rarity. BASIC is for starting cards, then there's COMMON/UNCOMMON/RARE, and then SPECIAL and CURSE. SPECIAL is for cards you only get from events. Curse is for curses, except for special curses like Curse of the Bell and Necronomicurse.
//            CardTarget.ALL_ENEMY, //The target. Single target is ENEMY, all enemies is ALL_ENEMY. Look at cards similar to what you want to see what to use.
//            0 //The card's base cost. -1 is X cost, -2 is no cost for unplayable cards like curses, or Reflex.
//    );
//
//    private static final int KARMA_AMOUNT_TIMES = 0;
//    private static final int KARMA_AMOUNT = 6;
//    private static final int UPG_KARMA_AMOUNT = 2;
//
//    public OLDSanchita() {
//        super(ID, info); //Pass the required information to the BaseCard constructor.
//        baseMagicNumber = magicNumber = KARMA_AMOUNT_TIMES;
//        baseSecondMagic = secondMagic = KARMA_AMOUNT;
//        tags.add(FriskTags.JUSTICE);
//    }
//
//    private int getBaseMagic() {
//        return OnWasteEnergyPatch.previous_e;
//    }
//
//    @Override
//    public void use(AbstractPlayer p, AbstractMonster m) {
//        this.magicNumber = getBaseMagic();
//        for (int i = 0; i < magicNumber; i++) {
//            AbstractMonster randomMonster = AbstractDungeon.getMonsters().getRandomMonster(null, true, AbstractDungeon.cardRandomRng);
//            if (randomMonster != null) {
//                addToBot(new ApplyPowerAction(randomMonster, p, new Karma(randomMonster, secondMagic), secondMagic));
//            }
//        }
//        this.rawDescription = cardStrings.DESCRIPTION;
//        initializeDescription();
//    }
//
//    public void applyPowers() {
//        this.magicNumber = getBaseMagic();
//        super.applyPowers();
//        this.rawDescription = cardStrings.DESCRIPTION;
//        this.rawDescription += cardStrings.EXTENDED_DESCRIPTION[0];
//        this.initializeDescription();
//    }
//
//    public void onMoveToDiscard() {
//        this.rawDescription = cardStrings.DESCRIPTION;
//        initializeDescription();
//    }
//
//    public void calculateCardDamage(AbstractMonster mo) {
//        this.magicNumber = getBaseMagic(); //is this needed?
//        super.calculateCardDamage(mo);
//        this.rawDescription = cardStrings.DESCRIPTION;
//        this.rawDescription += cardStrings.EXTENDED_DESCRIPTION[0];
//        initializeDescription();
//    }
//
//
//
//    @Override
//    public void upp() {
//        upgradeSecondMagic(UPG_KARMA_AMOUNT);
//    }
//}
