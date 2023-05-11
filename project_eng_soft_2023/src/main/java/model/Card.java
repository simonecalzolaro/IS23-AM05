package model;

import java.util.ArrayList;
import java.util.Map;

public interface Card {

    /**
     * @param shelf represent a matrix [6][5] of Tiles
     * @return true if the CGC Goal is achieved
     */
     boolean checkGoal(Tile [][] shelf);


}
