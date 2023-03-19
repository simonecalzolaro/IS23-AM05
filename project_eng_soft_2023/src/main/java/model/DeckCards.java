package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 */
public class DeckCards{

    private final List<CommonGoalCard> deckCGC;
    private final List<PersonalGoalCard> deckPGC;

    /**
     *
     */
    DeckCards(int nPlayers){
        CommonGoalCard CG1 = new CG1(nPlayers);
        CommonGoalCard CG2 = new CG2(nPlayers);
        CommonGoalCard CG3 = new CG3(nPlayers);
        CommonGoalCard CG4 = new CG4(nPlayers);
        CommonGoalCard CG5 = new CG5(nPlayers);
        CommonGoalCard CG6 = new CG6(nPlayers);
        CommonGoalCard CG7 = new CG7(nPlayers);
        CommonGoalCard CG8 = new CG8(nPlayers);
        CommonGoalCard CG9 = new CG9(nPlayers);
        CommonGoalCard CG10 = new CG10(nPlayers);
        CommonGoalCard CG11 = new CG11(nPlayers);
        CommonGoalCard CG12 = new CG12(nPlayers);

        PersonalGoalCard PG1 = new PG1();
        PersonalGoalCard PG2 = new PG2();
        PersonalGoalCard PG3 = new PG3();
        PersonalGoalCard PG4 = new PG4();
        PersonalGoalCard PG5 = new PG5();
        PersonalGoalCard PG6 = new PG6();
        PersonalGoalCard PG7 = new PG7();
        PersonalGoalCard PG8 = new PG8();
        PersonalGoalCard PG9 = new PG9();
        PersonalGoalCard PG10 = new PG10();
        PersonalGoalCard PG11 = new PG11();
        PersonalGoalCard PG12 = new PG12();


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

    /**
     *
     * @return
     */
    CommonGoalCard getRandCGC(){
        Random random = new Random();
        return deckCGC.get(random.nextInt(deckCGC.size()));
    }

    /**
     *
     * @return
     */
    PersonalGoalCard getRandPGC(){
        Random random = new Random();
        return deckPGC.get(random.nextInt(deckPGC.size()));
    }

}
