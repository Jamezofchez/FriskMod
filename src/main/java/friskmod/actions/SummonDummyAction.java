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
import friskmod.util.Wiz;

import java.util.ArrayList;

public class SummonDummyAction extends AbstractGameAction {
    private static final float MAX_Y = 250.0F;

    private static final float MIN_Y = 150.0F;

    private static final float MIN_X = -350.0F;

    private static final float MAX_X = 150.0F;

    private static final float BORDER = 50.0F * Settings.scale;


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
        if (this.target != null && this.target.isDeadOrEscaped()) {
            return;
        }
        float startX = MAX_X;
        float startY = MathUtils.random(MIN_Y, MAX_Y);
        AbstractDummy dummy;
        float y = MathUtils.random(MIN_Y, MAX_Y);
        switch (this.myDummy) {
            case MAD:
                dummy = new MadDummy(startX, startY, this.hp, this.magicNumber, this.target);
                break;
            case GLAD:
                dummy = new GladDummy(startX, startY, this.hp, this.magicNumber, this.target);
                break;
            case EMPTY:
            default:
                dummy = new EmptyDummy(startX, startY, this.hp);
                break;
        }
        // If there are existing monsters, start to the left of the leftmost one
        float leftMost = startX;
        ArrayList<AbstractMonster> aliveMonsters = Wiz.getMonsters();
        for (AbstractMonster m : aliveMonsters) {
            leftMost = Math.min(leftMost, m.hb.x);
        }
        startX = leftMost - dummy.hb.width - BORDER;
        EmptyDummy testDummy = new EmptyDummy(startX, startY, 0);
        testDummy.hb = new Hitbox(startX, startY, dummy.hb.width, dummy.hb.height);
        boolean success = false;
        int guard = 0;
        float actualX = dummy.hb.x;
        float actualY = dummy.hb.y;
        float adjustDistance = 0.0F;
        final float PI = (float) Math.PI;
        float adjustAngle = PI;
        float xOffset = 0.0F;
        float yOffset = 0.0F;
        if (!aliveMonsters.isEmpty()) {
            while (!success && guard++ < 100) {
                success = true;
                for (AbstractMonster monster : aliveMonsters) {
                    if (overlap(monster.hb, testDummy.hb)) {
                        success = false;
                        adjustAngle = (float) (adjustAngle + ((Math.random()*2)-1)*(PI/4));
                        adjustDistance += 10.0F;
                        xOffset = MathUtils.cos(adjustAngle) * adjustDistance;
                        yOffset = MathUtils.sin(adjustAngle) * adjustDistance;
                        testDummy.hb.x = actualX + xOffset;
                        testDummy.hb.y = actualY + yOffset;
                        break;
                    }
                }
            }
        }

        // Apply the resolved position to the real dummy's hitbox
        dummy.hb.x = testDummy.hb.x;
        dummy.hb.y = testDummy.hb.y;
        dummy.hb.move(dummy.hb.x + dummy.hb.width / 2.0F, dummy.hb.y + dummy.hb.height / 2.0F);
        dummy.hb_x = dummy.hb.cX - dummy.drawX + dummy.animX;
        dummy.hb_y = dummy.hb.cY - dummy.drawY + dummy.animY;
        dummy.healthHb.move(dummy.hb.cX, dummy.hb.cY - dummy.hb_h / 2.0F - dummy.healthHb.height / 2.0F);
        // Ensure the spawned dummy is first in the enemy queue after spawning
        addToTop(new AbstractGameAction() {
            public void update() {
                this.isDone = true;
                if (AbstractDungeon.getMonsters() != null && AbstractDungeon.getMonsters().monsters != null) {
                    java.util.ArrayList<AbstractMonster> list = AbstractDungeon.getMonsters().monsters;
                    if (list.contains(dummy)) {
                        list.remove(dummy);
                        list.add(0, dummy);
                    }
                }
            }
        });
        // Run pre-battle powers
        addToTop(new AbstractGameAction() {
            public void update() {
                this.isDone = true;
                dummy.usePreBattleAction();
            }
        });
        // Spawn the monster (added last so it executes first)
        addToTop((AbstractGameAction) new SpawnMonsterAction(dummy, true));
    }

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