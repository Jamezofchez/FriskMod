package friskmod.actions;

import com.evacipated.cardcrawl.mod.stslib.actions.common.SelectCardsInHandAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import friskmod.patches.CardXPFields;

import java.util.List;
import java.util.function.Consumer;

import static friskmod.FriskMod.makeID;

public class GiveInherentXPAction extends AbstractGameAction {
    public static final String ID = makeID(GiveInherentXPAction.class.getSimpleName()); //makeID adds the mod ID, so the final ID will be something like "modID:MyCard"
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(ID);
    public static final String[] TEXT = uiStrings.TEXT;

    private AbstractPlayer p;

    private boolean isRandom;

    private int xpAmount;

    public GiveInherentXPAction(int amount, int xpAmount, boolean isRandom) {
        this.p = AbstractDungeon.player;
        this.isRandom = isRandom;
        this.amount = amount;
        this.xpAmount = xpAmount;
        this.duration = this.startDuration = Settings.ACTION_DUR_FAST;
        this.actionType = ActionType.CARD_MANIPULATION;
    }

    public void update() {
        if (this.duration == this.startDuration) {
            if (this.p.hand.isEmpty()) {
                this.isDone = true;
                return;
            }
            if (this.isRandom) {
                for (int i = 0; i < amount; i++)
                    CardXPFields.addInherentXP(this.p.hand.getRandomCard(AbstractDungeon.cardRandomRng), xpAmount);
            } else {
                String selectionText;
                if (amount == 1) {
                    selectionText = String.format(TEXT[0], amount);
                } else{
                    selectionText = String.format(TEXT[1], amount);
                }

                addToBot(new SelectCardsInHandAction(amount, selectionText, true, true, (x -> true), GiveInherentXP()));
            }
        }
        tickDuration();
    }

    private Consumer<List<AbstractCard>> GiveInherentXP() {
        return (List<AbstractCard> cardList) -> {
            for (AbstractCard c : cardList) {
                CardXPFields.addInherentXP(c, xpAmount);
            }
        };
    }
}


/* Location:              C:\Program Files (x86)\Steam\steamapps\common\SlayTheSpire\desktop-1.0.jar!\com\megacrit\cardcrawl\actions\common\GiveInherentXPAction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */