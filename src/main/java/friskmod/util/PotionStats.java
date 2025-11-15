package friskmod.util;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.potions.AbstractPotion.PotionRarity;
import com.megacrit.cardcrawl.potions.AbstractPotion.PotionSize;



public class PotionStats {
    //String id, PotionRarity c, PotionSize size, Color liquidColor, Color hybridColor, Color spotsColor, AbstractPlayer.PlayerClass pool, Color labOutlineColor
    public final PotionRarity rarity;
    public final PotionSize size;
    public final Color baseColor;
    public final Color hybridColor;
    public final Color spotsColor;
    public final AbstractPlayer.PlayerClass pool;
    public final Color labOutlineColor;

    public PotionStats(PotionRarity c, PotionSize size, Color liquidColor, Color hybridColor, Color spotsColor, AbstractPlayer.PlayerClass pool, Color labOutlineColor)
    {
        this.rarity = c;
        this.size = size;
        this.baseColor = liquidColor;
        this.hybridColor = hybridColor;
        this.spotsColor = spotsColor;
        this.pool = pool;
        this.labOutlineColor = labOutlineColor;
    }
}