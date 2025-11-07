package friskmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.screens.*;
import com.megacrit.cardcrawl.screens.select.GridCardSelectScreen;
import com.megacrit.cardcrawl.screens.select.HandCardSelectScreen;
import friskmod.helper.GrillbysHelper;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Consumer;

import static friskmod.helper.GrillbysHelper.isCardCostAllowed;

public class LimitCardCostPatch {
    @SpirePatch2(clz = AbstractCard.class, method = "hasTag")
    public static class AvoidAddingHighCostPatch {
        public static boolean Postfix(AbstractCard __instance, boolean __result){
            return __result && isCardCostAllowed(__instance);
        }
    }
    @SpirePatch2(clz = CardGroup.class, method = SpirePatch.CONSTRUCTOR, paramtypez = {CardGroup.CardGroupType.class})
    @SpirePatch2(clz = CardGroup.class, method = SpirePatch.CONSTRUCTOR, paramtypez = {CardGroup.class, CardGroup.CardGroupType.class})
    public static class ReplaceCardGroupListPatch {

        @SpirePostfixPatch
        public static void Postfix(CardGroup __instance) {

            // If already filtered, skip
            if ( __instance.group instanceof FilteredCardList) {
                return;
            }

            // Create a new filtered list
            FilteredCardList filtered = new FilteredCardList();

            // Copy across only allowed cards from existing group (if any)
            if ( __instance.group != null) {
                for (AbstractCard card :  __instance.group) {
                    filtered.add(card);
                }
                __instance.group.clear();
            }

            // Replace the internal list
            __instance.group = filtered;
        }

        public static class FilteredCardList extends ArrayList<AbstractCard> {

            @Override
            public boolean add(AbstractCard card) {
                if (isCardCostAllowed(card)) {
                    return super.add(card);
                }
                return false;
            }

            @Override
            public void add(int index, AbstractCard card) {
                if (isCardCostAllowed(card)) {
                    super.add(index, card);
                }
            }

            @Override
            public boolean addAll(Collection<? extends AbstractCard> c) {
                boolean changed = false;
                for (AbstractCard card : c) {
                    if (isCardCostAllowed(card)) {
                        changed |= super.add(card);
                    }
                }
                return changed;
            }

            @Override
            public boolean addAll(int index, Collection<? extends AbstractCard> c) {
                if (c == null || c.isEmpty()) {
                    return false;
                }
                int inserted = 0;
                int pos = index;
                for (AbstractCard card : c) {
                    if (isCardCostAllowed(card)) {
                        super.add(pos++, card);
                        inserted++;
                    }
                }
                return inserted > 0;
            }
            @Override
            public boolean remove(Object o) {
                if (o instanceof AbstractCard && isCardCostAllowed((AbstractCard) o)) {
                    return super.remove(o);
                }
                return false;
            }

            @Override
            public AbstractCard remove(int index) {
                AbstractCard c = super.get(index);
                if (isCardCostAllowed(c)) {
                    return super.remove(index);
                }
                // Don't remove disallowed cards
                return c;
            }

            @Override
            public boolean removeAll(Collection<?> c) {
                boolean changed = false;
                for (Object o : c) {
                    if (o instanceof AbstractCard && isCardCostAllowed((AbstractCard) o)) {
                        changed |= super.remove(o);
                    }
                }
                return changed;
            }

            @Override
            public boolean retainAll(Collection<?> c) {
                // Only keep allowed and included cards
                return super.removeIf(card -> !isCardCostAllowed(card) || !c.contains(card));
            }

//
//            @Override
//            public AbstractCard set(int index, AbstractCard element) {
//                // Replacing should also respect the rule — but avoid instability
//                if (isCardCostAllowed(element)) {
//                    return super.set(index, element);
//                }
//                // If disallowed, keep the existing element
//                return super.get(index);
//            }

//            // Override iterator to skip invalid cards during iteration
//            @Override
//            public Iterator<AbstractCard> iterator() {
//                final Iterator<AbstractCard> it = super.iterator();
//                return new Iterator<AbstractCard>() {
//                    private AbstractCard nextOk = advance();
//                    private AbstractCard advance() {
//                        while (it.hasNext()) {
//                            AbstractCard c = it.next();
//                            if (isCardCostAllowed(c)) return c;
//                        }
//                        return null;
//                    }
//                    @Override
//                    public boolean hasNext() { return nextOk != null; }
//                    @Override
//                    public AbstractCard next() {
//                        if (nextOk == null) throw new NoSuchElementException();
//                        AbstractCard out = nextOk;
//                        nextOk = advance();
//                        return out;
//                    }
//                    @Override
//                    public void remove() {
//                        it.remove(); // behave like normal iterator
//                    }
//                };
//            }
//
//            @Override
//            public void forEach(Consumer<? super AbstractCard> action) {
//                // Respect the filter during forEach traversal
//                for (AbstractCard c : this) {
//                    if (isCardCostAllowed(c)) {
//                        action.accept(c);
//                    }
//                }
//            }
        }
    }
    public static class FilterLowCardsScreen{

        public static CardRewardScreen cardRewardScreen;
        public static MasterDeckViewScreen deckViewScreen;
        public static DiscardPileViewScreen discardPileViewScreen;
        public static DrawPileViewScreen gameDeckViewScreen;
        public static ExhaustPileViewScreen exhaustPileViewScreen;
        public static GridCardSelectScreen gridSelectScreen;
        public static HandCardSelectScreen handCardSelectScreen;

        @SpirePatch2(clz = GridCardSelectScreen.class, method = SpirePatch.CONSTRUCTOR)
        public static class ReplaceGridCardSelectSelectedCards {
            @SpireInstrumentPatch
            public static ExprEditor instrument() {
                return new ExprEditor() {
                    @Override
                    public void edit(javassist.expr.NewExpr e) throws CannotCompileException {
                        if (e.getClassName().equals(ArrayList.class.getName())) {
                            e.replace("{ $_ = new " + LimitCardCostPatch.ReplaceCardGroupListPatch.FilteredCardList.class.getName() + "(); }");
                        }
                    }
                };
            }
        }



    }
}
