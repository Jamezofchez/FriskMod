package friskmod.monsters;

import basemod.abstracts.CustomMonster;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.MinionPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.combat.FireballEffect;
import friskmod.FriskMod;
import friskmod.powers.SoulBound;
import friskmod.util.Wiz;

public abstract class AbstractDummy extends AbstractFriskmodEnemy {

    public AbstractDummy(String name, String id, float x, float y, int hp) {
        super(name, id, hp, 0.0F, 0.0F, 110.0F, 110.0F, null, x, y);
        this.currentHealth = this.maxHealth = hp; //shouldnt be effected
    }

    public void usePreBattleAction() {
//        applyToSelf(new MinionPower(this));
    }
}


/* Location:              C:\Program Files (x86)\Steam\steamapps\workshop\content\646570\3100352485\TheLocksmith.jar!\theAnime\monsters\AbstractDummy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */