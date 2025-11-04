package friskmod.cards;

import basemod.helpers.CardModifierManager;
import basemod.helpers.TooltipInfo;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.ExhaustiveField;
import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.FleetingField;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import friskmod.FriskMod;
import friskmod.character.Frisk;
import friskmod.util.CardStats;
import friskmod.util.FriskTags;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static friskmod.FriskMod.makeID;

public class EchoFlower extends AbstractEasyCard {
    public static final String ID = makeID(EchoFlower.class.getSimpleName()); //makeID adds the mod ID, so the final ID will be something like "modID:MyCard"
    //These will be used in the constructor. Technically you can just use the values directly,
    //but constants at the top of the file are easy to adjust.

//    private static final ShaderProgram PEARLESCENCE_SHADER = new ShaderProgram(
//            SpriteBatch.createDefaultShader().getVertexShaderSource(), Gdx.files
//            .internal("friskmod/shaders/PearlescenceShader.glsl").readString(String.valueOf(StandardCharsets.UTF_8)));


    public AbstractCard lastCard;

    private float time;

    private static final CardStats info = new CardStats(
            Frisk.Meta.Enums.CARD_COLOR, //The card color. If you're making your own character, it'll look something like this. Otherwise, it'll be CardColor.RED or similar for a basegame character color.
            CardType.SKILL, //The type. ATTACK/SKILL/POWER/CURSE/STATUS
            CardRarity.RARE, //Rarity. BASIC is for starting cards, then there's COMMON/UNCOMMON/RARE, and then SPECIAL and CURSE. SPECIAL is for cards you only get from events. Curse is for curses, except for special curses like Curse of the Bell and Necronomicurse.
            CardTarget.NONE, //The target. Single target is ENEMY, all enemies is ALL_ENEMY. Look at cards similar to what you want to see what to use.
            -2 //The card's base cost. -1 is X cost, -2 is no cost for unplayable cards like curses, or Reflex.
    );

    public EchoFlower() {
        super(ID, info); //Pass the required information to the BaseCard constructor.
        tags.add(FriskTags.INTEGRITY);
        this.glowColor = Color.WHITE.cpy();
    }
    public void onPlayCard(AbstractCard c, AbstractMonster m) {
        if (c.purgeOnUse)
            return;
//        boolean firstCard = (this.lastCard == null);
        this.lastCard = c.makeStatEquivalentCopy();
        if (upgraded) {
            this.lastCard.upgrade();
        }
        this.cardID = c.cardID;
        this.type = this.lastCard.type;
        this.target = c.target;
        this.cost = this.lastCard.cost;
        this.costForTurn = this.lastCard.costForTurn;
        this.lastCard.price = -100;
    }

    public void update() {
        AbstractPlayer p = AbstractDungeon.player;
        super.update();
        if (this.lastCard != null) {
            this.lastCard.update();
        }
    }

    public boolean cardPlayable(AbstractMonster m) {
        return (this.lastCard != null);
    }

    public boolean hasEnoughEnergy() {
        return (this.lastCard == null) ? super.hasEnoughEnergy() : this.lastCard.hasEnoughEnergy();
    }

    public boolean canPlay(AbstractCard card) {
        return (this.lastCard == null) ? super.canPlay(card) : this.lastCard.canPlay(card);
    }

    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        if (this.lastCard == null) {
            this.cantUseMessage = cardStrings.EXTENDED_DESCRIPTION[0];
            return false;
        }
        boolean canUse = this.lastCard.canUse(p, m);
        this.cantUseMessage = this.lastCard.cantUseMessage;
        return canUse;
    }

    public void calculateCardDamage(AbstractMonster mo) {
        if (this.lastCard != null)
            this.lastCard.calculateCardDamage(mo);
    }

    public void applyPowers() {
        if (this.lastCard != null)
            this.lastCard.applyPowers();
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        if (this.lastCard != null) {
            this.lastCard.use(p, m);
            AbstractDungeon.player.limbo.addToBottom(this.lastCard);
            stopGlowing();
        }
    }

    public boolean canUpgrade() {
        return (this.lastCard == null) ? false : this.lastCard.canUpgrade();
    }

    public void upgrade() {
        if (this.lastCard != null)
            this.lastCard.upgrade();
    }

    public AbstractCard makeCopy() {
        if (this.lastCard != null)
            return this.lastCard.makeCopy();
        return new EchoFlower();
    }

    public AbstractCard makeStatEquivalentCopy() {
        if (this.lastCard != null)
            return this.lastCard.makeStatEquivalentCopy();
        AbstractCard copy = super.makeStatEquivalentCopy();
        if (copy instanceof EchoFlower)
            copy.cardID = this.cardID;
        return copy;
    }

    public void render(SpriteBatch sb) {
        try {
            Method updateGlow = AbstractCard.class.getDeclaredMethod("updateGlow", new Class[0]);
            updateGlow.setAccessible(true);
            updateGlow.invoke(this, new Object[0]);
            Method renderGlow = AbstractCard.class.getDeclaredMethod("renderGlow", new Class[] { SpriteBatch.class });
            renderGlow.setAccessible(true);
            renderGlow.invoke(this, new Object[] { sb });
        } catch (Exception e) {
            FriskMod.logger.warn("{}: reflection failed in EchoFlower glow: {}", FriskMod.modID, String.valueOf(e));
        }
        if (this.lastCard == null) {
            // When there's no recorded last card, render normally to avoid an empty-looking card
            super.render(sb);
            return;
        }
        // With a recorded last card, render the special background and then render that card on top
        renderPearlescentBG(sb);
        this.lastCard.current_x = this.current_x;
        this.lastCard.current_y = this.current_y;
        this.lastCard.drawScale = this.drawScale;
        this.lastCard.angle = this.angle;
        this.lastCard.render(sb);
    }

    public void renderInLibrary(SpriteBatch sb) {
        if (this.lastCard == null) {
            super.renderInLibrary(sb);
            return;
        }
        renderPearlescentBG(sb);
        this.lastCard.current_x = this.current_x;
        this.lastCard.current_y = this.current_y;
        this.lastCard.drawScale = this.drawScale;
        this.lastCard.angle = this.angle;
        this.lastCard.renderInLibrary(sb);
    }

    void renderPearlescentBG(SpriteBatch sb) {
        ShaderProgram oldShader = sb.getShader();
//        sb.setShader(PEARLESCENCE_SHADER);
//        this.time += Gdx.graphics.getDeltaTime();
//        PEARLESCENCE_SHADER.setUniformf("x_time", this.time);
//        PEARLESCENCE_SHADER.setUniformf("alpha", this.transparency);
        try {
            Method renderCardBg = AbstractCard.class.getDeclaredMethod("renderCardBg", new Class[] { SpriteBatch.class, float.class, float.class });
            renderCardBg.setAccessible(true);
            renderCardBg.invoke(this, new Object[] { sb, Float.valueOf(this.current_x), Float.valueOf(this.current_y) });
        } catch (Exception e) {
            FriskMod.logger.warn("{}: reflection failed in EchoFlower bg: {}", FriskMod.modID, String.valueOf(e));
        }
        sb.setShader(oldShader);
    }

    public void renderCardTip(SpriteBatch sb) {
        if (this.lastCard != null) {
            try {
                Field renderTip = AbstractCard.class.getDeclaredField("renderTip");
                renderTip.setAccessible(true);
                renderTip.set(this.lastCard, renderTip.get(this));
            } catch (Exception e) {
                FriskMod.logger.warn("{}: reflection failed in EchoFlower tip: {}", FriskMod.modID, String.valueOf(e));
            }
            this.lastCard.renderCardTip(sb);
        } else {
            super.renderCardTip(sb);
        }
    }

    @Override
    public void upp() {
    }
}
