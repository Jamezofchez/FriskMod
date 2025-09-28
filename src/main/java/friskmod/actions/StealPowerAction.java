/*     */ package thePackmaster.actions.thieverypack;
/*     */ import basemod.BaseMod;
/*     */ import basemod.ReflectionHacks;
/*     */ import com.badlogic.gdx.graphics.Color;
/*     */ import com.badlogic.gdx.math.MathUtils;
/*     */ import com.megacrit.cardcrawl.actions.AbstractGameAction;
/*     */ import com.megacrit.cardcrawl.actions.animations.TalkAction;
/*     */ import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
/*     */ import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
/*     */ import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
/*     */ import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
/*     */ import com.megacrit.cardcrawl.cards.AbstractCard;
/*     */ import com.megacrit.cardcrawl.characters.AbstractPlayer;
/*     */ import com.megacrit.cardcrawl.core.AbstractCreature;
/*     */ import com.megacrit.cardcrawl.core.Settings;
/*     */ import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
/*     */ import com.megacrit.cardcrawl.localization.UIStrings;
/*     */ import com.megacrit.cardcrawl.monsters.AbstractMonster;
/*     */ import com.megacrit.cardcrawl.powers.AbstractPower;
/*     */ import com.megacrit.cardcrawl.powers.AngerPower;
/*     */ import com.megacrit.cardcrawl.powers.ArtifactPower;
/*     */ import com.megacrit.cardcrawl.powers.MetallicizePower;
/*     */ import com.megacrit.cardcrawl.powers.PlatedArmorPower;
/*     */ import com.megacrit.cardcrawl.powers.StasisPower;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import thePackmaster.powers.thieverypack.ThieveryMasteryPower;
/*     */ 
/*     */ public class StealPowerAction extends AbstractGameAction {
/*  31 */   public static final String ID = SpireAnniversary5Mod.makeID("StealPowerAction");
/*  32 */   private static final UIStrings UI_STRINGS = CardCrawlGame.languagePack.getUIString(ID);
/*     */   
/*  34 */   private static final float POWER_ICON_PADDING_X = 48.0F * Settings.scale; public float t; public HashSet<AbstractPower> affectedPowers; public final AnimationMode animationMode; public final ArrayList<AbstractMonster> monsters;
/*     */   public final HashMap<String, Integer> powIDAmountMap;
/*     */   public HashMap<String, Float> targetXMap;
/*     */   public HashMap<String, Float> targetYMap;
/*     */   
/*  39 */   public enum AnimationMode { TO_PLAYER, HAMMERED; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  47 */   public static StealPowerAction activatedInstance = null;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean didMiss;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StealPowerAction(ArrayList<AbstractMonster> monsters, HashMap<String, Integer> powIDAmountMap, AnimationMode animationMode) {
/*  59 */     this.actionType = AbstractGameAction.ActionType.WAIT;
/*  60 */     this.duration = this.startDuration = Settings.ACTION_DUR_LONG * 0.75F;
/*  61 */     this.monsters = monsters;
/*  62 */     this.powIDAmountMap = powIDAmountMap;
/*  63 */     this.animationMode = animationMode;
/*     */   }
/*     */ 
/*     */   
/*     */   public StealPowerAction(AbstractMonster m, HashMap<String, Integer> powIDAmountMap, AnimationMode animationMode) {
/*  68 */     this(new ArrayList<AbstractMonster>(m) {  }, powIDAmountMap, animationMode);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StealPowerAction(AbstractMonster m, ArrayList<String> powIDs, AnimationMode animationMode) {
/*  77 */     this(new ArrayList<AbstractMonster>(m) {  }, powIDs, animationMode);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StealPowerAction(ArrayList<AbstractMonster> monsters, ArrayList<String> powIDs, AnimationMode animationMode) {
/*  86 */     this(monsters, new HashMap<String, Integer>(powIDs) {  }, animationMode);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StealPowerAction(AbstractMonster m, String powID, AnimationMode animationMode, int amount) {
/*  95 */     this(new ArrayList<AbstractMonster>(m) {  }, new HashMap<String, Integer>(powID, amount) {  }animationMode);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void calcPosition(String powID, float[] x, float[] y, Color c, boolean isAmount) {
/* 103 */     if (this.animationMode == AnimationMode.TO_PLAYER) {
/* 104 */       float targetX = ((Float)this.targetXMap.get(powID)).floatValue();
/* 105 */       float targetY = ((Float)this.targetYMap.get(powID)).floatValue();
/* 106 */       x[0] = MathUtils.lerp(x[0], targetX + (isAmount ? (32.0F * Settings.scale) : 0.0F), this.t);
/* 107 */       y[0] = MathUtils.lerp(y[0], targetY - (isAmount ? (18.0F * Settings.scale) : 0.0F), this.t);
/* 108 */     } else if (this.animationMode == AnimationMode.HAMMERED) {
/* 109 */       float targetX = 0.0F;
/* 110 */       float targetY = -50.0F * Settings.scale;
/* 111 */       if (this.t < 0.4F) {
/* 112 */         float newt = (0.4F - this.t) / 0.4F;
/* 113 */         newt = 1.0F - newt * newt * newt;
/* 114 */         x[0] = x[0] + targetX * newt;
/* 115 */         y[0] = y[0] + targetY * newt;
/* 116 */       } else if (this.t < 0.7F) {
/* 117 */         x[0] = x[0] + targetX;
/* 118 */         y[0] = y[0] + targetY;
/*     */       } else {
/* 120 */         float newt = (this.t - 0.7F) / 0.3F;
/* 121 */         c.a *= (1.0F - newt) * (1.0F - newt);
/* 122 */         x[0] = x[0] + targetX;
/* 123 */         y[0] = y[0] + targetY - 20.0F * newt * Settings.scale;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void update() {
/* 130 */     AbstractPlayer p = AbstractDungeon.player;
/*     */     
/* 132 */     if (this.duration == this.startDuration) {
/* 133 */       if (this.monsters.isEmpty()) {
/* 134 */         didMiss = true;
/* 135 */         this.isDone = true;
/* 136 */         activatedInstance = null;
/*     */         
/*     */         return;
/*     */       } 
/* 140 */       if (this.animationMode == AnimationMode.TO_PLAYER) {
/* 141 */         this.targetXMap = new HashMap<>();
/* 142 */         this.targetYMap = new HashMap<>();
/* 143 */         for (String ID : this.powIDAmountMap.keySet()) {
/* 144 */           int index = 0;
/* 145 */           for (; index < p.powers.size(); index++) {
/* 146 */             final AbstractPower pow = p.powers.get(index);
/* 147 */             if (pow.ID.equals(ID))
/*     */               break; 
/*     */           } 
/* 150 */           this.targetXMap.put(ID, Float.valueOf(p.hb.cX - p.hb.width / 2.0F + 10.0F * Settings.scale + index * POWER_ICON_PADDING_X));
/* 151 */           this.targetYMap.put(ID, Float.valueOf(p.hb.cY - p.hb.height / 2.0F + ((Float)ReflectionHacks.getPrivate(p, AbstractCreature.class, "hbYOffset")).floatValue() - 48.0F * Settings.scale));
/*     */         } 
/* 153 */       } else if (this.animationMode == AnimationMode.HAMMERED) {
/*     */       
/*     */       } 
/*     */       
/* 157 */       this.affectedPowers = new HashSet<>();
/* 158 */       for (AbstractMonster m : this.monsters) {
/* 159 */         for (AbstractPower pow : m.powers) {
/* 160 */           if (this.powIDAmountMap.containsKey(pow.ID) && (!pow.canGoNegative || pow.amount > 0)) {
/* 161 */             this.affectedPowers.add(pow);
/*     */           }
/*     */         } 
/*     */       } 
/*     */       
/* 166 */       if (this.affectedPowers.isEmpty()) {
/* 167 */         didMiss = true;
/* 168 */         this.isDone = true;
/* 169 */         activatedInstance = null;
/*     */         return;
/*     */       } 
/* 172 */       this.t = 0.0F;
/*     */     } 
/* 174 */     tickDuration();
/* 175 */     didMiss = false;
/*     */     
/* 177 */     if (this.isDone) {
/* 178 */       this.t = 1.0F;
/* 179 */       int multiplier = 1;
/* 180 */       AbstractPower tmp = AbstractDungeon.player.getPower(ThieveryMasteryPower.POWER_ID);
/* 181 */       if (tmp != null) {
/* 182 */         tmp.flash();
/* 183 */         multiplier = 2;
/*     */       } 
/*     */       
/* 186 */       addToTop(new AbstractGameAction()
/*     */           {
/*     */             public void update() {
/* 189 */               StealPowerAction.activatedInstance = null;
/* 190 */               this.isDone = true;
/*     */             }
/*     */           });
/*     */       
/* 194 */       ArrayList<String> incompatibles = new ArrayList<>();
/* 195 */       for (AbstractPower pow : this.affectedPowers) {
/* 196 */         final int stealAmount, mapAmount = ((Integer)this.powIDAmountMap.get(pow.ID)).intValue();
/*     */ 
/*     */ 
/*     */         
/* 200 */         if (mapAmount <= 0) {
/* 201 */           stealAmount = pow.amount;
/*     */         } else {
/* 203 */           stealAmount = Math.min(pow.amount, mapAmount);
/*     */         } 
/*     */         
/* 206 */         int gainAmount = stealAmount * multiplier;
/* 207 */         AbstractPower playerPower = getDuplicatedPower((AbstractCreature)p, pow, gainAmount);
/* 208 */         if (playerPower == null) {
/* 209 */           incompatibles.add(pow.name);
/*     */         } else {
/* 211 */           addToTop((AbstractGameAction)new ApplyPowerAction((AbstractCreature)p, (AbstractCreature)p, playerPower, gainAmount, true)
/*     */               {
/*     */                 public void update() {
/* 214 */                   super.update();
/* 215 */                   this.isDone = true;
/*     */                 }
/*     */               });
/*     */         } 
/*     */ 
/*     */         
/* 221 */         if (stealAmount >= pow.amount) {
/* 222 */           addToTop((AbstractGameAction)new RemoveSpecificPowerAction(pow.owner, (AbstractCreature)p, pow)
/*     */               {
/*     */                 public void update() {
/* 225 */                   super.update();
/* 226 */                   this.isDone = true; }
/*     */               });
/*     */           continue;
/*     */         } 
/* 230 */         addToTop(new AbstractGameAction()
/*     */             {
/*     */               public void update() {
/* 233 */                 pow.amount -= stealAmount;
/* 234 */                 StealPowerAction.this.affectedPowers.remove(pow);
/* 235 */                 this.isDone = true;
/*     */               }
/*     */             });
/*     */       } 
/*     */ 
/*     */       
/* 241 */       if (!incompatibles.isEmpty()) {
/* 242 */         String s = String.format(UI_STRINGS.TEXT[(incompatibles.size() > 1) ? 1 : 0], new Object[] { String.join(", ", (Iterable)incompatibles) });
/* 243 */         addToTop((AbstractGameAction)new TalkAction(true, s, 0.1F, 3.5F));
/*     */       } 
/*     */     } else {
/* 246 */       this.t = (this.startDuration - this.duration) / this.startDuration;
/* 247 */       activatedInstance = this;
/*     */     } 
/*     */   }
/*     */   private AbstractPower getDuplicatedPower(final AbstractCreature c, AbstractPower pow, int gainAmount) {
/*     */     final AbstractCard card;
/* 252 */     switch (pow.ID) {
/*     */       case "Strength":
/* 254 */         return (AbstractPower)new StrengthPower(c, gainAmount);
/*     */       case "Artifact":
/* 256 */         return (AbstractPower)new ArtifactPower(c, gainAmount);
/*     */       case "Plated Armor":
/* 258 */         return (AbstractPower)new PlatedArmorPower(c, gainAmount);
/*     */       case "Thorns":
/*     */       case "Sharp Hide":
/* 261 */         return (AbstractPower)new ThornsPower(c, gainAmount);
/*     */       case "Metallicize":
/* 263 */         return (AbstractPower)new MetallicizePower(c, gainAmount);
/*     */       case "Intangible":
/* 265 */         return (AbstractPower)new IntangiblePlayerPower(c, gainAmount + 1);
/*     */       case "Ritual":
/* 267 */         return (AbstractPower)new RitualPower(c, gainAmount, true);
/*     */       case "Anger":
/* 269 */         return (AbstractPower)new AngerPower(c, gainAmount);
/*     */       case "Stasis":
/* 271 */         card = (AbstractCard)ReflectionHacks.getPrivate(pow, StasisPower.class, "card");
/* 272 */         if (card != null) {
/* 273 */           return (AbstractPower)new StasisPower(c, card)
/*     */             {
/*     */               public void onInitialApplication() {
/* 276 */                 if (AbstractDungeon.player.hand.size() < BaseMod.MAX_HAND_SIZE) {
/* 277 */                   addToBot((AbstractGameAction)new MakeTempCardInHandAction(card, false, true));
/*     */                 } else {
/* 279 */                   addToBot((AbstractGameAction)new MakeTempCardInDiscardAction(card, true));
/*     */                 } 
/* 281 */                 addToBot((AbstractGameAction)new RemoveSpecificPowerAction(c, c, (AbstractPower)this));
/*     */               }
/*     */             };
/*     */         }
/* 285 */         return null;
/*     */     } 
/*     */ 
/*     */     
/* 289 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Program Files (x86)\Steam\steamapps\workshop\content\646570\2920075378\ThePackmaster.jar!\thePackmaster\actions\thieverypack\StealPowerAction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */