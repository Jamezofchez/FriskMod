package friskmod.potions;

import basemod.BaseMod;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.GameDictionary;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import friskmod.character.Frisk;
import friskmod.powers.LV_Enemy;
import friskmod.powers.Mercied;
import friskmod.util.CardStats;
import friskmod.util.PotionStats;
import friskmod.util.Wiz;

import static friskmod.FriskMod.makeID;


public class MercyPotion extends AbstractEasyPotion {
    public static String ID = makeID(MercyPotion.class.getSimpleName());
    private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(ID);
    public static final String[] DESCRIPTIONS = potionStrings.DESCRIPTIONS;
    private static final int MERCIED_AMOUNT = 1;
    private static final int LV_AMOUNT = 5;
    public static final PotionStats info = new PotionStats(
            PotionRarity.UNCOMMON,
            PotionSize.HEART,
            Color.GOLD.cpy(),
            Color.PINK.cpy(),
            null,
            Frisk.Meta.Enums.THE_FALLEN_HUMAN,
            Frisk.Meta.characterColor
    );
    public MercyPotion() {
        super(ID, info);
        isThrown = false;
        targetRequired = false;
    }

    public int getPotency(int ascensionlevel) {
        return LV_AMOUNT;
    }

    public void use(AbstractCreature creature) {
        if (Wiz.isInCombat()) {
            for (AbstractMonster mo : Wiz.getMonsters()) {
                addToBot(new ApplyPowerAction(mo, AbstractDungeon.player, new LV_Enemy(mo, potency), potency));
            }
            for (AbstractMonster mo : Wiz.getMonsters()) {
                addToBot(new ApplyPowerAction(mo, AbstractDungeon.player, new Mercied(mo, MERCIED_AMOUNT), MERCIED_AMOUNT));
            }
        }
    }

    public String getDescription() {
        return DESCRIPTIONS[0] + potency + DESCRIPTIONS[1] + MERCIED_AMOUNT + DESCRIPTIONS[2];

    }

    public void addAdditionalTips() {
//        this.tips.add(new PowerTip((BaseMod.getKeywordProper(makeID("LV"))), GameDictionary.keywords.get(makeID("LV"))));
//        this.tips.add(new PowerTip((BaseMod.getKeywordProper(makeID("Mercied"))), GameDictionary.keywords.get(makeID("Mercied"))));
    }
}