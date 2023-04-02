package model;

import java.util.ArrayList;

/**
 * Common Goal card n 5
 * Goal: Three columns each formed by 6 tiles of one, two or three different types.
 * Different columns may have different combinations of tile's types.
 */

public class CGC5 extends CommonGoalCard{
    /**
     * @param np number of players
     */
    public CGC5(int np) {
        super(np);
    }

    /**
     *
     * @param shelf represent a matrix [6][5] of Tiles
     * @return true if the CGC5 Goal is achieved
     */
    public boolean checkGoal(Tile[][] shelf) {

        goodShelf(shelf);

        ArrayList<Tile> colTiles=new ArrayList<>();
        int nCol=0;

        for(int col=0; col< shelf[0].length; col++){

            if(!shelf[shelf.length-1][col].equals(Tile.EMPTY)){
                colTiles.clear();

                for(int row= shelf.length-1; row>=0; row--){
                    colTiles.add(shelf[row][col]);
                }

                if ( colTiles.stream().distinct().count()<=3) nCol++;

                if(nCol==3) return true;
            }
        }

        return false;
    }
}
