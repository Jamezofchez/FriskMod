package friskmod.cards.drinks;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.potions.FairyPotion;
import friskmod.cards.AbstractEasyCard;
import friskmod.util.CardStats;

public abstract class AbstractDrinkCard extends AbstractEasyCard {

    AbstractPotion potion;
    enum DrinkType {POTENCY, LOWCOST}
    public AbstractDrinkCard(String ID, AbstractPotion potion){
        super(
                ID,
                new CardStats(
                        CardColor.COLORLESS,
                        CardType.SKILL,
                        CardRarity.SPECIAL,
                        getTarget(potion),
                        0
                )
        );
        this.potion = potion;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        usePotion(m);
    }

    private static CardTarget getTarget(AbstractPotion potion) {
        if (potion == null){
            return CardTarget.NONE;
        }
        if (potion.targetRequired){
            return CardTarget.ENEMY;
        } else{
            return CardTarget.NONE;
        }

    }

    public void usePotion(AbstractMonster m){
        if (potion instanceof FairyPotion){
            this.cost = -2;
        }
        AbstractCreature realTarget;
        if (this.target == CardTarget.NONE){
            realTarget = null;
        } else{
            realTarget = m;
        }
    }
}