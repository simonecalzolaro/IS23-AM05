package model;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

public class Board {
    private Tile board [][];
    private boolean catchableTiles[][];

    private int nPlayers;
    private CommonGoalCard commonGoalCard1;
    private CommonGoalCard commonGoalCard2;

    private Token EOG;
    private Bag bag;
    public Board(Bag bag) {
        this.board = new Tile[9][9];
        this.catchableTiles = new boolean[9][9];
        this.bag=bag;
    }

    public void initializeBoard(int nPlayers, DeckCards deck){

        this.nPlayers=nPlayers;
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

        if(nPlayers==3){
            //3 players settings
            board[0][4]=Tile.NOTAVAILABLE;
            board[1][5]=Tile.NOTAVAILABLE;
            board[3][1]=Tile.NOTAVAILABLE;
            board[4][0]=Tile.NOTAVAILABLE;
            board[4][8]=Tile.NOTAVAILABLE;
            board[5][7]=Tile.NOTAVAILABLE;
            board[7][3]=Tile.NOTAVAILABLE;
            board[8][4]=Tile.NOTAVAILABLE;
        } else if(nPlayers==2){
            //2 players settings
            board[0][3]=Tile.NOTAVAILABLE;
            board[2][2]=Tile.NOTAVAILABLE;
            board[2][6]=Tile.NOTAVAILABLE;
            board[3][8]=Tile.NOTAVAILABLE;
            board[5][0]=Tile.NOTAVAILABLE;
            board[6][2]=Tile.NOTAVAILABLE;
            board[6][6]=Tile.NOTAVAILABLE;
            board[8][5]=Tile.NOTAVAILABLE;
        }

        //fill completely the board
        fill();
        updateBoard();
        setCommonGoalCard(deck);
    }

    public void fill(){
        //if i have enough tiles in the bag i fill completely the board
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

    public int RandomInt(int min, int max){
        return (int) (Math.floor(Math.random() * ( max - min + 1 ) ) + min);
    }

    public void updateBoard(){
        //Update the board every end of round
        //Update "catchableTiles" matrix
        for(int i=0; i<9; i++){
            for(int j=0; j<9;j++){
                catchableTiles[i][j]=okTiles(i,j);
            }
        }

        //check if the matrix is to be filled
        if(emptyBoard()) fill();
    }

    public boolean emptyBoard(){
        //controlla se ci sono solo tiles senza altri tiles adiacenti e quindi la board va riempita di tiles
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

    public boolean singleTile(int i, int j){
        //check if a tiles have no adjacent tile
        if(i!=0&&i!=8&&j!=0&&j!=8){
            return  (board[i - 1][j] == Tile.NOTAVAILABLE ||
                    board[i - 1][j] == Tile.EMPTY) &&
                    (board[i + 1][j] == Tile.NOTAVAILABLE ||
                            board[i + 1][j] == Tile.EMPTY) &&
                    (board[i][j - 1] == Tile.NOTAVAILABLE ||
                            board[i][j - 1] == Tile.EMPTY) &&
                    (board[i][j + 1] == Tile.NOTAVAILABLE ||
                            board[i][j + 1] == Tile.EMPTY);
        }

        if (i==0&&j==0){
            return (board[i + 1][j] == Tile.NOTAVAILABLE ||
                    board[i + 1][j] == Tile.EMPTY) &&
                    (board[i][j + 1] == Tile.NOTAVAILABLE ||
                            board[i][j + 1] == Tile.EMPTY);
        }

        if (i==0&&j==8){
            return (board[i + 1][j] == Tile.NOTAVAILABLE ||
                    board[i + 1][j] == Tile.EMPTY) &&
                    (board[i][j - 1] == Tile.NOTAVAILABLE ||
                            board[i][j - 1] == Tile.EMPTY);
        }

        if(i==8&&j==0){
            return (board[i - 1][j] == Tile.NOTAVAILABLE ||
                    board[i - 1][j] == Tile.EMPTY) &&
                    (board[i][j + 1] == Tile.NOTAVAILABLE ||
                            board[i][j + 1] == Tile.EMPTY);
        }
        if (i==8&&j==8){
            return (board[i - 1][j] == Tile.NOTAVAILABLE ||
                    board[i - 1][j] == Tile.EMPTY) &&
                    (board[i][j - 1] == Tile.NOTAVAILABLE ||
                            board[i][j - 1] == Tile.EMPTY);
        }

        if(i==0){
            return (board[i + 1][j] == Tile.NOTAVAILABLE ||
                    board[i + 1][j] == Tile.EMPTY) &&
                    (board[i][j - 1] == Tile.NOTAVAILABLE ||
                            board[i][j - 1] == Tile.EMPTY) &&
                    (board[i][j + 1] == Tile.NOTAVAILABLE ||
                            board[i][j + 1] == Tile.EMPTY);
        }

        if (j==0){
            return (board[i - 1][j] == Tile.NOTAVAILABLE ||
                    board[i - 1][j] == Tile.EMPTY) &&
                    (board[i + 1][j] == Tile.NOTAVAILABLE ||
                            board[i + 1][j] == Tile.EMPTY) &&
                    (board[i][j + 1] == Tile.NOTAVAILABLE ||
                            board[i][j + 1] == Tile.EMPTY);
        }

        if(i==8){
            return (board[i - 1][j] == Tile.NOTAVAILABLE ||
                    board[i - 1][j] == Tile.EMPTY) &&
                    (board[i][j - 1] == Tile.NOTAVAILABLE ||
                            board[i][j - 1] == Tile.EMPTY) &&
                    (board[i][j + 1] == Tile.NOTAVAILABLE ||
                            board[i][j + 1] == Tile.EMPTY);
        }

        return (board[i - 1][j] == Tile.NOTAVAILABLE ||
                board[i - 1][j] == Tile.EMPTY) &&
                (board[i + 1][j] == Tile.NOTAVAILABLE ||
                        board[i + 1][j] == Tile.EMPTY) &&
                (board[i][j - 1] == Tile.NOTAVAILABLE ||
                        board[i][j - 1] == Tile.EMPTY);
    }
    public boolean okTiles(int i, int j){
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

    public List<Tile> subTiles(int i1, int j1) throws NotAvailableTiles {
        //catch one tiles
        List<Tile> temp = new ArrayList<>();
        if(!catchableTiles[i1][j1]){
            throw new NotAvailableTiles();
        } else {
            temp.add(board[i1][j1]);
            board[i1][j1]=Tile.EMPTY;
            return temp;
        }

    }

    public List<Tile> subTiles(int i1, int j1, int i2, int j2, Bookshelf shelf) throws NotAvailableTiles, NotEnoughSpace, NotInLine {
        //catch two tiles
        List<Tile> temp = new ArrayList<>();
        if(!catchableTiles[i1][j1]||!catchableTiles[i2][j2]){
            throw new NotAvailableTiles();
        } else if(shelf.maxShelfSpace()<2){
            throw new NotEnoughSpace();
        } else if(!inLine(i1,j1,i2,j2)){
            throw new NotInLine();
        } else {
            temp.add(board[i1][j1]);
            board[i1][j1]=Tile.EMPTY;
            temp.add(board[i2][j2]);
            board[i2][j2]=Tile.EMPTY;
            return temp;
        }
    }

    public boolean inLine(int i1, int j1, int i2, int j2){
        //check if two tiles are adjacent
        return (i1==i2 && (j1==j2-1||j1==j2+1)) ||
                (j1 == j2 && (i1 == i2 - 1 || i1 == i2 + 1));
    }

    public List<Tile> subTiles(int i1, int j1, int i2, int j2, int i3, int j3, Bookshelf shelf) throws NotAvailableTiles, NotEnoughSpace, NotInLine {
        //catch 3 tiles
        List<Tile> temp = new ArrayList<>();
        if(!catchableTiles[i1][j1]||!catchableTiles[i2][j2]||!catchableTiles[i3][j3]){
            throw new NotAvailableTiles();
        } else if(shelf.maxShelfSpace()<3){
            throw new NotEnoughSpace();
        } else if(!inLine(i1,j1,i2,j2,i3,j3)) {
            throw new NotInLine();
        } else{
            temp.add(board[i1][j1]);
            board[i1][j1]=Tile.EMPTY;
            temp.add(board[i2][j2]);
            board[i2][j2]=Tile.EMPTY;
            temp.add(board[i3][j3]);
            board[i3][j3]=Tile.EMPTY;
            return temp;
        }
    }


    public boolean inLine(int i1, int j1, int i2, int j2, int i3, int j3){
        //check if three tiles ar adjacent and in line
        return (i1==i2&&i2==i3&&(j1==j2-1&&j2==j3-1)) ||
                (i1==i2&&i2==i3&&(j3==j1-1&&j1==j2-1)) ||
                (i1==i2&&i2==i3&&(j2==j3-1&&j3==j1-1)) ||
                (i1==i2&&i2==i3&&(j1==j3-1&&j3==j2-1)) ||
                (i1==i2&&i2==i3&&(j2==j1-1&&j1==j3-1)) ||
                (i1==i2&&i2==i3&&(j3==j2-1&&j2==j1-1)) ||

                (j1==j2&&j2==j3&&(i1==i2-1&&i2==i3-1)) ||
                (j1==j2&&j2==j3&&(i3==i1-1&&i1==i2-1)) ||
                (j1==j2&&j2==j3&&(i2==i3-1&&i3==i1-1)) ||
                (j1==j2&&j2==j3&&(i1==i3-1&&i3==i2-1)) ||
                (j1==j2&&j2==j3&&(i2==i1-1&&i1==i3-1)) ||
                (j1==j2&&j2==j3&&(i3==i2-1&&i2==i1-1)) ;
    }

    public int getEmptyTilesNum(){
        //return number of empty tiles in the board
        int num=0;
        for(int i=0; i<9; i++){
            for(int j=0; j<9;j++){
                if(board[i][j]==Tile.EMPTY) num++;
            }
        }
        return num;
    }
    public Tile[][] getBoard() {
        return board;
    }

    public int getnPlayers() {
        return nPlayers;
    }

    public void setCommonGoalCard(DeckCards deck) {
        commonGoalCard1=deck.getRandCGC();
        commonGoalCard2=deck.getRandCGC();
    }

    public CommonGoalCard getCommonGoalCard1() {
        return commonGoalCard1;
    }

    public CommonGoalCard getCommonGoalCard2() {
        return commonGoalCard2;
    }

    public Token getEOG() {
        return EOG;
    }

    public void setnPlayers(int nPlayers) {
        this.nPlayers = nPlayers;
    }

}