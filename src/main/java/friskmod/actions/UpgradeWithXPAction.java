package friskmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;

import friskmod.patches.CardXPFields;

public class UpgradeWithXPAction extends AbstractGameAction {
//    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("ArmamentsAction");
//    public static final String[] TEXT = uiStrings.TEXT;

    private AbstractPlayer p;
    private ArrayList<AbstractCard> cannotUpgrade = new ArrayList<>();
    private boolean all;

    public UpgradeWithXPAction(boolean all) {
        this.actionType = ActionType.CARD_MANIPULATION;
        this.p = AbstractDungeon.player;
        this.duration = Settings.ACTION_DUR_FAST;
        this.all = all;
    }


    public void update() {
        upgradeAllCardsInGroup(p.hand);
        if (all) {
            upgradeAllCardsInGroup(p.drawPile);
            upgradeAllCardsInGroup(p.discardPile);
            upgradeAllCardsInGroup(p.exhaustPile);
        }
        this.isDone = true;
    }

    private void upgradeAllCardsInGroup(CardGroup cardGroup) {
        for (AbstractCard c : cardGroup.group) {
            if (c.canUpgrade() && CardXPFields.getCardXPBool(c)) {
                if (cardGroup.type == CardGroup.CardGroupType.HAND) {
                    c.superFlash();
                }
                c.upgrade();
            }
        }
    }
}



/* Location:              C:\Program Files (x86)\Steam\steamapps\common\SlayTheSpire\desktop-1.0.jar!\com\megacrit\cardcrawl\action\\unique\ArmamentsAction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */