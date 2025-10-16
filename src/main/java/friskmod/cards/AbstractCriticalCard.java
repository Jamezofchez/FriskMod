package friskmod.cards;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import friskmod.patches.perseverance.PerseveranceFields;
import friskmod.util.CardStats;

public abstract class AbstractCriticalCard extends AbstractEasyCard{
    public boolean trig_critical = false;
    public AbstractCriticalCard(String ID, CardStats info) {
        super(ID, info);
    }

    @Override
    public void resetAttributes()
    {
        super.resetAttributes();

        this.trig_critical = false;
    }

    @Override
    public AbstractCard makeStatEquivalentCopy() {
        AbstractCard original = super.makeStatEquivalentCopy();

        ((AbstractCriticalCard)original).trig_critical = this.trig_critical;

        return original;
    }

    @Override
    public void triggerOnGlowCheck() {
        glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
        if (isCriticalPos()) {
            glowColor = AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy();
        }
    }

    public boolean isCritical()
    {
        if (isCriticalPos() || PerseveranceFields.perseverePlayed.get(this)){
            this.trig_critical = true;
        }

        return (trig_critical);
    }

    public boolean isCriticalPos()
    {
        double hand_pos = (AbstractDungeon.player.hand.group.indexOf(this)+0.5);
        double hand_size = (AbstractDungeon.player.hand.size());
        double relative = Math.abs(hand_pos-hand_size/2);

        return (relative<1);
    }

    public void onCritical() {
    }

    public void TriggerCriticalEffect(AbstractPlayer p, AbstractMonster m) {
        // Common Critical effects.
        onCritical();

        CriticalEffect(p, m);
    }
    public void CriticalEffect(AbstractPlayer p, AbstractMonster m){

    }
}
