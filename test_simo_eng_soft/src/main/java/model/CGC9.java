package model;

import java.util.ArrayList;

/**
 * Common Goal card n
 * Goal: two columns each formed by 6 different type of tile
 */
public class CGC9 extends CommonGoalCard{
    /**
     * @param np number of players
     */
    public CGC9(int np) {
        super(np);
    }

    /**
     *
     * @param shelf represent a matrix [6][5] of Tiles
     * @return true if the CGC9 Goal is achieved
     */
    public boolean checkGoal(Tile[][] shelf) {

        goodShelf(shelf);

        ArrayList<Tile> colTiles=new ArrayList<>();
        int nCol=0;

        for(int col=0; col< shelf[0].length; col++){

            if(!shelf[shelf.length-1][col].equals(Tile.EMPTY)){ //if the highest element is not EMPTY
                colTiles.clear();

                for(int row= shelf.length-1; row>=0; row--){
                    colTiles.add(shelf[row][col]);
                }

                if ( colTiles.stream().distinct().count()==6) nCol++;

                if(nCol==2) return true;
            }
        }
        return false;
    }
}
