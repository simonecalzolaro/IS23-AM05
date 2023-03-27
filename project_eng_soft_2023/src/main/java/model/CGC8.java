package model;

import java.util.ArrayList;

/**
 * Common Goal card n 8
 * Goal: Four rows each formed by 5 tiles of one, two or three different types.
 * Different lines may have different combinations of tile types.
 */
public class CGC8 extends CommonGoalCard{

    /**
     * @param np number of players
     */
    public CGC8(int np) {
        super(np);
    }

    /**
     *
     * @param shelf represent a matrix [6][5] of Tiles
     * @return true if the CGC8 Goal is achieved
     */
    public boolean checkGoal(Tile[][] shelf) {

        goodShelf(shelf);

        ArrayList<Tile> rowTiles = new ArrayList<>();
        int nRow=0;

        for(int row=0; row < shelf.length ; row++){

            rowTiles.clear();

            for(int col=0; col < shelf[0].length ; col++){
                if(!shelf[row][col].equals(Tile.EMPTY)) rowTiles.add(shelf[row][col]);
                else return false;
            }

            if( rowTiles.stream().count()==5
               && rowTiles.stream().distinct().count()<=3) nRow++;

            if(nRow==4) return true;
        }

        return false;
    }
}
