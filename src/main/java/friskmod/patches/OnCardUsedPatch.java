package friskmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import friskmod.FriskMod;
import friskmod.actions.ResetDamageTypeAttack;
import friskmod.cards.AbstractCriticalCard;
import friskmod.cards.AnnoyingDog;
import friskmod.cards.BreakFree;
import friskmod.patches.perseverance.PerseveranceFields;
import friskmod.powers.FavouriteNumberPower;
import friskmod.powers.NonAttackPower;
import friskmod.util.Wiz;

import java.util.List;

import static friskmod.FriskMod.logger;

public class OnCardUsedPatch {
    @SpirePatch(
            clz = UseCardAction.class,
            method = SpirePatch.CONSTRUCTOR,
            paramtypez = { AbstractCard.class, AbstractCreature.class }
    )
    public static class OnCardUsed {
        @SpirePostfixPatch
        public static void Postfix(UseCardAction __instance, AbstractCard card, AbstractCreature target) {
            if (!card.dontTriggerOnUseCard){
                AbstractPower possNonAttackPower = AbstractDungeon.player.getPower(NonAttackPower.POWER_ID);
                if (possNonAttackPower != null) {
                    Wiz.atb(new ResetDamageTypeAttack((NonAttackPower) possNonAttackPower));
                }
                AbstractPower possFavouriteNumber = AbstractDungeon.player.getPower(FavouriteNumberPower.POWER_ID);
                if (possFavouriteNumber != null) {
                    possFavouriteNumber.onSpecificTrigger();
                }
                if (card.cardID.equals(AnnoyingDog.ID)){
                    PerseveranceFields.trapped.set(card, true);
                    card.initializeDescription();
                }
                List<AbstractCard> lastList = (List<AbstractCard>) AbstractDungeon.player.hand.group.clone();
                for (AbstractCard possAnnoyingDog : lastList) {
                    if (possAnnoyingDog.cardID.equals(AnnoyingDog.ID)) {
                        int justUsedCardIndex = lastList.indexOf(card);
                        if (justUsedCardIndex == -1){
                            logger.warn("{}: Couldn't find just used card for critical calculation", FriskMod.modID);
                        } else {
                            if (((AbstractCriticalCard) possAnnoyingDog).isCriticalPos(justUsedCardIndex) || PerseveranceFields.isPerseverable.get(possAnnoyingDog)) {
                                PerseveranceFields.trapped.set(possAnnoyingDog, false);
                            } else {
                                if (!card.cardID.equals(BreakFree.ID)) {
                                    PerseveranceFields.trapped.set(possAnnoyingDog, true);
                                }
                            }
                            possAnnoyingDog.initializeDescription();
                        }
                    }
                }
            }
        }
    }
}
