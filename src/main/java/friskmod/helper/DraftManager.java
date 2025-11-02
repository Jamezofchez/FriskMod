package friskmod.helper;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;

import java.util.ArrayList;

public class DraftManager {
    public static AbstractCreature currentTarget = null;
    public static ArrayList<AbstractCard> lastChoices = null;
    public static void setDraftTarget(AbstractCreature newCurrentTarget) {
        currentTarget = newCurrentTarget;
    }
    public static void setLastChoices(ArrayList<AbstractCard> newLastChoices) {
        lastChoices = newLastChoices;
    }
}
