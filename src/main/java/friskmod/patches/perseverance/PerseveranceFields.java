package friskmod.patches.perseverance.occult;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;

@SpirePatch(
        clz = AbstractCard.class,
        method = SpirePatch.CLASS
)
public class PerseveranceFields {
    public static SpireField<Boolean> isPerseverable = new SpireField<>(()->false); //if card is always playable
    public static SpireField<Boolean> insufficientEnergy = new SpireField<>(()->false); //if you don't have enough energy for a card
    public static SpireField<Boolean> perseverePlayed = new SpireField<>(()->false); //if a card normally isn't playable, but is playable due to occult
}
