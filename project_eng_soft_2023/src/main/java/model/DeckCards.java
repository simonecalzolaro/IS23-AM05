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

    private final List<Integer> cgcAlreadyReturned = new ArrayList<>();
    private final List<Integer> PGCAlreadyReturned = new ArrayList<>();

    /**
     * creation of a full deck of cards ( CommonGoalCard and PersonalGoalCard)
     */
    public DeckCards(int nPlayers){

        this.nPlayers=nPlayers;

    }

    /**
     * after you get that card is removed from deck
     * @return a random CommonGoalCard from deck
     */
    public CommonGoalCard getRandCGC() throws IllegalArgumentException{

        int cardNumb;

        if(cgcAlreadyReturned.size()==12){
            throw new IllegalArgumentException();
        }else {

            do {
                cardNumb = RandomInt(1, 12);
            } while (cgcAlreadyReturned.contains(cardNumb));

            cgcAlreadyReturned.add(cardNumb);

            return new CommonGoalCard(cardNumb, this.nPlayers);
        }
    }

    private int RandomInt(int min, int max){
        return (int) (Math.floor(Math.random() * ( max - min + 1 ) ) + min);
    }

    /**
     * after you get that card is removed from deck
     * @return a random PersonalGoalCard from deck
     */
    public PersonalGoalCard getRandPGC() throws IllegalArgumentException{

        int cardNumb;

        if(PGCAlreadyReturned.size()==12){
            throw new IllegalArgumentException();
        }else {

            do {
                cardNumb = RandomInt(1, 12);
            } while (PGCAlreadyReturned.contains(cardNumb));

            PGCAlreadyReturned.add(cardNumb);

            return new PersonalGoalCard(cardNumb);
        }
    }

    public int getDeckPGCsize() {
        return deckPGCsize;
    }

    public int getDeckCGCsize() {
        return deckCGCsize;
    }

}