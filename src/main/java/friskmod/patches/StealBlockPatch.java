/*    */ package thePackmaster.patches.thieverypack;
/*    */ 
/*    */ import com.evacipated.cardcrawl.modthespire.lib.ByRef;
/*    */ import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
/*    */ import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
/*    */ import com.megacrit.cardcrawl.core.AbstractCreature;
/*    */ import thePackmaster.actions.thieverypack.StealAllBlockAction;
/*    */ 
/*    */ public class StealBlockPatch {
/*    */   @SpirePatch2(clz = AbstractCreature.class, method = "renderBlockIconAndValue")
/*    */   public static class RenderIconPatch {
/*    */     @SpirePrefixPatch
/*    */     public static void Prefix(AbstractCreature __instance, @ByRef float[] x, @ByRef float[] y) {
/* 14 */       if (StealAllBlockAction.activatedInstance != null && StealAllBlockAction.affectedCreature == __instance)
/* 15 */         StealAllBlockAction.activatedInstance.calcPosition(x, y); 
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Program Files (x86)\Steam\steamapps\workshop\content\646570\2920075378\ThePackmaster.jar!\thePackmaster\patches\thieverypack\StealBlockPatch.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */