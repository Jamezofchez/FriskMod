package friskmod.powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.unique.RetainCardsAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import friskmod.FriskMod;

public class RememberPatiencePower extends BasePower {
    public static final String POWER_ID = FriskMod.makeID(RememberPatiencePower.class.getSimpleName());
    private static final AbstractPower.PowerType TYPE = AbstractPower.PowerType.BUFF;
    private static final boolean TURN_BASED = false;

    //The only thing TURN_BASED controls is the color of the number on the power icon.
    //Turn based powers are white, non-turn based powers are red or green depending on if their amount is positive or negative.
    //For a power to actually decrease/go away on its own they do it themselves.
    //Look at powers that do this like VulnerablePower and DoubleTapPower.
    public RememberPatiencePower(AbstractCreature owner, int amount, int minCards) {
        super(POWER_ID, TYPE, TURN_BASED, owner, amount);
        this.amount2 = minCards;
    }

    //    @Override
//    public void atStartOfTurn() {
//        retainThisTurn = false;
//        checkOverflow();
//    }
//
//    @Override
//    public void onCardDraw(AbstractCard card) {
//        checkOverflow();
//    }
//    @Override
//    public void onAfterCardPlayed(AbstractCard usedCard) {
//        checkOverflow();
//    }
    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (isPlayer && !AbstractDungeon.player.hand.isEmpty() && !AbstractDungeon.player.hasRelic("Runic Pyramid") &&
                !AbstractDungeon.player.hasPower("Equilibrium")) {
            if (AbstractDungeon.player.hand.size() >= amount2) {
                this.flash();
                addToBot(new RetainCardsAction(this.owner, this.amount));
            }
        }
    }

    @Override
    public AbstractPower makeCopy() {
        return new RememberPatiencePower(owner, amount, amount2);
    }

    public void updateDescription() {
        this.description = String.format(DESCRIPTIONS[0], amount2, amount);
    }

}