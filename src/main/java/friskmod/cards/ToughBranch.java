package friskmod.cards;

import basemod.patches.com.megacrit.cardcrawl.cards.AbstractCard.MultiCardPreview;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.colorless.Blind;
import com.megacrit.cardcrawl.cards.colorless.Trip;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import friskmod.actions.CustomSFXAction;
import friskmod.cards.choosecard.ToughBranchAttack;
import friskmod.cards.choosecard.ToughBranchDefend;
import friskmod.character.Frisk;
import friskmod.patches.CardXPFields;
import friskmod.patches.HackyToughBranchPatch;
import friskmod.util.CardStats;
import friskmod.util.FriskTags;


import java.util.ArrayList;

import static friskmod.FriskMod.makeID;
import static friskmod.helper.DraftManager.setDraftTarget;

public class ToughBranch extends AbstractEasyCard {
    public static final String ID = makeID(ToughBranch.class.getSimpleName()); //makeID adds the mod ID, so the final ID will be something like "modID:MyCard"
    //These will be used in the constructor. Technically you can just use the values directly,
    //but constants at the top of the file are easy to adjust.
    private static final CardStats info = new CardStats(
            Frisk.Meta.Enums.CARD_COLOR, //The card color. If you're making your own character, it'll look something like this. Otherwise, it'll be CardColor.RED or similar for a basegame character color.
            CardType.ATTACK, //The type. ATTACK/SKILL/POWER/CURSE/STATUS
            CardRarity.UNCOMMON, //Rarity. BASIC is for starting cards, then there's COMMON/UNCOMMON/RARE, and then SPECIAL and CURSE. SPECIAL is for cards you only get from events. Curse is for curses, except for special curses like Curse of the Bell and Necronomicurse.
            CardTarget.ENEMY, //The target. Single target is ENEMY, all enemies is ALL_ENEMY. Look at cards similar to what you want to see what to use.
            1 //The card's base cost. -1 is X cost, -2 is no cost for unplayable cards like curses, or Reflex.
    );
    public static final int DAMAGE = 7;
    public static final int UPG_DAMAGE = 2;

    public static final int BLOCK = 6;
    public static final int UPG_BLOCK = 2;

    public static final int WAIT_TIMER = 4;
    
    public ToughBranch() {
        super(ID, info); //Pass the required information to the BaseCard constructor.
        baseDamage = DAMAGE;
        baseBlock = BLOCK;
        baseSecondMagic = secondMagic = WAIT_TIMER;
        tags.add(FriskTags.PATIENCE);
        MultiCardPreview.add(this, new ToughBranchAttack(), new ToughBranchDefend());
    }
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        ArrayList<AbstractCard> cards = new ArrayList<>();
        AbstractCard attack = new ToughBranchAttack();
        AbstractCard defend = new ToughBranchDefend();
        cards.add(attack);
        cards.add(defend);
        if (upgraded) for (AbstractCard c : cards) c.upgrade();
//        int currentXP = CardXPFields.getCardAddedXP(this);
//        if (currentXP > 0) {
//            CardXPFields.setAddedXP(attack, currentXP);
//            attack.applyPowers();
//        }
        setDraftTarget(m);
        DamageAction damageAction = new DamageAction(m, new DamageInfo(p, damage, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.SLASH_DIAGONAL);
        HackyToughBranchPatch.damageActionFields.fromToughBranch.set(damageAction, true);
        AbstractDungeon.cardRewardScreen.chooseOneOpen(cards);
        addToBot(damageAction);
    }

    @Override
    public void upp() {
        upgradeDamage(UPG_DAMAGE);
        upgradeBlock(UPG_BLOCK);
        AbstractCard q = new ToughBranchAttack();
        AbstractCard q2 = new ToughBranchDefend();
        q.upgrade();
        q2.upgrade();
        MultiCardPreview.clear(this);
        MultiCardPreview.add(this, q, q2);
    }
}
