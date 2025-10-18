//package friskmod.patches;
//
//import com.evacipated.cardcrawl.mod.stslib.patches.core.AbstractCreature.TempHPField;
//import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
//import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
//import com.megacrit.cardcrawl.audio.MainMusic;
//import com.megacrit.cardcrawl.audio.MusicMaster;
//import basemod.ReflectionHacks;
//import com.megacrit.cardcrawl.audio.TempMusic;
//import com.megacrit.cardcrawl.core.CardCrawlGame;
//import com.megacrit.cardcrawl.rooms.AbstractRoom;
//
//import java.util.List;
//
//public class FadeOutMusicOnCombatEnd { //cant make only my bgm fade out
//    @SpirePatch2(
//            clz = AbstractRoom.class,
//            method = "endBattle"
//    )
//    public static class AbstractRoomEndBattlePatch {
//        public static void Prefix(AbstractRoom __instance) {
//            CardCrawlGame.music.fadeOutTempBGM();
//        }
//    }
//
//}
