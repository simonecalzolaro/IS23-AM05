
package model;

import myShelfieException.InvalidLenghtException;
import myShelfieException.NotEnoughSpaceException;

import java.util.ArrayList;


public class Bookshelf {
    /**
     * shelf attribute represent the game bookshelf. Here is implemented with a 6x5 matrix
     */
    private Tile[][] shelf;


    /**
     * dimension of the shelf's matrix
     */
    private final int r= 6; //rows
    private final int c = 5; //columns

    /**
     * represents the PersonalGoalCard assigned at the beginning of the game
     */
    private PersonalGoalCard pgc;


    /**
     * represent all the token that the player can accumulate during the game
     */
    private Token tokenCG1;
    private Token tokenCG2;
    private int tokenEOG; //it's an integer because it's only meant to assign 1 point

    /**
     * represents the board used by all the players during the game
     * every update on the board must be visible to every player and so,  to everyone using a bookshelf
     * this motivates the choice of the static method for the attribute
     */
    private static Board board;


    /**
     *
     * @param board constructor instantiate the board described above
     */
    public Bookshelf(Board board){
        shelf = new Tile[r][c];
        for(int i=0;i<r;i++){
            for(int j=0; j<c;j++){
                shelf[i][j] = Tile.EMPTY;
            }
        }
        this.board = board;

        tokenCG1 = null;
        tokenCG2 = null;
        tokenEOG = 0;
        pgc = board.getDeck().getRandPGC();

    }


    //CLASS USED FOR TESTING : IT MUSTN'T BE USED DURING THE GAME
    public void setPGC(PersonalGoalCard pgc){
        this.pgc = pgc;
    }


    /**
     *
     * @param stream_tiles When the player draw the tiles from the deck they are setted into an array ;
     *                     The position of the tiles in the array is the order on which the player will insert the card on the column:
     *                     stream_tiles lenght must be more than 0 and less than 3
     * @param column The player must specifies which column he wants to put the tiles in
     *
     * @return always true if the operation is correct, in all other cases an exception in thrown
     *
     *
     */
    public boolean putTiles(ArrayList<Tile> stream_tiles, int column) throws IndexOutOfBoundsException, InvalidLenghtException, NotEnoughSpaceException {

        if(stream_tiles.size() > 3 || stream_tiles.size() <=0) throw new InvalidLenghtException();
        else if (column <0 || column >=5) throw new IndexOutOfBoundsException();
        else{
            //controllo a priori che ci sia spazio a sufficienza nelle colonna selezionata

            int count_col = 0;

            for(int i=0; i<r;i++){
                if(shelf[i][column] == Tile.EMPTY) count_col++;
            }

            if(count_col < stream_tiles.size()) throw new NotEnoughSpaceException(); //codice -1 rappresenta una colonna invalida

            int stream_tiles_pointer = 0;
            for(int i = 0; i<r; i++){
                if(shelf[i][column] == Tile.EMPTY && stream_tiles.get(stream_tiles_pointer) != Tile.EMPTY && stream_tiles.get(stream_tiles_pointer) != Tile.NOTAVAILABLE){
                    shelf[i][column] = stream_tiles.get(stream_tiles_pointer);
                    stream_tiles_pointer++;
                    if(stream_tiles_pointer == stream_tiles.size()) i=r; //break
                }
            }

            return true;
        }

    }


    /**
     * Feedback method, it shows the element of the matrix in order to render the whole matrix on the CLI
     */
    public void getBookshelf(){
        for(int i = r-1; i>=0; i--){
            System.out.println("\n");
            for(int j=0;j<c;j++){
                System.out.println(shelf[i][j]);

            }
        }
    }

    /**
     *
     * @return this method returns the number of max free spaces for all the columns of the shelf
     */

    public int maxShelfSpace(){
        int max=0; int count=0;
        for(int j=0;j<c;j++){
            for(int i=5; i>=0;i--){
                if(shelf[i][j] == Tile.EMPTY) count++;
                else break;
            }

            if(max < count) max = count;
            count = 0;
        }

        return max;

    }

    /**
     *
     * @return sum of all the points accumulated by the player during the game
     */
    public int getMyScore(){
        return getScoreGroups() + getScorePGC() + getScoreCGC() + getScoreEOG();
    }


    /**
     * The aim of this method is to calculate the points scored by creating groups of adjacent tiles in the matrix
     * The data structures used are a list of queues and an additional matrix
     * The additional matrix is used to track which tiles in the matrix has been already visited during the research of groups
     * The queue saves all the groups found so it can be easy to count the size of it and so calculate the points dued to it
     *
     * Groups are found through a recursive function "recursive_checker()" which inspect all the adjacent tiles of the same type
     *
     * @return return statement returns the point scored by the creation of all the groups of the matrix
     */
    public int getScoreGroups(){

        ArrayList<ArrayList<Coordinate>> groups= new ArrayList<ArrayList<Coordinate>>();

        int points=0;

        boolean[][] shelf_checker = new boolean[r][c];

        //inizializzo la matrice a false
        for(int i = 0; i<r;i++){
            for(int j = 0; j<c;j++){
                shelf_checker[i][j] = false;
            }
        }


        for(int i=0;i<r;i++){
            for(int j=0; j<c; j++){
                if(shelf[i][j] != Tile.EMPTY && shelf_checker[i][j] == false){

                    ArrayList<Coordinate> coda = new ArrayList<>();

                    coda.add(new Coordinate(i,j));
                    shelf_checker[i][j] = true;
                    recursiveChecker(coda.get(0), shelf_checker,coda);

                    groups.add(coda);


                }
            }
        }

        for(int i=0; i<groups.size(); i++){
            if(groups.get(i).size() == 3) points = points+2;
            if(groups.get(i).size() == 4) points = points+3;
            if(groups.get(i).size() == 5) points = points+5;
            if(groups.get(i).size() >= 6) points = points+8;

        }

        return points;
    }

    /**
     * It's a supporting method for getScoreGroup() function.
     * It can visit adjacent tiles of the same type and add them to the queue, if the tile hasn't been already visited
     *
     * @param point point indicates the coordinates of the Tile that I want to start inspecting
     * @param shelf_checker shelf_checker is the addition boolean matrix which indicates if the tiles has been already visited
     * @param coda is the structure where the single groups is stored
     *              then the queue is stores into an array of queues which stores all the groups found
     */

    private void recursiveChecker(Coordinate point,boolean[][] shelf_checker,ArrayList<Coordinate> coda){


        //NORTH
        if(point.getX()+1 < r)
            if(shelf[point.getX()][point.getY()] == shelf[point.getX()+1][point.getY()] && shelf_checker[point.getX()+1][point.getY()] == false)
                if(shelf[point.getX()+1][point.getY()] != Tile.EMPTY){
                    shelf_checker[point.getX()+1][point.getY()]  = true;
                    coda.add(new Coordinate(point.getX()+1, point.getY() ));
                    recursiveChecker(coda.get(coda.size()-1),shelf_checker,coda);

                }

        //SOUTH
        if(point.getX()-1 >= 0)
            if(shelf[point.getX()][point.getY()] == shelf[point.getX()-1][point.getY()] && shelf_checker[point.getX()-1][point.getY()] == false)
                if(shelf[point.getX()-1][point.getY()] != Tile.EMPTY){
                    shelf_checker[point.getX()-1][point.getY()]  = true;
                    coda.add(new Coordinate(point.getX()-1, point.getY() ));
                    recursiveChecker(coda.get(coda.size()-1),shelf_checker,coda);

                }

        //EAST
        if(point.getY()+1 < c)
            if(shelf[point.getX()][point.getY()] == shelf[point.getX()][point.getY()+1] && shelf_checker[point.getX()][point.getY()+1] == false)
                if(shelf[point.getX()][point.getY()+1] != Tile.EMPTY){
                    shelf_checker[point.getX()][point.getY()+1]  = true;
                    coda.add(new Coordinate(point.getX(), point.getY()+1));
                    recursiveChecker(coda.get(coda.size()-1),shelf_checker,coda);

                }

        //WEST
        if(point.getY()-1 >= 0)
            if(shelf[point.getX()][point.getY()] == shelf[point.getX()][point.getY()-1] && shelf_checker[point.getX()][point.getY()-1] == false)
                if(shelf[point.getX()][point.getY()-1] != Tile.EMPTY){
                    shelf_checker[point.getX()][point.getY()-1]  = true;
                    coda.add(new Coordinate(point.getX(), point.getY()-1 ));
                    recursiveChecker(coda.get(coda.size()-1),shelf_checker,coda);

                }






    }

    /**
     * assign the tokenCG1 if it has been completed
     */
    public void checkCG1(){

        if(board.getCommonGoalCard1().checkGoal(shelf) == true && tokenCG1 == null)
            tokenCG1 = board.getCommonGoalCard1().getTopStack();


    }


    /**
     * assign the tokenCG2 if it has been completed
     */
    public void checkCG2(){

        if(board.getCommonGoalCard2().checkGoal(shelf) == true && tokenCG2 == null)
            tokenCG2 = board.getCommonGoalCard2().getTopStack();

    }


    /**
     *
     * @return return the points scored by completing common goal card goals
     */
    public int getScoreCGC(){

        checkCG1();
        checkCG2();

        int points = 0;
        if(tokenCG1 != null) points = points + tokenCG1.getScoreToken();
        if(tokenCG2 != null) points = points +tokenCG2.getScoreToken();

        return points;
    }



    /**
     *
     * @return return the point scored by completing personal goal card goal
     */
    public int getScorePGC(){
        return pgc.getScore(shelf);

    }

    /**
     * It has a double aim:
     * 1. Indicates if someone has finished it's game (his/her matrix is all filled)
     * 2. Indicated if the "owner" of this bookshelf has finished by filling all the gaps in the matrix
     * ==> indicates is the game is still running or not
     * @return true if game is ended, false if game is not ended
     */

    public boolean checkEOG(){

        if(board.getEOG() == true) return true; //gioco finito
        else{
            boolean end = true;

            for(int i=0; i<r;i++)
                for(int j=0; j<c;j++)
                    if(shelf[i][j] == Tile.EMPTY) end = false;

            if(end == true){
                tokenEOG = 1;
                board.setEOG();
            }



            return end;
        }
    }

    /**
     * If the player is the first one finishing to fill the matrix so it has the EOG token it'll assign him 1 extra point
     */

    public int getScoreEOG(){
        return tokenEOG;
    }


    public static Board getBoard() {
        return board;
    }



    public void setShelf(Tile[][] shelf){

        this.shelf = shelf;
    }
}