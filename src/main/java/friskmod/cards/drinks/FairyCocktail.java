package friskmod.cards.drinks;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.potions.FairyPotion;
import friskmod.FriskMod;
import friskmod.powers.LesserDuplication;

import static friskmod.FriskMod.makeID;

public class FairyCocktail extends AbstractDrinkCard{
    public static final String ID = makeID(FairyCocktail.class.getSimpleName());

    public FairyCocktail() {
        this(new FairyPotion());
    }
    public FairyCocktail(AbstractPotion potion) {
        super(ID, potion);
        baseMagicNumber = magicNumber = getNewPotency();
    }


    @Override
    public void usePotion(AbstractMonster m) {
        try {
            float percent = magicNumber / 100.0F;
            int healAmt = (int) (AbstractDungeon.player.maxHealth * percent);
            if (healAmt < 1)
                healAmt = 1;
            AbstractDungeon.player.heal(healAmt, true);
            AbstractDungeon.topPanel.destroyPotion(potion.slot);
        } catch (Exception ignored) {
            FriskMod.logger.warn("{}: Failed to sip potion {}", FriskMod.modID, potion.name);
        }
    }

    @Override
    public void upp() {
        setUpgradeMagicNumber(getNewPotency());
    }
}
