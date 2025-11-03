package friskmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import friskmod.helper.SharedFunctions;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

import java.util.ArrayList;

import static friskmod.helper.GrillbysHelper.isCardCostAllowed;

public class LimitCardCostPatch {
    @SpirePatch2(clz = AbstractDungeon.class, method = SpirePatch.CONSTRUCTOR,
    paramtypez = {String.class, String.class, AbstractPlayer.class, ArrayList.class})
    public static class DungeonCheck {
        @SpireInstrumentPatch
        public static ExprEditor instrument() {
            return new ExprEditor() {
                @Override
                public void edit(MethodCall m) throws CannotCompileException {
                    // Look for calls to hasTag(CardTags.HEALING)
                    if (m.getClassName().equals(AbstractCard.class.getName()) && m.getMethodName().equals("hasTag")) {
                        m.replace(
                                "{ " +
                                        "  boolean original = $proceed($$);" +
                                        "  if ($1 == " + AbstractCard.CardTags.class.getName() + ".HEALING) {" +
                                        "    $_ = (original || " + SharedFunctions.class.getName() + ".isCardCostAllowed($0));" +
                                        "  } else {" +
                                        "    $_ = original;" +
                                        "  }" +
                                        "}"
                        );
                    }
                }
            };
        }
    }
    @SpirePatch2(clz = CardGroup.class, method = SpirePatch.CONSTRUCTOR, paramtypez = {CardGroup.class, CardGroup.CardGroupType.class})
    @SpirePatch2(clz = CardGroup.class, method = SpirePatch.CONSTRUCTOR, paramtypez = {CardGroup.CardGroupType.class})
    public static class RestrictZeroCostCardsPatch {
        @SpirePrefixPatch
        public static void replaceGroup(CardGroup __instance) {
            __instance.group = new FilteredCardList();
        }

        // Inner class that enforces the rule
        public static class FilteredCardList extends ArrayList<AbstractCard> {
            @Override
            public boolean add(AbstractCard card) {
                if (isCardCostAllowed(card)) {
                    return super.add(card);
                }
                return true;
            }

            @Override
            public void add(int index, AbstractCard card) {
                if (isCardCostAllowed(card)) {
                    super.add(index, card);
                }
            }

            @Override
            public boolean addAll(java.util.Collection<? extends AbstractCard> c) {
                boolean changed = false;
                for (AbstractCard card : c) {
                    changed |= this.add(card);
                }
                return changed;
            }
        }
    }
}
