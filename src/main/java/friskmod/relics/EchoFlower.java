package friskmod.relics;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import friskmod.FriskMod;
import friskmod.character.Frisk;
import friskmod.helper.ThreatenedCheck;
import friskmod.util.Wiz;

public class EchoFlower extends BaseRelic {
    public static final String ID = FriskMod.makeID(EchoFlower.class.getSimpleName());

    private static final RelicStrings relicStrings = CardCrawlGame.languagePack.getRelicStrings(ID);

    public static final String NAME = relicStrings.NAME;

    private static final String[] DESCRIPTION = relicStrings.DESCRIPTIONS;

    private static final RelicTier RARITY = RelicTier.UNCOMMON; //The relic's rarity.
    private static final LandingSound SOUND = LandingSound.MAGICAL; //The sound played when the relic is clicked.


    public EchoFlower() {
        super(ID, NAME, Frisk.Meta.Enums.CARD_COLOR, RARITY, SOUND);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTION[0];
    }


    @Override
    public void atTurnStart() {
        Wiz.atb(Wiz.actionify(() -> {
            if (ThreatenedCheck.isThreatened()) {
                addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
                addToBot(new MakeTempCardInHandAction(new friskmod.cards.EchoFlower(), 1, false));
            }
        }));
    }

    public AbstractRelic makeCopy() {
        return new EchoFlower();
    }
}