package friskmod.powers;

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.powers.AbstractPower;
import friskmod.FriskMod;
import friskmod.util.Wiz;

public class Betrayed extends BasePower {
    public static final String POWER_ID = FriskMod.makeID(Betrayed.class.getSimpleName());
    private static final AbstractPower.PowerType TYPE = AbstractPower.PowerType.BUFF;
    private static final boolean TURN_BASED = false;

    private final CountdownFlee fleePower;


    //The only thing TURN_BASED controls is the color of the number on the power icon.
    //Turn based powers are white, non-turn based powers are red or green depending on if their amount is positive or negative.
    //For a power to actually decrease/go away on its own they do it themselves.
    //Look at powers that do this like VulnerablePower and DoubleTapPower.
    public Betrayed(AbstractCreature owner, CountdownFlee fleePower) {
        super(POWER_ID, TYPE, TURN_BASED, owner, -1);
        this.fleePower = fleePower;
    }

    @Override
    public AbstractPower makeCopy() {
        return new Betrayed(owner, fleePower);
    }

    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }

    @Override
    public void onRemove(){
        CardCrawlGame.music.fadeOutTempBGM();
        fleePower.hackyRemove();
    }

    @Override
    public void onDeath(){
        CardCrawlGame.music.fadeOutTempBGM();
        fleePower.hackyRemove();
    }

}