/*    */ package thePackmaster.patches.thieverypack;
/*    */ 
/*    */ import com.badlogic.gdx.graphics.Color;
/*    */ import com.badlogic.gdx.graphics.g2d.SpriteBatch;
/*    */ import com.badlogic.gdx.graphics.g2d.TextureRegion;
/*    */ import com.evacipated.cardcrawl.modthespire.lib.ByRef;
/*    */ import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
/*    */ import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
/*    */ import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
/*    */ import com.megacrit.cardcrawl.core.Settings;
/*    */ import com.megacrit.cardcrawl.helpers.FontHelper;
/*    */ import com.megacrit.cardcrawl.powers.AbstractPower;
/*    */ import thePackmaster.actions.thieverypack.StealPowerAction;
/*    */ 
/*    */ public class StealPowerPatch {
/* 16 */   static float origAlpha = -3434.228F; static final float nullMagicNumber = -3434.228F;
/*    */   static final int nullOrigAmount = -1628;
/* 18 */   static int origAmount = -1628;
/*    */   
/*    */   @SpirePatch2(clz = AbstractPower.class, method = "renderIcons")
/*    */   public static class RenderIconPatch {
/*    */     @SpirePrefixPatch
/*    */     public static void Prefix(AbstractPower __instance, SpriteBatch sb, @ByRef float[] x, @ByRef float[] y, Color c) {
/* 24 */       if (StealPowerAction.activatedInstance != null && StealPowerAction.activatedInstance.affectedPowers.contains(__instance)) {
/* 25 */         StealPowerPatch.origAlpha = c.a;
/* 26 */         int mapAmount = ((Integer)StealPowerAction.activatedInstance.powIDAmountMap.get(__instance.ID)).intValue();
/* 27 */         if (mapAmount > 0 && 
/* 28 */           __instance.amount > mapAmount) {
/* 29 */           renderIcons(__instance, sb, x[0], y[0], c);
/*    */         }
/*    */         
/* 32 */         StealPowerAction.activatedInstance.calcPosition(__instance.ID, x, y, c, false);
/*    */       } 
/*    */     }
/*    */     
/*    */     private static void renderIcons(AbstractPower instance, SpriteBatch sb, float x, float y, Color c) {
/* 37 */       sb.setColor(c);
/* 38 */       if (instance.img != null) {
/* 39 */         sb.draw(instance.img, x - 12.0F, y - 12.0F, 16.0F, 16.0F, 32.0F, 32.0F, Settings.scale * 1.5F, Settings.scale * 1.5F, 0.0F, 0, 0, 32, 32, false, false);
/* 40 */       } else if (Settings.isMobile) {
/* 41 */         sb.draw((TextureRegion)instance.region48, x - instance.region48.packedWidth / 2.0F, y - instance.region48.packedHeight / 2.0F, instance.region48.packedWidth / 2.0F, instance.region48.packedHeight / 2.0F, instance.region48.packedWidth, instance.region48.packedHeight, Settings.scale * 1.17F, Settings.scale * 1.17F, 0.0F);
/*    */       } else {
/* 43 */         sb.draw((TextureRegion)instance.region48, x - instance.region48.packedWidth / 2.0F, y - instance.region48.packedHeight / 2.0F, instance.region48.packedWidth / 2.0F, instance.region48.packedHeight / 2.0F, instance.region48.packedWidth, instance.region48.packedHeight, Settings.scale, Settings.scale, 0.0F);
/*    */       } 
/*    */     }
/*    */     
/*    */     @SpirePostfixPatch
/*    */     public static void Postfix(Color c) {
/* 49 */       if (StealPowerPatch.origAlpha != -3434.228F) {
/*    */         
/* 51 */         c.a = StealPowerPatch.origAlpha;
/* 52 */         StealPowerPatch.origAlpha = -3434.228F;
/*    */       } 
/*    */     }
/*    */   }
/*    */   
/*    */   @SpirePatch2(clz = AbstractPower.class, method = "renderAmount")
/*    */   public static class RenderAmountPatch {
/*    */     @SpirePrefixPatch
/*    */     public static void Prefix(AbstractPower __instance, SpriteBatch sb, @ByRef float[] x, @ByRef float[] y, Color c, float ___fontScale) {
/* 61 */       if (StealPowerAction.activatedInstance != null && StealPowerAction.activatedInstance.affectedPowers.contains(__instance)) {
/* 62 */         StealPowerPatch.origAlpha = c.a;
/* 63 */         int mapAmount = ((Integer)StealPowerAction.activatedInstance.powIDAmountMap.get(__instance.ID)).intValue();
/* 64 */         if (mapAmount > 0) {
/* 65 */           int amountLeft = __instance.amount - mapAmount;
/* 66 */           if (amountLeft > 0) {
/* 67 */             StealPowerPatch.origAmount = __instance.amount;
/* 68 */             FontHelper.renderFontRightTopAligned(sb, FontHelper.powerAmountFont, Integer.toString(amountLeft), x[0], y[0], ___fontScale, c);
/* 69 */             __instance.amount = StealPowerAction.activatedInstance.amount;
/*    */           } 
/*    */         } 
/* 72 */         StealPowerAction.activatedInstance.calcPosition(__instance.ID, x, y, c, true);
/*    */       } 
/*    */     }
/*    */     
/*    */     @SpirePostfixPatch
/*    */     public static void Postfix(AbstractPower __instance, Color c) {
/* 78 */       if (StealPowerPatch.origAlpha != -3434.228F) {
/*    */         
/* 80 */         c.a = StealPowerPatch.origAlpha;
/* 81 */         StealPowerPatch.origAlpha = -3434.228F;
/*    */       } 
/* 83 */       if (StealPowerPatch.origAmount != -1628) {
/*    */         
/* 85 */         __instance.amount = StealPowerPatch.origAmount;
/* 86 */         StealPowerPatch.origAmount = -1628;
/*    */       } 
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Program Files (x86)\Steam\steamapps\workshop\content\646570\2920075378\ThePackmaster.jar!\thePackmaster\patches\thieverypack\StealPowerPatch.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */