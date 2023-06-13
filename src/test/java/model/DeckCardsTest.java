package model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class DeckCardsTest {

    @Test
    public void checkGetRandCGC(){
        DeckCards deckCards = new DeckCards(4);

        List<CommonGoalCard> CGC = new ArrayList<>();

        for(int i=0; i<12; i++){
            CGC.add(deckCards.getRandCGC());
        }

        assertEquals(12,CGC.stream().distinct().count());

    }

    @Test
    public void checkGetRandPGC(){
        DeckCards deckCards = new DeckCards(4);

        List<PersonalGoalCard> PGC = new ArrayList<>();

        for(int i=0; i<12; i++){
            PGC.add(deckCards.getRandPGC());
        }

        assertEquals(12,PGC.stream().distinct().count());

    }
}