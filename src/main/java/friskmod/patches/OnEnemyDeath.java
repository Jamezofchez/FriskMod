package friskmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.powers.AbstractPower;
import friskmod.FriskMod;
import friskmod.actions.KillEnemyAction;
import friskmod.monsters.AbstractDummy;
import friskmod.powers.SoulBound;
import friskmod.util.Wiz;
import javassist.CtBehavior;

@SpirePatch(clz = AbstractMonster.class, method = "die", paramtypez = {boolean.class})
public class OnEnemyDeath {
  @SpireInsertPatch(locator = Locator.class)
  public static void triggerOnDeathPowers(AbstractMonster __instance, boolean triggerRelics) {
    for (AbstractMonster t : Wiz.getMonsters()) {
      for (AbstractPower r : t.powers) {
        if (r instanceof SoulBound)
          ((SoulBound)r).onMonsterDeath(__instance);
      } 
    } 
    boolean allMinion = true;
    for (AbstractMonster m : (AbstractDungeon.getMonsters()).monsters) {
      if (!m.isDeadOrEscaped() && !m.hasPower("Minion")) {
        allMinion = false;
        break;
      } 
    } 
    if (allMinion)
      for (AbstractMonster m : (AbstractDungeon.getMonsters()).monsters) {
        if (!m.isDeadOrEscaped() && m instanceof AbstractDummy)
          Wiz.atb(new KillEnemyAction(m));
      }  
  }
  
  private static class Locator extends SpireInsertLocator {
    public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
      Matcher.MethodCallMatcher methodCallMatcher = new Matcher.MethodCallMatcher(MonsterGroup.class, "areMonstersBasicallyDead");
      return LineFinder.findInOrder(ctMethodToPatch, (Matcher)methodCallMatcher);
    }
  }
}


/* Location:              C:\Program Files (x86)\Steam\steamapps\workshop\content\646570\3100352485\TheLocksmith.jar!\theAnime\patches\OnEnemyDeath.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */