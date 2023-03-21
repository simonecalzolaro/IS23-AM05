package model;

import java.util.HashMap;
import java.util.Map;

/**
 * generic representation of a PersonalGoalCard
 */
public abstract class PersonalGoalCard implements Card{
    public int score=0;
    Map<Tile, int[]> map = new HashMap<>();


    /**
     *
     * @return the score achieved related to the PersonalGoalCard
     */
    public int getScore(Tile[][] shelf){
        updateScore(shelf);
        return score;
    }

    /**
     *
     * @param shelf represents a matrix [6][5] of Tiles
     * @return true if the PersonalGoalCard goals have been completed
     */
    public boolean checkGoal(Tile[][] shelf){
        return getScore(shelf) == 12;
    }

    /**
     * this method checks how many goals of the PersonalGoalCard have been achieved and gets the score related
     * @param shelf represents a matrix [6][5] of Tiles
     */
    public void updateScore(Tile[][] shelf) {
        int goal = 0;
        for (int i = 0; i < shelf.length; i++) {
            for (int j = 0; j < shelf[i].length; j++) {
                for (Map.Entry<Tile, int[]> entry : map.entrySet()) {
                    if (entry.getValue()[0] == i && entry.getValue()[1] == j && shelf[i][j].equals(entry.getKey())) goal++;
                }
            }
        }

        //System.out.println(goal);

        if(goal == 1) score=1;
        if(goal == 2) score=2;
        if(goal == 3) score=3;
        if(goal == 4) score=6;
        if(goal == 5) score=9;
        if(goal == 6) score=12;
    }

}