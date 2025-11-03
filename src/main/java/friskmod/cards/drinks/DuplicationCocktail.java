package friskmod.cards.drinks;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.potions.DuplicationPotion;
import com.megacrit.cardcrawl.powers.AbstractPower;
import friskmod.powers.LesserDuplication;
import friskmod.powers.RememberPatiencePower;

import static friskmod.FriskMod.makeID;

public class DuplicationCocktail extends AbstractDrinkCard{
    public static final String ID = makeID(DuplicationCocktail.class.getSimpleName());
    public static final int POWER_AMOUNT = 1;
    public static final int MAX_CARD_COST = 1;
    public static final int UPG_MAX_CARD_COST = 1;

    public DuplicationCocktail() {
        this(new DuplicationPotion());
    }
    public DuplicationCocktail(AbstractPotion potion) {
        super(ID, potion);
        baseMagicNumber = magicNumber = POWER_AMOUNT;
        baseSecondMagic = secondMagic = MAX_CARD_COST;
    }

    @Override
    public void usePotion(AbstractMonster m) {
        AbstractPlayer p = AbstractDungeon.player;
        AbstractPower posspow = p.getPower(LesserDuplication.POWER_ID);
        if (posspow != null) {
            ((LesserDuplication) posspow).amount2 = Math.min(((LesserDuplication) posspow).amount2, secondMagic);
        }
        addToBot(new ApplyPowerAction(p, p, new LesserDuplication(p, magicNumber, secondMagic), magicNumber));
    }

    @Override
    public void upp() {
        upgradeMagicNumber(UPG_MAX_CARD_COST);
    }
}
