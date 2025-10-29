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

    public AbstractChooseCard(){
        super(
                ID,
                new CardStats(
                        Frisk.Meta.Enums.CARD_COLOR,
                        CardType.POWER,
                        CardRarity.SPECIAL,
                        CardTarget.NONE,
                        -2
                )
        );
    }

    public abstract void chooseOption(AbstractPlayer p, AbstractMonster m);
}
