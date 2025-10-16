package friskmod.patches.perseverance;

import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;

@SpirePatch(
        clz = AbstractCard.class,
        method = SpirePatch.CLASS
)
public class PerseveranceFields {
    public static SpireField<Boolean> isPerseverable = new SpireField<>(()->false); //if card is always playable
    public static SpireField<Boolean> isPerseverableFromOvercome = new SpireField<>(()->false); //if card is always playable from overcome
    public static SpireField<Boolean> insufficientEnergy = new SpireField<>(()->false); //if you don't have enough energy for a card
    public static SpireField<Integer> currentEnergy = new SpireField<>(()->0); //store the amount of energy so it can be restored later
    public static SpireField<Boolean> perseverePlayed = new SpireField<>(()->false); //if a card normally isn't playable, but is playable due to perseverance
    public static SpireField<Boolean> trapped = new SpireField<>(()->false);
    public static void setIsPerseverable(AbstractCard c, boolean value) {
        setIsPerseverable(c, value, false);
    }
    public static void setIsPerseverable(AbstractCard c, boolean value, boolean fromOvercome) {
        PerseveranceFields.isPerseverable.set(c, value);
        if (value) {
            c.superFlash(Color.VIOLET.cpy());
            if (fromOvercome) {
                PerseveranceFields.isPerseverableFromOvercome.set(c, true);
            } else {
                PerseveranceFields.isPerseverableFromOvercome.set(c, false);
            }
        } else{
            PerseveranceFields.isPerseverableFromOvercome.set(c, false);
        }
        c.initializeDescription();
    }
}

