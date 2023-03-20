package model;

/**
 * representation of PersonalGoalCard1
 */
public class PG1 extends PersonalGoalCard{

    /**
     * creation of PG1 with all the coordinates related to Tiles color
     */
    PG1(){

        map.put(Tile.GREEN, new int[]{4,4});
        map.put(Tile.BLUE, new int[]{5,2});
        map.put(Tile.WHITE, new int[]{3,3});
        map.put(Tile.PINK, new int[]{5,0});
        map.put(Tile.YELLOW, new int[]{2,1});
        map.put(Tile.LIGHTBLUE, new int[]{0,2});

    }

}