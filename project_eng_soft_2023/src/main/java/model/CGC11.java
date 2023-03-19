package model;

/**
 * Common Goal card n 11
 * Goal: Five identical tiles forming an X shape
 */
public class CGC11 extends CommonGoalCard{
    /**
     * @param np number of players
     */
    public CGC11(int np) {
        super(np);
    }

    /**
     *
     * @param shelf represent a matrix [6][5] of Tiles
     * @return true if the CGC11 Goal is achieved
     */
    public boolean checkGoal(Tile[][] shelf) {

        for(int row=1; row < shelf.length-1; row++){

            for(int col=1; col < shelf[0].length-1 ; col++){

                if(shelf[row][col].equals(shelf[row-1][col-1])
                   && shelf[row][col].equals(shelf[row-1][col+1])
                   && shelf[row][col].equals(shelf[row+1][col+1])
                   && shelf[row][col].equals(shelf[row+1][col-1])
                   && !shelf[row][col].equals(Tile.EMPTY)) return true;
            }
        }
        return false;
    }
}
