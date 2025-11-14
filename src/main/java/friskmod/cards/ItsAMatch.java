package friskmod.cards;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import friskmod.actions.EnemyStealMatchingDebuffsAction;
import friskmod.actions.StealAllBlockAction;
import friskmod.actions.StealPowerAction;
import friskmod.character.Frisk;
import friskmod.helper.StealableWhitelist;
import friskmod.util.CardStats;
import friskmod.util.FriskTags;
import friskmod.util.Wiz;


import java.util.List;

import static friskmod.FriskMod.makeID;

public class ItsAMatch extends AbstractEasyCard {
    public static final String ID = makeID(ItsAMatch.class.getSimpleName()); //makeID adds the mod ID, so the final ID will be something like "modID:MyCard"
    //These will be used in the constructor. Technically you can just use the values directly,
    //but constants at the top of the file are easy to adjust.

    private static final CardStats info = new CardStats(
            Frisk.Meta.Enums.CARD_COLOR, //The card color. If you're making your own character, it'll look something like this. Otherwise, it'll be CardColor.RED or similar for a basegame character color.
            CardType.SKILL, //The type. ATTACK/SKILL/POWER/CURSE/STATUS
            CardRarity.UNCOMMON, //Rarity. BASIC is for starting cards, then there's COMMON/UNCOMMON/RARE, and then SPECIAL and CURSE. SPECIAL is for cards you only get from events. Curse is for curses, except for special curses like Curse of the Bell and Necronomicurse.
            CardTarget.ENEMY, //The target. Single target is ENEMY, all enemies is ALL_ENEMY. Look at cards similar to what you want to see what to use.
            1 //The card's base cost. -1 is X cost, -2 is no cost for unplayable cards like curses, or Reflex.
    );

    public ItsAMatch() {
        super(ID, info); //Pass the required information to the BaseCard constructor.
        tags.add(FriskTags.KINDNESS);
    }
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (!upgraded) {
            if (p.currentBlock > 0) {
                addToBot(new StealAllBlockAction(m));
            }
            addToBot(new StealPowerAction(m, true, true));
            addToBot(new EnemyStealMatchingDebuffsAction(m));
        }
        else{
            List<AbstractMonster> monsters = Wiz.getMonsters();
            if (p.currentBlock > 0) {
                addToBot(new StealAllBlockAction(monsters));
            }
            addToBot(new StealPowerAction(monsters, true, true));
            addToBot(new EnemyStealMatchingDebuffsAction(monsters));
        }
    }

    @Override
    public void triggerOnGlowCheck() {
        glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
        AbstractPlayer p = AbstractDungeon.player;
        for (AbstractMonster m : Wiz.getMonsters()) {
            for (AbstractPower mpow : m.powers) {
                if ((mpow.type == AbstractPower.PowerType.DEBUFF)) {
                    for (AbstractPower ppow : p.powers) {
                        if (!(ppow.type == AbstractPower.PowerType.DEBUFF)) {
                            continue;
                        }
                        if (mpow.ID.equals(ppow.ID)) {
                            glowColor = AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy();
                        }
                    }
                } else {
                    for (AbstractPower ppow : p.powers) {
                        if (!(ppow.type == AbstractPower.PowerType.BUFF)) {
                            continue;
                        }
                        if (StealPowerAction.stealablePows.contains(mpow.ID) && StealableWhitelist.getInstance().checkPreProcess(mpow, true) && StealPowerAction.stealMatchingCheck().test(mpow)) {
                            glowColor = AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy();
                        }
                    }
                }
            }
        }
    }

    @Override
    public void upp() {
        this.target = CardTarget.ALL_ENEMY;
    }
}
