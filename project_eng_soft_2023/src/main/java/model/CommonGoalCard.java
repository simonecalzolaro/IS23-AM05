package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.EmptyStackException;
import java.util.Stack;
import java.util.stream.Collectors;

public class CommonGoalCard implements Card, Serializable {
    private Stack<Token> stackTiles;
    private final int CGCnumber;

    public CommonGoalCard(int CGCnumber, int np) {

        if(np>=2) {
            stackTiles = new Stack<Token>();
            stackTiles.push(Token.ST2);
            stackTiles.push(Token.ST4);
            if (np > 2) {
                stackTiles.push(Token.ST6);
                if (np > 3) stackTiles.push(Token.ST8);
            }
        }else throw new IllegalArgumentException(" Number of players must be greater than 1 ");

        if(CGCnumber>=1 && CGCnumber<=12) this.CGCnumber = CGCnumber;
        else throw new IllegalArgumentException(" CGCnumber must be between 1 and 12 ");

    }



    @Override
    public boolean checkGoal(Tile[][] shelf) {

        goodShelf(shelf);

        switch(this.CGCnumber) {

            /**
             * Common Goal card n 1
             * Goal: Six separated groups of two identical tiles.
             * The tiles can be of different types from group to group.
             */
            case 1:
                return checkGroups(6, 2, shelf);

            /**
             * Common Goal card n 2
             * Goal:Four tiles of the same type
             * at the four corners of the Library.
             */
            case 2:
                return checkShape(this.CGCnumber, shelf);

            /**
             * Common Goal card n 3
             * Goal: Four distinct groups each formed by four tiles of the same type.
             * The tiles can be of different types from group to group.
             */
            case 3:
                return checkGroups(4, 4, shelf);

            /**
             * Common Goal card n 4
             * Goal: Two separate groups of 4 tiles of the same type forming a 2x2 square.
             * The tiles of the two groups must be of the same type.
             */
            case 4:
                return checkShape(this.CGCnumber, shelf);

            /**
             * Common Goal card n 5
             * Goal: Three columns each formed by 6 tiles of one, two or three different types.
             * Different columns may have different combinations of tile's types.
             */
            case 5:
                return maxDiffTiles(3, 3, true, shelf);

            /**
             * Common Goal card n 6
             * Goal: Eight tiles of the same type.
             * There are no restrictions on the location of these tiles.
             */
            case 6:
                return check_CGC6(shelf);

            /**
             * Common Goal card n 7
             * Goal: Five tiles of the same type on a diagonal
             */
            case 7:
                return checkShape(this.CGCnumber, shelf);

            /**
             * Common Goal card n 8
             * Goal: Four rows each formed by 5 tiles of one, two or three different types.
             * Different lines may have different combinations of tile types.
             */
            case 8:
                return maxDiffTiles(4, 3, false, shelf);

            /**
             * Common Goal card n 9
             * Goal: two columns each formed by 6 different type of tile
             */
            case 9:
                return allDiffTiles(2, true, shelf);

            /**
             * Common Goal card n 10
             * Goal: Two rows each formed by 5 different type of tile
             */
            case 10:
                return allDiffTiles(2, false, shelf);

            /**
             * Common Goal card n 11
             * Goal: Five identical tiles forming an X shape
             */
            case 11:
                return checkShape( this.CGCnumber, shelf );

            /**
             * Common Goal card n 12
             * Goal: five columns in ascendant or descendent order with a difference of height of max one tile
             */
            case 12:
                return check_CGC12( shelf );

        }

        return false;
    }


    /**
     * @return the top Token on the stack, if EmptyStackException occurs it returns always the smallest token
     */
    public Token getTopStack() {
        try{
            return stackTiles.pop();
        }catch (EmptyStackException e){
            return Token.ST2;
        }

    }

    public int getCGCnumber() {
        return CGCnumber;
    }

    /**
     * check if the shelf is correctly filled, it means that is impossible to have "floating" Tiles
     * @param shelf
     */
    protected static void goodShelf(Tile[][] shelf){

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

    /**
     *search if exist "nubOfGroups" groups with "elemPerGroup" elements
     * @param nubOfGroups total number of groups to reach
     * @param elemPerGroup total number of identical elements to reach in each group
     * @param shelf is the shelf to check
     * @return true if the goals set by the parameters are achieved
     */
    protected static boolean checkGroups(int nubOfGroups, int elemPerGroup, Tile[][] shelf ){


        boolean [][] visited;
        boolean [][] booleShelf;
        ArrayList<Tile> tiles;

        int nElem;
        int nGroups=0;

        //declare booleshelf
        booleShelf=new boolean[shelf.length][shelf[0].length];

        //declare and initialize visited all to false
        visited = new boolean[shelf.length][shelf[0].length];
        for(int row=0; row < shelf.length; row++){
            for(int col=0; col< shelf[0].length; col++){
                visited[row][col]=false;
            }
        }

        //declare and initialize tiles
        tiles=new ArrayList<>();
        tiles.add(Tile.BLUE);
        tiles.add(Tile.PINK);
        tiles.add(Tile.LIGHTBLUE);
        tiles.add(Tile.GREEN);
        tiles.add(Tile.YELLOW);
        tiles.add(Tile.WHITE);


        //for each type of tile
        for(Tile t: tiles) {

            //initialize booleShelf to true when there is a tile of the same type of t in [row][col]
            for (int row = 0; row < shelf.length; row++) {
                for (int col = 0; col < shelf[0].length; col++) {
                    booleShelf[row][col]=shelf[row][col].equals(t);
                }
            }


            for (int row = 0; row < shelf.length; row++) {
                for (int col = 0; col < shelf[0].length; col++) {

                    //if not visited yet
                    if (!visited[row][col]) {
                        if(countElem(row, col, 0, booleShelf, visited)==elemPerGroup) nGroups ++;
                        if (nGroups == nubOfGroups) return true;
                    }
                }
            }
        }
        return false;
    }


    /**
     * recursive function
     * @param row current row
     * @param col current column
     * @param nElem number of elements founded
     * @return the number of elements in a group
     */
    private static int countElem(int row, int col, int nElem, boolean[][] booleShelf, boolean[][] visited){

        if(booleShelf[row][col] && !visited[row][col]) {

            visited[row][col]=true;

            //up shift
            if (row < visited.length - 1) {
                nElem = countElem(row + 1, col, nElem, booleShelf, visited);
            }

            //shift down
            if (row > 0) {
                nElem = countElem(row -1, col, nElem, booleShelf, visited);
            }

            //shift right
            if (col < visited[0].length - 1) {
                nElem = countElem(row , col+1 , nElem, booleShelf, visited);
            }

            //shift left
            if (col > 0) {
                nElem = countElem(row , col-1 , nElem, booleShelf, visited);
            }

            return nElem+1;

        }
        return nElem;
    }


    /**
     * depending on the parameter nShape, verify if that specific shape is present
     * @param nShape indicates the type of shape depending on the card
     * @param shelf shelf of tiles
     * @return true in the shape has been found
     */
    private static boolean checkShape(int nShape, Tile[][] shelf ){

        switch(nShape){

            case 2:
                return     shelf[0][0].equals(shelf[0][shelf[0].length - 1])
                        && shelf[0][0].equals(shelf[shelf.length - 1][shelf[0].length - 1])
                        && shelf[0][0].equals(shelf[shelf.length - 1][0])
                        && !shelf[0][0].equals(Tile.EMPTY);

            case 4:

                ArrayList<Tile> tiles;
                boolean [][] visited;
                boolean [][] booleShelf;
                int nGroups;

                //initializing
                tiles=new ArrayList<Tile>();
                tiles.add(Tile.BLUE);
                tiles.add(Tile.PINK);
                tiles.add(Tile.LIGHTBLUE);
                tiles.add(Tile.GREEN);
                tiles.add(Tile.YELLOW);
                tiles.add(Tile.WHITE);

                visited = new boolean[shelf.length][shelf[0].length];
                for(int row=0; row < shelf.length; row++){
                    for(int col=0; col< shelf[0].length; col++){
                        visited[row][col]=false;
                    }
                }

                booleShelf=new boolean[6][5];

                nGroups=0;



                for(Tile t : tiles){

                    nGroups=0;

                    for (int row = 0; row < shelf.length; row++) {
                        for (int col = 0; col < shelf[0].length; col++) {
                            booleShelf[row][col]=shelf[row][col].equals(t);
                        }
                    }

                    for(int row=0; row < shelf.length-1; row++){
                        for(int col=0; col< shelf[0].length-1 ; col++){

                            if( !visited[row][col]
                                    && booleShelf[row][col]
                                    && booleShelf[row][col+1]
                                    && booleShelf[row+1][col+1]
                                    && booleShelf[row+1][col] ) {

                                visited[row][col]=true;
                                visited[row+1][col]=true;
                                visited[row][col+1]=true;
                                visited[row+1][col+1]=true;

                                nGroups++;

                            }

                            if (nGroups==2) return true;

                        }
                    }
                }

                return false;

            case 7:

                ArrayList<Tile> diagTiles = new ArrayList<>();

                diagTiles.clear();
                //left to right, row 0, bottom up
                for(int pos=0; pos<shelf[0].length; pos++){
                    diagTiles.add(shelf[pos][pos]);
                }
                if(diagTiles.stream().distinct().count()==1 && !diagTiles.stream().anyMatch(x->x.equals(Tile.EMPTY))) return true;


                diagTiles.clear();
                //right to left, row 0, top down
                for(int pos=shelf[0].length-1; pos>=0; pos--){ // 5 -> 1
                    diagTiles.add(shelf[pos][shelf[0].length-1-pos]);
                }
                if(diagTiles.stream().distinct().count()==1 && !diagTiles.stream().anyMatch(x->x.equals(Tile.EMPTY))) return true;

                diagTiles.clear();
                //left to right, row 1, bottom up
                for(int pos=0; pos<shelf[0].length; pos++){
                    diagTiles.add(shelf[pos+1][pos]);
                }
                if(diagTiles.stream().distinct().count()==1 && !diagTiles.stream().anyMatch(x->x.equals(Tile.EMPTY))) return true;

                diagTiles.clear();
                //right to left, row 1, top down
                for(int pos=shelf[0].length-1; pos>=0; pos--){
                    diagTiles.add(shelf[pos+1][shelf[0].length-1-pos]);
                }
                if(diagTiles.stream().distinct().count()==1 && !diagTiles.stream().anyMatch(x->x.equals(Tile.EMPTY))) return true;


                return false;

            case 11:

                for(int row=1; row < shelf.length-1; row++){

                    for(int col=1; col < shelf[0].length-1 ; col++){

                        if(shelf[row][col].equals(shelf[row-1][col-1])
                                && shelf[row][col].equals(shelf[row-1][col+1])
                                && shelf[row][col].equals(shelf[row+1][col+1])
                                && shelf[row][col].equals(shelf[row+1][col-1])
                                && !shelf[row][col].equals(Tile.EMPTY)) return true;
                    }
                }
                return false;


        }

        return false;

    }


    /**
     * check if in the shelf exists at least totNumbOfLines (columns or rows) with maxNumbOfDiffTypes different type of tiles
     * @param totNumbOfLines number of row or columns to satisfy the request
     * @param maxNumbOfDiffTypes max number of distinct type of tiles that can be present in a line (column or row)
     * @param verticalLine is true if the lines must be vertical (columns) otherwise is false (row)
     * @param shelf the shelf[][] of tiles
     * @return true if the request is satisfied
     */
    private static boolean maxDiffTiles(int totNumbOfLines, int maxNumbOfDiffTypes, boolean verticalLine, Tile[][] shelf ){

        int numRow;
        int numCol;
        ArrayList<Tile> LineTiles=new ArrayList<>();
        int totLines=0;

        if(verticalLine){
            numRow=shelf.length;
            numCol=shelf[0].length;
        } else{
            numCol=shelf.length;
            numRow=shelf[0].length;
        }

        for(int col=0; col< numCol; col++){

            LineTiles.clear();

            for(int row= 0; row<numRow; row++ ){

                    if (verticalLine){ if(!shelf[row][col].equals(Tile.EMPTY)) LineTiles.add(shelf[row][col]); }
                    else if(!shelf[col][row].equals(Tile.EMPTY)) LineTiles.add(shelf[col][row]);

            }

            if ( LineTiles.stream().count()==numRow
                && LineTiles.stream().distinct().count() <= maxNumbOfDiffTypes) totLines++;

            if( totLines == totNumbOfLines ) return true;

        }

        return false;

    }


    /**
     * check if in the shelf exists at least totNumbOfLines (columns or rows) without two or more tiles of the same type
     * @param totNumbOfLines number of row or columns to satisfy the request
     * @param verticalLine is true if the lines must be vertical (columns) otherwise is false (row)
     * @param shelf the shelf[][] of tiles
     * @return true if the request is satisfied
     */
    private static boolean allDiffTiles(int totNumbOfLines, boolean verticalLine, Tile[][] shelf){

        int numRow;
        int numCol;
        ArrayList<Tile> LineTiles=new ArrayList<>();
        int totLines=0;

        if(verticalLine){
            numRow=shelf.length;
            numCol=shelf[0].length;
        } else{
            numCol=shelf.length;
            numRow=shelf[0].length;
        }

        for( int col=0; col < numCol ; col++){

            LineTiles.clear();

            for(int row=0; row < numRow ; row++){

                if (verticalLine){ if(!shelf[row][col].equals(Tile.EMPTY)) LineTiles.add(shelf[row][col]); }
                else if(!shelf[col][row].equals(Tile.EMPTY)) LineTiles.add(shelf[col][row]);

            }

            if( LineTiles.stream().count()==numRow
               && LineTiles.stream().distinct().count()==numRow ) totLines++;

            if(totLines==totNumbOfLines) return true;
        }

        return false;
    }


    /**
     * verify the common goal card number 6
     * @param shelf
     * @return true if the request is satisfied
     */
    private static boolean check_CGC6(Tile[][] shelf){
        ArrayList<Tile> tiles = new ArrayList<Tile>();
        tiles.add(Tile.BLUE);
        tiles.add(Tile.PINK);
        tiles.add(Tile.LIGHTBLUE);
        tiles.add(Tile.GREEN);
        tiles.add(Tile.YELLOW);
        tiles.add(Tile.WHITE);

        ArrayList<Tile> allTiles = new ArrayList<>();

        //collecting all the tiles in a single ArrayList
        for (int row = 0; row < shelf.length; row++) {
            for (int col = 0; col < shelf[0].length; col++) {
                if (!shelf[row][col].equals(Tile.EMPTY)) allTiles.add(shelf[row][col]);
            }
        }

        //checking ho many elements ar present for each type of tile
        for (Tile t : tiles) {

            if (allTiles.stream().filter(x -> x.equals(t)).count() == 8) return true;

        }

        return false;
    }


    /**
     * verify the common goal card number 12
     * @param shelf
     * @return true if the request is satisfied
     */
    private static boolean check_CGC12(Tile[][] shelf){
        {

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
}
