package friskmod.actions;

import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.ThoughtBubble;
import friskmod.FriskMod;
import friskmod.powers.CountdownAttack;
import friskmod.util.Wiz;

import java.util.List;
import java.util.function.BooleanSupplier;

import static friskmod.FriskMod.makeID;
import static friskmod.helper.SharedFunctions.toList;

public class EnemyStealMatchingDebuffsAction extends AbstractGameAction {
    public static final String ID = makeID(EnemyStealMatchingDebuffsAction.class.getSimpleName());
    private static final UIStrings UI_STRINGS = CardCrawlGame.languagePack.getUIString(ID);
    List<AbstractMonster> monsters;
    public EnemyStealMatchingDebuffsAction(List<AbstractMonster> monsters){
        this.monsters = monsters;
    }
    public EnemyStealMatchingDebuffsAction(AbstractMonster monster){
        this.monsters = toList(monster);
    }

    @Override
    public void update() {
        this.isDone = true;
        stealMatchingDebuffs(monsters);
    }
    private void stealMatchingDebuffs(List<AbstractMonster> monsters){
            AbstractPlayer p = AbstractDungeon.player;
            for (AbstractMonster m : monsters) {
                if (m.isDeadOrEscaped()){
                    continue;
                }
                for (AbstractPower mpow : m.powers) {
                    if (mpow instanceof CountdownAttack){
                        for (AbstractPower ppow : p.powers) {
                            if (ppow instanceof CountdownAttack){
                                AbstractPower copy = ((CloneablePowerInterface) ppow).makeCopy();
                                copy.owner = m;
                                Wiz.att(new ApplyPowerAction(m, p, copy));
                                Wiz.att(new RemoveSpecificPowerAction(p, p, ppow));
                            }
                        }
                        continue;
                    }
                    if (!(mpow.type == AbstractPower.PowerType.DEBUFF)){
                        continue;
                    }
                    for (AbstractPower ppow : p.powers) {
                        if (!(ppow.type == AbstractPower.PowerType.DEBUFF)){
                            continue;
                        }
                        if (mpow.ID.equals(ppow.ID)){
                            if (ppow instanceof CloneablePowerInterface) {
                                AbstractPower copy = ((CloneablePowerInterface) ppow).makeCopy();
                                copy.owner = m;
                                // Allow caller to customize before applying
                                Wiz.att(new ApplyPowerAction(m, p, copy));
                                Wiz.att(new RemoveSpecificPowerAction(p, p, ppow));
                            } else {
                                FriskMod.logger.warn("{}: enemy taking failed", FriskMod.modID);
                                AbstractDungeon.effectList.add(new ThoughtBubble(AbstractDungeon.player.dialogX, AbstractDungeon.player.dialogY, 3.0F, String.format(UI_STRINGS.TEXT[0], mpow.name), true));
                        }
                    }
                }
            }
        }
    }
}
