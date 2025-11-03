package friskmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.city.SphericGuardian;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.InvinciblePower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.ThoughtBubble;
import friskmod.helper.StealableWhitelist;
import friskmod.patches.InherentPowerTagFields;
import friskmod.powers.AfterCardPlayedInterface;
import friskmod.util.Wiz;

import static friskmod.FriskMod.makeID;
import static friskmod.helper.SharedFunctions.isInvincible;

public class OnBattleStartAction extends AbstractGameAction {
    public static final String ID = makeID(OnBattleStartAction.class.getSimpleName());
    private static final UIStrings UI_STRINGS = CardCrawlGame.languagePack.getUIString(ID);
    private final AbstractRoom room;


    public OnBattleStartAction(AbstractRoom room) {
        this.room = room;
        this.duration = Settings.ACTION_DUR_FAST;
    }

    @Override
    public void update() {
        publishOnBattleStart();
        isDone = true;
    }
    private void publishOnBattleStart() {
        Wiz.att(new ResetDraftAction());
        for (AbstractMonster m : (AbstractDungeon.getMonsters()).monsters) {
            if (m instanceof SphericGuardian){
                AbstractDungeon.effectList.add(new ThoughtBubble(AbstractDungeon.player.dialogX, AbstractDungeon.player.dialogY, 3.0F, UI_STRINGS.TEXT[0], true));
            }
            if (isInvincible(m)){
                AbstractDungeon.effectList.add(new ThoughtBubble(AbstractDungeon.player.dialogX, AbstractDungeon.player.dialogY, 3.0F, UI_STRINGS.TEXT[1], true));
            }
            for (AbstractPower p : m.powers){
                InherentPowerTagFields.inherentPowerFields.inherentPower.set(p, true);
                InherentPowerTagFields.inherentPowerFields.inherentPowerAmount.set(p, p.amount);
            }
        }
    }
}
