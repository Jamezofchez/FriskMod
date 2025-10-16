package friskmod.vfx;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import friskmod.actions.CustomSFXAction;
import friskmod.util.Wiz;

public class ThrowDaggerEffect {
    public static void throwDaggerEffect(float angleDeviation){
        boolean flipX = AbstractDungeon.getMonsters().shouldFlipVfx();
        float playerX = AbstractDungeon.player.hb.cX;
        float playerY = AbstractDungeon.player.hb.cY;

        // Random vertical spread around player
        //float offsetY = MathUtils.random(-100.0F, 100.0F) * Settings.scale;
        // Forward-biased horizontal spread
        //float offsetX = MathUtils.random(200.0F, 400.0F) * Settings.scale;
        float offsetX = 0;
        float offsetY = 0;
        // Mirror depending on which way we’re facing
        float x = flipX ? playerX - offsetX : playerX + offsetX;
        float y = playerY + offsetY;

        // Random small rotation variation
        float rotation = MathUtils.random(-angleDeviation, angleDeviation);
        AbstractDungeon.effectsQueue.add(
                new FlyingDaggerEffect(x, y, rotation, flipX)
        );
    }
}
