/*     */ package friskmod.patches;
/*     */ import Snowpunk.cardmods.EverburnMod;
/*     */ import Snowpunk.cardmods.TemperatureMod;
/*     */ import Snowpunk.cards.interfaces.OnTempChangeCard;
/*     */ import Snowpunk.powers.CoolNextCardPower;
/*     */ import Snowpunk.powers.FireballPower;
/*     */ import Snowpunk.powers.FireburstPower;
/*     */ import Snowpunk.util.Wiz;
/*     */ import basemod.abstracts.AbstractCardModifier;
/*     */ import basemod.helpers.CardModifierManager;
/*     */ import com.badlogic.gdx.graphics.Color;
/*     */ import com.evacipated.cardcrawl.modthespire.lib.SpireField;
/*     */ import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
/*     */ import com.megacrit.cardcrawl.cards.AbstractCard;
/*     */ 
/*     */ public class CardTemperatureFields {
/*  17 */   public static final Color HOT_TINT = new Color(1.0F, 0.81960785F, 0.81960785F, 1.0F);
/*  18 */   public static final Color COLD_TINT = new Color(0.81960785F, 0.99215686F, 1.0F, 1.0F);
/*  19 */   public static final Color STABLE_TINT = Color.WHITE.cpy();
/*  20 */   public static final Color OVERHEAT_TINT = new Color(1.0F, 0.50980395F, 0.50980395F, 1.0F);
/*  21 */   public static final Color FROZEN_TINT = new Color(0.50980395F, 0.9843137F, 1.0F, 1.0F);
/*     */   public static final int COLD = -1;
/*     */   public static final int HOT = 1;
/*     */   
/*     */   @SpirePatch(clz = AbstractCard.class, method = "<class>")
/*  26 */   public static class TemperatureFields { public static SpireField<Integer> inherentHeat = new SpireField(() -> Integer.valueOf(0));
/*  27 */     public static SpireField<Integer> addedHeat = new SpireField(() -> Integer.valueOf(0)); }
/*     */ 
/*     */   
/*     */   public static int getCardHeat(AbstractCard card) {
/*  31 */     return ((Integer)TemperatureFields.inherentHeat.get(card)).intValue() + ((Integer)TemperatureFields.addedHeat.get(card)).intValue();
/*     */   }
/*     */   
/*     */   public static int getCardHeatFloored(AbstractCard card) {
/*  35 */     if (((Integer)TemperatureFields.inherentHeat.get(card)).intValue() + ((Integer)TemperatureFields.addedHeat.get(card)).intValue() < 0)
/*  36 */       return -1; 
/*  37 */     if (((Integer)TemperatureFields.inherentHeat.get(card)).intValue() + ((Integer)TemperatureFields.addedHeat.get(card)).intValue() > 0)
/*  38 */       return 1; 
/*  39 */     return 0;
/*     */   }
/*     */   
/*     */   public static int getExpectedCardHeatWhenPlayed(AbstractCard card) {
/*  43 */     int heat = getCardHeat(card);
/*  44 */     if (Wiz.adp() != null) {
/*  45 */       if (Wiz.adp().hasPower(FireballPower.POWER_ID) || Wiz.adp().hasPower(FireburstPower.POWER_ID)) {
/*  46 */         if (heat <= 0) {
/*  47 */           heat = 1;
/*     */         } else {
/*  49 */           heat++;
/*     */         } 
/*  51 */         if (Wiz.adp().hasPower(FireburstPower.POWER_ID))
/*  52 */           heat++; 
/*     */       } 
/*  54 */       if (Wiz.adp().hasPower(CoolNextCardPower.POWER_ID)) {
/*  55 */         if (heat >= 0) {
/*  56 */           heat = -1;
/*     */         } else {
/*  58 */           heat--;
/*     */         } 
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  68 */     return heat;
/*     */   }
/*     */   
/*     */   public static void reduceTemp(AbstractCard card) {
/*  72 */     int prevTotal = ((Integer)TemperatureFields.inherentHeat.get(card)).intValue() + ((Integer)TemperatureFields.addedHeat.get(card)).intValue();
/*  73 */     if (prevTotal > 0)
/*  74 */       TemperatureFields.addedHeat.set(card, Integer.valueOf(((Integer)TemperatureFields.addedHeat.get(card)).intValue() - 1)); 
/*  75 */     if (prevTotal < 0)
/*  76 */       TemperatureFields.addedHeat.set(card, Integer.valueOf(((Integer)TemperatureFields.addedHeat.get(card)).intValue() + 1)); 
/*     */   }
/*     */   
/*     */   public static void addInherentHeat(AbstractCard card, int amount) {
/*  80 */     if (amount == 0)
/*     */       return; 
/*  82 */     addAndClampHeat(card, amount, true);
/*  83 */     CardModifierManager.addModifier(card, (AbstractCardModifier)new TemperatureMod());
/*     */   }
/*     */   
/*     */   public static void addHeat(AbstractCard card, int amount) {
/*  87 */     if (amount == 0)
/*     */       return; 
/*  89 */     addAndClampHeat(card, amount, false);
/*  90 */     CardModifierManager.addModifier(card, (AbstractCardModifier)new TemperatureMod());
/*     */   }
/*     */   
/*     */   public static void setAddedHeat(AbstractCard card, int amount) {
/*  94 */     if (amount == 0)
/*     */       return; 
/*  96 */     TemperatureFields.addedHeat.set(card, Integer.valueOf(amount));
/*  97 */     CardModifierManager.addModifier(card, (AbstractCardModifier)new TemperatureMod());
/*     */   }
/*     */   
/*     */   private static void addAndClampHeat(AbstractCard card, int amount, boolean addInherent) {
/* 101 */     int prevTotal = ((Integer)TemperatureFields.inherentHeat.get(card)).intValue() + ((Integer)TemperatureFields.addedHeat.get(card)).intValue();
/*     */     
/* 103 */     int heat = 0;
/* 104 */     if (amount > 0) {
/* 105 */       if (prevTotal <= 0)
/* 106 */       { heat = amount; }
/*     */       else
/* 108 */       { heat = prevTotal + amount; } 
/* 109 */     } else if (amount < 0) {
/* 110 */       if (prevTotal > 0) {
/* 111 */         heat = amount;
/*     */       } else {
/* 113 */         heat = prevTotal + amount;
/*     */       } 
/*     */     } 
/* 116 */     if (addInherent) {
/* 117 */       TemperatureFields.inherentHeat.set(card, Integer.valueOf(heat));
/* 118 */       TemperatureFields.addedHeat.set(card, Integer.valueOf(0));
/*     */     } else {
/* 120 */       TemperatureFields.addedHeat.set(card, Integer.valueOf(heat - ((Integer)TemperatureFields.inherentHeat.get(card)).intValue()));
/*     */     } 
/*     */     
/* 123 */     int inherent = ((Integer)TemperatureFields.inherentHeat.get(card)).intValue();
/* 124 */     int added = ((Integer)TemperatureFields.addedHeat.get(card)).intValue();
/*     */     
/* 126 */     if (CardModifierManager.hasModifier(card, EverburnMod.ID)) {
/* 127 */       if (inherent < 1) {
/* 128 */         TemperatureFields.inherentHeat.set(card, Integer.valueOf(1));
/* 129 */         inherent = ((Integer)TemperatureFields.inherentHeat.get(card)).intValue();
/*     */       } 
/* 131 */       if (inherent + added < 1) {
/* 132 */         TemperatureFields.addedHeat.set(card, Integer.valueOf(1 - inherent));
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 146 */     if (added + inherent != prevTotal) {
/* 147 */       flashHeatColor(card);
/* 148 */       if (card instanceof OnTempChangeCard) {
/* 149 */         ((OnTempChangeCard)card).onTempChange(added + inherent - prevTotal);
/*     */       }
/*     */     } 
/*     */     
/* 153 */     TemperatureFields.inherentHeat.set(card, Integer.valueOf(inherent));
/* 154 */     TemperatureFields.addedHeat.set(card, Integer.valueOf(added));
/*     */   }
/*     */   
/*     */   public static Color getCardTint(AbstractCard card) {
/* 158 */     switch (getCardHeatFloored(card)) {
/*     */       case -1:
/* 160 */         return COLD_TINT;
/*     */       case 1:
/* 162 */         return HOT_TINT;
/*     */     } 
/* 164 */     return STABLE_TINT;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void flashHeatColor(AbstractCard card) {
/* 169 */     switch (getCardHeatFloored(card)) {
/*     */       case -1:
/* 171 */         card.superFlash(COLD_TINT.cpy());
/*     */         break;
/*     */       case 0:
/* 174 */         card.superFlash(Color.WHITE.cpy());
/*     */         break;
/*     */       case 1:
/* 177 */         card.superFlash(HOT_TINT.cpy());
/*     */         break;
/*     */     } 
/*     */   }
/*     */   
/*     */   public static boolean canModTemp(AbstractCard card, int amount) {
/* 183 */     int heat = getCardHeat(card);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 188 */     return true;
/*     */   }
/*     */   
/*     */   @SpirePatch(clz = AbstractCard.class, method = "makeStatEquivalentCopy")
/*     */   public static class MakeStatEquivalentCopy
/*     */   {
/*     */     public static AbstractCard Postfix(AbstractCard result, AbstractCard self) {
/* 195 */       CardTemperatureFields.setAddedHeat(result, ((Integer) TemperatureFields.addedHeat.get(self)).intValue());
/* 196 */       return result;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Program Files (x86)\Steam\steamapps\workshop\content\646570\3390561252\Snowpunk.jar!\Snowpunk\patches\CardTemperatureFields.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */