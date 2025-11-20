package friskmod.external;


import com.megacrit.cardcrawl.monsters.AbstractMonster;
import downfall.powers.NeowInvulnerablePower;
import friskmod.helper.ModManager;

public class Ruina {
    public static Ruina INSTANCE = null;

    public static Ruina getInstance() {
        if (!ModManager.isRuinaLoaded){
            return null;
        }
        if (INSTANCE == null) {
            INSTANCE = new Ruina();
        }
        return INSTANCE;
    }
}
