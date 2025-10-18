package friskmod.powers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import friskmod.FriskMod;
import friskmod.actions.GainGoldAction;
import friskmod.actions.SmokeBombAction;

public class CountdownFlee extends AbstractCountdownPower {
    public static final String POWER_ID = FriskMod.makeID(CountdownFlee.class.getSimpleName());

    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public static final String NAME = powerStrings.NAME;

    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final PowerType TYPE = PowerType.BUFF;
    private static final boolean TURN_BASED = false;

    private boolean upgraded;

    public CountdownFlee(AbstractCreature owner, int amount, int countdown, boolean upgraded) {
        super(POWER_ID, TYPE, TURN_BASED, owner, amount, countdown); //not unique
        this.name = NAME;
        updateDescription();
        this.upgraded = upgraded;
    }

    @Override
    public void onCountdownTrigger(boolean expire) {
        if (expire) {
            if (this.upgraded) {
                int increaseGold = 0;
                for (AbstractMonster mo : (AbstractDungeon.getCurrRoom()).monsters.monsters) {
                    if (!mo.isDead && !mo.escaped) {
                        if (AbstractDungeon.getCurrRoom() instanceof com.megacrit.cardcrawl.rooms.MonsterRoomElite) {
                            increaseGold = AbstractDungeon.treasureRng.random(25, 35) / (AbstractDungeon.getCurrRoom()).monsters.monsters.size();
                        } else if (AbstractDungeon.getCurrRoom() instanceof com.megacrit.cardcrawl.rooms.MonsterRoom) {
                            increaseGold = AbstractDungeon.treasureRng.random(10, 20) / (AbstractDungeon.getCurrRoom()).monsters.monsters.size();
                        }
                        AbstractDungeon.actionManager.addToBottom(new GainGoldAction(mo, increaseGold));
                    }
                }
            }
            AbstractDungeon.actionManager.addToBottom(new WaitAction(0.5F));
            AbstractDungeon.actionManager.addToBottom(new SmokeBombAction(this.upgraded));
        }
        super.onCountdownTrigger(expire);
    }

    @Override
    public AbstractPower makeCopy() {
        return new CountdownFlee(owner, amount, amount2, upgraded);
    }

    @Override
    public void stackPower(int stackAmount) {
        this.fontScale = 8.0F;
        this.amount2 += stackAmount;
    }

    @Override
    public void updateDescription() {
        int descNum = 0;
        if (amount2 == 1){
            descNum += 1;
        }
        String baseDescription = DESCRIPTIONS[descNum];
        this.description = String.format(baseDescription, amount2);
    }
    @Override
    public void upgrade(){
        super.upgrade();
        this.upgraded = true;
        updateDescription();
    }
    @Override
    public void renderAmount(SpriteBatch sb, float x, float y, Color c) {
        FontHelper.renderFontRightTopAligned(sb, FontHelper.powerAmountFont, Integer.toString(this.amount2), x, y * Settings.scale, this.fontScale, Color.CYAN.cpy());
    }
}
