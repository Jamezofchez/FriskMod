package friskmod.customintent;

import basemod.ReflectionHacks;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import friskmod.FriskMod;

import static friskmod.FriskMod.makeID;

public class MassAttackIntent extends CustomIntent {
    public static final String ID = makeID(MassAttackIntent.class.getSimpleName());
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(ID);
    private static final String[] TEXT = uiStrings.TEXT;
    public MassAttackIntent() {
        super(IntentEnums.MASS_ATTACK, TEXT[0],
                FriskMod.UIPath("areaIntent_L.png"),
                FriskMod.UIPath("areaIntent.png"));
    }

    public String description(AbstractMonster mo) {
        String result = TEXT[1];
        result = result + mo.getIntentDmg();
        result = result + TEXT[2];
        if (((Boolean) ReflectionHacks.getPrivate(mo, AbstractMonster.class, "isMultiDmg")).booleanValue()) {
            int hitCount = ((Integer) ReflectionHacks.getPrivate(mo, AbstractMonster.class, "intentMultiAmt")).intValue();
            result = result + " #b" + hitCount + TEXT[4];
        } else {
            result = result + TEXT[3];
        }
        return result;
    }
}


/* Location:              C:\Program Files (x86)\Steam\steamapps\workshop\content\646570\2396661789\Ruina.jar!\ruina\customintent\MassAttackIntent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */