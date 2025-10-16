package friskmod.vfx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.combat.FlyingDaggerEffect;

public class FlurryOfKnivesVFX extends AbstractGameEffect {
    private boolean flipX;
    private int numKnives;

    public FlurryOfKnivesVFX(int numKnives, boolean shouldFlip) {
        this.flipX = shouldFlip;
        this.numKnives = numKnives;
    }

    public void update() {
        this.isDone = true;
        float playerX = AbstractDungeon.player.hb.cX;
        float playerY = AbstractDungeon.player.hb.cY;
        for (int i = 0; i < numKnives; i++) {
//            // Random vertical spread around player
//            float offsetY = MathUtils.random(-100.0F, 100.0F) * Settings.scale;
//
//            // Forward-biased horizontal spread
//            float offsetX = MathUtils.random(200.0F, 400.0F) * Settings.scale;
            float offsetX = 0;
            float offsetY = 0;
            // Mirror depending on which way we’re facing
            float x = flipX ? playerX - offsetX : playerX + offsetX;
            float y = playerY + offsetY;

            // Random small rotation variation (-15° to +15°)
            float rotation = MathUtils.random(-15.0F, 15.0F);

            AbstractDungeon.effectsQueue.add(
                    new FlyingDaggerEffect(x, y, rotation, flipX)
            );
        }
    }

    public void render(SpriteBatch sb) {
    }

    public void dispose() {
    }
}


/* Location:              C:\Program Files (x86)\Steam\steamapps\common\SlayTheSpire\desktop-1.0.jar!\com\megacrit\cardcrawl\vfx\combat\FlurryOfKnivesVFX.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */