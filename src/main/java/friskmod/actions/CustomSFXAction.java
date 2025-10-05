package friskmod.actions;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;

public class CustomSFXAction extends AbstractGameAction {
    private final String key;

//    private final float pitchVar;

    private final float pitchAdjust;
    
    private final float loudMultiplier;

    public CustomSFXAction(String key) {
        //this(key, 0.0F, false);
        this(key, 0.0F);
    }

    public CustomSFXAction(String key, float pitchVar) {
        this(key, pitchVar, 1.0F);

    }

    public CustomSFXAction(String key, float pitchVar, float loudMultiplier) {
        this.key = key;
        this.pitchAdjust = 1.0F + MathUtils.random(-pitchVar, pitchVar);
//        this.adjust = pitchAdjust;
        this.loudMultiplier = loudMultiplier;
        this.actionType = AbstractGameAction.ActionType.WAIT;
    }

//  public CustomSFXAction(String key, float pitchVar, boolean pitchAdjust) {
//    this.key = key;
//    this.pitchVar = pitchVar;
//    this.adjust = pitchAdjust;
//    this.actionType = AbstractGameAction.ActionType.WAIT;
//  }

    public void update() {
        CardCrawlGame.sound.playAV(this.key, this.pitchAdjust, this.loudMultiplier);
        this.isDone = true;
    }
}


/* Location:              C:\Program Files (x86)\Steam\steamapps\workshop\content\646570\2839809078\RhythmGirl.jar!\theRhythmGirl\actions\CustomSFXAction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */