package friskmod.vfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class EchoFlowerDisappearEffect extends AbstractGameEffect {
  AbstractCard c;
  
  public EchoFlowerDisappearEffect(AbstractCard c) {
    this.duration = Settings.ACTION_DUR_LONG;
    this.c = (AbstractCard)c;
    c.stopGlowing();
    AbstractDungeon.player.limbo.addToTop((AbstractCard)c);
    c.fadingOut = true;
  }
  
  public void update() {
    this.duration -= Gdx.graphics.getDeltaTime();
    AbstractDungeon.player.hand.removeCard(this.c);
    AbstractDungeon.player.discardPile.removeCard(this.c);
    AbstractDungeon.player.exhaustPile.removeCard(this.c);
    if (this.duration < 0.0F) {
      AbstractDungeon.player.limbo.removeCard(this.c);
      this.isDone = true;
    } 
  }
  
  public void render(SpriteBatch spriteBatch) {}
  
  public void dispose() {}
}


/* Location:              C:\Program Files (x86)\Steam\steamapps\workshop\content\646570\2371967829\Reliquary.jar!\vfx\EchoFlowerDisappearEffect.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */