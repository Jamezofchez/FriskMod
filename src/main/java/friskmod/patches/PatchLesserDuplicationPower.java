package friskmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.DuplicationPower;
import friskmod.powers.LesserDuplication;

@SpirePatch(clz = DuplicationPower.class, method = "onUseCard")
public class PatchLesserDuplicationPower {
  public static SpireReturn<Void> Prefix(DuplicationPower __instance, AbstractCard card, UseCardAction action) {
      AbstractPower posspow = AbstractDungeon.player.getPower(LesserDuplication.POWER_ID);
      if (posspow != null) {
          int maxCardCost = ((LesserDuplication) posspow).amount2;
          if (card.costForTurn < maxCardCost) {
              SpireReturn.Return();
          }
      }
      return SpireReturn.Continue();
  }
}


/* Location:              C:\Program Files (x86)\Steam\steamapps\workshop\content\646570\2371967829\Reliquary.jar!\patches\PatchLesserDuplicationPower.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */