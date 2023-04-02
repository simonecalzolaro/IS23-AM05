package model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class DeckCardsTest {

    @Test
    public void checkDeckCards(){
        DeckCards deckCards = new DeckCards(4);

        assertEquals(12, deckCards.deckCGC.size());
        assertEquals(12, deckCards.deckPGC.size());
    }

    @Test
    public void checkGetRandCGC(){
        DeckCards deckCards = new DeckCards(4);

        CommonGoalCard Cgc = deckCards.getRandCGC();

        for(int i=0; i<deckCards.deckCGC.size(); i++){
            assertNotEquals(Cgc, deckCards.deckCGC.get(i));
        }

    }

    @Test
    public void checkGetRandPGC(){
        DeckCards deckCards = new DeckCards(4);

        PersonalGoalCard Pgc = deckCards.getRandPGC();

        for(int i=0; i<deckCards.deckPGC.size(); i++){
            assertNotEquals(Pgc, deckCards.deckPGC.get(i));
        }
    }
}