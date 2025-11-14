package friskmod.powers;

import basemod.ReflectionHacks;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import friskmod.FriskMod;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import friskmod.patches.GhostlyPatch;
import friskmod.util.Wiz;
import friskmod.util.interfaces.AfterCardPlayedInterface;

public class NonAttackPower extends BasePower {
    public static final String POWER_ID = FriskMod.makeID(NonAttackPower.class.getSimpleName());
    private static final AbstractPower.PowerType TYPE = AbstractPower.PowerType.BUFF;
    private static final boolean TURN_BASED = false;

    //The only thing TURN_BASED controls is the color of the number on the power icon.
    //Turn based powers are white, non-turn based powers are red or green depending on if their amount is positive or negative.
    //For a power to actually decrease/go away on its own they do it themselves.
    //Look at powers that do this like VulnerablePower and DoubleTapPower.
    public NonAttackPower(AbstractCreature owner, int amount) {
        super(POWER_ID, TYPE, TURN_BASED, owner, amount);
    }

    @Override
    public void atEndOfRound() {
//       if (this.amount == 0) {
        addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this.ID));
//       } else {
//            addToBot(new ReducePowerAction(this.owner, this.owner, this.ID, 1));
//       }
    }

    @Override
    public void onRemove() {
        resetCardsGhostly();
    }

    private void resetGhostly(AbstractCard card){
        if (GhostlyPatch.GhostlyCardFields.isGhostly.get(card)) {
            ReflectionHacks.setPrivate(card, AbstractCard.class, "damageType", DamageInfo.DamageType.NORMAL);
            card.damageTypeForTurn = DamageInfo.DamageType.NORMAL;
            card.dontTriggerOnUseCard = false;
            GhostlyPatch.GhostlyCardFields.isGhostly.set(card, false);
            GhostlyPatch.GhostlyDamageTypeFields.isGhostly.set(card.damageTypeForTurn, false);

        }
    }

    private void resetCardsGhostly() {
        if (AbstractDungeon.player == null) return;
        for (AbstractCard c : Wiz.getAllCardsInCardGroups()) {
            resetGhostly(c);
        }
    }


    @Override
    public AbstractPower makeCopy() {
        return new NonAttackPower(owner, amount);
    }

    public void updateDescription() {
        String baseDescription = DESCRIPTIONS[1];
        if (amount == 1){
            baseDescription = DESCRIPTIONS[0];
        }
        this.description = String.format(baseDescription, amount);
    }

    public void ghostlyCardPlayed() {
        this.flash();
        addToBot(new ReducePowerAction(this.owner, this.owner, this.ID, 1));

    }
}