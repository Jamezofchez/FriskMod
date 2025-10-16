package friskmod.relics;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.PureWater;
import friskmod.FriskMod;
import friskmod.character.Frisk;
import friskmod.cards.ItsYou;
import friskmod.powers.CountdownPounce;

public class HeartShapedLocket extends BaseRelic {
    public static final String ID = FriskMod.makeID(HeartShapedLocket.class.getSimpleName());

    private static final RelicStrings relicStrings = CardCrawlGame.languagePack.getRelicStrings(ID);

    public static final String NAME = relicStrings.NAME;

    private static final String[] DESCRIPTION = relicStrings.DESCRIPTIONS;

    private static final RelicTier RARITY = RelicTier.STARTER; //The relic's rarity.
    private static final LandingSound SOUND = LandingSound.CLINK; //The sound played when the relic is clicked.

    public HeartShapedLocket() {
        super(ID, NAME, Frisk.Meta.Enums.CARD_COLOR, RARITY, SOUND);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTION[0];
    }

    public void atBattleStartPreDraw() {
        addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        addToBot(new MakeTempCardInHandAction(new ItsYou(), 1, false));
    }

    public AbstractRelic makeCopy() {
        return new HeartShapedLocket();
    }
}