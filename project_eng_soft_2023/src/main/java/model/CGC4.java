package model;



import java.util.ArrayList;

/**
 * Common Goal card n 4
 * Goal: Two separate groups of 4 tiles of the same type forming a 2x2 square.
 * The tiles of the two groups must be of the same type.
 */
public class CGC4 extends CommonGoalCard{

    ArrayList<Tile> tiles;
    int nGroups;
    /**
     * @param np number of players
     */
    public CGC4(int np) {
        super(np);

        tiles=new ArrayList<Tile>();
        tiles.add(Tile.BLUE);
        tiles.add(Tile.PINK);
        tiles.add(Tile.LIGHTBLUE);
        tiles.add(Tile.GREEN);
        tiles.add(Tile.YELLOW);
        tiles.add(Tile.WHITE);

        nGroups=0;

    }

    /**
     *
     * @param shelf represent a matrix [6][5] of Tiles
     * @return true if the CGC4 Goal is achieved
     */
    public boolean checkGoal(Tile[][] shelf) {

        for(Tile t : tiles){

            nGroups=0;

            for(int row=0; row < shelf.length-1; row++){
                for(int col=0; col< shelf[0].length-1 ; col++){

                    if(shelf[row][col].equals(shelf[row][col+1])
                       && shelf[row][col].equals(shelf[row+1][col+1])
                       && shelf[row][col].equals(shelf[row+1][col]) ) nGroups++;

                    if (nGroups==2) return true;

                }
            }
        }

        return false;
    }
}
