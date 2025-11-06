package friskmod.monsters;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import friskmod.FriskMod;

import static friskmod.FriskMod.makeID;

public class EmptyDummy extends AbstractDummy {
    public static final String ID = FriskMod.makeID(EmptyDummy.class.getSimpleName());
  
  public EmptyDummy(float x, float y, int hp) {
    super(ID, ID, x, y, hp);
  }
}


/* Location:              C:\Program Files (x86)\Steam\steamapps\workshop\content\646570\3100352485\TheLocksmith.jar!\theAnime\monsters\EmptyDummy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */