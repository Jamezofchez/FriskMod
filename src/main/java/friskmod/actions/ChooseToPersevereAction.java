//package friskmod.actions;
//
//import com.evacipated.cardcrawl.mod.stslib.actions.common.SelectCardsInHandAction;
//import com.megacrit.cardcrawl.actions.AbstractGameAction;
//import com.megacrit.cardcrawl.actions.common.DrawCardAction;
//import com.megacrit.cardcrawl.cards.AbstractCard;
//import com.megacrit.cardcrawl.characters.AbstractPlayer;
//import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
//import friskmod.patches.CardXPFields;
//
//import java.util.List;
//import java.util.function.Consumer;
//
//public class ChooseToPersevereAction extends AbstractGameAction {
//
//    private int persevereAmt;
//
//    public ChooseToPersevereAction(int persevereAmt) {
//        this.persevereAmt = persevereAmt;
//    }
//
//    @Override
//    public void update() {
//        this.isDone = true;
//        addToTop(new SelectCardsInHandAction(magicNumber, selectionText, true, true, (x -> true), SacrificeCard()));
//    }
//    private Consumer<List<AbstractCard>> ChooseToPersevere(){
//        return (List<AbstractCard> cardList) -> {
//            for (AbstractCard c: cardList) {
//                AbstractPlayer p = AbstractDungeon.player;
//                int xp = CardXPFields.getCardXP(c);
//                addToBot(new DrawCardAction(p, xp));
//                p.hand.moveToExhaustPile(c);
//            }
//        };
//    }
//}
