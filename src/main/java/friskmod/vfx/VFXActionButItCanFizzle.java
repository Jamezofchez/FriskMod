package friskmod.vfx;

import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class VFXActionButItCanFizzle extends VFXAction {
  public VFXActionButItCanFizzle(AbstractGameEffect effect) {
    this((AbstractCreature)null, effect, 0.0F);
  }
  
  public VFXActionButItCanFizzle(AbstractGameEffect effect, float duration) {
    this((AbstractCreature)null, effect, duration);
  }
  
  public VFXActionButItCanFizzle(AbstractCreature source, AbstractGameEffect effect) {
    this(source, effect, 0.0F);
  }
  
  public VFXActionButItCanFizzle(AbstractCreature source, AbstractGameEffect effect, float duration) {
    super(source, effect, duration);
  }
  
  public VFXActionButItCanFizzle(AbstractCreature source, AbstractGameEffect effect, float duration, boolean topLevel) {
    super(source, effect, duration, topLevel);
  }
  
  public void update() {
    if (this.source != null && this.source.isDeadOrEscaped()) {
      this.isDone = true;
    } else {
      super.update();
    } 
  }
}


/* Location:              C:\Program Files (x86)\Steam\steamapps\workshop\content\646570\2396661789\Ruina.jar!\ruina\vfx\VFXActionButItCanFizzle.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */