package friskmod.cards;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import friskmod.FriskMod;
import friskmod.actions.BalletShoesAction;
import friskmod.character.Frisk;
import friskmod.patches.ExternalGlowPatch;
import friskmod.util.CardStats;
import friskmod.util.FriskTags;
import friskmod.util.Wiz;


import java.util.List;

import static friskmod.FriskMod.makeID;

public class BalletShoes extends AbstractEasyCard {
    public static final String ID = makeID(BalletShoes.class.getSimpleName()); //makeID adds the mod ID, so the final ID will be something like "modID:MyCard"
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(ID);
    public static final String[] TEXT = uiStrings.TEXT;
    //These will be used in the constructor. Technically you can just use the values directly,
    //but constants at the top of the file are easy to adjust.

    private static final CardStats info = new CardStats(
            Frisk.Meta.Enums.CARD_COLOR, //The card color. If you're making your own character, it'll look something like this. Otherwise, it'll be CardColor.RED or similar for a basegame character color.
            CardType.SKILL, //The type. ATTACK/SKILL/POWER/CURSE/STATUS
            CardRarity.RARE, //Rarity. BASIC is for starting cards, then there's COMMON/UNCOMMON/RARE, and then SPECIAL and CURSE. SPECIAL is for cards you only get from events. Curse is for curses, except for special curses like Curse of the Bell and Necronomicurse.
            CardTarget.ENEMY, //The target. Single target is ENEMY, all enemies is ALL_ENEMY. Look at cards similar to what you want to see what to use.
            2 //The card's base cost. -1 is X cost, -2 is no cost for unplayable cards like curses, or Reflex.
    );
    private static final int TIMES_PLAY = 2;
    private static final int UPG_TIMES_PLAY = 1;


    public BalletShoes() {
        super(ID, info); //Pass the required information to the BaseCard constructor.
        baseMagicNumber = magicNumber = TIMES_PLAY;
        tags.add(FriskTags.INTEGRITY);
    }
    @Override
    public void update() {
        super.update();
        if (Wiz.isInCombat()) {
            if (AbstractDungeon.player.hand.group.contains(this)) {
                makeMiddleCardGlow();
            }
        }
    }
//    @Override
//    public void triggerWhenDrawn() {
//        Wiz.actB(this::makeMiddleCardGlow);
//    }
//
//    @Override
//    public void onRetained() {
//        Wiz.actB(this::makeMiddleCardGlow);
//    }


    private void makeMiddleCardGlow() {
        try {
            int card_pos;
            List<AbstractCard> afterHand = AbstractDungeon.player.hand.group.stream().filter(x -> x != this).collect(java.util.stream.Collectors.toList());
            for (AbstractCard c : afterHand) {
                if (ExternalGlowPatch.BalletShoesGlowFields.glowingBecauseBalletShoes.get(c)) {
                    ExternalGlowPatch.BalletShoesGlowFields.glowingBecauseBalletShoes.set(c, false);
                    c.glowColor = BLUE_BORDER_GLOW_COLOR.cpy();
                }
            }
            double hand_size = (afterHand.size());
            boolean validFlag = false;
            card_pos = (int) (hand_size - 1) / 2;
            if (hand_size % 2 != 0) {
                validFlag = true;
            }
            AbstractCard chosenCard = afterHand.get(card_pos);
            if (validFlag) {
                chosenCard.glowColor = GOLD_BORDER_GLOW_COLOR.cpy();
                ExternalGlowPatch.BalletShoesGlowFields.glowingBecauseBalletShoes.set(chosenCard, true);
            }
        } catch (Exception ignored) {
            FriskMod.logger.warn("{}: BalletShoes set middle glow failed",FriskMod.modID);
        }
    }


    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        boolean canUse = super.canUse(p, m);
        if (!canUse)
            return false;
        double hand_size = (AbstractDungeon.player.hand.size());
        if (hand_size % 2 != 0) {
            canUse = false;
            this.cantUseMessage = TEXT[0];
        }
        return canUse;
    }

//    @Override
//    public void use(AbstractPlayer p, AbstractMonster m) {
//        double hand_size = (AbstractDungeon.player.hand.size())-1; //hasn't updated yet?
//        if (hand_size % 2 == 0) {
//            --hand_size;
//        }
//        int card_pos = (int) (hand_size-1)/2;
//        List<AbstractCard> afterHand = AbstractDungeon.player.hand.group.stream().filter(x -> x != this).collect(java.util.stream.Collectors.toList());
//        AbstractCard chosenCard = afterHand.get(card_pos);
//        if (chosenCard != null) {
//            for (int i = 0; i < magicNumber; ++i) {
//                if (m == null || m.isDeadOrEscaped()) {
//                    m = AbstractDungeon.getRandomMonster();
//                }
//                Wiz.att(new CardPlayAction(chosenCard, m));
//                Wiz.att(new CustomSFXAction("snd_punchstrong"));
//                Wiz.att(new WaitAction(0.25f));
//            }
//            Wiz.att(new CustomSFXAction("mus_sfx_voice_triple"));
//        }
//        p.hand.moveToExhaustPile(chosenCard);
//    }
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new BalletShoesAction(m, magicNumber));
    }


    @Override
    public void upp() {
        upgradeMagicNumber(UPG_TIMES_PLAY);
    }

//    @Override
//    public void afterCardPlayed(AbstractCard card) {
//        makeMiddleCardGlow();
//    }
}
