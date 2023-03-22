package model;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * Common Goal card n 12
 * Goal: five columns in ascendant or descendent order with a difference of height of max one tile
 */
public class CGC12 extends CommonGoalCard{

    /**
     * @param np number of players
     */
    public CGC12(int np) {
        super(np);
    }

    /**
     *
     * @param shelf represent a matrix [6][5] of Tiles
     * @return  true if the CGC12 Goal is achieved
     */
    public boolean checkGoal(Tile[][] shelf) {

        ArrayList<Integer> nTiles= new ArrayList<>();

        for(int col=0; col < shelf[0].length ; col++){
            nTiles.add(0);
            for(int row=0; row < shelf.length; row++){
                if(shelf[row][col].equals(Tile.EMPTY)) break;
                else nTiles.set( nTiles.size()-1 , nTiles.get(nTiles.size()-1)+1 );//always updating the last position
            }
        }

        if(  ( nTiles.stream().sorted().equals(nTiles.stream()) //ascending order
               || nTiles.stream().sorted(Comparator.reverseOrder()).equals(nTiles.stream()) ) //descending order
           && nTiles.stream().distinct().count()==nTiles.stream().count() //no columns of the same height
           &&  nTiles.stream().min(Integer::compareTo).equals(nTiles.stream().max(Integer::compareTo).orElse(0)-shelf[0].length)
              ) return true;

        return false;
    }
}
