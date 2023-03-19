package model;

public interface Card {
    /**
     *
     * @param shelf represent a matrix [6][5] of Tiles
     * @return true if the CGC Goal is achieved
     */
    public boolean checkGoal(Tile [][] shelf);
}
