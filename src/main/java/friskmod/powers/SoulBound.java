package friskmod.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import friskmod.FriskMod;
import friskmod.actions.KillEnemyAction;
import friskmod.util.Wiz;

public class SoulBound extends BasePower {
    public static final String POWER_ID = FriskMod.makeID(SoulBound.class.getSimpleName());
    private static final AbstractPower.PowerType TYPE = AbstractPower.PowerType.BUFF;
    private static final boolean TURN_BASED = false;
    private static final UIStrings UI_STRINGS = CardCrawlGame.languagePack.getUIString(FriskMod.makeID(SoulBound.class.getSimpleName()));


    AbstractCreature boundTarget;
    //The only thing TURN_BASED controls is the color of the number on the power icon.
    //Turn based powers are white, non-turn based powers are red or green depending on if their amount is positive or negative.
    //For a power to actually decrease/go away on its own they do it themselves.
    //Look at powers that do this like VulnerablePower and DoubleTapPower.
    public SoulBound(AbstractCreature owner, AbstractCreature boundTarget) {
        super(POWER_ID, TYPE, TURN_BASED, owner, -1);
        this.boundTarget = boundTarget;
    }

    @Override
    public AbstractPower makeCopy() {
        return new SoulBound(owner, boundTarget);
    }

    public void updateDescription() {
        String name;
        if (boundTarget == null || boundTarget.name == null) {
            name = UI_STRINGS.TEXT[0];
        } else{
            name = boundTarget.name;
        }
        this.description = String.format(DESCRIPTIONS[0], name);
    }

    public void onMonsterDeath(AbstractMonster instance) {
        if (instance == boundTarget) {
            Wiz.atb(new KillEnemyAction((AbstractMonster) this.owner));
        }
    }
}