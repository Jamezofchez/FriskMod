package friskmod.external;


import com.megacrit.cardcrawl.monsters.AbstractMonster;
import downfall.powers.NeowInvulnerablePower;
import friskmod.helper.ModManager;

public class Downfall {
    public static Downfall INSTANCE = null;

    public static Downfall getInstance() {
        if (!ModManager.isDownfallLoaded){
            return null;
        }
        if (INSTANCE == null) {
            INSTANCE = new Downfall();
        }
        return INSTANCE;
    }
    public boolean checkNeowInvulnerable (AbstractMonster m){
        if (m.getPower(NeowInvulnerablePower.POWER_ID) != null){
            return true;
        }
        return false;
    }
}
