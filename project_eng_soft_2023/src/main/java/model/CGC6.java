package model;

import java.util.ArrayList;

/**
 * Common Goal card n 6
 * Goal: Eight tiles of the same type.
 * There are no restrictions on the location of these tiles.
 */
public class CGC6 extends CommonGoalCard{

    ArrayList<Tile> tiles;
    /**
     * @param np number of players
     */
    public CGC6(int np) {
        super(np);

        tiles=new ArrayList<Tile>();
        tiles.add(Tile.BLUE);
        tiles.add(Tile.PINK);
        tiles.add(Tile.LIGHTBLUE);
        tiles.add(Tile.GREEN);
        tiles.add(Tile.YELLOW);
        tiles.add(Tile.WHITE);
    }

    /**
     *
     * @param shelf represent a matrix [6][5] of Tiles
     * @return true if the CGC6 Goal is achieved
     */
    public boolean checkGoal(Tile[][] shelf) {

        ArrayList<Tile> allTiles = new ArrayList<>();

        //collecting all the tiles in a single ArrayList
        for(int row=0; row < shelf.length ; row++){
            for(int col=0; col< shelf[0].length ; col++){
                if(!shelf[row][col].equals(Tile.EMPTY)) allTiles.add(shelf[row][col]);
            }
        }

        //checking ho many elements ar present for each type of tile
        for(Tile t: tiles){

            if( allTiles.stream().filter(x -> x.equals(t)).count()==8) return true;

        }

        return false;
    }
}
