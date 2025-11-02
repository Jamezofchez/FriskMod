//package friskmod.cards.choosecard;
//
//import com.megacrit.cardcrawl.powers.AbstractPower;
//import com.megacrit.cardcrawl.powers.VulnerablePower;
//import friskmod.powers.Karma;
//
//import static friskmod.FriskMod.makeID;
//
//public class NightmareOfKarma extends AbstractDreamCard{
//    public static final String ID = makeID(NightmareOfKarma.class.getSimpleName()); //makeID adds the mod ID, so the final ID will be something like "modID:MyCard"
//
//    private static final int POWER_AMOUNT = 3;
//
//    public NightmareOfKarma() {
//        super(ID);
//        setDisplayRarity(CardRarity.UNCOMMON);
//    }
//
//    @Override
//    AbstractPower getPower() {
//        return new Karma(getTarget(), POWER_AMOUNT);
//    }
//
//}
