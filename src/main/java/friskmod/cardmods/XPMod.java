package friskmod.cardmods;

import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import friskmod.FriskMod;
import friskmod.patches.CardXPFields;
import friskmod.util.TexLoader;
import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.mod.stslib.util.extraicons.ExtraIcons;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import friskmod.util.Wiz;

import static friskmod.FriskMod.imagePath;


public class XPMod
        extends AbstractCardModifier {
    public static String ID = FriskMod.makeID(XPMod.class.getSimpleName());
    private static final Texture xptex = TexLoader.getTexture(imagePath("icons/XP.png"));
    int defaultXP = 0;

    public XPMod() {
        this.priority = -2;
    }

    public XPMod(int defaultXP) {
        this.priority = -2;
        this.defaultXP = defaultXP;
    }


    public void onInitialApplication(AbstractCard card) {
        super.onInitialApplication(card);
        if (this.defaultXP != 0) {
            CardXPFields.addXP(card, this.defaultXP);
        }
    }

//    public void onUse(AbstractCard card, AbstractCreature target, UseCardAction action) {
//        if (card.type == AbstractCard.CardType.ATTACK){
//            AbstractPower LV_enemy_power = target.getPower(LV_Enemy.POWER_ID);
//            int LV_transfer_from = 0;
//            if (LV_enemy_power != null) {
//                int LV_Enemy = LV_enemy_power.amount;
//                LV_transfer_from = LV_Enemy - 1;
//            }
//            int xp = CardXPFields.getCardXP(card);
//            int LV_transfer_to = 0;
//            if (xp > 0) {
//                LV_transfer_to = xp - 1;
//            }
//            //transfer player LV to XP
//            //upon card played
//
//            if (xp >= 1) {
//                //transfer XP to LV
//                //remove xp from card
//                //remove lv from enemy
//                //add lv to enemy
//                //add xp to card
//                this.addToBot(new ApplyPowerAction(target, AbstractDungeon.player, new LV_Enemy(target,LV_transfer_to), LV_transfer_to));
//
//            }
//            if (LV_enemy_power != null){
//                //transfer LV to XP
//                this.addToTop(new RemoveSpecificPowerAction(target, AbstractDungeon.player, LV_enemy_power.ID));
//                CardXPFields.addXP(card, LV_transfer_from);
//            }
//        }
//    }

    public boolean shouldApply(AbstractCard card) {
        if (CardModifierManager.hasModifier(card, ID)) {
            card.initializeDescription();
            return false;
        }
        return true;
    }

    public void onRender(AbstractCard card, SpriteBatch sb) {
        int xp = CardXPFields.getCardXP(card);
        if (xp >= 1)
            ExtraIcons.icon(xptex).text(String.valueOf(xp)).render(card);
    }


    public void onSingleCardViewRender(AbstractCard card, SpriteBatch sb) {
        int xp = CardXPFields.getCardXP(card);
        if (xp >= 1) {
            ExtraIcons.icon(xptex).text(String.valueOf(xp)).render(card);
        }
    }

    public String identifier(AbstractCard card) {
        return ID;
    }


    public boolean isInherent(AbstractCard card) {
        return true;
    }


    public AbstractCardModifier makeCopy() {
        return new XPMod();
    }

}


/* Location:              C:\Program Files (x86)\Steam\steamapps\workshop\content\646570\3390561252\friskmod.jar!\friskmod\cardmods\TemperatureMod.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */