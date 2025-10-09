package friskmod.powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.ThoughtBubble;
import friskmod.FriskMod;

public class CountdownPouncePower extends AbstractCountdownPower {
    public static final String POWER_ID = FriskMod.makeID(CountdownPouncePower.class.getSimpleName());
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(POWER_ID);
    public static final String[] TEXT = uiStrings.TEXT;
    private static final PowerType TYPE = PowerType.DEBUFF;
    private static final boolean TURN_BASED = false;

    public CountdownPouncePower(AbstractCreature owner, int amount, int countdown) {
        super(POWER_ID, TYPE, TURN_BASED, owner, amount, countdown);
    }

    @Override
    public void onCountdownTrigger(boolean expire) {
        if (owner != null && (owner instanceof AbstractMonster) && !(((AbstractMonster )owner).getIntentBaseDmg() >= 0)) {
            addToBot(new DamageAction(owner, new DamageInfo(AbstractDungeon.player, amount, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
        } else {
            AbstractDungeon.effectList.add(new ThoughtBubble(AbstractDungeon.player.dialogX, AbstractDungeon.player.dialogY, 3.0F, TEXT[0], true));
        }
        super.onCountdownTrigger(expire);
    }

    @Override
    public AbstractPower makeCopy() {
        return new CountdownDrawPower(owner, amount, amount2);
    }

    @Override
    public void updateDescription() {
        String baseDescription = DESCRIPTIONS[0];
        this.description = String.format(baseDescription, amount);
    }
}
