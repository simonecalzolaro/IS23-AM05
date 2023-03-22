package model;

import java.util.ArrayList;

/**
 * Common Goal card n 3
 * Goal: Four distinct groups each formed by four tiles of the same type.
 * The tiles can be of different types from group to group.
 */

public class CGC3 extends CommonGoalCard{

    private static boolean [][] visited;
    private static boolean [][] booleShelf;
    ArrayList<Tile> tiles;

    /**
     * @param np number of players
     */
    public CGC3(int np) {
        super(np);

        booleShelf=new boolean[6][5];

        visited = new boolean[6][5];
        for(int row=0; row < 6; row++){
            for(int col=0; col< 5; col++){
                visited[row][col]=false;
            }
        }

        tiles=new ArrayList<Tile>();
        tiles.add(Tile.BLUE);
        tiles.add(Tile.PINK);
        tiles.add(Tile.LIGHTBLUE);
        tiles.add(Tile.GREEN);
        tiles.add(Tile.YELLOW);
        tiles.add(Tile.WHITE);
    }

    /**
     *
     * @param shelf represent a matrix [6][5] of Tiles
     * @return true if the CGC3 Goal is achieved
     */
    public boolean checkGoal(Tile[][] shelf) {

        int nElem;
        int nGroups=0;


        for(Tile t: tiles) {

            //initialize booleShelf
            for (int row = 0; row < shelf.length; row++) {
                for (int col = 0; col < shelf[0].length; col++) {
                    booleShelf[row][col]=shelf[row][col].equals(t);
                }
            }


            for (int row = 0; row < shelf.length; row++) {
                for (int col = 0; col < shelf[0].length; col++) {
                    if (!visited[row][col]) {

                        nElem = countElem(row, col, 0);
                        nGroups = nGroups + (int) nElem / 4;

                        if (nGroups == 4) return true;

                    }
                }
            }
        }

        return false;
    }




    private int countElem(int row, int col, int nElem){


        if(booleShelf[row][col] && !visited[row][col]) {

            visited[row][col]=true;

            //up shift
            if (row < visited.length - 1) {
                nElem = countElem(row + 1, col, nElem);
            }

            //shift down
            if (row > 0) {
                nElem = countElem(row -1, col, nElem);
            }

            //shift right
            if (col < visited[0].length - 1) {
                nElem = countElem(row , col+1 , nElem);
            }

            //shift left
            if (col > 0) {
                nElem = countElem(row , col-1 , nElem);
            }

            return nElem+1;

        }

        return nElem;
    }
}
