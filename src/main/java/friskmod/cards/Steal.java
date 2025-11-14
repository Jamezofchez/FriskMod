package friskmod.cards;

import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import friskmod.actions.StealAllBlockAction;
import friskmod.actions.StealEnemyHP;
import friskmod.actions.StealPowerAction;
import friskmod.character.Frisk;
import friskmod.helper.StealableWhitelist;
import friskmod.util.CardStats;
import friskmod.util.FriskTags;
import friskmod.util.Wiz;

import static friskmod.FriskMod.makeID;

public class Steal extends AbstractEasyCard {
    public static final String ID = makeID(Steal.class.getSimpleName()); //makeID adds the mod ID, so the final ID will be something like "modID:MyCard"
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    //These will be used in the constructor. Technically you can just use the values directly,
    //but constants at the top of the file are easy to adjust.

    private static final CardStats info = new CardStats(
            Frisk.Meta.Enums.CARD_COLOR, //The card color. If you're making your own character, it'll look something like this. Otherwise, it'll be CardColor.RED or similar for a basegame character color.
            CardType.SKILL, //The type. ATTACK/SKILL/POWER/CURSE/STATUS
            CardRarity.BASIC, //Rarity. BASIC is for starting cards, then there's COMMON/UNCOMMON/RARE, and then SPECIAL and CURSE. SPECIAL is for cards you only get from events. Curse is for curses, except for special curses like Curse of the Bell and Necronomicurse.
            CardTarget.ALL_ENEMY, //The target. Single target is ENEMY, all enemies is ALL_ENEMY. Look at cards similar to what you want to see what to use.
            2 //The card's base cost. -1 is X cost, -2 is no cost for unplayable cards like curses, or Reflex.
    );

    private static final int HP_STEAL = 3;
    private static final int UPG_HP_STEAL = 2;


    public Steal() {
        super(ID, info); //Pass the required information to the BaseCard constructor.
        tags.add(FriskTags.YOU);
        baseMagicNumber = magicNumber = HP_STEAL;
    }

    @Override
    public void update() {
        super.update();
        initializeDescription();
    }

//    @Override
//    public void initializeDescription() {
//        if (!this.upgraded) {
//            this.rawDescription = cardStrings.DESCRIPTION;
//        } else {
//            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
//        }
//        try {
//            if (Wiz.getMonsters().stream().anyMatch(m -> m.hasPower(ArtifactPower.POWER_ID))) {
//                this.rawDescription = this.rawDescription + cardStrings.EXTENDED_DESCRIPTION[0];
//            }
//        } catch (NullPointerException ignored){
//            //good code!
//        }
//        super.initializeDescription();
//    }

    @Override
    public void triggerOnGlowCheck() {
        glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
        for (AbstractMonster m : Wiz.getMonsters()) {
            if (!m.isDeadOrEscaped() && (m.powers.stream().anyMatch(p -> StealPowerAction.stealablePows.contains(p.ID) && StealableWhitelist.getInstance().checkPreProcess(p, true)))) {
                glowColor = AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy();
            }
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new StealAllBlockAction(Wiz.getMonsters()));
        addToBot(new StealPowerAction(Wiz.getMonsters()));
        for (AbstractMonster monster : Wiz.getMonsters()) {
            addToBot(new StealEnemyHP(magicNumber, monster));
        }
    }

//    @Override
//    public void CriticalEffect(AbstractPlayer p, AbstractMonster m) {
//        addToBot(new GainEnergyAction(CRITICAL_ENERGY_GAIN)); //glow confusion
//    }

    @Override
    public void upp() {
        upgradeMagicNumber(UPG_HP_STEAL);
    }
}
