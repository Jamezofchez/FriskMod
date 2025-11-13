package friskmod.powers;

import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import friskmod.FriskMod;

public class MadDummy extends BasePower {
    public static final String POWER_ID = FriskMod.makeID(MadDummy.class.getSimpleName());
    private static final PowerType TYPE = PowerType.BUFF;
    private static final boolean TURN_BASED = false;
    private static final UIStrings UI_STRINGS = CardCrawlGame.languagePack.getUIString(FriskMod.makeID(SoulBound.class.getSimpleName()));



    //The only thing TURN_BASED controls is the color of the number on the power icon.
    //Turn based powers are white, non-turn based powers are red or green depending on if their amount is positive or negative.
    //For a power to actually decrease/go away on its own they do it themselves.
    //Look at powers that do this like VulnerablePower and DoubleTapPower.
    AbstractCreature boundTarget;
    public MadDummy(AbstractCreature owner, AbstractCreature boundTarget) {
        super(POWER_ID, TYPE, TURN_BASED, owner, -1);
        this.boundTarget = boundTarget;
        updateDescription();
    }
    @Override
    public AbstractPower makeCopy() {
        return new MadDummy(owner, boundTarget);
    }

    public void onLoseHpMonster(int damageAmount) {
        if (damageAmount > 0) {
            flash();
            addToBot(new DamageAction(this.boundTarget, new DamageInfo(this.boundTarget, damageAmount, DamageInfo.DamageType.HP_LOSS)));
        }
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