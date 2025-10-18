//package friskmod.patches;
//
//import com.badlogic.gdx.audio.Music;
//import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
//import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
//import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
//import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
//import com.megacrit.cardcrawl.audio.MainMusic;
//import com.megacrit.cardcrawl.audio.TempMusic;
//import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
//import friskmod.character.Frisk;
//
//@SpirePatch2(clz = TempMusic.class, method = "getSong", paramtypez = {String.class})
//public class FriskBGMPatch {
//    @SpirePrefixPatch
//    public static SpireReturn<Music> FriskBGMPatchPostfix(TempMusic __instance, String key) {
//        switch (key){
//            case "ENEMY_RETREATING":
//                return SpireReturn.Return(MainMusic.newMusic("audio/music/mus_enemy_retreating.ogg"));
//            default:
//                return SpireReturn.Continue();
//        }
//    }
//}
//
////        if (AbstractDungeon.player != null && AbstractDungeon.player.chosenClass
////                .equals(Frisk.Meta.Enums.THE_FALLEN_HUMAN))
////            switch (key) {
////                case "SHOP":
////                    return MainMusic.newMusic("audio/music/STS_Soviet_Shop.ogg");
////                case "SHRINE":
////                    return MainMusic.newMusic("audio/music/STS_Soviet_Shrine.ogg");
////                case "MINDBLOOM":
////                    return MainMusic.newMusic("audio/music/STS_Soviet_Boss1_Mind_Bloom.ogg");
////                case "BOSS_BOTTOM":
////                    return MainMusic.newMusic("audio/music/STS_Soviet_Boss1.ogg");
////                case "BOSS_CITY":
////                    return MainMusic.newMusic("audio/music/STS_Soviet_Boss2.ogg");
////                case "BOSS_BEYOND":
////                    return MainMusic.newMusic("audio/music/STS_Soviet_Boss3.ogg");
////                case "BOSS_ENDING":
////                    return MainMusic.newMusic("audio/music/STS_Soviet_Boss4_L'hymne_national_russe.ogg");
////                case "ELITE":
////                    return MainMusic.newMusic("audio/music/STS_Soviet_Elite_Boss.ogg");
////                case "CREDITS":
////                    return MainMusic.newMusic("audio/music/STS_Soviet_Credits.ogg");
////            }
//
//
///* Location:              C:\Program Files (x86)\Steam\steamapps\workshop\content\646570\2938463316\SovietMod.jar!\ModExample\Patches\FriskBGMPatch.class
// * Java compiler version: 8 (52.0)
// * JD-Core Version:       1.1.3
// */