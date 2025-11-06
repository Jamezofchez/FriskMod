package friskmod.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import friskmod.FriskMod;

public class GladDummy extends BasePower {
    public static final String POWER_ID = FriskMod.makeID(GladDummy.class.getSimpleName());
    private static final PowerType TYPE = PowerType.BUFF;
    private static final boolean TURN_BASED = false;
    private static final UIStrings UI_STRINGS = CardCrawlGame.languagePack.getUIString(FriskMod.makeID(SoulBound.class.getSimpleName()));



    //The only thing TURN_BASED controls is the color of the number on the power icon.
    //Turn based powers are white, non-turn based powers are red or green depending on if their amount is positive or negative.
    //For a power to actually decrease/go away on its own they do it themselves.
    //Look at powers that do this like VulnerablePower and DoubleTapPower.
    AbstractCreature boundTarget;
    public GladDummy(AbstractCreature owner, AbstractCreature boundTarget) {
        super(POWER_ID, TYPE, TURN_BASED, owner, -1);
        this.boundTarget = boundTarget;
    }
    @Override
    public AbstractPower makeCopy() {
        return new GladDummy(owner, boundTarget);
    }


    public void onPlayerApplyPower(AbstractPower powerToApply) {
        if (powerToApply instanceof CloneablePowerInterface) {
            flash();
            AbstractPower copy = ((CloneablePowerInterface) powerToApply).makeCopy();
            copy.owner = this.boundTarget;
            addToBot(new ApplyPowerAction(this.boundTarget, this.owner, copy));
        }
    }
    public void onGainBlock(int amount) {
        addToBot(new GainBlockAction(this.boundTarget, this.owner, amount));
    }
    public void onGainTempHP(int amount) {
        addToBot(new AddTemporaryHPAction(this.boundTarget, this.owner, amount));
    }

    public void updateDescription() {
        String name;
        if (boundTarget == null || boundTarget.name == null) {
            name = UI_STRINGS.TEXT[0];
        } else{
            name = boundTarget.name;
        }
        this.description = String.format(DESCRIPTIONS[0], name);
    }
    @Override
    public void flash() { //disable sound flashing
        super.flashWithoutSound();
    }
}