package friskmod.cards.choosecard;

public abstract class AbstractNightmareCard extends AbstractDreamNightmareCard {
    public AbstractNightmareCard(String ID) {
        super(ID);
        this.name = getCardName();
    }
}
