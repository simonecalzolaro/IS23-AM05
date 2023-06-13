package model;

import myShelfieException.InvalidChoiceException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Board implements Serializable {
    /**
     * Represent the living room board of the game
     */
    private final Tile[][] board;

    /**
     * Mark the tiles that can be caught by a player during the round
     */
    private final boolean[][] catchableTiles;

    /**
     * number of players in the current game
     */
    private int nPlayers;

    /**
     * Common Goal card of the current game
     */
    private CommonGoalCard commonGoalCard1;
    private CommonGoalCard commonGoalCard2;

    /**
     * Set as false if the EOGToken is not taken yet
     */
    private boolean EOG;

    /**
     * Tiles bag of the current game
     */
    private final Bag bag;

    /**
     * Cards deck of the current game
     */
    private DeckCards deck;

    /**
     * constructor
     */
    public Board() {
        this.board = new Tile[9][9];
        this.catchableTiles = new boolean[9][9];
        this.bag=new Bag();
        bag.initializeBag();
        EOG=false;
    }

    /**
     * set number of player
     * initialize the board by setting N.A. tiles depending on the number of players
     * fill completely the board
     * update board
     * set common goal cards
     * @param nPlayers number of player
     */
    public void initializeBoard(int nPlayers){

        this.nPlayers=nPlayers;

        deck=new DeckCards(nPlayers);
        //initialize the board by setting N.A. tiles depending on the number of players
        //4 players setting

        for(int i=0; i<9; i++){
            for(int j=0; j<9; j++){
                if((i==0&&(j<3||j>=5)) ||
                        (i==1&&(j<3||j>=6)) ||
                        (i==2&&(j<2||j>=7)) ||
                        (i==3&&j==0) ||
                        (i==5&&j==8) ||
                        (i==6&&(j<2||j>=7)) ||
                        (i==7&&(j<3||j>=6))||
                        (i==8&&(j<4||j>=6))){
                    board[i][j]=Tile.NOTAVAILABLE;
                } else {
                    board[i][j]=Tile.EMPTY;
                }
            }
        }



        switch (nPlayers) {
            case 2 : {
                //2 players settings
                board[0][3] = Tile.NOTAVAILABLE;
                board[2][2] = Tile.NOTAVAILABLE;
                board[2][6] = Tile.NOTAVAILABLE;
                board[3][8] = Tile.NOTAVAILABLE;
                board[5][0] = Tile.NOTAVAILABLE;
                board[6][2] = Tile.NOTAVAILABLE;
                board[6][6] = Tile.NOTAVAILABLE;
                board[8][5] = Tile.NOTAVAILABLE;

            }
            case 3: {
                //3 players settings
                board[0][4] = Tile.NOTAVAILABLE;
                board[1][5] = Tile.NOTAVAILABLE;
                board[3][1] = Tile.NOTAVAILABLE;
                board[4][0] = Tile.NOTAVAILABLE;
                board[4][8] = Tile.NOTAVAILABLE;
                board[5][7] = Tile.NOTAVAILABLE;
                board[7][3] = Tile.NOTAVAILABLE;
                board[8][4] = Tile.NOTAVAILABLE;

            }

        }

        //fill completely the board
        updateBoard();

        setCommonGoalCard();
    }

    /**
     * Get tiles from the bag and fill randomly the board
     */
    public void fill(){
        //if there are enough tiles in the bag, fill completely the board
        if (getEmptyTilesNum() <= bag.getTilesNum()){
            for (int i = 0; i<9; i++) {
                for(int j = 0; j<9; j++){
                    if (board[i][j]==Tile.EMPTY){
                        board[i][j]=bag.getTile();
                    }
                }
            }
        } else {
            //case with fewer tiles than the free places I fill it pseudo-randomly
            //More probable tiles in the center of the matrix as the tiles on the edges are N.A.
            int[] intArray ={0,1,1,2,2,2,3,3,3,4,4,4,4,5,5,5,6,6,6,7,7,8};
            //Fill the board until the bag is empty
            while(bag.getTilesNum()!=0){
                //select randomly a matrix tiles
                int rowIndex = RandomInt(0, intArray.length-1);
                int columnIndex = RandomInt(0, intArray.length-1);
                //if the tile is empty, insert tiles and move on to the next one
                if (board[intArray[rowIndex]][intArray[columnIndex]]==Tile.EMPTY){
                    board[intArray[rowIndex]][intArray[columnIndex]]=bag.getTile();
                }
                else {

                    //If the tile is occupied, scroll the matrix to the first empty tile and insert it
                    int i;
                    int tempNum = bag.getTilesNum();
                    int j = intArray[columnIndex]+1;
                    for (i = intArray[rowIndex]; i<9; i++){
                        while (j<9){
                            if(board[i][j]==Tile.EMPTY){
                                board[i][j]=bag.getTile();
                                break;
                            }
                            j++;
                        }
                        if (tempNum != bag.getTilesNum()) break;
                        j=0;
                    }

                    if(tempNum == bag.getTilesNum()){
                        for(i=0;i < intArray[rowIndex]; i++){
                            for (j = 0 ; j<9 && (i!=intArray[rowIndex]||j!=intArray[columnIndex]); j++){
                                if(board[i][j]==Tile.EMPTY){
                                    board[i][j]=bag.getTile();
                                    break;
                                }
                            }
                            if (tempNum != bag.getTilesNum()) break;
                        }
                    }
                }
            }
        }
    }

    /**
     *
     * @param min int
     * @param max int
     * @return random int between min and max (included)
     */
    private int RandomInt(int min, int max){
        return (int) (Math.floor(Math.random() * ( max - min + 1 ) ) + min);
    }

    /**
     * update board every time a round end
     * Update "catchableTiles" matrix
     * check if the matrix is to be filled (if true fill the board)
     */
    public void updateBoard(){

        if(emptyBoard()) fill();
        for(int i=0; i<9; i++) {
            for (int j = 0; j < 9; j++) {
                catchableTiles[i][j] = okTile(i, j);
            }
        }
    }

    /**
     * check if there is only tiles with no adjacent tiles
     * @return true if the board is to be filled
     */
    protected boolean emptyBoard(){

        //check if there is only tiles with no adjacent tiles
        for(int i =0; i<9; i++){
            for(int j=0; j<9; j++){
                if(board[i][j]!=Tile.EMPTY &&
                        board[i][j]!=Tile.NOTAVAILABLE &&
                        !singleTile(i,j)
                ) return false;
            }
        }
        return true;
    }

    /**
     * A tile is a single tiles if it has no adjacent tiles
     * @param i row
     * @param j column
     * @return if a tile in (i,j) position in the board is a single tile
     * */
    protected boolean singleTile(int i, int j){
        //check if a tiles has no adjacent tile
        return switch (i) {
            case 0 -> switch (j) {
                case 0 -> (board[i + 1][j] == Tile.NOTAVAILABLE ||
                        board[i + 1][j] == Tile.EMPTY) &&
                        (board[i][j + 1] == Tile.NOTAVAILABLE ||
                                board[i][j + 1] == Tile.EMPTY);
                case 8 -> (board[i + 1][j] == Tile.NOTAVAILABLE ||
                        board[i + 1][j] == Tile.EMPTY) &&
                        (board[i][j - 1] == Tile.NOTAVAILABLE ||
                                board[i][j - 1] == Tile.EMPTY);
                default -> (board[i + 1][j] == Tile.NOTAVAILABLE ||
                        board[i + 1][j] == Tile.EMPTY) &&
                        (board[i][j - 1] == Tile.NOTAVAILABLE ||
                                board[i][j - 1] == Tile.EMPTY) &&
                        (board[i][j + 1] == Tile.NOTAVAILABLE ||
                                board[i][j + 1] == Tile.EMPTY);
            };
            case 8 -> switch (j) {
                case 0 -> (board[i - 1][j] == Tile.NOTAVAILABLE ||
                        board[i - 1][j] == Tile.EMPTY) &&
                        (board[i][j + 1] == Tile.NOTAVAILABLE ||
                                board[i][j + 1] == Tile.EMPTY);
                case 8 -> (board[i - 1][j] == Tile.NOTAVAILABLE ||
                        board[i - 1][j] == Tile.EMPTY) &&
                        (board[i][j - 1] == Tile.NOTAVAILABLE ||
                                board[i][j - 1] == Tile.EMPTY);
                default -> (board[i - 1][j] == Tile.NOTAVAILABLE ||
                        board[i - 1][j] == Tile.EMPTY) &&
                        (board[i][j - 1] == Tile.NOTAVAILABLE ||
                                board[i][j - 1] == Tile.EMPTY) &&
                        (board[i][j + 1] == Tile.NOTAVAILABLE ||
                                board[i][j + 1] == Tile.EMPTY);
            };
            default -> switch (j) {
                case 0 -> (board[i - 1][j] == Tile.NOTAVAILABLE ||
                        board[i - 1][j] == Tile.EMPTY) &&
                        (board[i + 1][j] == Tile.NOTAVAILABLE ||
                                board[i + 1][j] == Tile.EMPTY) &&
                        (board[i][j + 1] == Tile.NOTAVAILABLE ||
                                board[i][j + 1] == Tile.EMPTY);
                case 8 -> (board[i - 1][j] == Tile.NOTAVAILABLE ||
                        board[i - 1][j] == Tile.EMPTY) &&
                        (board[i + 1][j] == Tile.NOTAVAILABLE ||
                                board[i + 1][j] == Tile.EMPTY) &&
                        (board[i][j - 1] == Tile.NOTAVAILABLE ||
                                board[i][j - 1] == Tile.EMPTY);
                default -> (board[i - 1][j] == Tile.NOTAVAILABLE ||
                        board[i - 1][j] == Tile.EMPTY) &&
                        (board[i + 1][j] == Tile.NOTAVAILABLE ||
                                board[i + 1][j] == Tile.EMPTY) &&
                        (board[i][j - 1] == Tile.NOTAVAILABLE ||
                                board[i][j - 1] == Tile.EMPTY) &&
                        (board[i][j + 1] == Tile.NOTAVAILABLE ||
                                board[i][j + 1] == Tile.EMPTY);
            };
        };
    }

    /**
     * check if tiles have at least one side free that means that the tile is catchable
     * @param i row
     * @param j column
     * @return if the tile in (i, j) position is catchable
     */
    protected boolean okTile(int i, int j){
        //check if tiles have at least one side free and then are catchable
        if(board[i][j]== Tile.NOTAVAILABLE||board[i][j]==Tile.EMPTY) return false;
        if(i==0||i==8||j==0||j==8)  return true;

        return  board[i - 1][j] == Tile.NOTAVAILABLE ||
                board[i - 1][j] == Tile.EMPTY ||
                board[i + 1][j] == Tile.NOTAVAILABLE ||
                board[i + 1][j] == Tile.EMPTY ||
                board[i][j - 1] == Tile.NOTAVAILABLE ||
                board[i][j - 1] == Tile.EMPTY ||
                board[i][j + 1] == Tile.NOTAVAILABLE ||
                board[i][j + 1] == Tile.EMPTY;
    }

    /**
     * The player catches 1 tile, check if the move is allowed
     * @param i1 row
     * @param j1 column
     * @return list of tile taken
     * @throws InvalidChoiceException the tile is not catchable
     */
    public List<Tile> chooseTiles(Integer i1, Integer j1) throws InvalidChoiceException {
        //catch one tiles
        List<Tile> temp = new ArrayList<>();
        if(!catchableTiles[i1][j1]){
            throw new InvalidChoiceException("These tiles are not catchable");
        } else {
            temp.add(board[i1][j1]);
            return temp;
        }

    }


    /**
     * The player catches 2 tiles, check if the move is allowed
     * @param i1 row first tile
     * @param j1 column first tile
     * @param i2 row second tile
     * @param j2 column second tile
     * @param shelf bookshelf of the player
     * @return list of the tiles taken
     * @throws InvalidChoiceException one or more tiles are not catchable, the player has no enough space in his bookshelf for 2 tiles or the tiles are not in line
     */
    public List<Tile> chooseTiles(Integer i1, Integer j1, Integer i2, Integer j2, Bookshelf shelf) throws InvalidChoiceException {
        //catch two tiles
        List<Tile> temp = new ArrayList<>();
        if(!catchableTiles[i1][j1]||!catchableTiles[i2][j2]){
            throw new InvalidChoiceException("These tiles are not catchable");
        } else if(shelf.maxShelfSpace()<2){
            throw new InvalidChoiceException("You don't have enough space in your board");
        } else if(!inLine(i1,j1,i2,j2)){
            throw new InvalidChoiceException("Invalid selection");
        } else {
            temp.add(board[i1][j1]);
            temp.add(board[i2][j2]);
            return temp;
        }
    }

    /**
     * the tiles have to be in line and adjacent to be taken
     * @param i1 row first tile
     * @param j1 column first tile
     * @param i2 row second tile
     * @param j2 column second tile
     * @return true if the move is allowed
     */
    protected boolean inLine(int i1, int j1, int i2, int j2){
        //check if two tiles are adjacent
        return (i1==i2 && (j1==j2-1||j1==j2+1)) ||
                (j1 == j2 && (i1 == i2 - 1 || i1 == i2 + 1));
    }

    /**
     * The player catches 3 tiles, check if the move is allowed
     * @param i1 row first tile
     * @param j1 column first tile
     * @param i2 row second tile
     * @param j2 column second tile
     * @param i3 row third tile
     * @param j3 column third tile
     * @param shelf bookshelf of the player
     * @return list of the tiles taken
     * @throws InvalidChoiceException one or more tiles are not catchable, the player has no enough space in his bookshelf for 3 tiles or tiles are not in line
     */
    public List<Tile> chooseTiles(Integer i1, Integer j1, Integer i2, Integer j2, Integer i3, Integer j3, Bookshelf shelf) throws InvalidChoiceException {
        //catch 3 tiles
        List<Tile> temp = new ArrayList<>();
        if(!catchableTiles[i1][j1]||!catchableTiles[i2][j2]||!catchableTiles[i3][j3]){
            throw new InvalidChoiceException("These tiles are not catchable");
        } else if(shelf.maxShelfSpace()<3){
            throw new InvalidChoiceException("You don't have enough space in your board");
        } else if(!inLine(i1,j1,i2,j2,i3,j3)) {
            throw new InvalidChoiceException("Invalid selection");
        } else{
            temp.add(board[i1][j1]);
            temp.add(board[i2][j2]);
            temp.add(board[i3][j3]);
            return temp;
        }
    }

    /**
     * the tiles have to be in line and adjacent to be taken
     * @param i1 row first tile
     * @param j1 column fist tile
     * @param i2 row second tile
     * @param j2 column second tile
     * @param i3 row third tile
     * @param j3 column third tile
     * @return true if the move is allowed
     */
    protected boolean inLine(int i1, int j1, int i2, int j2, int i3, int j3){
        //check if three tiles ar adjacent and in line
        if((i1!=i2||i2!=i3)&&(j1!=j2&&j2!=j3)) return false;
        int a, b, c;
        if(i1==i2&&i2==i3){
            a=j1;
            b=j2;
            c=j3;
        }
        else {
            a=i1;
            b=i2;
            c=i3;
        }

        return (a==b-1&&b==c-1) ||
                    (c==a-1&&a==b-1) ||
                    (b==c-1&&c==a-1) ||
                    (a==c-1&&c==b-1) ||
                    (b==a-1&&a==c-1) ||
                    (c==b-1&&b==a-1);
    }

    /**
     *clear the tiles taken from the board
     * @param coord: tile coordinates
     */
    public void subTiles(List<Integer> coord){
        switch (coord.size()) {
            case 2 -> board[coord.get(0)][coord.get(1)] = Tile.EMPTY;
            case 4 -> {
                board[coord.get(0)][coord.get(1)] = Tile.EMPTY;
                board[coord.get(2)][coord.get(3)] = Tile.EMPTY;
            }
            case 6 -> {
                board[coord.get(0)][coord.get(1)] = Tile.EMPTY;
                board[coord.get(2)][coord.get(3)] = Tile.EMPTY;
                board[coord.get(4)][coord.get(5)] = Tile.EMPTY;
            }
            default -> throw new IllegalArgumentException();
        }

    }
    /**
     *
     * @return number of empty tiles
     */
    protected int getEmptyTilesNum(){
        //return number of empty tiles in the board
        int num=0;
        for(int i=0; i<9; i++){
            for(int j=0; j<9;j++){
                if(board[i][j]==Tile.EMPTY) num++;
            }
        }
        return num;
    }

    /**
     *
     * @return the board
     */
    public Tile[][] getBoard() {
        return board;
    }

    /**
     *
     * @return the number of player of the current game
     */
    protected int getNPlayers() {
        return nPlayers;
    }

    /**
     * set the common goal cards
     */
    private void setCommonGoalCard() {

        commonGoalCard1=deck.getRandCGC();
        commonGoalCard2=deck.getRandCGC();
    }

    /**
     *
     * @return common goal card 1
     */
    public CommonGoalCard getCommonGoalCard1() {
        return commonGoalCard1;
    }

    /**
     *
     * @return common goal card 2
     */

    public CommonGoalCard getCommonGoalCard2() {
        return commonGoalCard2;
    }

    /**
     *
     * @return true if the EOG token is not taken yet
     */
    public boolean getEOG() {
        return EOG;
    }

    /**
     * set EOG true if the EOG token is assigned
     */
    public void setEOG() {
        this.EOG = true;
    }


    /**
     *
     * @param nPlayers number of player of the current game
     */
    private void setNPlayers(int nPlayers) {
        this.nPlayers = nPlayers;
    }

    /**
     *
     * @param i row
     * @param j column
     * @param tile tile type
     */
    protected void setTile(int i, int j, Tile tile){
        if(board[i][j]!=Tile.NOTAVAILABLE){
            board[i][j]= tile;
        }
    }

    /**
     *
     * @return bag
     */
    public Bag getBag() {
        return bag;
    }

    /**
     *
     * @return deck
     */
    public DeckCards getDeck() {
        return deck;
    }

    public void setCGC1(int num,int np){
        commonGoalCard1 =new CommonGoalCard(num,np);
    }

    public void setCGC2(int num,int np){
        commonGoalCard2 =new CommonGoalCard(num,np);
    }
}