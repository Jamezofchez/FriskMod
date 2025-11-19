package friskmod.cards.drinks;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.potions.DuplicationPotion;
import com.megacrit.cardcrawl.powers.AbstractPower;
import friskmod.powers.LVRitual;
import friskmod.powers.LesserDuplication;

import static friskmod.FriskMod.makeID;

public class CultistPotion extends AbstractDrinkCard{
    public static final String ID = makeID(CultistPotion.class.getSimpleName());

    public CultistPotion() {
        this(new com.megacrit.cardcrawl.potions.CultistPotion());
    }
    public CultistPotion(AbstractPotion potion) {
        super(ID, potion);
        baseMagicNumber = magicNumber = getNewPotency();
    }

    @Override
    public void usePotion(AbstractMonster m) {
        AbstractPlayer p = AbstractDungeon.player;
        addToBot(new ApplyPowerAction(p, p, new LVRitual(p, magicNumber), magicNumber));
    }

    @Override
    public void upp() {
        setUpgradeMagicNumber(getNewPotency());
    }
}
