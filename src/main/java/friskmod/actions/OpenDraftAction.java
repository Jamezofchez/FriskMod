package friskmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import friskmod.cards.choosecard.*;
import friskmod.helper.DraftManager;

import java.util.ArrayList;

import static friskmod.FriskMod.makeID;
import static friskmod.helper.DraftManager.setDraftTarget;
import static friskmod.helper.DraftManager.setLastChoices;

public class OpenDraftAction extends AbstractGameAction {
    public static final String ID = makeID(OpenDraftAction.class.getSimpleName()); //makeID adds the mod ID, so the final ID will be something like "modID:MyCard"
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(makeID(OpenDraftAction.class.getSimpleName()));
    private static final String[] TEXT = uiStrings.TEXT;
    private AbstractCreature target;
    private DreamType type;
    private int selectionAmount;

    static final ArrayList<AbstractCard> possDreamChoices;
    static final ArrayList<AbstractCard> possNightmareChoices;

    public enum DreamType {DREAM, NIGHTMARE}


    static {
        possDreamChoices = new ArrayList<>();
        possDreamChoices.add(new DreamOfArtifact());
        possDreamChoices.add(new DreamOfBlur());
        possDreamChoices.add(new DreamOfDetermination());
        possDreamChoices.add(new DreamOfDexterity());
        possDreamChoices.add(new DreamOfEvolve());
        possDreamChoices.add(new DreamOfLV());
        possDreamChoices.add(new DreamOfStrength());

        possNightmareChoices = new ArrayList<>();
        possNightmareChoices.add(new NightmareOfFrail());
        possNightmareChoices.add(new NightmareOfNEO());
        possNightmareChoices.add(new NightmareOfKarma());
        possNightmareChoices.add(new NightmareOfMercied());
        possNightmareChoices.add(new NightmareOfAttack());
        possNightmareChoices.add(new NightmareOfVulnerable());
        possNightmareChoices.add(new NightmareOfWeak());
    }

    public OpenDraftAction(AbstractCreature target, DreamType type, int selectionAmount){
        this.target = target;
        this.type = type;
        this.selectionAmount = selectionAmount;
    }

    @Override
    public void update() {
        this.isDone = true;
        ArrayList<AbstractCard> posschoices;
        if (type == DreamType.DREAM) {
            posschoices = possDreamChoices;
        } else {
            posschoices = possNightmareChoices;
        }
        ArrayList<AbstractCard> choices;
        if (DraftManager.lastChoices != null) {
            choices = DraftManager.lastChoices;
        } else {
            choices = new ArrayList<>();
            for (int i = 0; i < selectionAmount; i++) {
                boolean reroll;
                AbstractCard card;
                do {
                    reroll = false;
                    int randomIndex = AbstractDungeon.cardRng.random(posschoices.size() - 1);
                    card = posschoices.get(randomIndex);
                    for (AbstractCard c : choices) {
                        if (c.cardID.equals(card.cardID)) {
                            reroll = true;
                        }
                    }
                } while (reroll);
                choices.add(card.makeStatEquivalentCopy());
            }
        }
        setDraftTarget(target);
        setLastChoices(choices);
        for (AbstractCard c : choices) {
            c.initializeDescription();
        }
        if (choices.size() <= 1) {
            if (choices.isEmpty()) {
                return;
            }
            ((AbstractDreamNightmareCard) choices.get(0)).chooseOption();
        } else {
            AbstractDungeon.cardRewardScreen.chooseOneOpen(choices); //making a skip both drafts seems hard and icba
        }
    }
    private String getSelectionText(DreamType type, AbstractCreature target) {
        String basename = TEXT[0];
        String descriptor = TEXT[1];
        if (type == DreamType.NIGHTMARE) {
            descriptor = TEXT[2];
        }
        String targetName = TEXT[3];
        if (target instanceof AbstractMonster) {
            targetName = TEXT[4];
        }
        return String.format(basename, descriptor, targetName);
    }
}
