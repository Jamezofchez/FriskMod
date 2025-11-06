package friskmod.vfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class WaitEffect extends AbstractGameEffect {
  private static final float DURATION = 0.0F;
  
  public void update() {
    this.duration -= Gdx.graphics.getDeltaTime();
    if (this.duration <= 0.0F)
      this.isDone = true; 
  }
  
  public void render(SpriteBatch sb) {}
  
  public void dispose() {}
}


/* Location:              C:\Program Files (x86)\Steam\steamapps\workshop\content\646570\2396661789\Ruina.jar!\ruina\vfx\WaitEffect.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */