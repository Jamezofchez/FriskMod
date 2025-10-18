package friskmod.helper;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import friskmod.FriskMod;

public class playBGM {
    public static void playEnemyRetreating() {
        CardCrawlGame.music.unsilenceBGM();
        AbstractDungeon.scene.fadeOutAmbiance();
        CardCrawlGame.music.fadeOutTempBGM();
        try {
            CardCrawlGame.music.playTempBgmInstantly("mus_enemy_retreating.ogg");
        } catch (Exception ignored){
            FriskMod.logger.warn("{}: Playing music failed :(", FriskMod.modID);
        }
    }
}
