/*    */ package Snowpunk.powers;
/*    */ 
/*    */ import Snowpunk.util.TexLoader;
/*    */ import basemod.interfaces.CloneablePowerInterface;
/*    */ import com.badlogic.gdx.graphics.Texture;
/*    */ import com.badlogic.gdx.graphics.g2d.TextureAtlas;
/*    */ import com.megacrit.cardcrawl.core.AbstractCreature;
/*    */ import com.megacrit.cardcrawl.powers.AbstractPower;
/*    */ 
/*    */ public abstract class AbstractEasyPower
/*    */   extends AbstractPower
/*    */   implements CloneablePowerInterface
/*    */ {
/*    */   public AbstractEasyPower(String ID, String name, AbstractPower.PowerType powerType, boolean isTurnBased, AbstractCreature owner, int amount) {
/* 15 */     this.ID = ID;
/* 16 */     this.isTurnBased = isTurnBased;
/*    */     
/* 18 */     this.name = name;
/*    */     
/* 20 */     this.owner = owner;
/* 21 */     this.amount = amount;
/* 22 */     this.type = powerType;
/*    */     
/* 24 */     String power = ID.replaceAll("Snowpunk:", "").replaceAll("Power", "");
/* 25 */     Texture normalTexture = TexLoader.getTexture("SnowpunkResources/images/powers/" + power + "32.png");
/* 26 */     Texture hiDefImage = TexLoader.getTexture("SnowpunkResources/images/powers/" + power + "84.png");
/*    */     
/* 28 */     if (hiDefImage != null) {
/* 29 */       this.region128 = new TextureAtlas.AtlasRegion(hiDefImage, 0, 0, hiDefImage.getWidth(), hiDefImage.getHeight());
/* 30 */       if (normalTexture != null)
/* 31 */         this.region48 = new TextureAtlas.AtlasRegion(normalTexture, 0, 0, normalTexture.getWidth(), normalTexture.getHeight()); 
/* 32 */     } else if (normalTexture != null) {
/* 33 */       this.img = normalTexture;
/* 34 */       this.region48 = new TextureAtlas.AtlasRegion(normalTexture, 0, 0, normalTexture.getWidth(), normalTexture.getHeight());
/*    */     } 
/*    */     
/* 37 */     updateDescription();
/*    */   }
/*    */ }


/* Location:              C:\Program Files (x86)\Steam\steamapps\workshop\content\646570\3390561252\Snowpunk.jar!\Snowpunk\powers\AbstractEasyPower.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */