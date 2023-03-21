package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * representation of a deck of cards ( CommonGoalCard and PersonalGoalCard)
 */
public class DeckCards{

    private final List<CommonGoalCard> deckCGC;
    private final List<PersonalGoalCard> deckPGC;

    /**
     * creation of a full deck of cards ( CommonGoalCard and PersonalGoalCard)
     */
    DeckCards(int nPlayers){
        CommonGoalCard CG1 = new CGC1(nPlayers);
        CommonGoalCard CG2 = new CGC2(nPlayers);
        CommonGoalCard CG3 = new CGC3(nPlayers);
        CommonGoalCard CG4 = new CGC4(nPlayers);
        CommonGoalCard CG5 = new CGC5(nPlayers);
        CommonGoalCard CG6 = new CGC6(nPlayers);
        CommonGoalCard CG7 = new CGC7(nPlayers);
        CommonGoalCard CG8 = new CGC8(nPlayers);
        CommonGoalCard CG9 = new CGC9(nPlayers);
        CommonGoalCard CG10 = new CGC10(nPlayers);
        CommonGoalCard CG11 = new CGC11(nPlayers);
        CommonGoalCard CG12 = new CGC12(nPlayers);

        PersonalGoalCard PG1 = new PersonalGoalCard(1);
        PersonalGoalCard PG2 = new PersonalGoalCard(2);
        PersonalGoalCard PG3 = new PersonalGoalCard(3);
        PersonalGoalCard PG4 = new PersonalGoalCard(4);
        PersonalGoalCard PG5 = new PersonalGoalCard(5);
        PersonalGoalCard PG6 = new PersonalGoalCard(6);
        PersonalGoalCard PG7 = new PersonalGoalCard(7);
        PersonalGoalCard PG8 = new PersonalGoalCard(8);
        PersonalGoalCard PG9 = new PersonalGoalCard(9);
        PersonalGoalCard PG10 = new PersonalGoalCard(10);
        PersonalGoalCard PG11 = new PersonalGoalCard(11);
        PersonalGoalCard PG12 = new PersonalGoalCard(12);


        deckCGC = new ArrayList<>();
        deckPGC = new ArrayList<>();

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
     * after you get that card is removed from deck
     * @return a random CommonGoalCard from deck
     */
    CommonGoalCard getRandCGC(){
        CommonGoalCard Cgc;
        Random random = new Random();
        Cgc = deckCGC.get(random.nextInt(deckCGC.size()));
        deckCGC.remove(Cgc);
        return Cgc;
    }

    /**
     * after you get that card is removed from deck
     * @return a random PersonalGoalCard from deck
     */
    PersonalGoalCard getRandPGC(){
        PersonalGoalCard Pgc;
        Random random = new Random();
        Pgc = deckPGC.get(random.nextInt(deckPGC.size()));
        deckPGC.remove(Pgc);
        return Pgc;
    }

}