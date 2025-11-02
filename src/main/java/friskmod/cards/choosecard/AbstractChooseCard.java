package friskmod.cards.choosecard;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import friskmod.cards.AbstractEasyCard;
import friskmod.cards.AnnoyingDog;
import friskmod.character.Frisk;
import friskmod.patches.perseverance.PerseveranceFields;
import friskmod.util.CardStats;

import java.util.List;

import static friskmod.FriskMod.makeID;

public abstract class AbstractChooseCard extends AbstractEasyCard {

    public AbstractChooseCard(String ID){
        super(
                ID,
                new CardStats(
                        CardColor.COLORLESS,
                        CardType.SKILL,
                        CardRarity.SPECIAL,
                        CardTarget.NONE,
                        -2
                )
        );
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        chooseOption();
    }

    @Override
    public void onChoseThisOption() {
        chooseOption();
    }

    public abstract void chooseOption();
}
