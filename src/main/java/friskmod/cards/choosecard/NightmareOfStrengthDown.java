package friskmod.cards.choosecard;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.FrailPower;
import com.megacrit.cardcrawl.powers.GainStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

import static friskmod.FriskMod.makeID;

public class NightmareOfStrengthDown extends AbstractNightmareCard{
    public static final String ID = makeID(NightmareOfStrengthDown.class.getSimpleName()); //makeID adds the mod ID, so the final ID will be something like "modID:MyCard"

    private static final int POWER_AMOUNT = 2;

    public NightmareOfStrengthDown() {
        super(ID);
        setDisplayRarity(CardRarity.UNCOMMON);
    }

    @Override
    public void chooseOption() {
        addToBot(new ApplyPowerAction(getTarget(), AbstractDungeon.player, new StrengthPower(getTarget(), -POWER_AMOUNT), -POWER_AMOUNT, true, AbstractGameAction.AttackEffect.NONE));
        if (!getTarget().hasPower("Artifact"))
            addToBot(new ApplyPowerAction(getTarget(), AbstractDungeon.player, new GainStrengthPower(getTarget(), POWER_AMOUNT), POWER_AMOUNT, true, AbstractGameAction.AttackEffect.NONE));
    }

}
