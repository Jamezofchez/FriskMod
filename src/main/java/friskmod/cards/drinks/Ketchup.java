package friskmod.cards.drinks;

import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.potions.AbstractPotion;

import static friskmod.FriskMod.makeID;

public class Ketchup extends AbstractDrinkCard{
    public static final String ID = makeID(Ketchup.class.getSimpleName());
    public static final int COMMON_TEMP_HP = 2;
    public static final int UNCOMMON_TEMP_HP = 3;
    public static final int RARE_TEMP_HP = 4;

    @Override
    protected int getNewPotency() {
        if (potion == null){
            return 0;
        }
        int newPotency = ketchupPotency();
        if (upgraded) {
            newPotency = (int) (newPotency * 1.5F);
        }
        return newPotency;
    }

    private int ketchupPotency() {
        switch (potion.rarity) {
            case UNCOMMON:
                return UNCOMMON_TEMP_HP;
            case RARE:
                return RARE_TEMP_HP;
            default:
                return COMMON_TEMP_HP;
        }
    }

    public Ketchup() {
        this(null);
    }
    public Ketchup(AbstractPotion startpotion) {
        super(ID, startpotion);
        baseMagicNumber = magicNumber = getNewPotency();
        if (AbstractDungeon.player != null){
            if (AbstractDungeon.player.hasRelic("SacredBark")){
                setUpgradeMagicNumber(magicNumber*2);
            }
        }
    }

    @Override
    public void usePotion(AbstractMonster m) {
        addToTop(new AddTemporaryHPAction(AbstractDungeon.player, AbstractDungeon.player, magicNumber));
    }

    @Override
    public void upp() {
        setUpgradeMagicNumber(getNewPotency());
    }
}
