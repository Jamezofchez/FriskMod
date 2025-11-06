package friskmod.util;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.BobEffect;
import friskmod.FriskMod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DetailedIntent {
  private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(FriskMod.makeID("IntentStrings"));
  
  public static final String[] TEXT = uiStrings.TEXT;
  
  public static final String YOU = TEXT[0];
  
  public static final String SELF = TEXT[1];
  
  public static final String ALL_ENEMIES = TEXT[2];
  
  public static final String RANDOM_ENEMY = TEXT[3];
  
  public static final String LIFESTEAL = TEXT[4];
  
  public static final String ALL_MINIONS = TEXT[5];
  
  public static final String STEALS = TEXT[6];
  
  public static final String CLEANSE = TEXT[7];
  
  public static final String PARASITE = TEXT[8];
  
  public static final String DIES = TEXT[9];
  
  public static final String HALF_HEAL = TEXT[10];
  
  public static final String REMOVE_NEG_STR = TEXT[11];
  
  public static final String SUMMON = TEXT[12];
  
  public static final String WEAK = FriskMod.UIPath("detailedIntents/Weak.png");
  
  public static Texture WEAK_TEXTURE = TexLoader.getTexture(WEAK);
  
  public static final String FRAIL = FriskMod.UIPath("detailedIntents/Frail.png");
  
  public static Texture FRAIL_TEXTURE = TexLoader.getTexture(FRAIL);
  
  public static final String VULNERABLE = FriskMod.UIPath("detailedIntents/Vulnerable.png");
  
  public static Texture VULNERABLE_TEXTURE = TexLoader.getTexture(VULNERABLE);
  
  public static final String STRENGTH = FriskMod.UIPath("detailedIntents/Strength.png");
  
  public static Texture STRENGTH_TEXTURE = TexLoader.getTexture(STRENGTH);
  
  public static final String FLEX = FriskMod.UIPath("detailedIntents/Flex.png");
  
  public static Texture FLEX_TEXTURE = TexLoader.getTexture(FLEX);
  
  public static final String DEXTERITY = FriskMod.UIPath("detailedIntents/Dexterity.png");
  
  public static Texture DEXTERITY_TEXTURE = TexLoader.getTexture(DEXTERITY);
  
  public static final String PLATED_ARMOR = FriskMod.UIPath("detailedIntents/PlatedArmor.png");
  
  public static Texture PLATED_ARMOR_TEXTURE = TexLoader.getTexture(PLATED_ARMOR);
  
  public static final String METALLICIZE = FriskMod.UIPath("detailedIntents/Metal.png");
  
  public static Texture METALLICIZE_TEXTURE = TexLoader.getTexture(METALLICIZE);
  
  public static final String DRAW_DOWN = FriskMod.UIPath("detailedIntents/DrawDown.png");
  
  public static Texture DRAW_DOWN_TEXTURE = TexLoader.getTexture(DRAW_DOWN);
  
  public static final String DRAW_UP = FriskMod.UIPath("detailedIntents/DrawUp.png");
  
  public static Texture DRAW_UP_TEXTURE = TexLoader.getTexture(DRAW_UP);
  
  public static final String THORNS = FriskMod.UIPath("detailedIntents/Thorns.png");
  
  public static Texture THORNS_TEXTURE = TexLoader.getTexture(THORNS);
  
  public static final String WEB = FriskMod.UIPath("detailedIntents/Web.png");
  
  public static Texture WEB_TEXTURE = TexLoader.getTexture(WEB);
  
  public static final String RITUAL = FriskMod.UIPath("detailedIntents/Ritual.png");
  
  public static Texture RITUAL_TEXTURE = TexLoader.getTexture(RITUAL);

  public static final String HEAL = FriskMod.UIPath("detailedIntents/Heal.png");
  
  public static Texture HEAL_TEXTURE = TexLoader.getTexture(HEAL);
  
  public static final String BLOCK = FriskMod.UIPath("detailedIntents/Block.png");
  
  public static Texture BLOCK_TEXTURE = TexLoader.getTexture(BLOCK);
  
  public static final String DRAW_PILE = FriskMod.UIPath("detailedIntents/DrawPile.png");
  
  public static Texture DRAW_PILE_TEXTURE = TexLoader.getTexture(DRAW_PILE);
  
  public static final String DISCARD_PILE = FriskMod.UIPath("detailedIntents/DiscardPile.png");
  
  public static Texture DISCARD_PILE_TEXTURE = TexLoader.getTexture(DISCARD_PILE);
  
  public static final String BURN = FriskMod.UIPath("detailedIntents/Burn.png");
  
  public static Texture BURN_TEXTURE = TexLoader.getTexture(BURN);
  
  public static final String DAZED = FriskMod.UIPath("detailedIntents/Dazed.png");
  
  public static Texture DAZED_TEXTURE = TexLoader.getTexture(DAZED);
  
  public static final String SLIMED = FriskMod.UIPath("detailedIntents/Slimed.png");
  
  public static Texture SLIMED_TEXTURE = TexLoader.getTexture(SLIMED);
  
  public static final String VOID = FriskMod.UIPath("detailedIntents/Void.png");
  
  public static Texture VOID_TEXTURE = TexLoader.getTexture(VOID);
  
  public static final String WOUND = FriskMod.UIPath("detailedIntents/Wound.png");
  
  public static Texture WOUND_TEXTURE = TexLoader.getTexture(WOUND);
  
  private final AbstractMonster monster;
  
  private final int amount;
  
  private final Texture icon;
  
  private final TargetType target;
  
  private boolean overrideWithDescription;
  
  private String description;
  
  public enum TargetType {
    SIMPLE(""),
    YOU(DetailedIntent.YOU),
    SELF(DetailedIntent.SELF),
    ALL_ENEMIES(DetailedIntent.ALL_ENEMIES),
    RANDOM_ENEMY(DetailedIntent.RANDOM_ENEMY),
    DRAW_PILE(""),
    DISCARD_PILE(""),
    ALL_MINIONS(DetailedIntent.ALL_MINIONS);
    
    public String text;
    
    TargetType(String text) {
      this.text = text;
    }
  }
  
  float scaleWidth = Settings.scale;
  
  float scaleHeight = Settings.scale;
  
  BobEffect bob;
  
  public static Map<AbstractMonster, Map<Integer, ArrayList<DetailedIntent>>> intents = new HashMap<>();
  
  private final float Y_OFFSET = 46.0F;
  
  private float X_OFFSET = 106.0F;
  
  public DetailedIntent(AbstractMonster monster, int amount, Texture icon) {
    this(monster, amount, icon, TargetType.SIMPLE);
  }
  
  public DetailedIntent(AbstractMonster monster, int amount, Texture icon, TargetType target) {
    this.monster = monster;
    this.amount = amount;
    this.icon = icon;
    this.target = target;
    this.bob = (BobEffect)ReflectionHacks.getPrivate(monster, AbstractMonster.class, "bobEffect");
    if (monster.flipHorizontal)
      this.X_OFFSET = -this.X_OFFSET; 
  }
  
  public DetailedIntent(AbstractMonster monster, String description) {
    this(monster, 0, null, null);
    this.overrideWithDescription = true;
    this.description = description;
  }
  
  public void renderDetails(SpriteBatch sb, int yPosition, int intentNum) {
    Color color = (Color)ReflectionHacks.getPrivate(this.monster, AbstractMonster.class, "intentColor");
    Color white = Color.WHITE.cpy();
    white.a = color.a;
    sb.setColor(white);
    float textY = this.monster.intentHb.cY + 46.0F * this.scaleHeight * yPosition + this.bob.y;
    float iconY = this.monster.intentHb.cY - 16.0F + 46.0F * this.scaleHeight * yPosition + this.bob.y;
    float xOffset = this.X_OFFSET * this.scaleWidth * intentNum;
    if (!this.overrideWithDescription) {
      if (this.target == TargetType.SIMPLE) {
        FontHelper.renderFontCentered(sb, FontHelper.topPanelInfoFont, Integer.toString(this.amount), this.monster.intentHb.cX - 22.0F * this.scaleWidth + xOffset, textY, white);
        sb.draw(this.icon, this.monster.intentHb.cX - 16.0F + 8.0F * this.scaleWidth + xOffset, iconY, 16.0F, 16.0F, 32.0F, 32.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 32, 32, false, false);
      } else if (this.target == TargetType.DRAW_PILE || this.target == TargetType.DISCARD_PILE) {
        Texture pileTexture;
        if (this.target == TargetType.DRAW_PILE) {
          pileTexture = DRAW_PILE_TEXTURE;
        } else {
          pileTexture = DISCARD_PILE_TEXTURE;
        } 
        FontHelper.renderFontCentered(sb, FontHelper.topPanelInfoFont, Integer.toString(this.amount), this.monster.intentHb.cX - 32.0F * this.scaleWidth + xOffset, textY, white);
        sb.draw(this.icon, this.monster.intentHb.cX - 16.0F - 7.0F * this.scaleWidth + xOffset, iconY, 16.0F, 16.0F, 32.0F, 32.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 32, 32, false, false);
        sb.draw(pileTexture, this.monster.intentHb.cX - 16.0F + 27.0F * this.scaleWidth + xOffset, iconY, 16.0F, 16.0F, 32.0F, 32.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 32, 32, false, false);
      } else if (this.target == TargetType.ALL_ENEMIES || this.target == TargetType.ALL_MINIONS || this.target == TargetType.RANDOM_ENEMY) {
        FontHelper.renderFontCentered(sb, FontHelper.topPanelInfoFont, Integer.toString(this.amount), this.monster.intentHb.cX - 42.0F * this.scaleWidth + xOffset, textY, white);
        sb.draw(this.icon, this.monster.intentHb.cX - 16.0F - 12.0F * this.scaleWidth + xOffset, iconY, 16.0F, 16.0F, 32.0F, 32.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 32, 32, false, false);
        FontHelper.renderFontCentered(sb, FontHelper.topPanelInfoFont, "-> " + this.target.text, this.monster.intentHb.cX - 42.0F * this.scaleWidth + 145.0F * this.scaleWidth + xOffset, textY, white);
      } else {
        FontHelper.renderFontCentered(sb, FontHelper.topPanelInfoFont, Integer.toString(this.amount), this.monster.intentHb.cX - 42.0F * this.scaleWidth + xOffset, textY, white);
        sb.draw(this.icon, this.monster.intentHb.cX - 16.0F - 12.0F * this.scaleWidth + xOffset, iconY, 16.0F, 16.0F, 32.0F, 32.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 32, 32, false, false);
        FontHelper.renderFontCentered(sb, FontHelper.topPanelInfoFont, "-> " + this.target.text, this.monster.intentHb.cX - 42.0F * this.scaleWidth + 90.0F * this.scaleWidth + xOffset, textY, white);
      } 
    } else {
      FontHelper.renderFontCentered(sb, FontHelper.topPanelInfoFont, this.description, this.monster.intentHb.cX - 12.0F * this.scaleWidth + xOffset, textY, white);
    } 
  }
}


/* Location:              C:\Program Files (x86)\Steam\steamapps\workshop\content\646570\2396661789\Ruina.jar!\ruin\\util\DetailedIntent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */