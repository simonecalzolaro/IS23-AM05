package model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;


public class BagTest {
    private Bag bag;

    @Test

    public void testInitializeBag() {
        bag = new Bag();
        assertEquals(0, bag.getTilesNum());
        bag.initializeBag();
        assertEquals(132, bag.getTilesNum());
        bag= null;
    }

    @Test
    public void testGetTilesFullBag(){

        assertEquals(0, bag.getTilesNum());
        bag.initializeBag();
        Tile tile;
        Tile test=bag.getTilesBag().get(0);
        tile = bag.getTile();
        assertEquals(test, tile);
        assertEquals(131, bag.getTilesNum());
        bag= null;

    }

    @Test
    public void testGetTilesEmptyBag(){
        bag = new Bag();
        assertEquals(0, bag.getTilesNum());
        Tile tile;
        tile=bag.getTile();
        assertEquals(Tile.EMPTY,tile);
        bag= null;
    }
}