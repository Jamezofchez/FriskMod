package friskmod.cards;

import basemod.BaseMod;
import com.evacipated.cardcrawl.mod.stslib.actions.common.MultiGroupSelectAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import friskmod.character.Frisk;
import friskmod.patches.perseverance.PerseveranceFields;
import friskmod.patches.perseverance.PerseverancePatch;
import friskmod.util.CardStats;
import friskmod.util.FriskTags;
import friskmod.util.Wiz;


import java.util.Collections;

import static friskmod.FriskMod.makeID;

public class HiddenDeep extends AbstractEasyCard {
    public static final String ID = makeID(HiddenDeep.class.getSimpleName()); //makeID adds the mod ID, so the final ID will be something like "modID:MyCard"
    //These will be used in the constructor. Technically you can just use the values directly,
    //but constants at the top of the file are easy to adjust.

    private static final CardStats info = new CardStats(
            Frisk.Meta.Enums.CARD_COLOR, //The card color. If you're making your own character, it'll look something like this. Otherwise, it'll be CardColor.RED or similar for a basegame character color.
            CardType.SKILL, //The type. ATTACK/SKILL/POWER/CURSE/STATUS
            CardRarity.COMMON, //Rarity. BASIC is for starting cards, then there's COMMON/UNCOMMON/RARE, and then SPECIAL and CURSE. SPECIAL is for cards you only get from events. Curse is for curses, except for special curses like Curse of the Bell and Necronomicurse.
            CardTarget.SELF, //The target. Single target is ENEMY, all enemies is ALL_ENEMY. Look at cards similar to what you want to see what to use.
            0 //The card's base cost. -1 is X cost, -2 is no cost for unplayable cards like curses, or Reflex.
    );
    private static final int BLOCK = 3;
    private static final int UPG_BLOCK = 2;
    private static final int NUM_CARDS = 1;
//    private static final int UPG_NUM_CARDS = 1;

    public HiddenDeep() {
        super(ID, info); //Pass the required information to the BaseCard constructor.
        baseBlock = BLOCK;
        baseMagicNumber = magicNumber = NUM_CARDS;
        tags.add(FriskTags.PERSEVERANCE);
    }
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        blck();
        Wiz.atb(new MultiGroupSelectAction(
                cardStrings.EXTENDED_DESCRIPTION[magicNumber == 1 ? 0 : 1],
                (cards, groups) -> {
                    Collections.reverse(cards);
                    cards.forEach(c -> Wiz.att(new AbstractGameAction() {
                        public void update() {
                            isDone = true;
                            if (p.hand.size() >= BaseMod.MAX_HAND_SIZE) {
                                if (groups.get(c) == p.drawPile)
                                    p.drawPile.moveToDiscardPile(c);
                                p.createHandIsFullDialog();
                            } else
                                p.hand.moveToHand(c, groups.get(c));
                        }
                    }));
                },
                magicNumber, false, c -> PerseverancePatch.isRealUnplayable(c) || PerseveranceFields.trapped.get(c), CardGroup.CardGroupType.DISCARD_PILE
        ));
    }

    @Override
    public void upp() {
        upgradeBlock(UPG_BLOCK);
    }
}
