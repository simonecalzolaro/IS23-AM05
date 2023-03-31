package model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class DeckCardsTest {

    @Test
    public void checkDeckCards(){
        DeckCards deckCards = new DeckCards(4);

        assertEquals(12, deckCards.getDeckCGCsize());
        assertEquals(12, deckCards.getDeckPGCsize());
    }

    @Test
    public void checkGetRandCGC(){
        DeckCards deckCards = new DeckCards(4);

        CommonGoalCard Cgc = deckCards.getRandCGC();

        for(int i=0; i<deckCards.getDeckCGCsize()-1; i++){
            assertNotEquals(Cgc, deckCards.getRandCGC());
        }

    }

    @Test
    public void checkGetRandPGC(){
        DeckCards deckCards = new DeckCards(4);

        PersonalGoalCard Pgc = deckCards.getRandPGC();

        for(int i=0; i<deckCards.getDeckPGCsize(); i++){
            assertNotEquals(Pgc, deckCards.getRandPGC());
        }
    }
}