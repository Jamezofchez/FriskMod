package friskmod.powers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import friskmod.FriskMod;
import friskmod.actions.ConvertLVKarmaAction;

public class CountdownAgami extends AbstractCountdownPower {
    public static final String POWER_ID = FriskMod.makeID(CountdownAgami.class.getSimpleName());

    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public static final String NAME = powerStrings.NAME;

    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final PowerType TYPE = PowerType.DEBUFF;
    private static final boolean TURN_BASED = false;

    private final int DETONATION_AMOUNT;

    public CountdownAgami(AbstractCreature owner, int amount, int countdown, int upg_amount) {
        super(POWER_ID, TYPE, TURN_BASED, owner, amount, countdown);
        this.name = NAME;
        this.DETONATION_AMOUNT = upg_amount;
        updateDescription();
    }

    @Override
    public void onCountdownTrigger(boolean expire) {
        addToBot(new ConvertLVKarmaAction(owner, amount));
        super.onCountdownTrigger(expire);
    }

    @Override
    public AbstractPower makeCopy() {
        return new CountdownAgami(owner, amount, amount2, DETONATION_AMOUNT);
    }

    @Override
    public void updateDescription() {
        int descNum = getDescNum();
        String baseDescription = DESCRIPTIONS[descNum];
        if (descNum % 2 == 0) {
            this.description = String.format(baseDescription, amount2);
        } else{
            this.description = String.format(baseDescription, amount2, amount);
        }
    }

    private int getDescNum() {
        int descNum = 0;
        if (amount2 == 1){
            descNum += 2;
        }
        if (amount != 0){
            descNum += 1;
        }
        return descNum;
    }

    @Override
    public void upgrade(){
        super.upgrade();
        this.amount += DETONATION_AMOUNT;
        updateDescription();
    }
    @Override
    public void renderAmount(SpriteBatch sb, float x, float y, Color c) {
        int descNum = getDescNum();
        if (descNum % 2 == 0) {
            FontHelper.renderFontRightTopAligned(sb, FontHelper.powerAmountFont, Integer.toString(this.amount2), x, y * Settings.scale, this.fontScale, Color.CYAN.cpy());
        } else {
            super.renderAmount(sb, x, y, c);
        }
    }
}
