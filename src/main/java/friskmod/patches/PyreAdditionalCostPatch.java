package friskmod.patches;

import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import friskmod.cardmods.PyreMod;
import friskmod.cards.OnPyreCard;
import com.evacipated.cardcrawl.mod.stslib.actions.common.SelectCardsInHandAction;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import friskmod.util.Wiz;

import static friskmod.FriskMod.makeID;

@SpirePatch(clz = AbstractPlayer.class, method = "useCard")
public class PyreAdditionalCostPatch {
    private static UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(makeID(PyreAdditionalCostPatch.class.getSimpleName()));

    public static void Postfix(AbstractPlayer p, AbstractCard c, AbstractMonster m, int energyonuse) {
        if (CardModifierManager.hasModifier(c, PyreMod.ID)) {
            for (AbstractCardModifier r : CardModifierManager.getModifiers(c, PyreMod.ID)) {
                if (r instanceof PyreMod) {

                    Wiz.att(new SelectCardsInHandAction(uiStrings.TEXT[0], (cards) -> {
                        if (c instanceof OnPyreCard)
                            ((OnPyreCard) c).onPyred(cards.get(0));
                        Wiz.att(new ExhaustSpecificCardAction(cards.get(0), AbstractDungeon.player.hand));
                    }));

                }
            }
        }
    }
}