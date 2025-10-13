//package friskmod.actions;
//
//import com.megacrit.cardcrawl.actions.AbstractGameAction;
//import com.megacrit.cardcrawl.cards.AbstractCard;
//import com.megacrit.cardcrawl.core.AbstractCreature;
//import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
//import com.megacrit.cardcrawl.powers.AbstractPower;
//import friskmod.powers.AfterCardPlayedInterface;
//
//public class CardUseAction extends AbstractGameAction {
//    private final AbstractCard card;
//
//    public CardUseAction(AbstractCard abstractCard) {
//        this.card = abstractCard;
//    }
//
//    @Override
//    public void update() {
//        publishOnUseCard();
//        isDone = true;
//    }
//    private void publishOnUseCard() {
//        for (AbstractCreature m : (AbstractDungeon.getMonsters()).monsters) {
//            for (AbstractPower p : m.powers) {
//                if (p instanceof AfterCardPlayedInterface)
//                    ((AfterCardPlayedInterface) p).onCardPlayed(card);
//            }
//        }
//        for (AbstractPower p : AbstractDungeon.player.powers) {
//            if (p instanceof AfterCardPlayedInterface)
//                ((AfterCardPlayedInterface) p).onCardPlayed(card);
//        }
//    }
//}
