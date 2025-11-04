package friskmod.patches.perseverance;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import friskmod.FriskMod;
//import friskmod.cards.Exhaustion;
import friskmod.cards.BreakFree;
import friskmod.powers.Overcome;
import friskmod.util.Wiz;
import javassist.*;
import javassist.bytecode.*;
import javassist.bytecode.annotation.Annotation;
import javassist.bytecode.annotation.BooleanMemberValue;
import org.clapper.util.classutil.*;
import org.clapper.util.classutil.ClassInfo;
import friskmod.annotations.Playable;
import friskmod.annotations.Unplayable;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

//hooray for dynamic patching
public class PerseverancePatch {
    public static void patch(ClassFinder finder, ClassPool pool) throws NotFoundException {
        //System.out.println("- Occult patch:");

        // Get all classes for AbstractCards
        ClassFilter filter = new AndClassFilter(
                new NotClassFilter(new InterfaceOnlyClassFilter()),
                new ClassModifiersClassFilter(Modifier.PUBLIC),
                new OrClassFilter(
                        new SubclassClassFilter(AbstractCard.class),
                        (classInfo, classFinder) -> classInfo.getClassName().equals(AbstractCard.class.getName())
                )
        );

        ArrayList<ClassInfo> foundClasses = new ArrayList<>();
        finder.findClasses(foundClasses, filter);

        //System.out.println("\t- Potential targets found.\n\t- Patching:");

        int skipped = 0, patched = 0;

        Field modified = null;
        boolean alreadyModified, changed, logged;
        Collection<String> references;
        CtMethod[] methods;
        CtMethod canUse, hasEnoughEnergy;

        outer:
        for (ClassInfo classInfo : foundClasses)
        {
            CtClass ctClass = pool.get(classInfo.getClassName());

            changed = false;
            logged = false;
            canUse = null;
            hasEnoughEnergy = null;

            try
            {
                references = ctClass.getRefClasses();

                for (String s : references) {
                    if (pool.getOrNull(s) == null)
                    {
                        //System.out.println("\t\t- Class " + ctClass.getSimpleName() + " refers to an unloaded class, " + s + ", and will be skipped.");
                        continue outer;
                    }
                }

                alreadyModified = ctClass.isModified();

                methods = ctClass.getDeclaredMethods();
                for (CtMethod m : methods) {
                    switch (m.getName()) {
                        case "canUse":
                            CtClass[] params = m.getParameterTypes();
                            if (params.length == 2 && params[0].getName().equals(AbstractPlayer.class.getName()) && params[1].getName().equals(AbstractMonster.class.getName())) {
                                canUse = m;
                            }
                            break;
                        case "hasEnoughEnergy":
                            hasEnoughEnergy = m;
                            break;
                    }
                }
                if (checkUnplayable(ctClass, canUse))
                    changed = logged = true;

                if (canUse != null) {
                    //System.out.println("\t\t\t- Modifying Method: canUse");
                    canUse.insertAfter("{" +
                            "$_ = " + PerseverancePatch.class.getName() + ".checkUsability($0, $_);" +
                            "}");

                    changed = true;
                }
                if (hasEnoughEnergy != null) {
                    //System.out.println("\t\t\t- Modifying Method: hasEnoughEnergy");
                    hasEnoughEnergy.insertAfter("{" +
                            "$_ = " + PerseverancePatch.class.getName() + ".checkEnergy($0, $_);" +
                            "}");

                    changed = true;
                }

                if (changed)
                {
                    /*if (!logged)
                        System.out.println("\t\t- Class patched: " + ctClass.getSimpleName());*/
                    ++patched;
                }
                else
                {
                    ++skipped;
                    if (!alreadyModified) {
                        try {
                            if (modified == null) {
                                modified = ctClass.getClass().getDeclaredField("wasChanged");
                                modified.setAccessible(true);
                            }
                            modified.set(ctClass, false);
                            //System.out.println("\t\t- Marked class as unchanged: " + ctClass.getSimpleName());
                        }
                        catch (NoSuchFieldException | IllegalAccessException e) {
                            System.out.println("\t\t- Failed to mark class as unchanged: " + ctClass.getSimpleName());
                        }
                    }
                }
            } catch(CannotCompileException e) {
                System.out.println("\t\t- Error occurred while patching class: " + ctClass.getSimpleName() + "\n");
                e.printStackTrace();
            } catch(BadBytecode e) {
                System.out.println("\t\t- Class's canUse method has bad bytecode: " + ctClass.getSimpleName() + "\n");
                e.printStackTrace();
            }
        }
        System.out.println("- Perseverance patch complete. " + (patched + skipped) + " classes checked. " + patched + " classes changed. " + skipped + " classes unchanged.");
    }

    public static boolean checkUsability(AbstractCard c, boolean normallyUsable)
    {
        setPerserveable(c, normallyUsable);
        if (PerseveranceFields.isPerseverable.get(c))
        {
            PerseveranceFields.perseverePlayed.set(c, !normallyUsable || PerseveranceFields.insufficientEnergy.get(c));
            return true;
        }
        return normallyUsable;
    }

    public static boolean checkEnergy(AbstractCard c, boolean normallyEnoughEnergy)
    {
        setPerserveable(c, normallyEnoughEnergy);
        if (PerseveranceFields.isPerseverable.get(c))
        {
            PerseveranceFields.insufficientEnergy.set(c, !normallyEnoughEnergy);
            if (PerseveranceFields.insufficientEnergy.get(c)) {
                PerseveranceFields.currentEnergy.set(c, EnergyPanel.totalCount);
            }
            return true;
        } else{
            PerseveranceFields.currentEnergy.set(c, 0);
        }
        return normallyEnoughEnergy;
    }

    private static void setPerserveable(AbstractCard c, boolean defaultBoolean) {
        if (!defaultBoolean) {
            if (!PerseveranceFields.isPerseverable.get(c)) {
                AbstractPower posspow = AbstractDungeon.player.getPower(Overcome.POWER_ID);
                if (posspow != null) {
                    if (PerseveranceFields.setIsPerseverable(c, true, true)) {
                        PerseveranceFields.overcomePlayed.set(c, true);
                    }
                }
            } else{
                PerseveranceFields.overcomePlayed.set(c, true);
            }
        }
    }



    /*
        BASIC:
            3 (value 0)
            172 (return integer)
            This is code that just directly returns false.

        LONGER (example), method that sets a cantUseMessage:
            42 load a reference onto the stack from local variable 0
            178
            180
            3 (value 0)
            50
            181
            3 (value 0)
            172 (return integer)

        For a method to mean "unplayable":
        Every return op must follow the pattern of 3 172 (always returns false).
     */
    //Specifically checks for classes that will always return false for canUse
    private static Set<String> testedClasses = new HashSet<>();

    private static final Set<Integer> returnOps = new HashSet<>();
    private static final int zeroInt = 3;
    private static final int intReturn = 0b10101100; //172
    static {
        returnOps.add(0b10101101); //long
        returnOps.add(0b10101110); //float
        returnOps.add(0b10101111); //double
        returnOps.add(0b10110000); //reference
        returnOps.add(0b10110001); //void
    }
    private static boolean checkUnplayable(CtClass cardClass, CtMethod canUse) throws BadBytecode {
        testedClasses.add(cardClass.getName());
        if (cardClass.getName().equals(AbstractCard.class.getName())) {
            return false;
        }
        if (canUse != null) {
            //Check if canUse method directly returns false
            //System.out.println("\t\t- Class has canUse: " + cardClass.getSimpleName());
            StringBuilder opCode = new StringBuilder();
            CodeAttribute ca = canUse.getMethodInfo().getCodeAttribute();

            int lastOp = 0, index, op;
            CodeIterator ci = ca.iterator();
            boolean hardFalse = false, unplayable = true;
            while (ci.hasNext()) {
                index = ci.next();
                op = ci.byteAt(index);
                opCode.append(op).append(" ");

                if (returnOps.contains(op)) {
                    //non-int return op. This has a possibly non-false return.
                    //System.out.println("\t\t\t- " + "Has non-boolean constant return value.");
                    unplayable = false;
                }
                if (op == intReturn && lastOp != zeroInt) {
                    //returns not a constant false
                    //System.out.println("\t\t\t- " + "Has non-false constant return value.");
                    unplayable = false;
                }
                else if (op == intReturn) { // && lastOp == zeroInt
                    //has a hard-false return
                    hardFalse = true;
                }
                lastOp = op;
            }
            //System.out.println("\t\t\t- Opcodes: " + opCode);

            if (unplayable) {
                //System.out.println("\t\t\t- " + "Unplayable.");
                addUnusableAnnotation(cardClass);
            }
            else {
                //System.out.println("\t\t\t- " + (hardFalse ? "Sometimes unplayable?" : "Not unplayable."));
                addPlayableAnnotation(cardClass, hardFalse);
            }
            return true;
        }
        else {
            //Check super
            return recursiveCheckUnplayable(cardClass, cardClass);
        }
    }
    //If a class doesn't declare a canUse, this will attempt to check the superclass.
    private static boolean recursiveCheckUnplayable(CtClass baseClass, CtClass testClass) throws BadBytecode {
        try {
            CtClass superClass = testClass.getSuperclass();
            if (superClass != null) {
                if (!testedClasses.contains(superClass.getName())) {
                    testedClasses.add(superClass.getName());

                    CtMethod[] methods = superClass.getDeclaredMethods();
                    for (CtMethod m : methods) {
                        if ("canUse".equals(m.getName())) {
                            CtClass[] params = m.getParameterTypes();
                            if (params.length == 2 && params[0].getName().equals(AbstractPlayer.class.getName()) && params[1].getName().equals(AbstractMonster.class.getName())) {
                                checkUnplayable(superClass, m);
                                break;
                            }
                        }
                    }
                }
                if (superClass.hasAnnotation(unplayableAnnotation)) {
                    addUnusableAnnotation(baseClass);
                    return true;
                }
            }
            return false;
        }
        catch (NotFoundException ignored) {
            return false;
        }
    }

    private static final String playableAnnotation = "friskmod/annotations/Playable";
    private static final String unplayableAnnotation = "friskmod/annotations/Unplayable";

    private static void addPlayableAnnotation(CtClass clazz, boolean sometimes) {
        ConstPool pool = clazz.getClassFile().getConstPool();
        Annotation usable = new Annotation(playableAnnotation, pool);
        usable.addMemberValue("sometimes", new BooleanMemberValue(sometimes, pool));
        AttributeInfo info = clazz.getClassFile().getAttribute(AnnotationsAttribute.visibleTag);
        AnnotationsAttribute attr;
        if (info != null) {
            attr = (AnnotationsAttribute) info;
        }
        else {
            attr = new AnnotationsAttribute(pool, AnnotationsAttribute.visibleTag);
        }
        attr.addAnnotation(usable);
        clazz.getClassFile().addAttribute(attr);
    }
    private static void addUnusableAnnotation(CtClass clazz) {
        ConstPool pool = clazz.getClassFile().getConstPool();
        Annotation usable = new Annotation(unplayableAnnotation, pool);
        AttributeInfo info = clazz.getClassFile().getAttribute(AnnotationsAttribute.visibleTag);
        AnnotationsAttribute attr;
        if (info != null) {
            attr = (AnnotationsAttribute) info;
        }
        else {
            attr = new AnnotationsAttribute(pool, AnnotationsAttribute.visibleTag);
        }
        attr.addAnnotation(usable);
        clazz.getClassFile().addAttribute(attr);
    }


    public static boolean isUnplayable(AbstractPlayer p, AbstractCard c) {
        if (alwaysUnplayable.contains(c.getClass().getName())) //unplayable.
            return true;

        if (sometimesUnplayable.contains(c.getClass().getName()) && c.cost == -2 && c.costForTurn == -2 && //might be unplayable, cost says it is
                (!c.canUse(p, null) || PerseveranceFields.perseverePlayed.get(c))) //it's not playable or only playable because perseverance
            return true;

        return false;
    }
    private static Set<String> alwaysUnplayable = new HashSet<>();
    private static Set<String> sometimesUnplayable = new HashSet<>();
    private static boolean hasUnplayableAnnotation(Object o) {
        return o.getClass().isAnnotationPresent(Unplayable.class);
    }
    private static boolean hasPlayableAnnotation(Object o) {
        return o.getClass().isAnnotationPresent(Playable.class);
    }
    private static boolean sometimesUnplayable(AbstractCard c) {
        Playable a = c.getClass().getAnnotation(Playable.class);

        if (a != null) {
            return a.sometimes();
        }
        return false;
    }

    public static void testPlayability() {
        ArrayList<AbstractCard> all = CardLibrary.getAllCards();
        for (AbstractCard c : all) {
            if ((c.type == AbstractCard.CardType.CURSE || c.type == AbstractCard.CardType.STATUS) && c.costForTurn == -2) {
                //Theoretically, an unplayable curse/status.
                //unplayableCards shouldn't contain this since its superclass is just AbstractCard, which doesn't guarantee unplayability.
                if (!hasPlayableAnnotation(c)) {
                    //Does not have a canUse override that sometimes returns true.
                    //System.out.println("\t- Unplayable: " + c.name);
                    alwaysUnplayable.add(c.getClass().getName());
                }
            }
            else {
                if (hasUnplayableAnnotation(c)) {
                    //System.out.println("\t- Unplayable: " + c.name);
                    alwaysUnplayable.add(c.getClass().getName());
                }
                if (sometimesUnplayable(c)) {
                    //System.out.println("\t- Sometimes unplayable? " + c.name);
                    sometimesUnplayable.add(c.getClass().getName());
                }
            }
        }
    }
    @SpirePatch(
            clz = UseCardAction.class,
            method = SpirePatch.CONSTRUCTOR,
            paramtypez = { AbstractCard.class, AbstractCreature.class }
    )
    public static class OnCardPersevered {
        @SpirePrefixPatch
        public static void Prefix(UseCardAction __instance, AbstractCard card, AbstractCreature target) {
            if (!card.dontTriggerOnUseCard) {
                if (card != null && (PerseveranceFields.perseverePlayed.get(card) || PerseveranceFields.dontTrap.get(card))) {
                    if (PerseveranceFields.overcomePlayed.get(card)) {
                        PerseveranceFields.overcomePlayed.set(card, false);
                        AbstractPower pow = AbstractDungeon.player.getPower(Overcome.POWER_ID);
                        if (pow != null) {
                            pow.flash();
                            Wiz.atb(new ReducePowerAction(AbstractDungeon.player, AbstractDungeon.player, Overcome.POWER_ID, 1));
                        } else {
                            FriskMod.logger.warn("{}: overcomePlayed set but no power found", FriskMod.modID);
                        }
                    }
                    if (PerseveranceFields.insufficientEnergy.get(card)) {
                        int refundEnergy = PerseveranceFields.currentEnergy.get(card);
                        Wiz.atb(new GainEnergyAction(refundEnergy));
                    }
                    PerseveranceFields.setIsPerseverable(card, false);
                    __instance.exhaustCard = true;
                    if (!PerseveranceFields.dontTrap.get(card)){
                        AbstractCard tmp = card.makeStatEquivalentCopy();
                        PerseveranceFields.trapped.set(tmp, true);
                        Wiz.atb(new MakeTempCardInHandAction(tmp));
                    }
                    PerseveranceFields.dontTrap.set(card, false);
                }
            }
        }
        @SpirePostfixPatch
        public static void Postfix(UseCardAction __instance, AbstractCard card, AbstractCreature target) {
            PerseveranceFields.perseverePlayed.set(card, false);
        }
    }


}
