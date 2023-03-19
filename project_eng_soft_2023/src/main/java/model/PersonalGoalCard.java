package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
public abstract class PersonalGoalCard implements Card{
    public int score=0;
    Map<Tile, int[]> map = new HashMap<>();

    public int getScore(){
        return score;
    }

    public boolean checkGoal(Tile shelf[][]){
        return getScore() == 12;
    }

    public void updateScore(Tile[][] shelf) {
        int goal = 0;
        for (int i = 0; i < shelf.length; i++) {
            for (int j = 0; j < shelf[i].length; j++) {
                for (Map.Entry<Tile, int[]> entry : map.entrySet()) {
                    if (entry.getValue()[0] == i && entry.getValue()[1] == j && shelf[i][j].equals(entry.getKey())) goal++;
                }
            }
        }

        System.out.println(goal);

        if(goal ==1) score=1;
        if(goal ==2) score=2;
        if(goal ==3) score=3;
        if(goal ==4) score=6;
        if(goal ==5) score=9;
        if(goal ==6) score=12;
    }

}
