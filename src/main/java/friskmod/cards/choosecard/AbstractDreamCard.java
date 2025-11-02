package friskmod.cards.choosecard;

public abstract class AbstractDreamCard extends AbstractDreamNightmareCard {
    public AbstractDreamCard(String ID) {
        super(ID);
        this.name = getCardName();
    }
}
