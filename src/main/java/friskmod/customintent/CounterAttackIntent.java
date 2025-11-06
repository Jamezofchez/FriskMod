package friskmod.customintent;

import basemod.ReflectionHacks;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import friskmod.FriskMod;

import static friskmod.FriskMod.makeID;

public class CounterAttackIntent extends CustomIntent {
    public static final String ID = makeID(CounterAttackIntent.class.getSimpleName());
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(ID);
    private static final String[] TEXT = uiStrings.TEXT;

    public CounterAttackIntent() {
        super(IntentEnums.COUNTER_ATTACK, TEXT[0],
                FriskMod.UIPath("counterIntent_L.png"),
                FriskMod.UIPath("counterIntent.png"));
    }

    public String description(AbstractMonster mo) {
        String result = TEXT[1];
        result = result + mo.getIntentDmg();
        if (((Boolean) ReflectionHacks.getPrivate(mo, AbstractMonster.class, "isMultiDmg")).booleanValue()) {
            int hitCount = ((Integer) ReflectionHacks.getPrivate(mo, AbstractMonster.class, "intentMultiAmt")).intValue();
            result = result + TEXT[3];
            result = result + hitCount + TEXT[4];
        } else {
            result = result + TEXT[2];
        }
        return result;
    }
}


/* Location:              C:\Program Files (x86)\Steam\steamapps\workshop\content\646570\2396661789\Ruina.jar!\ruina\customintent\CounterAttackIntent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */