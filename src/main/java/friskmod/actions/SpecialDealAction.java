package friskmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import friskmod.cards.SpecialDeal;
import friskmod.helper.SharedFunctions;
import friskmod.powers.Mercied;
import friskmod.util.Wiz;

public class SpecialDealAction extends AbstractGameAction {


    public SpecialDealAction(int amount) {
        actionType = ActionType.DAMAGE; //how did these devs get hired
        this.amount = amount;
    }
    @Override
    public void update() {
        SharedFunctions.cycleDeals();
        AbstractPlayer p = AbstractDungeon.player;
        addToBot(new ApplyPowerAction(p, p, new Mercied(p, amount), amount));
        for (AbstractMonster monster : Wiz.getMonsters()) {
            addToBot(new ApplyPowerAction(monster, p, new ArtifactPower(monster, amount), amount));
        }
        int specialDealCycle = SharedFunctions.getSpecialDealBonus();
        if (specialDealCycle == 0){
            addToBot(new CustomSFXAction("spamton2"));
            addToBot(new GainGoldAction(p, SpecialDeal.JACKPOT_GOLD));
        } else{
            addToBot(new CustomSFXAction("spamton"));
            addToBot(new GainGoldAction(p, SpecialDeal.DEFAULT_GOLD));
        }
        isDone = true;
    }
}
