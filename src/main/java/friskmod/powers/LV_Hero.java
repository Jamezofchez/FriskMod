package friskmod.powers;

import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.OnReceivePowerPower;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import friskmod.patches.CardXPFields;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;

import com.megacrit.cardcrawl.powers.AbstractPower;
import friskmod.FriskMod;

import java.util.Objects;

public class LV_Hero extends BasePower{
    public static final String POWER_ID = FriskMod.makeID(LV_Hero.class.getSimpleName());
    private static final AbstractPower.PowerType TYPE = AbstractPower.PowerType.BUFF;
    private static final boolean TURN_BASED = false;
    //The only thing TURN_BASED controls is the color of the number on the power icon.
    //Turn based powers are white, non-turn based powers are red or green depending on if their amount is positive or negative.
    //For a power to actually decrease/go away on its own they do it themselves.
    //Look at powers that do this like VulnerablePower and DoubleTapPower.

    public LV_Hero(AbstractCreature owner, int amount) {
        super(POWER_ID, TYPE, TURN_BASED, owner, amount);
    }

    @Override
    public AbstractPower makeCopy() {
        return new LV_Hero(owner, amount);
    }

    public void updateDescription() {
        this.description = String.format(DESCRIPTIONS[0], amount);
    }
    private static void refreshAllCardXPPreview(int playerLV) {
        if (AbstractDungeon.player == null) return;
        for (AbstractCard c : AbstractDungeon.player.hand.group) applyPreviewXP(c, playerLV);
        for (AbstractCard c : AbstractDungeon.player.drawPile.group) applyPreviewXP(c, playerLV);
        for (AbstractCard c : AbstractDungeon.player.discardPile.group) applyPreviewXP(c, playerLV);
        for (AbstractCard c : AbstractDungeon.player.exhaustPile.group) applyPreviewXP(c, playerLV);
        // Limbo (cards being played/created)
        for (AbstractCard c : AbstractDungeon.player.limbo.group) applyPreviewXP(c, playerLV);
        // Optionally master deck (compendium/preview)
        // for (AbstractCard c : AbstractDungeon.player.masterDeck.group) applyPreviewXP(c, playerLV);
    }

    private static void applyPreviewXP(AbstractCard card, int playerLV) {
        if (card == null) return;
        // Only touch attack cards
        if (card.type != AbstractCard.CardType.ATTACK) return;

    }
    private int lastAmount = 0;

    // Called once when the power is first applied
    @Override
    public void onInitialApplication() {
        refreshAllCardXPPreview(amount);
        lastAmount = amount;
    }

    // Called when the same power is applied again to stack
    @Override
    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);
        refreshAllCardXPPreview(amount);
        lastAmount = amount;
    }

    // Called when the power is reduced by something that calls reducePower
    @Override
    public void reducePower(int reduceAmount) {
        super.reducePower(reduceAmount);
        refreshAllCardXPPreview(amount);
        lastAmount = amount;
    }

    @Override
    public void onRemove() {
        refreshAllCardXPPreview(0);
        lastAmount = 0;
    }

    // Fallback: detect any direct edits to 'amount' from other code
    @Override
    public void update(int slot) {
        super.update(slot);
        if (amount != lastAmount) {
            lastAmount = amount;
            refreshAllCardXPPreview(amount);
        }
    }

}