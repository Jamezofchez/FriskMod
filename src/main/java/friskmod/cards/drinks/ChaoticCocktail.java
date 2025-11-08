package friskmod.cards.drinks;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.PlayTopCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.potions.DistilledChaosPotion;
import com.megacrit.cardcrawl.potions.FairyPotion;
import friskmod.FriskMod;
import friskmod.powers.LesserDuplication;
import friskmod.util.Wiz;

import static friskmod.FriskMod.makeID;
import static friskmod.helper.GrillbysHelper.isCardCostAllowed;

public class ChaoticCocktail extends AbstractDrinkCard{
    public static final String ID = makeID(ChaoticCocktail.class.getSimpleName());

    public ChaoticCocktail() {
        this(new DistilledChaosPotion());
    }
    public ChaoticCocktail(AbstractPotion potion) {
        super(ID, potion);
        baseMagicNumber = magicNumber = getNewPotency();
    }


    @Override
    public void usePotion(AbstractMonster m) {
        AbstractCard topCard = AbstractDungeon.player.drawPile.getTopCard();
        if (isCardCostAllowed(topCard)) {
            addToBot((AbstractGameAction) new PlayTopCardAction(
                    (AbstractCreature) (AbstractDungeon.getCurrRoom()).monsters.getRandomMonster(null, true, AbstractDungeon.cardRandomRng), false));
        } else{
            //maybe VFX?
        }
    }

    @Override
    public void upp() {
        setUpgradeMagicNumber(getNewPotency());
    }
}
