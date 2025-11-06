package friskmod.monsters;

import basemod.ReflectionHacks;
import basemod.abstracts.CustomMonster;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import friskmod.BetterSpriterAnimation;
import friskmod.actions.CustomSFXAction;
import friskmod.util.Wiz;
import friskmod.vfx.VFXActionButItCanFizzle;
import friskmod.vfx.WaitEffect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public abstract class AbstractFriskmodEnemy extends CustomMonster {
    public String NAME;

    public String[] MOVES;

    public String[] DIALOG;

    protected Map<Byte, EnemyMoveInfo> moves;

    public boolean firstMove = true;

    protected DamageInfo info;

    protected int multiplier;

    public AbstractFriskmodEnemy(String name, String id, int maxHealth, float hb_x, float hb_y, float hb_w, float hb_h, String imgUrl, float offsetX, float offsetY) {
        super(name, id, maxHealth, hb_x, hb_y, hb_w, hb_h, imgUrl, offsetX, offsetY);
        setUpMisc();
        setUpStrings(id);
    }

    public AbstractFriskmodEnemy(String name, String id, int maxHealth, float hb_x, float hb_y, float hb_w, float hb_h, String imgUrl, float offsetX, float offsetY, boolean ignoreBlights) {
        super(name, id, maxHealth, hb_x, hb_y, hb_w, hb_h, imgUrl, offsetX, offsetY, ignoreBlights);
        setUpMisc();
        setUpStrings(id);
    }

    public AbstractFriskmodEnemy(String name, String id, int maxHealth, float hb_x, float hb_y, float hb_w, float hb_h, String imgUrl) {
        super(name, id, maxHealth, hb_x, hb_y, hb_w, hb_h, imgUrl);
        setUpMisc();
        setUpStrings(id);
    }

    protected void setUpMisc() {
        this.moves = new HashMap<>();
        this.dialogX = (this.hb_x - 70.0F) * Settings.scale;
        this.dialogY -= (this.hb_y - 55.0F) * Settings.scale;
    }

    protected void setUpStrings(String ID) {
        MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
        this.name = monsterStrings.NAME;
        this.MOVES = monsterStrings.MOVES;
        this.DIALOG = monsterStrings.DIALOG;
    }

    protected void addMove(byte moveCode, AbstractMonster.Intent intent) {
        addMove(moveCode, intent, -1);
    }

    protected void addMove(byte moveCode, AbstractMonster.Intent intent, int baseDamage) {
        addMove(moveCode, intent, baseDamage, 0, false);
    }

    protected void addMove(byte moveCode, AbstractMonster.Intent intent, int baseDamage, int multiplier) {
        addMove(moveCode, intent, baseDamage, multiplier, true);
    }

    protected void addMove(byte moveCode, AbstractMonster.Intent intent, int baseDamage, int multiplier, boolean isMultiDamage) {
        this.moves.put(Byte.valueOf(moveCode), new EnemyMoveInfo(moveCode, intent, baseDamage, multiplier, isMultiDamage));
    }
    public void setMoveShortcut(byte next) {
        EnemyMoveInfo info = this.moves.get(Byte.valueOf(next));
        setMove(null, next, info.intent, info.baseDamage, info.multiplier, info.isMultiDamage);
    }


    protected DamageInfo getInfoFromMove(byte move) {
        if (this.moves.containsKey(Byte.valueOf(move))) {
            EnemyMoveInfo emi = this.moves.get(Byte.valueOf(move));
            return new DamageInfo(this, emi.baseDamage, DamageInfo.DamageType.NORMAL);
        }
        return new DamageInfo(this, 0, DamageInfo.DamageType.NORMAL);
    }

    protected int getMultiplierFromMove(byte move) {
        int multiplier = 0;
        if (this.moves.containsKey(Byte.valueOf(move))) {
            EnemyMoveInfo emi = this.moves.get(Byte.valueOf(move));
            multiplier = emi.multiplier;
        }
        return multiplier;
    }

    protected int calcAscensionDamage(float base) {
        switch (this.type) {
            case BOSS:
                if (AbstractDungeon.ascensionLevel >= 4)
                    base *= 1.1F;
                break;
            case ELITE:
                if (AbstractDungeon.ascensionLevel >= 3)
                    base *= 1.1F;
                break;
            case NORMAL:
                if (AbstractDungeon.ascensionLevel >= 2)
                    base *= 1.1F;
                break;
        }
        return Math.round(base);
    }

    protected int calcAscensionTankiness(float base) {
        switch (this.type) {
            case BOSS:
                if (AbstractDungeon.ascensionLevel >= 9)
                    base *= 1.1F;
                break;
            case ELITE:
                if (AbstractDungeon.ascensionLevel >= 8)
                    base *= 1.1F;
                break;
            case NORMAL:
                if (AbstractDungeon.ascensionLevel >= 7)
                    base *= 1.1F;
                break;
        }
        return Math.round(base);
    }

    protected int calcAscensionSpecial(float base) {
        switch (this.type) {
            case BOSS:
                if (AbstractDungeon.ascensionLevel >= 19)
                    base *= 1.5F;
                break;
            case ELITE:
                if (AbstractDungeon.ascensionLevel >= 18)
                    base *= 1.5F;
                break;
            case NORMAL:
                if (AbstractDungeon.ascensionLevel >= 17)
                    base *= 1.5F;
                break;
        }
        return Math.round(base);
    }


    public void createIntent() {
        super.createIntent();
        setMoveName();
    }

    protected void setMoveName() {
        EnemyMoveInfo move = (EnemyMoveInfo)ReflectionHacks.getPrivate(this, AbstractMonster.class, "move");
        if (move.nextMove >= 0 && move.nextMove < this.MOVES.length)
            this.moveName = this.MOVES[move.nextMove];
    }


    public void rollMove() {
        getMove(getMonsterAIRng().random(99));
    }

    public Random getMonsterAIRng() {
        return AbstractDungeon.aiRng;
    }

    public void die(boolean triggerRelics) {
        useShakeAnimation(5.0F);
        if (this.animation instanceof BetterSpriterAnimation)
            ((BetterSpriterAnimation)this.animation).startDying();
        super.die(triggerRelics);
    }

    public void resetIdle() {
        resetIdle(0.5F);
    }

    public void resetIdle(float duration) {
        Wiz.atb((AbstractGameAction)new VFXActionButItCanFizzle((AbstractCreature)this, (AbstractGameEffect)new WaitEffect(), duration));
        Wiz.atb(new AbstractGameAction() {
            public void update() {
                AbstractFriskmodEnemy.this.runAnim("Idle");
                this.isDone = true;
            }
        });
    }

    public void waitAnimation() {
        waitAnimation(0.5F, (AbstractCreature)null);
    }

    public void waitAnimation(float duration) {
        waitAnimation(duration, (AbstractCreature)null);
    }

    protected void waitAnimation(AbstractCreature enemy) {
        waitAnimation(0.5F, enemy);
    }

    public void waitAnimation(final float time, final AbstractCreature enemy) {
        Wiz.atb(new AbstractGameAction() {
            public void update() {
                if (AbstractFriskmodEnemy.this.isDeadOrEscaped()) {
                    this.isDone = true;
                    return;
                }
                if (enemy == null) {
                    Wiz.att((AbstractGameAction)new VFXActionButItCanFizzle((AbstractCreature)AbstractFriskmodEnemy.this, (AbstractGameEffect)new WaitEffect(), time));
                } else if (!enemy.isDeadOrEscaped()) {
                    Wiz.att((AbstractGameAction)new VFXActionButItCanFizzle((AbstractCreature)AbstractFriskmodEnemy.this, (AbstractGameEffect)new WaitEffect(), time));
                }
                this.isDone = true;
            }
        });
    }

    public void runAnim(String animation) {
        ((BetterSpriterAnimation)this.animation).myPlayer.setAnimation(animation);
    }

    protected void animationAction(final String animation, final String sound, final AbstractCreature enemy, final AbstractCreature owner) {
        Wiz.atb(new AbstractGameAction() {
            public void update() {
                if (owner.isDeadOrEscaped()) {
                    this.isDone = true;
                    return;
                }
                if (enemy == null) {
                    AbstractFriskmodEnemy.this.runAnim(animation);
                    Wiz.att(new CustomSFXAction(sound));
                } else if (!enemy.isDeadOrEscaped()) {
                    AbstractFriskmodEnemy.this.runAnim(animation);
                    Wiz.att(new CustomSFXAction(sound));
                }
                this.isDone = true;
            }
        });
    }

    protected void animationAction(final String animation, final String sound, final float volume, final AbstractCreature enemy, final AbstractCreature owner) {
        Wiz.atb(new AbstractGameAction() {
            public void update() {
                if (owner.isDeadOrEscaped()) {
                    this.isDone = true;
                    return;
                }
                if (enemy == null) {
                    AbstractFriskmodEnemy.this.runAnim(animation);
                    Wiz.att(new CustomSFXAction(sound, 0.0F, volume));
                } else if (!enemy.isDeadOrEscaped()) {
                    AbstractFriskmodEnemy.this.runAnim(animation);
                    Wiz.att(new CustomSFXAction(sound, 0.0F, volume));
                }
                this.isDone = true;
            }
        });

    }

    protected void animationAction(String animation, String sound, AbstractCreature owner) {
        animationAction(animation, sound, (AbstractCreature)null, owner);
    }

    protected void animationAction(String animation, String sound, float volume, AbstractCreature owner) {
        animationAction(animation, sound, volume, (AbstractCreature)null, owner);
    }
    
    public void applyToSelf(AbstractPower po) {
        Wiz.atb((AbstractGameAction)new ApplyPowerAction((AbstractCreature)this, (AbstractCreature)this, po, po.amount, true));
    }


    @Override
    public void takeTurn() {
        this.info = getInfoFromMove(this.nextMove);
        this.multiplier = getMultiplierFromMove(this.nextMove);
        if (this.firstMove) {
            this.firstMove = false;
        }
    }

    @Override
    protected void getMove(int i) {
    }
}
