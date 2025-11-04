package friskmod.powers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import friskmod.FriskMod;
import friskmod.helper.GrillbysHelper;
import friskmod.util.Wiz;

import static friskmod.FriskMod.makeID;

public class LesserDuplication extends BasePower {
    public static final String POWER_ID = makeID(friskmod.powers.LesserDuplication.class.getSimpleName());
    private static final AbstractPower.PowerType TYPE = AbstractPower.PowerType.BUFF;
    private static final boolean TURN_BASED = false;

    //The only thing TURN_BASED controls is the color of the number on the power icon.
    //Turn based powers are white, non-turn based powers are red or green depending on if their amount is positive or negative.
    //For a power to actually decrease/go away on its own they do it themselves.
    //Look at powers that do this like VulnerablePower and DoubleTapPower.
    public LesserDuplication(AbstractCreature owner, int amount, int maxCost) {
        super(POWER_ID, TYPE, TURN_BASED, owner, amount);
        this.amount2 = maxCost;
    }

    @Override
    public AbstractPower makeCopy() {
        return new friskmod.powers.LesserDuplication(owner, amount, amount2);
    }
  
  public void updateDescription() {
    if (this.amount == 1) {
      this.description = DESCRIPTIONS[0];
    } else {
      this.description = DESCRIPTIONS[1] + this.amount + DESCRIPTIONS[2];
    } 
  }
  
  public void onUseCard(AbstractCard card, UseCardAction action) {
    if (!GrillbysHelper.checkLesserDuplication(card, amount2))
      return; 
    if (this.amount > 0) {
      flash();
      duplicatePlayedCard(card, (AbstractMonster)action.target);
      this.amount--;
      if (this.amount == 0)
        addToBot((AbstractGameAction)new RemoveSpecificPowerAction(this.owner, this.owner, this.ID)); 
    } 
  }
  
  public void atEndOfRound() {
    if (this.amount == 0) {
      addToBot((AbstractGameAction)new RemoveSpecificPowerAction(this.owner, this.owner, this.ID));
    } else {
      addToBot((AbstractGameAction)new ReducePowerAction(this.owner, this.owner, this.ID, 1));
    } 
  }
    public static void duplicatePlayedCard(AbstractCard card, AbstractMonster m) {
        AbstractCard tmp = card.makeSameInstanceOf();
        AbstractDungeon.player.limbo.addToBottom(tmp);
        tmp.current_x = card.current_x;
        tmp.current_y = card.current_y;
        tmp.target_x = Settings.WIDTH / 2.0F - 300.0F * Settings.scale;
        tmp.target_y = Settings.HEIGHT / 2.0F;
        if (m != null)
            tmp.calculateCardDamage(m);
        tmp.applyPowers();
        tmp.purgeOnUse = true;
        AbstractDungeon.actionManager.addCardQueueItem(new CardQueueItem(tmp, m, card.energyOnUse, true, true), true);
    }
}


/* Location:              C:\Program Files (x86)\Steam\steamapps\workshop\content\646570\2371967829\Reliquary.jar!\powers\LesserDuplicationPower.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */