package friskmod.potions;

import basemod.abstracts.CustomPotion;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import friskmod.util.CardStats;
import friskmod.util.PotionStats;

public abstract class AbstractEasyPotion extends CustomPotion {
    public AbstractPlayer.PlayerClass pool;
    protected PotionStrings strings;

    public AbstractEasyPotion(String ID, PotionStats info) {
        this(ID, info.rarity, info.size, info.baseColor, info.hybridColor, info.spotsColor, info.pool, info.labOutlineColor);
    }

    public AbstractEasyPotion(String id, PotionRarity rarity, PotionSize size, Color liquidColor, Color hybridColor, Color spotsColor) {
        super("", id, rarity, size, PotionColor.WHITE);
        this.liquidColor = liquidColor;
        this.hybridColor = hybridColor;
        this.spotsColor = spotsColor;
        initializeData();
    }

    public AbstractEasyPotion(String id, PotionRarity rarity, PotionSize size, Color liquidColor, Color hybridColor, Color spotsColor, AbstractPlayer.PlayerClass pool, Color labOutlineColor) {
        this(id, rarity, size, liquidColor, hybridColor, spotsColor);
        this.labOutlineColor = labOutlineColor;
        this.pool = pool;
    }

    @Override
    public void initializeData() {
        potency = getPotency();

        strings = CardCrawlGame.languagePack.getPotionString(ID);
        name = strings.NAME;
        description = getDescription();

        tips.clear();
        tips.add(new PowerTip(name, description));
        addAdditionalTips();
    }

    public abstract String getDescription();

    public void addAdditionalTips() {}

    public AbstractPotion makeCopy() {
        try {
            return getClass().newInstance();
        } catch (IllegalAccessException | InstantiationException e) {
            throw new RuntimeException("BaseMod failed to auto-generate makeCopy for potion: " + ID);
        }
    }
}
