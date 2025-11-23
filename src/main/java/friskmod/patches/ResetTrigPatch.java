package friskmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.megacrit.cardcrawl.cards.AbstractCard;

import javax.smartcardio.Card;

@SpirePatch2(clz = AbstractCard.class, method = "resetAttributes")
public class ResetTrigXPPatch {
    public static void Prefix(AbstractCard __instance){
        CardXPFields.XPFields.triginherentXP.set(__instance, 0);
        CardXPFields.XPFields.trigaddedXP.set(__instance, 0);

    }
}
