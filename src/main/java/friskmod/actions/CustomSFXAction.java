package friskmod.actions;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;

public class CustomSFXAction extends AbstractGameAction {
    private final String key;

//    private final float pitchVariance;

    private final float adjust;
    
    private final float loudMultiplier;

    public CustomSFXAction(String key) {
        //this(key, 0.0F, false);
        this(key, 0.0F);
    }

    public CustomSFXAction(String key, float pitchVariance) {
        this(key, pitchVariance, 1.0F);

    }

    public CustomSFXAction(String key, float pitchVariance, float loudMultiplier) {
        this.key = key;
        this.adjust = 1.0F + MathUtils.random(-pitchVariance, pitchVariance);
//        this.adjust = pitchAdjust;
        this.loudMultiplier = loudMultiplier;
        this.actionType = ActionType.DAMAGE;
        this.duration = this.startDuration = Settings.ACTION_DUR_FAST;
    }

//  public CustomSFXAction(String key, float pitchVariance, boolean pitchAdjust) {
//    this.key = key;
//    this.pitchVariance = pitchVariance;
//    this.adjust = pitchAdjust;
//    this.actionType = AbstractGameAction.ActionType.WAIT;
//  }

    public void update() {
        if (duration == startDuration) {
//            CardCrawlGame.sound.playAV(this.key, this.adjust, this.loudMultiplier);
            CardCrawlGame.sound.play(this.key);
        }
        tickDuration();
    }
}


/* Location:              C:\Program Files (x86)\Steam\steamapps\workshop\content\646570\2839809078\RhythmGirl.jar!\theRhythmGirl\actions\CustomSFXAction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */