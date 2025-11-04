package friskmod.cards.drinks;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.potions.AncientPotion;
import com.megacrit.cardcrawl.potions.DuplicationPotion;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import friskmod.powers.Karma;
import friskmod.powers.LesserDuplication;
import friskmod.util.Wiz;

import static friskmod.FriskMod.makeID;

public class AncientCocktail extends AbstractDrinkCard{
    public static final String ID = makeID(AncientCocktail.class.getSimpleName());
    public static final int POWER_AMOUNT = 1;
    public static final int UPG_POWER_AMOUNT = 1;

    public AncientCocktail() {
        this(new AncientPotion());
    }
    public AncientCocktail(AbstractPotion potion) {
        super(ID, potion);
        baseMagicNumber = magicNumber = POWER_AMOUNT;
    }

    @Override
    public void usePotion(AbstractMonster m) {
        AbstractPlayer p = AbstractDungeon.player;
        addToBot(new ApplyPowerAction(p, p, new ArtifactPower(p, magicNumber), magicNumber));
        for(AbstractMonster monster : Wiz.getMonsters()) {
            addToBot(new ApplyPowerAction(monster, p, new ArtifactPower(monster, magicNumber), magicNumber));
        }
    }

    @Override
    public void upp() {
        upgradeMagicNumber(UPG_POWER_AMOUNT);
    }
}
