package friskmod.powers;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import friskmod.FriskMod;

public class CountdownDraw extends AbstractCountdownPower {
    public static final String POWER_ID = FriskMod.makeID(CountdownDraw.class.getSimpleName());

    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public static final String NAME = powerStrings.NAME;

    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final PowerType TYPE = PowerType.BUFF;
    private static final boolean TURN_BASED = false;

    private final int UPG_CARD_DRAW;

    public CountdownDraw(AbstractCreature owner, int amount, int countdown, int upg_amount) {
        super(POWER_ID, TYPE, TURN_BASED, owner, amount, countdown);
        this.name = NAME;
        this.UPG_CARD_DRAW = upg_amount;
        updateDescription();
    }

    @Override
    public void onCountdownTrigger(boolean expire) {
        addToBot(new DrawCardAction(owner, amount));
        super.onCountdownTrigger(expire);
    }

    @Override
    public AbstractPower makeCopy() {
        return new CountdownDraw(owner, amount, amount2, UPG_CARD_DRAW);
    }

    @Override
    public void updateDescription() {
        int descNum = getDescNum();
        String baseDescription = DESCRIPTIONS[descNum];
        this.description = String.format(baseDescription, amount2, amount);
    }

    private int getDescNum() {
        int descNum = 0;
        if (amount2 == 1){
            descNum += 2;
        }
        if (amount == 1){
            descNum += 1;
        }
        return descNum;
    }

    @Override
    public void upgrade(){
        super.upgrade();
        this.amount += UPG_CARD_DRAW;
        updateDescription();
    }
}
