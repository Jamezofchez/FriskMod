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
                                        "    $_ = (original || " + GrillbysHelper.class.getName() + ".isCardCostAllowed($0));" +
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
    public static class RestrictLowCostCardsPatch {
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

            @Override
            public boolean addAll(int index, java.util.Collection<? extends AbstractCard> c) {
                if (c == null || c.isEmpty()) {
                    return false;
                }
                int inserted = 0;
                int pos = index;
                for (AbstractCard card : c) {
                    if (isCardCostAllowed(card)) {
                        super.add(pos, card);
                        pos++;
                        inserted++;
                    }
                }
                return inserted > 0;
            }
//
//            @Override
//            public AbstractCard set(int index, AbstractCard element) {
//                if (isCardCostAllowed(element)) {
//                    return super.set(index, element);
//                }
//                return super.get(index);
//            }
//
//            @Override
//            public boolean remove(Object o) {
//                if (o instanceof AbstractCard) {
//                    AbstractCard card = (AbstractCard) o;
//                    if (isCardCostAllowed(card)) {
//                        return super.remove(o);
//                    }
//                    return false;
//                }
//                return super.remove(o);
//            }

            @Override
            public java.util.Iterator<AbstractCard> iterator() {
                final java.util.Iterator<AbstractCard> it = super.iterator();
                return new java.util.Iterator<AbstractCard>() {
                    private AbstractCard nextOk = advance();
                    private AbstractCard advance() {
                        while (it.hasNext()) {
                            AbstractCard c = it.next();
                            if (isCardCostAllowed(c)) return c;
                        }
                        return null;
                    }
                    @Override
                    public boolean hasNext() {
                        return nextOk != null;
                    }
                    @Override
                    public AbstractCard next() {
                        if (nextOk == null) throw new java.util.NoSuchElementException();
                        AbstractCard out = nextOk;
                        nextOk = advance();
                        return out;
                    }
                };
            }

            @Override
            public void forEach(java.util.function.Consumer<? super AbstractCard> action) {
                // Ensure forEach respects the filter as well
                for (AbstractCard c : this) {
                    action.accept(c);
                }
            }
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

        //public void open(CardGroup group, int numCards, String tipMsg, boolean forUpgrade, boolean forTransform, boolean canCancel, boolean forPurge) {
        @SpirePatch2(
            clz = GridCardSelectScreen.class,
            method = "open",
            paramtypez = {CardGroup.class, int.class, String.class, boolean.class, boolean.class, boolean.class, boolean.class}
        )
        public static class GridCardSelectScreenOpenPatch{
            @SpirePrefixPatch
            public static void Prefix(GridCardSelectScreen __instance, CardGroup group) {
                if (group == null || group.group == null) return;
                CardGroup filtered = new CardGroup(group, group.type);
                filtered.group.clear();
                for (AbstractCard c : group.group) {
                    if (isCardCostAllowed(c)){
                        filtered.group.add(c);
                }
                }
                group.group = filtered.group;
            }
        }
    }
}
