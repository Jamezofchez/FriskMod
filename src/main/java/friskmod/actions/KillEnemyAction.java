package friskmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.DeckPoofEffect;

public class KillEnemyAction extends AbstractGameAction {
  private AbstractMonster m;
  
  public KillEnemyAction(AbstractMonster m) {
    this.actionType = ActionType.SPECIAL;
    this.m = m;
  }
  
  public void update() {
    this.m.currentHealth = 0;
    loseHP(999999);
    this.m.die();
    if (AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
      AbstractDungeon.actionManager.cleanCardQueue();
      AbstractDungeon.effectList.add(new DeckPoofEffect(64.0F * Settings.scale, 64.0F * Settings.scale, true));
      AbstractDungeon.effectList.add(new DeckPoofEffect(Settings.WIDTH - 64.0F * Settings.scale, 64.0F * Settings.scale, false));
      AbstractDungeon.overlayMenu.hideCombatPanels();
    } 
    if ((AbstractDungeon.getCurrRoom()).monsters.areMonstersBasicallyDead())
      AbstractDungeon.actionManager.clearPostCombatActions(); 
    this.isDone = true;
  }
  
  private void loseHP(int damageAmount) {
    this.m.loseBlock();
    this.m.currentHealth -= damageAmount;
    if (this.m.currentHealth < 0)
      this.m.currentHealth = 0; 
    this.m.healthBarUpdatedEvent();
  }
}


/* Location:              C:\Program Files (x86)\Steam\steamapps\workshop\content\646570\3100352485\TheLocksmith.jar!\theAnime\actions\KillEnemyAction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */