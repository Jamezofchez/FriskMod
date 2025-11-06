package friskmod.patches.perseverance;

import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import friskmod.FriskMod;
import friskmod.powers.Overcome;
import friskmod.util.Wiz;

@SpirePatch(
        clz = AbstractCard.class,
        method = SpirePatch.CLASS
)
public class PerseveranceFields {
    public static SpireField<Boolean> isPerseverable = new SpireField<>(()->false); //if card is currently perseverable
    public static SpireField<Boolean> insufficientEnergy = new SpireField<>(()->false); //if you don't have enough energy for a card
    public static SpireField<Integer> cardEnergy = new SpireField<>(()->0); //store the amount of energy so it can be restored later
    public static SpireField<Boolean> perseverePlayed = new SpireField<>(()->false); //if a card normally isn't playable, but was played due to perseverance
    public static SpireField<Boolean> overcomePlayed = new SpireField<>(()->false); //if a card normally isn't playable, but is playable due to perseverance specifically due to overcome
    public static SpireField<Boolean> trapped = new SpireField<>(()->false);
    public static void setIsOvercomePlayed(AbstractCard c, boolean value) {
        PerseveranceFields.overcomePlayed.set(c, value);
    }
    public static void setIsPerseverable(AbstractCard c, boolean value) {
        setIsPerseverable(c, value, false);
    }
    public static void setIsPerseverable(AbstractCard c, boolean value, boolean fromOvercome) {
        if (value) {
            if (!fromOvercome){ //take priority over overcome
                PerseveranceFields.overcomePlayed.set(c, false);
            }
            c.superFlash(Color.VIOLET.cpy());
        } else{
            if (PerseveranceFields.overcomePlayed.get(c)) {
                PerseveranceFields.overcomePlayed.set(c, false);
                AbstractPower pow = AbstractDungeon.player.getPower(Overcome.POWER_ID);
                if (pow != null) {
                    pow.flash();
                    Wiz.atb(new ReducePowerAction(AbstractDungeon.player, AbstractDungeon.player, Overcome.POWER_ID, 1));
                } else {
                    FriskMod.logger.warn("{}: overcomePlayed set but no power found", FriskMod.modID);
                }
            }
        }
        PerseveranceFields.isPerseverable.set(c, value);
        c.initializeDescription();
    }
}

