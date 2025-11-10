package friskmod.cards;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import friskmod.patches.perseverance.PerseveranceFields;
import friskmod.util.CardStats;

import java.util.List;

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

        ((AbstractCriticalCard)original).trig_critical = isCritical();

        return original;
    }

    @Override
    public void triggerOnGlowCheck() {
        glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
        if (isCriticalPos() || PerseveranceFields.isPerseverable.get(this)) {
            glowColor = AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy();
        }
    }

    public boolean isCritical()
    {
        if (trig_critical){
            return true;
        }
        if (isCriticalPos() || PerseveranceFields.perseverePlayed.get(this)){
            return true;
        } else{
            return false;
        }
    }
    public boolean isCriticalPos(){
        List<AbstractCard> hand = AbstractDungeon.player.hand.group;
        double hand_pos = hand.indexOf(this)+0.5;
        double hand_size = hand.size();
        if (hand_size==0){
            return false;
        }
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
    public abstract void CriticalEffect(AbstractPlayer p, AbstractMonster m);
}
