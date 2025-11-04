package friskmod.powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import friskmod.FriskMod;

public class CountdownRuins extends AbstractCountdownPower {
    public static final String POWER_ID = FriskMod.makeID(CountdownRuins.class.getSimpleName());

    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public static final String NAME = powerStrings.NAME;

    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final PowerType TYPE = PowerType.BUFF;
    private static final boolean TURN_BASED = false;

    private final int UPG_DAMAGE;

    public CountdownRuins(AbstractCreature owner, int amount, int countdown, int upg_amount) {
        super(POWER_ID, TYPE, TURN_BASED, owner, amount, countdown);
        this.name = NAME;
        this.UPG_DAMAGE = upg_amount;
        updateDescription();
    }

    @Override
    public void onCountdownTrigger(boolean expire) {
        addToBot(new DamageAllEnemiesAction(null,
                DamageInfo.createDamageMatrix(this.amount, true), DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.BLUNT_HEAVY));
        super.onCountdownTrigger(expire);
    }

    @Override
    public AbstractPower makeCopy() {
        return new CountdownRuins(owner, amount, amount2, UPG_DAMAGE);
    }

    @Override
    public void updateDescription() {
        int descNum = 0;
        if (amount2 == 1){
            descNum += 1;
        }
        String baseDescription = DESCRIPTIONS[descNum];
        this.description = String.format(baseDescription, amount2, amount, UPG_DAMAGE);
    }

    public void atStartOfTurn() {
        upgrade();
    }

    @Override
    public void upgrade(){
        super.upgrade();
        this.amount += UPG_DAMAGE;
        updateDescription();
    }
}
