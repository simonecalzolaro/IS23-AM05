package model;

/**
 * Common Goal card n 3
 * Goal:
 */

public class CGC3 extends CommonGoalCard{

    /**
     * @param np number of players
     */
    public CGC3(int np) {
        super(np);
    }

    /**
     *
     * @param shelf represent a matrix [6][5] of Tiles
     * @return true if the CGC3 Goal is achieved
     */
    public boolean checkGoal(Tile[][] shelf) {
        return false;
    }
}
