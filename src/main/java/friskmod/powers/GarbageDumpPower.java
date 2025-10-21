package friskmod.powers;

import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import friskmod.FriskMod;
import friskmod.util.Wiz;

public class GarbageDumpPower extends BasePower implements WastedEnergyInterface {
    public static final String POWER_ID = FriskMod.makeID(GarbageDumpPower.class.getSimpleName());
    private static final AbstractPower.PowerType TYPE = AbstractPower.PowerType.BUFF;
    private static final boolean TURN_BASED = false;


    //The only thing TURN_BASED controls is the color of the number on the power icon.
    //Turn based powers are white, non-turn based powers are red or green depending on if their amount is positive or negative.
    //For a power to actually decrease/go away on its own they do it themselves.
    //Look at powers that do this like VulnerablePower and DoubleTapPower.
    public GarbageDumpPower(AbstractCreature owner, int amount) {
        super(POWER_ID, TYPE, TURN_BASED, owner, amount);
    }



    @Override
    public AbstractPower makeCopy() {
        return new GarbageDumpPower(owner, amount);
    }

    public void updateDescription() {
        this.description = String.format(DESCRIPTIONS[0], amount);
    }

    @Override
    public void WasteEnergyAction(int amount) {
        if (amount <= 0) {
            return;
        }
        this.flash();
        for (int i = 0; i < amount; i++) {
            WastedEnergy();
        }
    }
//    @Override
//    public void atEndOfTurn(boolean isPlayer) {
//        if (isPlayer) {
//            if (EnergyPanel.getCurrentEnergy() <= 0) {
//                return;
//            }
//            this.flash();
//            for (int i = 0; i < EnergyPanel.getCurrentEnergy(); i++) {
//                WastedEnergy();
//            }
//        }
//    }

    private void WastedEnergy() {
//        Wiz.atb(new AddTemporaryHPAction(owner, owner, amount));
        Wiz.atb(new GainBlockAction(owner, owner, amount));
    }

}