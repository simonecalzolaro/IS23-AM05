package model;

import junit.framework.TestCase;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class BagTest {
        private Bag bag;
        @Before
        public void setUp() throws Exception{
            bag = new Bag();
        Assert.assertEquals(0, bag.getTilesNum());
        }

        @After
        public void tearDown() throws Exception{
            bag= null;
            Assert.assertNull(bag);
        }

        @Test

        public void testInitializeBag() {
            bag.initializeBag();
            Assert.assertEquals(132, bag.getTilesNum());
        }

        @Test
        public void testGetTilesFullBag(){
            bag.initializeBag();
            Tile tile;
            Tile test=bag.getTilesBag().get(0);
            tile = bag.getTile();
            Assert.assertEquals(test, tile);
            Assert.assertEquals(131, bag.getTilesNum());

        }

        @Test
    public void testGetTilesEmptyBag(){
            Tile tile;
            tile=bag.getTile();
            Assert.assertEquals(Tile.EMPTY,tile);
    }
}