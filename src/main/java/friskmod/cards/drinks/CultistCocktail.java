package friskmod.cards.drinks;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.city.Byrd;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import friskmod.powers.LVRitual;

import static friskmod.FriskMod.makeID;

public class CultistCocktail extends AbstractDrinkCard{
    public static final String ID = makeID(CultistCocktail.class.getSimpleName());

    public CultistCocktail() {
        this(new com.megacrit.cardcrawl.potions.CultistPotion());
    }
    public CultistCocktail(AbstractPotion potion) {
        super(ID, potion);
        baseMagicNumber = magicNumber = getNewPotency();
    }

    @Override
    public void usePotion(AbstractMonster m) {
        AbstractPlayer p = AbstractDungeon.player;
        playSfx();
        AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new TalkAction(true, Byrd.DIALOG[0], 1.2F, 1.2F));
        addToBot(new ApplyPowerAction(p, p, new LVRitual(p, magicNumber), magicNumber));
    }

    @Override
    public void upp() {
        setUpgradeMagicNumber(getNewPotency());
    }
    private void playSfx() {
        int roll = MathUtils.random(2);
        if (roll == 0) {
            AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new SFXAction("VO_CULTIST_1A"));
        } else if (roll == 1) {
            AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new SFXAction("VO_CULTIST_1B"));
        } else {
            AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new SFXAction("VO_CULTIST_1C"));
        }
    }
}
