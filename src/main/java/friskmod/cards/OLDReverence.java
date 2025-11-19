//package friskmod.cards;
//
//import com.megacrit.cardcrawl.actions.AbstractGameAction;
//import com.megacrit.cardcrawl.characters.AbstractPlayer;
//import com.megacrit.cardcrawl.monsters.AbstractMonster;
//import friskmod.character.Frisk;
//import friskmod.util.CardStats;
//import friskmod.util.FriskTags;
//
//
//import static friskmod.FriskMod.makeID;
//
//public class Reverence extends AbstractCriticalCard {
//    public static final String ID = makeID(Reverence.class.getSimpleName()); //makeID adds the mod ID, so the final ID will be something like "modID:MyCard"
//    //These will be used in the constructor. Technically you can just use the values directly,
//    //but constants at the top of the file are easy to adjust.
//    private static final CardStats info = new CardStats(
//            Frisk.Meta.Enums.CARD_COLOR, //The card color. If you're making your own character, it'll look something like this. Otherwise, it'll be CardColor.RED or similar for a basegame character color.
//            CardType.ATTACK, //The type. ATTACK/SKILL/POWER/CURSE/STATUS
//            CardRarity.COMMON, //Rarity. BASIC is for starting cards, then there's COMMON/UNCOMMON/RARE, and then SPECIAL and CURSE. SPECIAL is for cards you only get from events. Curse is for curses, except for special curses like Curse of the Bell and Necronomicurse.
//            CardTarget.ENEMY, //The target. Single target is ENEMY, all enemies is ALL_ENEMY. Look at cards similar to what you want to see what to use.
//            1 //The card's base cost. -1 is X cost, -2 is no cost for unplayable cards like curses, or Reflex.
//    );
//    private static final int DAMAGE = 7;
//    private static final int UPG_DAMAGE = 2;
//    public Reverence() {
//        super(ID, info); //Pass the required information to the BaseCard constructor.
//        baseDamage = DAMAGE;
//        tags.add(FriskTags.INTEGRITY);
//    }
//    @Override
//    public void use(AbstractPlayer p, AbstractMonster m) {
//        if (isCritical()) {
//            dmg(m, AbstractGameAction.AttackEffect.BLUNT_HEAVY);
//        } else{
//            dmg(m, AbstractGameAction.AttackEffect.BLUNT_LIGHT);
//        }
//        super.use(p, m);
//    }
//    @Override
//    public void calculateCardDamage(AbstractMonster mo) {
//        super.calculateCardDamage(mo);
//        //if this triggers dead on 3 times it deals 400% damage by adding 100% 3 times, not 800% damage, this is intentional.
//        if (!mo.hasPower("Intangible")) {
//            if (isCritical()) {
//                int base_dam = this.damage;
//                this.damage += base_dam;
//            }
//        }
//        //don't uncomment this, bad idea!!!
//        //this.baseDamage = base_dam;
//        isDamageModified = damage != baseDamage;
//    }
//
//
//    @Override
//    public void upp() {
//        upgradeDamage(UPG_DAMAGE);
//    }
//
//    @Override
//    public void CriticalEffect(AbstractPlayer p, AbstractMonster m) {
//
//    }
//}
