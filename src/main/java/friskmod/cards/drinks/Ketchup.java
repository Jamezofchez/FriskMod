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

    public static final int UPG_TEMP_HP = 2;

    public Ketchup() {
        this(null);
    }
    public Ketchup(AbstractPotion potion) {
        super(ID, potion);
        if (potion == null) {
            baseMagicNumber = magicNumber = COMMON_TEMP_HP;
        } else {
            switch (potion.rarity) {
                case UNCOMMON:
                    baseMagicNumber = magicNumber = UNCOMMON_TEMP_HP;
                    break;
                case RARE:
                    baseMagicNumber = magicNumber = RARE_TEMP_HP;
                    break;
                default:
                    baseMagicNumber = magicNumber = COMMON_TEMP_HP;
                    break;
            }
        }
    }

    @Override
    public void usePotion(AbstractMonster m) {
        addToTop(new AddTemporaryHPAction(AbstractDungeon.player, AbstractDungeon.player, magicNumber));
    }

    @Override
    public void upp() {
        upgradeMagicNumber(UPG_TEMP_HP);
    }
}
