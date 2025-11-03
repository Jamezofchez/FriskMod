package friskmod.cards.drinks;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.potions.EntropicBrew;
import com.megacrit.cardcrawl.potions.FairyPotion;
import friskmod.powers.LesserDuplication;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static friskmod.FriskMod.makeID;

public class EntropicCocktail extends AbstractDrinkCard{
    public static final String ID = makeID(EntropicCocktail.class.getSimpleName());

    public EntropicCocktail() {
        this(new EntropicBrew());
    }
    public EntropicCocktail(AbstractPotion potion) {
        super(ID, potion);
    }

    @Override
    public void usePotion(AbstractMonster m) {
        AbstractPlayer p = AbstractDungeon.player;
        List<Integer> indices = IntStream.range(0, p.potionSlots).boxed().collect(Collectors.toList()); //inanity is to allow shuffling
        Collections.shuffle(indices);
        for (Integer integer : indices) {
            int index = integer;
            AbstractPotion oldPotion = p.potions.get(index);
            if (oldPotion instanceof com.megacrit.cardcrawl.potions.PotionSlot)
                continue;
            while (true) {
                AbstractPotion newPotion = AbstractDungeon.returnRandomPotion(true);
                if (!oldPotion.ID.equals(newPotion.ID)) {
                    p.removePotion(oldPotion);
                    p.obtainPotion(newPotion);
                    break;
                }
            }
            if (!this.upgraded)
                return;
        }
    }

    @Override
    public void upp() {
    }
}
