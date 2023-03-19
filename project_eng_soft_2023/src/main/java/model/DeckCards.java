package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DeckCards{
    private static final CommonGoalCard CG1 = new CG1();
    private static final CommonGoalCard CG2 = new CG2();
    private static final CommonGoalCard CG3 = new CG3();
    private static final CommonGoalCard CG4 = new CG4();
    private static final CommonGoalCard CG5 = new CG5();
    private static final CommonGoalCard CG6 = new CG6();
    private static final CommonGoalCard CG7 = new CG7();
    private static final CommonGoalCard CG8 = new CG8();
    private static final CommonGoalCard CG9 = new CG9();
    private static final CommonGoalCard CG10 = new CG10();
    private static final CommonGoalCard CG11 = new CG11();
    private static final CommonGoalCard CG12 = new CG12();
    private static final PersonalGoalCard PG1 = new PG1();
    private static final PersonalGoalCard PG2 = new PG2();
    private static final PersonalGoalCard PG3 = new PG3();
    private static final PersonalGoalCard PG4 = new PG4();
    private static final PersonalGoalCard PG5 = new PG5();
    private static final PersonalGoalCard PG6 = new PG6();
    private static final PersonalGoalCard PG7 = new PG7();
    private static final PersonalGoalCard PG8 = new PG8();
    private static final PersonalGoalCard PG9 = new PG9();
    private static final PersonalGoalCard PG10 = new PG10();
    private static final PersonalGoalCard PG11 = new PG11();
    private static final PersonalGoalCard PG12 = new PG12();

    private List deckCGC;
    private List deckPGC;


    DeckCards(){
        deckCGC = new ArrayList<CommonGoalCard>();
        deckPGC = new ArrayList<PersonalGoalCard>();

        deckCGC.add(CG1);
        deckCGC.add(CG2);
        deckCGC.add(CG3);
        deckCGC.add(CG4);
        deckCGC.add(CG5);
        deckCGC.add(CG6);
        deckCGC.add(CG7);
        deckCGC.add(CG8);
        deckCGC.add(CG9);
        deckCGC.add(CG10);
        deckCGC.add(CG11);
        deckCGC.add(CG12);

        deckPGC.add(PG1);
        deckPGC.add(PG2);
        deckPGC.add(PG3);
        deckPGC.add(PG4);
        deckPGC.add(PG5);
        deckPGC.add(PG6);
        deckPGC.add(PG7);
        deckPGC.add(PG8);
        deckPGC.add(PG9);
        deckPGC.add(PG10);
        deckPGC.add(PG11);
        deckPGC.add(PG12);

    }

    CommonGoalCard getRandCGC(){
        Random random = new Random();
        return (CommonGoalCard) deckCGC.get(random.nextInt(deckCGC.size()));
    }

    PersonalGoalCard getRandPGC(){
        Random random = new Random();
        return (PersonalGoalCard) deckPGC.get(random.nextInt(deckPGC.size()));
    }

}
