package friskmod.actions;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.SpawnMonsterAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import friskmod.monsters.AbstractDummy;
import friskmod.monsters.GladDummy;
import friskmod.monsters.MadDummy;
import friskmod.monsters.EmptyDummy;

public class SummonDummyAction extends AbstractGameAction {
    private static final float MAX_Y = 250.0F;

    private static final float MIN_Y = 150.0F;

    private static final float MIN_X = -350.0F;

    private static final float MAX_X = 150.0F;

    private DummyTypes myDummy;

    private int hp;

    private int magicNumber;

    public enum DummyTypes {
        EMPTY, MAD, GLAD;
    }

    public SummonDummyAction(DummyTypes d, int hp, int magicNumber) {
        this(d, hp, magicNumber, null);
    }
    public SummonDummyAction(DummyTypes d, int hp, AbstractMonster target) {
        this(d, hp, 0, target);
    }
    public SummonDummyAction(DummyTypes d, int hp, int magicNumber, AbstractMonster target) {
        this.myDummy = d;
        this.hp = hp;
        this.magicNumber = magicNumber;
        this.target = target;
    }

    public void update() {
        this.isDone = true;
        if (target.isDeadOrEscaped()) {
            return;
        }
        AbstractDummy dummy;
        float x = MathUtils.random(-1000.0F, 0.0F);
        float y = MathUtils.random(150.0F, 250.0F);
        switch (this.myDummy) {
            case MAD:
                dummy = new MadDummy(x, y, this.hp, this.magicNumber, this.target);
                break;
            case GLAD:
                dummy = new GladDummy(x, y, this.hp, this.magicNumber, this.target);
                break;
            case EMPTY:
            default:
                dummy = new EmptyDummy(x, y, this.hp);
                break;
        }
        float actualX = dummy.hb.x;
        float actualY = dummy.hb.y;
        float adjustDistance = 0.0F;
        float adjustAngle = 0.0F;
        float xOffset = 0.0F;
        float yOffset = 0.0F;
        boolean success = false;
        EmptyDummy testDummy = new EmptyDummy(dummy.hb_x, dummy.hb_y, 0);
        testDummy.hb = new Hitbox(dummy.hb.x, dummy.hb.y, dummy.hb.width, dummy.hb.height);
        while (!success) {
            success = true;
            for (AbstractMonster monster : (AbstractDungeon.getMonsters()).monsters) {
                //|| monster.id.equals("GremlinWarrior") || monster.id.equals("GremlinTsundere") || monster.id.equals("GremlinThief") || monster.id.equals("GremlinFat") || monster.id.equals("GremlinWizard") || monster.id.equals("Dagger")
//                if (!monster.isDeadOrEscaped()) {
                if (overlap(monster.hb, testDummy.hb)) {
                    success = false;
                    adjustAngle = (adjustAngle + 0.1F) % 6.2831855F;
                    adjustDistance += 10.0F;
                    xOffset = MathUtils.cos(adjustAngle) * adjustDistance;
                    yOffset = MathUtils.sin(adjustAngle) * adjustDistance;
                    testDummy.hb.x = actualX + xOffset;
                    testDummy.hb.y = actualY + yOffset;
                }
            }
        }
        dummy.hb.move(dummy.hb.x + dummy.hb.width / 2.0F, dummy.hb.y + dummy.hb.height / 2.0F);
        dummy.hb_x = dummy.hb.cX - dummy.drawX + dummy.animX;
        dummy.hb_y = dummy.hb.cY - dummy.drawY + dummy.animY;
        dummy.healthHb.move(dummy.hb.cX, dummy.hb.cY - dummy.hb_h / 2.0F - dummy.healthHb.height / 2.0F);
        addToTop(new AbstractGameAction() {
            public void update() {
                this.isDone = true;
                dummy.usePreBattleAction();
            }
        });
        addToTop((AbstractGameAction) new SpawnMonsterAction(dummy, true));
    }

    private static final float BORDER = 50.0F * Settings.scale;

    private static boolean overlap(Hitbox a, Hitbox b) {
        if (a.x > b.x + b.width + BORDER || b.x > a.x + a.width + BORDER)
            return false;
        return (a.y <= b.y + b.height + BORDER && b.y <= a.y + a.height + BORDER);
    }
}


/* Location:              C:\Program Files (x86)\Steam\steamapps\workshop\content\646570\3100352485\TheLocksmith.jar!\theAnime\actions\SummonDummyAction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */