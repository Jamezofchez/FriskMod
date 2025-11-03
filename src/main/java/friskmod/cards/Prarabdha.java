package friskmod.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import friskmod.actions.StealAllBlockAction;
import friskmod.actions.StealPowerAction;
import friskmod.character.Frisk;
import friskmod.helper.StealableWhitelist;
import friskmod.helper.ThreatenedCheck;
import friskmod.powers.Karma;
import friskmod.powers.LV_Enemy;
import friskmod.util.CardStats;
import friskmod.util.FriskTags;
import hermit.util.Wiz;

import static friskmod.FriskMod.makeID;

public class Prarabdha extends AbstractEasyCard {
    public static final String ID = makeID(Prarabdha.class.getSimpleName()); //makeID adds the mod ID, so the final ID will be something like "modID:MyCard"
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(ID);
    public static final String[] TEXT = uiStrings.TEXT;
    //These will be used in the constructor. Technically you can just use the values directly,
    //but constants at the top of the file are easy to adjust.
//    private AbstractCard stealCard;

    private static final CardStats info = new CardStats(
            Frisk.Meta.Enums.CARD_COLOR, //The card color. If you're making your own character, it'll look something like this. Otherwise, it'll be CardColor.RED or similar for a basegame character color.
            CardType.SKILL, //The type. ATTACK/SKILL/POWER/CURSE/STATUS
            CardRarity.UNCOMMON, //Rarity. BASIC is for starting cards, then there's COMMON/UNCOMMON/RARE, and then SPECIAL and CURSE. SPECIAL is for cards you only get from events. Curse is for curses, except for special curses like Curse of the Bell and Necronomicurse.
            CardTarget.ENEMY, //The target. Single target is ENEMY, all enemies is ALL_ENEMY. Look at cards similar to what you want to see what to use.
            2 //The card's base cost. -1 is X cost, -2 is no cost for unplayable cards like curses, or Reflex.
    );
    private static final int KARMA_AND_LV_AMOUNT = 6;
    private static final int UPG_KARMA_AND_LV_AMOUNT = 2;

    public Prarabdha() {
        super(ID, info); //Pass the required information to the BaseCard constructor.
        baseMagicNumber = magicNumber = KARMA_AND_LV_AMOUNT;
        tags.add(FriskTags.JUSTICE);
//        stealCard = new Steal();
//        stealCard.setCostForTurn(0);
//        stealCard.initializeDescription();
//        stealCard.rawDescription = stealCard.rawDescription + TEXT[0];
//        stealCard.exhaust = true;
//        this.cardsToPreview = stealCard;
    }
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(m, p, new LV_Enemy(m, magicNumber), magicNumber));
        addToBot(new ApplyPowerAction(m, p, new Karma(m, magicNumber), magicNumber));
        if (extraEffectCheck()) {
            glowColor = AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy();
        }
    }

    @Override
    public void triggerOnGlowCheck() {
        glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
        if (extraEffectCheck()) {
            glowColor = AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy();
            addToBot(new StealAllBlockAction(Wiz.getEnemies()));
            addToBot(new StealPowerAction(Wiz.getEnemies()));
        }
    }

    private boolean extraEffectCheck(){
        if (!ThreatenedCheck.isThreatened()){
            return false;
        }
        for (AbstractMonster m : Wiz.getEnemies()) {
            if ((m.powers.stream().anyMatch(p -> StealPowerAction.stealablePows.contains(p.ID) && StealableWhitelist.getInstance().checkPreProcess(p)))) {
                return true;
            }
        }
        return false;
    }


    @Override
    public void upp() {
        upgradeMagicNumber(UPG_KARMA_AND_LV_AMOUNT);
    }
}
