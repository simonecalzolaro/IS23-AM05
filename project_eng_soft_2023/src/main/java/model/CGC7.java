package model;

import java.util.ArrayList;

/**
 * Common Goal card n 7
 * Goal: Five tiles of the same type on diagonal
 */
public class CGC7 extends CommonGoalCard{
    /**
     * @param np number of players
     */
    public CGC7(int np) {
        super(np);
    }

    /**
     *
     * @param shelf represent a matrix [6][5] of Tiles
     * @return true if the CGC7 Goal is achieved
     */
    public boolean checkGoal(Tile[][] shelf) {

        ArrayList<Tile> diagTiles = new ArrayList<>();

        diagTiles.clear();
        for(int pos=0; pos<shelf[0].length; pos++){
            diagTiles.add(shelf[pos][pos]);
        }
        if(diagTiles.stream().distinct().count()==1) return true;

        diagTiles.clear();
        for(int pos=0; pos<shelf[0].length; pos++){
            diagTiles.add(shelf[pos+1][pos]);
        }
        if(diagTiles.stream().distinct().count()==1) return true;

        diagTiles.clear();
        for(int pos=shelf[0].length-1; pos>=0; pos--){
            diagTiles.add(shelf[pos][shelf[0].length-1-pos]);
        }
        if(diagTiles.stream().distinct().count()==1) return true;

        diagTiles.clear();
        for(int pos=shelf[0].length-1; pos>=0; pos--){
            diagTiles.add(shelf[pos+1][shelf[0].length-1-pos]);
        }
        if(diagTiles.stream().distinct().count()==1) return true;


        return false;
    }
}
