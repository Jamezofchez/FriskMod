package friskmod.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.utility.LoseBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.ThoughtBubble;
import friskmod.character.Frisk;
import friskmod.powers.PleadedPower;
import friskmod.util.CardStats;
import friskmod.util.FriskTags;
import friskmod.util.Wiz;

import java.util.ArrayList;
import java.util.List;

import static friskmod.FriskMod.makeID;

public class Plead extends AbstractEasyCard {
    public static final String ID = makeID(Plead.class.getSimpleName()); //makeID adds the mod ID, so the final ID will be something like "modID:MyCard"
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(ID);
    public static final String[] TEXT = uiStrings.TEXT;
    //These will be used in the constructor. Technically you can just use the values directly,
    //but constants at the top of the file are easy to adjust.

    static final int POWER_AMOUNT = 1;

    private static final CardStats info = new CardStats(
            Frisk.Meta.Enums.CARD_COLOR, //The card color. If you're making your own character, it'll look something like this. Otherwise, it'll be CardColor.RED or similar for a basegame character color.
            CardType.SKILL, //The type. ATTACK/SKILL/POWER/CURSE/STATUS
            CardRarity.UNCOMMON, //Rarity. BASIC is for starting cards, then there's COMMON/UNCOMMON/RARE, and then SPECIAL and CURSE. SPECIAL is for cards you only get from events. Curse is for curses, except for special curses like Curse of the Bell and Necronomicurse.
            CardTarget.ENEMY, //The target. Single target is ENEMY, all enemies is ALL_ENEMY. Look at cards similar to what you want to see what to use.
            0 //The card's base cost. -1 is X cost, -2 is no cost for unplayable cards like curses, or Reflex.
    );

    public Plead() {
        super(ID, info); //Pass the required information to the BaseCard constructor.
        tags.add(FriskTags.KINDNESS);
    }
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int playerBlock = p.currentBlock;
        if (playerBlock <= 0){
            AbstractDungeon.effectList.add(new ThoughtBubble(AbstractDungeon.player.dialogX, AbstractDungeon.player.dialogY, 3.0F, TEXT[0], true));
            return;
        }
        addToBot(new LoseBlockAction(p, p, playerBlock));
        if (upgraded) {
            List<AbstractMonster> enemies = Wiz.getEnemies();
            useEnemies(p, enemies, playerBlock);
        } else {
            // Wrap single target into a list
            ArrayList<AbstractMonster> single = new ArrayList<>();
            if (m != null && !m.isDeadOrEscaped()) {
                single.add(m);
            }
            useEnemies(p, single, playerBlock);
        }
    }

    private void useEnemies(AbstractPlayer p, List<AbstractMonster> enemies, int playerBlock) {
        for (AbstractMonster mo : enemies) {
            addToBot(new GainBlockAction(mo, p, playerBlock));
            addToBot(new ApplyPowerAction(mo, p, new PleadedPower(mo, POWER_AMOUNT), POWER_AMOUNT));
        }
    }

    @Override
    public void upp() {

    }
}
