package friskmod.patches.perseverance;

import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import friskmod.cards.BreakFree;

@SpirePatch(
        clz = AbstractCard.class,
        method = SpirePatch.CLASS
)
public class PerseveranceFields {
    public static SpireField<Boolean> isPerseverable = new SpireField<>(()->false); //if card is currently perseverable
    public static SpireField<Boolean> insufficientEnergy = new SpireField<>(()->false); //if you don't have enough energy for a card
    public static SpireField<Integer> currentEnergy = new SpireField<>(()->0); //store the amount of energy so it can be restored later
    public static SpireField<Boolean> perseverePlayed = new SpireField<>(()->false); //if a card normally isn't playable, but was played due to perseverance
    public static SpireField<Boolean> overcomePlayed = new SpireField<>(()->false); //if a card normally isn't playable, but is playable due to perseverance specifically due to overcome
    public static SpireField<Boolean> dontTrap = new SpireField<>(()->false); //if a card shouldnt be trapped
    public static SpireField<Boolean> trapped = new SpireField<>(()->false);
    public static boolean setIsPerseverable(AbstractCard c, boolean value) {
        return setIsPerseverable(c, value, false);
    }
    public static boolean setIsPerseverable(AbstractCard c, boolean value, boolean fromOvercome) {
        if (c instanceof BreakFree){
            return true;
        }
        if (value) {
            if (PerseveranceFields.trapped.get(c)) {
                return false;
            }
            if (!fromOvercome){ //take priority over overcome
                PerseveranceFields.overcomePlayed.set(c, false);
            }
            c.superFlash(Color.VIOLET.cpy());
        }
        PerseveranceFields.isPerseverable.set(c, value);
        c.initializeDescription();
        return true;
    }
}

