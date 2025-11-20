package friskmod.powers;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import friskmod.FriskMod;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import friskmod.cards.SlackOff;
import friskmod.util.Wiz;

public class Mercied extends BasePower {
    public static final String POWER_ID = FriskMod.makeID(Mercied.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private static final AbstractPower.PowerType TYPE = AbstractPower.PowerType.DEBUFF;
    private static final boolean TURN_BASED = true;

    //The only thing TURN_BASED controls is the color of the number on the power icon.
    //Turn based powers are white, non-turn based powers are red or green depending on if their amount is positive or negative.
    //For a power to actually decrease/go away on its own they do it themselves.
    //Look at powers that do this like VulnerablePower and DoubleTapPower.
    public Mercied(AbstractCreature owner, int amount) {
        super(POWER_ID, TYPE, TURN_BASED, owner, amount);
    }

    public void atEndOfRound() {
//        if (this.amount == 0) {
            addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this.ID));
//        } else {
//            addToBot(new ReducePowerAction(this.owner, this.owner, this.ID, 1));
//        }
    }
    @Override
    public void onInitialApplication() {
        if (owner instanceof AbstractPlayer) {
            for (AbstractCard c : Wiz.getAllCardsInCardGroups()) {
                if (c instanceof SlackOff) {
                    ((SlackOff) c).setSeriousDescription();
                }
            }
        }
        super.onInitialApplication();
    }
    @Override
    public void onRemove() {
        if (owner instanceof AbstractPlayer) {
            for (AbstractCard c : Wiz.getAllCardsInCardGroups()) {
                if (c instanceof SlackOff) {
                    ((SlackOff) c).setNormalDescription();
                }
            }
        }
        super.onRemove();
    }

    @Override
    public AbstractPower makeCopy() {
        return new Mercied(owner, amount);
    }

    public void updateDescription() {
        int LV_enemy = 0;
        if (owner != null) {
            AbstractPower lvPower = owner.getPower(LV_Enemy.POWER_ID);
            if (lvPower != null) {
                LV_enemy = lvPower.amount;
            }
        }
        String baseDescription = DESCRIPTIONS[0];
        this.description = String.format(baseDescription, LV_enemy*amount);
    }

}