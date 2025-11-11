package friskmod.cards;

import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.tempCards.Shiv;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import friskmod.cardmods.PyreMod;
import friskmod.character.Frisk;
import friskmod.util.CardStats;
import friskmod.util.FriskTags;
import friskmod.util.Wiz;

import static friskmod.FriskMod.makeID;

public class RockPuzzle extends AbstractEasyCard implements OnPyreCard {
    public final static String ID = makeID(RockPuzzle.class.getSimpleName());
    // intellij stuff attack, enemy, common, 10, 2, , , , 


    private static final CardStats info = new CardStats(
            Frisk.Meta.Enums.CARD_COLOR, //The card color. If you're making your own character, it'll look something like this. Otherwise, it'll be CardColor.RED or similar for a basegame character color.
            CardType.ATTACK, //The type. ATTACK/SKILL/POWER/CURSE/STATUS
            CardRarity.UNCOMMON, //Rarity. BASIC is for starting cards, then there's COMMON/UNCOMMON/RARE, and then SPECIAL and CURSE. SPECIAL is for cards you only get from events. Curse is for curses, except for special curses like Curse of the Bell and Necronomicurse.
            CardTarget.ENEMY, //The target. Single target is ENEMY, all enemies is ALL_ENEMY. Look at cards similar to what you want to see what to use.
            3 //The card's base cost. -1 is X cost, -2 is no cost for unplayable cards like curses, or Reflex.
    );
    private static final int DAMAGE = 15;
    private static final int UPG_DAMAGE = 2;

    private static final int ROCK = 0;
    private static final int UPG_ROCK = 1;
    public RockPuzzle() {
        super(ID, info); //Pass the required information to the BaseCard constructor.
        baseDamage = DAMAGE;
        baseMagicNumber = magicNumber = ROCK;
        tags.add(FriskTags.PERSEVERANCE);
        CardModifierManager.addModifier(this, new PyreMod());
    }

    private int toAdd = -1;

    public void use(AbstractPlayer p, AbstractMonster m) {
        dmg(m, AbstractGameAction.AttackEffect.FIRE);
        AbstractCard q = new Shiv();
        Wiz.makeInHand(q, magicNumber);
        Wiz.atb(new AbstractGameAction() {
            @Override
            public void update() {
                isDone = true;
                if (toAdd >= 0) {
                    Wiz.att(new MakeTempCardInHandAction(q, toAdd, true));
                }
            }
        });
    }

    @Override
    public void onPyred(AbstractCard card) {
        int result = freeToPlay() ? 0 : card.costForTurn;
        toAdd = result > 0 ? result : -1;
    }

    public void upp() {
        upgradeDamage(UPG_DAMAGE);
        upgradeMagicNumber(UPG_ROCK);
    }
}