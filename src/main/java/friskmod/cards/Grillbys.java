package friskmod.cards;

import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;
import com.evacipated.cardcrawl.mod.stslib.cards.interfaces.OnObtainCard;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.common.ObtainPotionAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.shrines.Lab;
import com.megacrit.cardcrawl.helpers.PotionHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.Sozu;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.ObtainPotionEffect;
import friskmod.cards.drinks.Ketchup;
import friskmod.character.Frisk;
import friskmod.helper.GrillbysHelper;
import friskmod.util.CardStats;
import friskmod.util.FriskTags;
import friskmod.util.Wiz;

import static friskmod.FriskMod.makeID;

public class Grillbys extends AbstractEasyCard implements OnObtainCard {
    public static final String ID = makeID(Grillbys.class.getSimpleName()); //makeID adds the mod ID, so the final ID will be something like "modID:MyCard"
    //These will be used in the constructor. Technically you can just use the values directly,
    //but constants at the top of the file are easy to adjust.

    private static final CardStats info = new CardStats(
            Frisk.Meta.Enums.CARD_COLOR, //The card color. If you're making your own character, it'll look something like this. Otherwise, it'll be CardColor.RED or similar for a basegame character color.
            CardType.SKILL, //The type. ATTACK/SKILL/POWER/CURSE/STATUS
            CardRarity.RARE, //Rarity. BASIC is for starting cards, then there's COMMON/UNCOMMON/RARE, and then SPECIAL and CURSE. SPECIAL is for cards you only get from events. Curse is for curses, except for special curses like Curse of the Bell and Necronomicurse.
            CardTarget.SELF, //The target. Single target is ENEMY, all enemies is ALL_ENEMY. Look at cards similar to what you want to see what to use.
            0 //The card's base cost. -1 is X cost, -2 is no cost for unplayable cards like curses, or Reflex.
    );


    public Grillbys() {
        super(ID, info); //Pass the required information to the BaseCard constructor.
        tags.add(FriskTags.YOU);
//        this.cardsToPreview = new Ketchup();
//        this.exhaust = true;
    }
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
//        AbstractCard ketchup = new Ketchup();
//        if (upgraded) {
//            ketchup.upgrade();
//        }
//        addToBot(new MakeTempCardInHandAction(this.cardsToPreview.makeStatEquivalentCopy()));
        for (AbstractPotion potion : p.potions) {
            if (potion == null || potion instanceof com.megacrit.cardcrawl.potions.PotionSlot) {
                continue;
            }
            AbstractCard card = GrillbysHelper.getPotionCard(potion);
            if (upgraded && card.canUpgrade()){
                card.upgrade();
            }
            addToBot(new MakeTempCardInHandAction(card));
        }
    }

    @Override
    public void upp() {

    }

    @Override
    public void onObtainCard() {
        AbstractRelic sozu = AbstractDungeon.player.getRelic(Sozu.ID);
        if (sozu != null) {
            sozu.flash();
        } else if (Wiz.isInCombat()) {
            this.addToBot(new ObtainPotionAction(AbstractDungeon.returnRandomPotion(true)));
        } else {
            AbstractDungeon.topLevelEffectsQueue.add(new ObtainPotionEffect(AbstractDungeon.returnRandomPotion()));
        }
//        PotionHelper.getPotion(AbstractDungeon.returnRandomPotion(true).ID);
    }
}
