package model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Stream;
import java.util.stream.Collectors;

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

        goodShelf(shelf);

        ArrayList<Integer> nTiles= new ArrayList<>();
        int nTileCol, min=10, max=0;


        for(int col=0; col < shelf[0].length ; col++){
            nTileCol=0;
            for(int row=0; row < shelf.length; row++){
                if(shelf[row][col].equals(Tile.EMPTY)) break;
                else nTileCol++;
            }
            if(nTileCol > max) max=nTileCol;
            if(nTileCol < min) min=nTileCol;

            nTiles.add(nTileCol);
        }

        if(max==0 || min==0 ) return false;



        if( ( (   nTiles.stream().sorted().collect(Collectors.toList()).equals(nTiles.stream().collect(Collectors.toList()))
                 && nTiles.stream().findFirst().orElse(-1)==min
                 && nTiles.stream().reduce((first, second) -> second).orElse(-1)==max ) //ascending order
               ||
              (   nTiles.stream().sorted(Comparator.reverseOrder()).collect(Collectors.toList()).equals(nTiles.stream().collect(Collectors.toList()))
                 && nTiles.stream().findFirst().orElse(-1)==max
                 && nTiles.stream().reduce((first, second) -> second).orElse(-1)==min ) //descending order
            )
            && nTiles.stream().distinct().count() == shelf[0].length //no columns of the same height
            && shelf[0].length==max-min+1

          ) return true;

        return false;
    }
}
