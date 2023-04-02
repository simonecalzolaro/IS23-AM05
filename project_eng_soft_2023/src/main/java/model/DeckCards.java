package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * representation of a deck of cards ( CommonGoalCard and PersonalGoalCard)
 */
public class DeckCards{

    final private int deckPGCsize=12;
    final private int deckCGCsize=12;

    final private int nPlayers;

    private final List<Integer> cgcAlreadyReturned= new ArrayList<>();
    public final List<PersonalGoalCard> deckPGC;

    /**
     * creation of a full deck of cards ( CommonGoalCard and PersonalGoalCard)
     */
    public DeckCards(int nPlayers){

        this.nPlayers=nPlayers;

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


        deckPGC = new ArrayList<>();
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
    public CommonGoalCard getRandCGC(){

        int cardNumb;
        do{
            cardNumb= RandomInt(1,12);

        }while(cgcAlreadyReturned.contains(cardNumb));

        cgcAlreadyReturned.add(cardNumb);

        return new CommonGoalCard(cardNumb, this.nPlayers );
    }

    private int RandomInt(int min, int max){
        return (int) (Math.floor(Math.random() * ( max - min + 1 ) ) + min);
    }

    /**
     * after you get that card is removed from deck
     * @return a random PersonalGoalCard from deck
     */
    public PersonalGoalCard getRandPGC(){
        PersonalGoalCard Pgc;
        Random random = new Random();
        Pgc = deckPGC.get(random.nextInt(deckPGC.size()));
        deckPGC.remove(Pgc);
        return Pgc;
    }
    public int getDeckPGCsize() {
        return deckPGCsize;
    }

    public int getDeckCGCsize() {
        return deckCGCsize;
    }

}