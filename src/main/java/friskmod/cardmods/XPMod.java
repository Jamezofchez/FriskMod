/*     */ package friskmod.cardmods;
/*     */ 
/*     */ import friskmod.FriskMod;
/*     */ import friskmod.patches.CardTemperatureFields;
/*     *
/*     */ import friskmod.util.KeywordManager;
/*     */ import friskmod.util.TexLoader;
/*     */ import friskmod.util.Wiz;
/*     */ import basemod.abstracts.AbstractCardModifier;
/*     */ import basemod.helpers.CardModifierManager;
/*     */ import com.badlogic.gdx.graphics.Texture;
/*     */ import com.badlogic.gdx.graphics.g2d.SpriteBatch;
/*     */ import com.badlogic.gdx.math.MathUtils;
/*     */ import com.evacipated.cardcrawl.mod.stslib.util.extraicons.ExtraIcons;
/*     */ import com.megacrit.cardcrawl.actions.AbstractGameAction;
/*     */ import com.megacrit.cardcrawl.actions.common.DrawCardAction;
/*     */ import com.megacrit.cardcrawl.actions.utility.UseCardAction;
/*     */ import com.megacrit.cardcrawl.cards.AbstractCard;
/*     */ import com.megacrit.cardcrawl.core.AbstractCreature;
/*     */ import com.megacrit.cardcrawl.core.CardCrawlGame;
/*     */ import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
/*     */ import com.megacrit.cardcrawl.localization.CardStrings;
/*     */ import com.megacrit.cardcrawl.localization.UIStrings;

/*     */
/*     */ public class TemperatureMod
/*     */   extends AbstractCardModifier {
/*  28 */   public static String ID = FriskMod.makeID(TemperatureMod.class.getSimpleName());
/*  29 */   public static CardStrings strings = CardCrawlGame.languagePack.getCardStrings(ID);
/*  30 */   public static UIStrings UIstrings = CardCrawlGame.languagePack.getUIString(FriskMod.makeID("TemperatureContext"));
/*  31 */   public static String[] TEXT = strings.EXTENDED_DESCRIPTION;
/*     */   
/*  33 */   private static final Texture tex = TexLoader.getTexture("friskmodResources/images/icons/Temp.png");
/*  34 */   private static final Texture hot = TexLoader.getTexture("friskmodResources/images/icons/Hot.png");
/*  35 */   private static final Texture cold = TexLoader.getTexture("friskmodResources/images/icons/Cold.png");
/*     */   public static final int COLD = -1;
/*     */   public static final int HOT = 1;
/*  38 */   int heatMod = 0;
/*     */   
/*     */   public TemperatureMod() {
/*  41 */     this.priority = -2;
/*     */   }
/*     */   
/*     */   public TemperatureMod(int heatMod) {
/*  45 */     this.priority = -2;
/*  46 */     this.heatMod = heatMod;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onInitialApplication(AbstractCard card) {
/*  51 */     super.onInitialApplication(card);
/*  52 */     if (this.heatMod != 0) {
/*  53 */       CardTemperatureFields.addHeat(card, this.heatMod);
/*     */     }
/*     */   }
/*     */   
/*     */   public String modifyDescription(String rawDescription, AbstractCard card) {
/*  58 */     if (CardTemperatureFields.getCardHeat(card) >= 1 && !CardModifierManager.hasModifier(card, FlaminMod.ID)) {
/*  59 */       String out = KeywordManager.HOT.replace("friskmod".toLowerCase(), "");
/*  60 */       out = out.replace(":", "");
/*  61 */       out = out.substring(0, 1).toUpperCase() + out.substring(1);
/*  62 */       return "friskmod".toLowerCase() + ":" + out + UIstrings.TEXT[0] + " NL " + rawDescription;
/*     */     } 
/*     */     
/*  65 */     if (CardTemperatureFields.getCardHeat(card) <= -1) {
/*  66 */       String out = KeywordManager.COLD.replace("friskmod".toLowerCase(), "");
/*  67 */       out = out.replace(":", "");
/*  68 */       out = out.substring(0, 1).toUpperCase() + out.substring(1);
/*  69 */       return "friskmod".toLowerCase() + ":" + out + UIstrings.TEXT[0] + " NL " + rawDescription;
/*     */     } 
/*     */     
/*  72 */     return rawDescription;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onUse(AbstractCard card, AbstractCreature target, UseCardAction action) {
/*  77 */     int heat = CardTemperatureFields.getCardHeat(card);
/*     */     
/*  79 */     if (heat >= 1) {
/*  80 */       EvaporatePanelPatches.EvaporateField.evaporate.set(card, Boolean.valueOf(true));
/*     */     }
/*     */     
/*  83 */     if (heat <= -1) {
/*  84 */       Wiz.att((AbstractGameAction)new DrawCardAction(Math.abs(heat)));
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean shouldApply(AbstractCard card) {
/*  89 */     if (CardModifierManager.hasModifier(card, ID)) {
/*  90 */       card.initializeDescription();
/*  91 */       return false;
/*     */     } 
/*  93 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onUpdate(AbstractCard card) {
/*  98 */     super.onUpdate(card);
/*  99 */     int heat = CardTemperatureFields.getCardHeat(card);
/* 100 */     if (AbstractDungeon.player != null && 
/* 101 */       heat <= -1 && MathUtils.random(20) < 1 && (AbstractDungeon.player.hand.contains(card) || AbstractDungeon.player.limbo.contains(card))) {
/*     */     }
/*     */   }
/*     */   
/*     */   public void onRender(AbstractCard card, SpriteBatch sb) {
/* 107 */     int heat = CardTemperatureFields.getCardHeat(card);
/* 108 */     if (heat >= 1)
/* 109 */       ExtraIcons.icon(hot).text(String.valueOf(heat)).render(card); 
/* 110 */     if (heat <= -1) {
/* 111 */       ExtraIcons.icon(cold).text(String.valueOf(Math.abs(heat))).render(card);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void onSingleCardViewRender(AbstractCard card, SpriteBatch sb) {
/* 117 */     int heat = CardTemperatureFields.getCardHeat(card);
/* 118 */     if (heat >= 1)
/* 119 */       ExtraIcons.icon(hot).text(String.valueOf(heat)).render(card); 
/* 120 */     if (heat <= -1) {
/* 121 */       ExtraIcons.icon(cold).text(String.valueOf(Math.abs(heat))).render(card);
/*     */     }
/*     */   }
/*     */   
/*     */   public String identifier(AbstractCard card) {
/* 126 */     return ID;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isInherent(AbstractCard card) {
/* 131 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public AbstractCardModifier makeCopy() {
/* 136 */     return new TemperatureMod();
/*     */   }
/*     */ }


/* Location:              C:\Program Files (x86)\Steam\steamapps\workshop\content\646570\3390561252\friskmod.jar!\friskmod\cardmods\TemperatureMod.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */