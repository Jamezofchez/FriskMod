package friskmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import friskmod.powers.NEO;

@SpirePatch(clz = AbstractCreature.class, method = "addBlock")
public class NEOPatch {
    @SpirePostfixPatch
    public static void checkForDentedArmor(AbstractCreature creature, int blockAmount) {
        AbstractPower posspow = creature.getPower(NEO.POWER_ID);
        if (posspow != null) {
            ((NEO) posspow).triggerNEO(creature.currentBlock);
        }
    }
}
