package model;

import java.util.ArrayList;

/**
 * Common Goal card n 10
 * Goal: Two rows each formed by 5 different type of tile
 */
public class CGC10 extends CommonGoalCard{

    /**
     * @param np number of players
     */
    public CGC10(int np) {
        super(np);
    }

    /**
     *
     * @param shelf represent a matrix [6][5] of Tiles
     * @return true if the CGC10 Goal is achieved
     */
    public boolean checkGoal(Tile[][] shelf) {

        ArrayList<Tile> rowTiles = new ArrayList<>();
        int nRow=0;

        for(int row=0; row < shelf.length ; row++){

            rowTiles.clear();

            for(int col=0; col < shelf[0].length ; col++){
                if(!shelf[row][col].equals(Tile.EMPTY)) rowTiles.add(shelf[row][col]);
                else return false;
            }

            if( rowTiles.stream().count()==5
                    && rowTiles.stream().distinct().count()==5) nRow++;

            if(nRow==2) return true;
        }

        return false;
    }
}
