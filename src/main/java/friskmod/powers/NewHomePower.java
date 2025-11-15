package friskmod.powers;

import basemod.helpers.CardModifierManager;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import friskmod.FriskMod;
import friskmod.cardmods.MonochromeMod;
import friskmod.helper.SharedFunctions;
import friskmod.patches.MonochromePatch;
import friskmod.patches.ExternalGlowPatch;
import friskmod.util.Wiz;

import java.util.List;

import static friskmod.patches.MonochromePatch.makeMonochromeCard;

public class NewHomePower extends BasePower {
    public static final String POWER_ID = FriskMod.makeID(NewHomePower.class.getSimpleName());
    private static final PowerType TYPE = PowerType.BUFF;
    private static final boolean TURN_BASED = false;

    static final Color BLUE_BORDER_GLOW_COLOR = new Color(0.2F, 0.9F, 1.0F, 0.25F);
    static final Color GOLD_BORDER_GLOW_COLOR = Color.GOLD.cpy();

    private int copiedThisTurn = 0;



    //The only thing TURN_BASED controls is the color of the number on the power icon.
    //Turn based powers are white, non-turn based powers are red or green depending on if their amount is positive or negative.
    //For a power to actually decrease/go away on its own they do it themselves.
    //Look at powers that do this like VulnerablePower and DoubleTapPower.
    public NewHomePower(AbstractCreature owner, int amount) {
        super(POWER_ID, TYPE, TURN_BASED, owner, amount);
    }

    @Override
    public AbstractPower makeCopy() {
        return new NewHomePower(owner, amount);
    }

    public void updateDescription() {
        if (amount == 1) {
            this.description = String.format(DESCRIPTIONS[0], amount);
        } else{
            this.description = String.format(DESCRIPTIONS[1], amount);
        }
    }

    public void atStartOfTurn() {
        this.copiedThisTurn = 0;
    }

    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (canCopy(card)) {
             Wiz.atb(Wiz.actionify( () -> {
                 if (MonochromePatch.MonochromeFields.wasCriticalPlayed.get(card)) {
                     if (!SharedFunctions.isCurseOrStatus(card)) {
                         this.copiedThisTurn++;
                         flash();
                         makeMonochromeCard(card);
                     }
                 }
             }));
        }
    }

    private boolean canCopy(AbstractCard card) {
        return !card.purgeOnUse && this.copiedThisTurn < this.amount && !CardModifierManager.hasModifier(card, MonochromeMod.ID);
    }

    @Override
    public void update(int slot) {
        super.update(slot);
        if (Wiz.isInCombat()) {
            makeMiddleCardGlow();
        }
    }

    private void makeMiddleCardGlow() {
        try {
            int card_pos;
            List<AbstractCard> hand = AbstractDungeon.player.hand.group;
            for (AbstractCard c : hand) {
                if (ExternalGlowPatch.NewHomeGlowFields.glowingBecauseNewHome.get(c)) {
                    ExternalGlowPatch.NewHomeGlowFields.glowingBecauseNewHome.set(c, false);
                    c.glowColor = BLUE_BORDER_GLOW_COLOR.cpy();
                }
            }
            boolean reset = this.copiedThisTurn >= this.amount;
            if (reset) {
                return;
            }
            double hand_size = (hand.size());
            card_pos = (int) (hand_size - 1) / 2;
            boolean evenNumCards = false;
            if (hand_size % 2 == 0) {
                evenNumCards = true;
            }
            int cycle;
            if (evenNumCards) {
                cycle = 2;
            } else{
                cycle = 1;
            }
            int i;

            for (i = 0; i < cycle; i++) {
                AbstractCard chosenCard = hand.get(card_pos+i);
                if (canCopy(chosenCard)) {
                    chosenCard.glowColor = GOLD_BORDER_GLOW_COLOR.cpy();
                    ExternalGlowPatch.NewHomeGlowFields.glowingBecauseNewHome.set(chosenCard, true);
                }
            }
        } catch (Exception ignored) {
            FriskMod.logger.warn("{}: BalletShoes set middle glow failed",FriskMod.modID);
        }
    }


}