package friskmod.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
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

    private final int UPG_LV_GAIN;

    public CountdownAgami(AbstractCreature owner, int amount, int countdown, int upg_amount) {
        super(POWER_ID, TYPE, TURN_BASED, owner, amount, countdown);
        this.name = NAME;
        this.UPG_LV_GAIN = upg_amount;
        updateDescription();
    }

    @Override
    public void onCountdownTrigger(boolean expire) {
        addToBot(new ConvertLVKarmaAction(owner, amount));
        super.onCountdownTrigger(expire);
    }

    @Override
    public AbstractPower makeCopy() {
        return new CountdownAgami(owner, amount, amount2, UPG_LV_GAIN);
    }

    @Override
    public void updateDescription() {
        int descNum = 0;
        if (amount2 == 1){
            descNum += 2;
        }
        if (amount != 0){
            descNum += 1;
        }
        String baseDescription = DESCRIPTIONS[descNum];
        if (descNum % 2 == 0) {
            this.description = String.format(baseDescription, amount2);
        } else{
            this.description = String.format(baseDescription, amount2, amount);
        }
    }
    @Override
    public void upgrade(){
        super.upgrade();
        this.amount += UPG_LV_GAIN;
        updateDescription();
    }
}
