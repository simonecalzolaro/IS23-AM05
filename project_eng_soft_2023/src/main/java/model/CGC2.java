package model;

/**
 * Common Goal card n 2
 * Goal:Four tiles of the same type
 * at the four corners of the Library.
 */
public class CGC2 extends CommonGoalCard{
    /**
     * @param np number of players
     */
    public CGC2(int np) {
        super(np);
    }

    /**
     *
     * @param shelf represent a matrix [6][5] of Tiles
     * @return true if the CGC2 Goal is achieved
     */
    public boolean checkGoal(Tile[][] shelf) {

        return shelf[0][0].equals(shelf[0][shelf[0].length - 1])
                && shelf[0][0].equals(shelf[shelf.length - 1][shelf[0].length - 1])
                && shelf[0][0].equals(shelf[shelf[0].length - 1][0]);
    }
}
