package friskmod.actions;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;
import friskmod.cards.HugItOff;
import friskmod.helper.StealableWhitelist;

import java.util.*;

import static friskmod.FriskMod.makeID;

/// This piece of sh*t is derived from with the Thievery pack.
public class StealPowerAction extends AbstractGameAction {
    public static final String ID = makeID(StealPowerAction.class.getSimpleName());
    private static final UIStrings UI_STRINGS = CardCrawlGame.languagePack.getUIString(ID);

    public static final float POWER_ICON_PADDING_X = 48.0F * Settings.scale;

    public final boolean steal;
    public float t;

    public Set<AbstractPower> affectedPowers;

    public Set<String> affectedIDs = new HashSet<>();
    //    public Set<String> removedIDs;
    public Set<String> unaffectedIDs = new HashSet<>();


    public final List<AbstractMonster> monsters;
    public static final Set<String> stealablePows = StealableWhitelist.getInstance().getWhitelist();
    public Map<String, Float> targetXMap = new HashMap<>();
    public Map<String, Float> targetYMap = new HashMap<>();

    public static StealPowerAction activatedInstance = null;

    public static boolean didMiss;

    // Primary constructor — for multiple monsters
    public StealPowerAction(List<AbstractMonster> monsters) {
        this(monsters, true);
    }
    public StealPowerAction(List<AbstractMonster> monsters, boolean steal) {
        this.actionType = ActionType.WAIT;
        this.duration = this.startDuration = Settings.ACTION_DUR_LONG * 0.75f;
        this.monsters = monsters;
        this.steal = steal;
    }

    // Overload for a single monster
    public StealPowerAction(AbstractMonster m) {
        this(toList(m));
    }

    public StealPowerAction(AbstractMonster m, boolean steal) {
        this(toList(m), steal);
    }

    // Helper method to create a list with a single monster (if non-null)
    private static List<AbstractMonster> toList(AbstractMonster m) {
        List<AbstractMonster> list = new ArrayList<>();
        if (m != null) {
            list.add(m);
        }
        return list;
    }

    public void calcPosition(String powID, float[] x, float[] y, Color c, boolean isAmount) {
        if (affectedIDs.contains(powID)) {
            float targetX = targetXMap.get(powID);
            float targetY = targetYMap.get(powID);
            x[0] = MathUtils.lerp(x[0], targetX + (isAmount ? 32.0F * Settings.scale : 0), t);
            y[0] = MathUtils.lerp(y[0], targetY - (isAmount ? 18.0F * Settings.scale : 0), t);
        }
//        } else if (removedIDs.contains(powID)) {
//            float targetX = 0;
//            float targetY = -50.0f * Settings.scale;
//            if (t < 0.4f) {
//                float newt = (0.4f - t) / 0.4f;
//                newt = 1 - newt * newt * newt;
//                x[0] += targetX * newt;
//                y[0] += targetY * newt;
//            } else if (t < 0.7f) {
//                x[0] += targetX;
//                y[0] += targetY;
//            } else {
//                float newt = (t - 0.7f) / 0.3f;
//                c.a *= (1 - newt) * (1 - newt);
//                x[0] += targetX;
//                y[0] += targetY - 20.0f * newt * Settings.scale;
//            }
//        }
    }

    @Override
    public void update() {
        AbstractPlayer p = AbstractDungeon.player;

        if (duration == startDuration) {
            if (monsters.isEmpty()) {
                didMiss = true;
                isDone = true;
                activatedInstance = null;
                return;
            }

            affectedPowers = new HashSet<>();
            for (AbstractMonster m : monsters) {
//                AbstractPower posspow = m.getPower(ArtifactPower.POWER_ID);
//                if (posspow != null) {
//                    posspow.onSpecificTrigger();
//                    continue;
//                }
                for (AbstractPower pow : m.powers) {
                    if (pow.type == AbstractPower.PowerType.BUFF) { //should handle e.g not stealing negative strength
                        if (stealablePows.contains(pow.ID) && StealableWhitelist.getInstance().checkPreProcess(pow)){
                            affectedPowers.add(pow);
                            affectedIDs.add(pow.ID);
                        } else{
                            unaffectedIDs.add(pow.ID);
                        }
                    }
                }
            }

            if (affectedPowers.isEmpty()) {
                didMiss = true;
                isDone = true;
                activatedInstance = null;
                return;
            }

            for (AbstractPower abstractPower : affectedPowers) {
                String ID = abstractPower.ID;
                int index = 0;
                for (; index < p.powers.size(); index++) {
                    AbstractPower playerPow = p.powers.get(index);
                    if (playerPow.ID.equals(ID))
                        break;
                }
                targetXMap.put(ID, p.hb.cX - p.hb.width / 2.0F + 10.0F * Settings.scale + index * POWER_ICON_PADDING_X);
                targetYMap.put(ID, p.hb.cY - p.hb.height / 2.0F + ReflectionHacks.<Float>getPrivate(p, AbstractCreature.class, "hbYOffset") - 48.0F * Settings.scale);
            }
            t = 0;
        }
        tickDuration();
        didMiss = false;

        if (isDone) {
            t = 1;
//            int multiplier = 1;
//            AbstractPower tmp = AbstractDungeon.player.getPower(ThieveryMasteryPower.POWER_ID);
//            if (tmp != null) {
//                tmp.flash();
//                multiplier = 2;
//            }

            addToTop(new AbstractGameAction() {
                @Override
                public void update() {
                    activatedInstance = null;
                    isDone = true;
                }
            });

            for (AbstractPower pow : affectedPowers) {
                // You gain the power and enemy loses power
                StealableWhitelist.getInstance().attachClonePowerToPlayer(pow, steal);

            }

            if (!unaffectedIDs.isEmpty()) {
                List<String> names = new ArrayList<>();
                for (String ID : unaffectedIDs) {
                    PowerStrings ps = CardCrawlGame.languagePack.getPowerStrings(ID);
                    String name = ps.NAME;
                    names.add(name);
                }
                String s;
                if (unaffectedIDs.size() == 1) {
                    s = String.format(UI_STRINGS.TEXT[0], names.get(0));
                } else {
                    String lastName = names.remove(names.size() - 1);
                    s = String.format(UI_STRINGS.TEXT[1], String.join(", ", names), lastName);
                }
                addToTop(new TalkAction(true, s, 0.1F, 3.5F));
            }
        } else {
            t = (startDuration - duration) / startDuration; //isn't this just setting it to 0?
            activatedInstance = this;
        }
    }

//    private AbstractPower getDuplicatedPower(AbstractCreature c, AbstractPower pow, int gainAmount) {
//        switch (pow.ID) {
//            case StrengthPower.POWER_ID:
//                return new StrengthPower(c, gainAmount);
//            case ArtifactPower.POWER_ID:
//                return new ArtifactPower(c, gainAmount);
//            case PlatedArmorPower.POWER_ID:
//                return new PlatedArmorPower(c, gainAmount);
//            case ThornsPower.POWER_ID:
//            case SharpHidePower.POWER_ID:
//                return new ThornsPower(c, gainAmount);
//            case MetallicizePower.POWER_ID:
//                return new MetallicizePower(c, gainAmount);
//            case IntangiblePower.POWER_ID:
//                return new IntangiblePlayerPower(c, gainAmount + 1); // It works!
//            case RitualPower.POWER_ID:
//                return new RitualPower(c, gainAmount, true);
//            case AngerPower.POWER_ID:
//                return new AngerPower(c, gainAmount);
//            case StasisPower.POWER_ID:
//                AbstractCard card = ReflectionHacks.getPrivate(pow, StasisPower.class, "card");
//                if (card != null) {
//                    return new StasisPower(c, card) {
//                        @Override
//                        public void onInitialApplication() {
//                            if (AbstractDungeon.player.hand.size() < BaseMod.MAX_HAND_SIZE) {
//                                addToBot(new MakeTempCardInHandAction(card, false, true));
//                            } else {
//                                addToBot(new MakeTempCardInDiscardAction(card, true));
//                            }
//                            addToBot(new RemoveSpecificPowerAction(c, c, this));
//                        }
//                    };
//                } else {
//                    return null;
//                }
//        }
//        // all other buffs are incompatible
//        return null;
//    }
}
