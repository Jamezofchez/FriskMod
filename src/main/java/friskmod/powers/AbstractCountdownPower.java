package friskmod.powers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import friskmod.util.interfaces.AfterCardPlayedInterface;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class AbstractCountdownPower extends BasePower implements AfterCardPlayedInterface {
    //The only thing TURN_BASED controls is the color of the number on the power icon.
    //Turn based powers are white, non-turn based powers are red or green depending on if their amount is positive or negative.
    //For a power to actually decrease/go away on its own they do it themselves.
    //Look at powers that do this like VulnerablePower and DoubleTapPower.

    private final HashMap<Integer, Integer> countdownMap = new HashMap<>();

    public AbstractCountdownPower(String powerID, PowerType type, boolean isTurnBased, AbstractCreature owner, int amount, int countdown) {
        super(powerID, "_" + System.nanoTime(), type, isTurnBased, owner, amount);
        this.amount2 = countdown;
    }

    public void storeCountdown(int turnCounter) {
        countdownMap.put(turnCounter, amount2);
    }

    public Integer getCountdown(int turnCounter) {
        return countdownMap.get(turnCounter);
    }

    @Override
    public void afterCardPlayed(AbstractCard card) {
        if (this.amount2 <= 0)
            onCountdownTrigger(true);
        if (!AbstractDungeon.getMonsters().areMonstersBasicallyDead() && this.amount2 > 0) {
            this.amount2 -= 1;
            updateDescription();
            if (this.amount2 <= 0)
                onCountdownTrigger(true);
        }
    }

    public void addCountdown(int amount) {
        if (amount == 0) { return;}
        this.amount2 += amount;
        updateDescription();
        if (this.amount2 <= 0)
            onCountdownTrigger(true);
    }
    @Override
    public void renderAmount(SpriteBatch sb, float x, float y, Color c) {
        float alpha = c.a;
        if (this.type == PowerType.DEBUFF)
            c = Color.RED.cpy();
        else if (this.type == PowerType.BUFF){
            c = Color.GREEN.cpy();
        } else{
            c = Color.WHITE.cpy();
        }
        c.a = alpha;
        FontHelper.renderFontRightTopAligned(sb, FontHelper.powerAmountFont, Integer.toString(this.amount), x, y + 15.0F, this.fontScale, c);
        FontHelper.renderFontRightTopAligned(sb, FontHelper.powerAmountFont, Integer.toString(this.amount2), x, y * Settings.scale, this.fontScale, Color.CYAN.cpy());
    }
    public void onCountdownTrigger(boolean expire){
        if (expire){
            hackyRemove();
        }
    }
    public void upgrade(){
    }
    public void hackyRemove(){
        addToTop(new RemoveSpecificPowerAction(owner, owner, this));
    }
}


/* Location:              C:\Program Files (x86)\Steam\steamapps\workshop\content\646570\2839809078\RhythmGirl.jar!\theRhythmGirl\powers\AbstractCountdownPower.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */