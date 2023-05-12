package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * generic representation of a PersonalGoalCard
 */
public class PersonalGoalCard implements Card, Serializable {
    public int score=0;
    Map<Tile, int[]> map = new HashMap<>();

    private int cardNumber;
    /**
     * creation of a specific PersonalGoalCard
     * @param card indicates the type of PersonalGoalCard you want to create
     */
    public PersonalGoalCard(int card){
        switch (card) {
            case 1 -> {
                map.put(Tile.GREEN, new int[]{4, 4});
                map.put(Tile.BLUE, new int[]{5, 2});
                map.put(Tile.WHITE, new int[]{3, 3});
                map.put(Tile.PINK, new int[]{5, 0});
                map.put(Tile.YELLOW, new int[]{2, 1});
                map.put(Tile.LIGHTBLUE, new int[]{0, 2});
            }
            case 2 -> {
                map.put(Tile.GREEN, new int[]{3, 0});
                map.put(Tile.BLUE, new int[]{0, 4});
                map.put(Tile.WHITE, new int[]{2, 4});
                map.put(Tile.PINK, new int[]{4, 1});
                map.put(Tile.YELLOW, new int[]{3, 2});
                map.put(Tile.LIGHTBLUE, new int[]{1, 3});
            }
            case 3 -> {
                map.put(Tile.GREEN, new int[]{2, 1});
                map.put(Tile.BLUE, new int[]{4, 0});
                map.put(Tile.WHITE, new int[]{0, 0});
                map.put(Tile.PINK, new int[]{3, 2});
                map.put(Tile.YELLOW, new int[]{4, 3});
                map.put(Tile.LIGHTBLUE, new int[]{2, 4});
            }
            case 4 -> {
                map.put(Tile.GREEN, new int[]{1, 2});
                map.put(Tile.BLUE, new int[]{3, 2});
                map.put(Tile.WHITE, new int[]{1, 1});
                map.put(Tile.PINK, new int[]{2, 3});
                map.put(Tile.YELLOW, new int[]{5, 4});
                map.put(Tile.LIGHTBLUE, new int[]{3, 2});
            }
            case 5 -> {
                map.put(Tile.GREEN, new int[]{0, 3});
                map.put(Tile.BLUE, new int[]{2, 1});
                map.put(Tile.WHITE, new int[]{2, 2});
                map.put(Tile.PINK, new int[]{1, 4});
                map.put(Tile.YELLOW, new int[]{0, 0});
                map.put(Tile.LIGHTBLUE, new int[]{4, 1});
            }
            case 6 -> {
                map.put(Tile.GREEN, new int[]{5, 4});
                map.put(Tile.BLUE, new int[]{1, 3});
                map.put(Tile.WHITE, new int[]{3, 3});
                map.put(Tile.PINK, new int[]{0, 0});
                map.put(Tile.YELLOW, new int[]{1, 1});
                map.put(Tile.LIGHTBLUE, new int[]{5, 2});
            }
            case 7 -> {
                map.put(Tile.GREEN, new int[]{5, 0});
                map.put(Tile.BLUE, new int[]{4, 3});
                map.put(Tile.WHITE, new int[]{0, 2});
                map.put(Tile.PINK, new int[]{3, 1});
                map.put(Tile.YELLOW, new int[]{1, 4});
                map.put(Tile.LIGHTBLUE, new int[]{2, 0});
            }
            case 8 -> {
                map.put(Tile.GREEN, new int[]{4, 1});
                map.put(Tile.BLUE, new int[]{5, 4});
                map.put(Tile.WHITE, new int[]{1, 3});
                map.put(Tile.PINK, new int[]{2, 0});
                map.put(Tile.YELLOW, new int[]{0, 3});
                map.put(Tile.LIGHTBLUE, new int[]{3, 2});
            }
            case 9 -> {
                map.put(Tile.GREEN, new int[]{3, 2});
                map.put(Tile.BLUE, new int[]{0, 0});
                map.put(Tile.WHITE, new int[]{2, 4});
                map.put(Tile.PINK, new int[]{1, 4});
                map.put(Tile.YELLOW, new int[]{5, 2});
                map.put(Tile.LIGHTBLUE, new int[]{1, 1});
            }
            case 10 -> {
                map.put(Tile.GREEN, new int[]{2, 3});
                map.put(Tile.BLUE, new int[]{1, 1});
                map.put(Tile.WHITE, new int[]{3, 0});
                map.put(Tile.PINK, new int[]{0, 3});
                map.put(Tile.YELLOW, new int[]{4, 1});
                map.put(Tile.LIGHTBLUE, new int[]{5, 4});
            }
            case 11 -> {
                map.put(Tile.GREEN, new int[]{1, 4});
                map.put(Tile.BLUE, new int[]{2, 2});
                map.put(Tile.WHITE, new int[]{4, 1});
                map.put(Tile.PINK, new int[]{5, 2});
                map.put(Tile.YELLOW, new int[]{3, 0});
                map.put(Tile.LIGHTBLUE, new int[]{0, 3});
            }
            case 12 -> {
                map.put(Tile.GREEN, new int[]{0, 0});
                map.put(Tile.BLUE, new int[]{3, 2});
                map.put(Tile.WHITE, new int[]{5, 2});
                map.put(Tile.PINK, new int[]{4, 1});
                map.put(Tile.YELLOW, new int[]{1, 4});
                map.put(Tile.LIGHTBLUE, new int[]{2, 3});
            }
        }

    }


    /**
     *
     * @param shelf represents a matrix [6][5] of Tiles
     * @return the score achieved in that PersonalGoalCard
     */
    public int getScore(Tile[][] shelf){
        updateScore(shelf);
        return score;
    }

    /**
     * @return this.cardNumber
     */
    public int getCardNumber(){
        return this.cardNumber;
    }

    /**
     *
     * @param shelf represents a matrix [6][5] of Tiles
     * @return true if the PersonalGoalCard goals have been completed
     */
    public boolean checkGoal(Tile[][] shelf){
        goodShelf(shelf);
        return getScore(shelf) == 12;
    }


    public Map<Tile, Integer[]> getCardMap() {

        Map<Tile, Integer[]> newMap=new HashMap<>();

        for (Map.Entry<Tile, int[]> entry : map.entrySet()){

            newMap.put(entry.getKey(), new Integer[]{entry.getValue()[0], entry.getValue()[1]});


        }

        return newMap;
    }

    /**
     * this method checks how many goals of the PersonalGoalCard have been achieved and gets the score related
     * @param shelf represents a matrix [6][5] of Tiles
     */
    public void updateScore(Tile[][] shelf) {
        int goal = 0;

        for (Map.Entry<Tile, int[]> entry : map.entrySet()) {
            if (shelf[entry.getValue()[0]][entry.getValue()[1]].equals(entry.getKey())) goal++;
        }

        //System.out.println(goal);

        if(goal == 1) score=1;
        if(goal == 2) score=2;
        if(goal == 3) score=3;
        if(goal == 4) score=6;
        if(goal == 5) score=9;
        if(goal == 6) score=12;
    }

    public void goodShelf(Tile[][] shelf){

        //for each column I check that "EMPTY" tiles are ONLY at the end;

        boolean emptyTilesFound;

        for(int col=0; col< shelf[0].length; col++){//columns

            emptyTilesFound=false;

            for(int row=0; row< shelf.length; row++){//columns

                if (shelf[row][col].equals(Tile.EMPTY)) emptyTilesFound=true ;
                else{
                    if ( emptyTilesFound ) throw new IllegalArgumentException(" The shelf is not filled properly ");;
                }
            }
        }
    }
}