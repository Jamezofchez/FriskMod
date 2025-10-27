package friskmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import friskmod.actions.ResetDamageTypeAttack;
import friskmod.powers.FavouriteNumberPower;
import friskmod.powers.NonAttackPower;
import friskmod.util.Wiz;

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
                    Wiz.atb(new ResetDamageTypeAttack(card, (NonAttackPower) possNonAttackPower));
                }
                AbstractPower possFavouriteNumber = AbstractDungeon.player.getPower(FavouriteNumberPower.POWER_ID);
                if (possFavouriteNumber != null) {
                    possFavouriteNumber.onSpecificTrigger();
                }

            }
        }
    }
}
