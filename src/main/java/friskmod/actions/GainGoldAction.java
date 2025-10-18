package friskmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.GainPennyEffect;

public class GainGoldAction extends AbstractGameAction {
  private int increaseGold;
  
  public GainGoldAction(AbstractCreature source, int goldAmount) {
    this.source = source;
    this.target = (AbstractCreature)AbstractDungeon.player;
    this.increaseGold = goldAmount;
    this.actionType = ActionType.DAMAGE;
    this.duration = Settings.ACTION_DUR_MED;
  }
  
  public void update() {
    if (this.duration == Settings.ACTION_DUR_MED && this.target != null) {
      AbstractDungeon.player.gainGold(this.increaseGold);
      for (int i = 0; i < this.increaseGold; i++)
        AbstractDungeon.effectList.add(new GainPennyEffect(this.target, this.source.hb.cX, this.source.hb.cY, this.target.hb.cX, this.target.hb.cY, true)); 
    } 
    tickDuration();
  }
}


/* Location:              C:\Program Files (x86)\Steam\steamapps\workshop\content\646570\1609846039\TheDisciple.jar!\chronomuncher\actions\GainGoldAction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */