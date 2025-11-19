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

    public DuplicationCocktail() {
        this(new DuplicationPotion());
    }
    public DuplicationCocktail(AbstractPotion potion) {
        super(ID, potion);
        baseMagicNumber = magicNumber = getNewHighestCost();
        baseSecondMagic = secondMagic = potion.getPotency();
    }

    @Override
    public void usePotion(AbstractMonster m) {
        AbstractPlayer p = AbstractDungeon.player;
        AbstractPower posspow = p.getPower(LesserDuplication.POWER_ID);
        int upgAmount = secondMagic;
        if (posspow != null) {
            upgAmount = Math.min(((LesserDuplication) posspow).amount2, upgAmount);
            ((LesserDuplication) posspow).amount2 = upgAmount;
        }
        addToBot(new ApplyPowerAction(p, p, new LesserDuplication(p, upgAmount, magicNumber), upgAmount));
    }

    @Override
    public void upp() {
        setUpgradeMagicNumber(getNewHighestCost());
    }

    public void applyPowers() {
        super.applyPowers();
        this.rawDescription = cardStrings.DESCRIPTION;
        if (upgraded){
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
        }
        secondMagic = potion.getPotency();
        if (secondMagic > 1) {
            this.rawDescription += cardStrings.EXTENDED_DESCRIPTION[0];
        }
        this.initializeDescription();

    }
}
