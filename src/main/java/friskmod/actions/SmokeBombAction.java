package friskmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.ThoughtBubble;
import com.megacrit.cardcrawl.vfx.combat.SmokeBombEffect;

import static friskmod.FriskMod.makeID;

public class SmokeBombAction extends AbstractGameAction {
  private AbstractPlayer p;

  private boolean upgraded = false;

    public static final String ID = makeID(SmokeBombAction.class.getSimpleName()); //makeID adds the mod ID, so the final ID will be something like "modID:MyCard"
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(ID);
    public static final String[] TEXT = uiStrings.TEXT;
  
  public SmokeBombAction(boolean upgraded) {
    this.upgraded = upgraded;
    this.p = AbstractDungeon.player;
    this.actionType = ActionType.SPECIAL; //this is so shit, but to make sure it doesnt happens
  }
  
  public void update() {
    for (AbstractMonster m : (AbstractDungeon.getCurrRoom()).monsters.monsters) {
      if (m.type == AbstractMonster.EnemyType.BOSS) {
        AbstractDungeon.effectList.add(new ThoughtBubble(AbstractDungeon.player.dialogX, AbstractDungeon.player.dialogY, 3.0F, TEXT[0], true));
        this.isDone = true;
        return;
      } 
    } 
    if ((AbstractDungeon.getCurrRoom()).phase == AbstractRoom.RoomPhase.COMBAT) {
      this.target = (AbstractCreature)AbstractDungeon.player;
      (AbstractDungeon.getCurrRoom()).smoked = true;
      AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new VFXAction((AbstractGameEffect)new SmokeBombEffect(this.target.hb.cX, this.target.hb.cY)));
      this.target.hideHealthBar();
      this.target.isEscaping = true;
      this.target.escapeTimer = 2.5F;
    } 
    this.isDone = true;
  }
}


/* Location:              C:\Program Files (x86)\Steam\steamapps\workshop\content\646570\1609846039\TheDisciple.jar!\chronomuncher\actions\SmokeBombAction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */