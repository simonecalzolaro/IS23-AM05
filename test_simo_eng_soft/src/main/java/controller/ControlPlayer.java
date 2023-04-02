package controller;

import model.Board;
import model.Bookshelf;
import model.PersonalGoalCard;
import model.Tile;

import java.util.ArrayList;

public class ControlPlayer {

    private int playerID;

    //private View playerView;

    private PlayerStatus playerStatus;


    //private Protocol playerProtocol;

    private static Board board;

    private int score;

    private Bookshelf bookshelf;

    public ControlPlayer(){

        playerStatus = PlayerStatus.NOT_MY_TURN;
        score = 0;


    }

    public void cathTiles(int i1,int i2){

        if(playerStatus == PlayerStatus.CHOOSE_BOARD_TILES){

            //statement
            this.notifyAll();

        }

    }

    public void insertTiles(ArrayList<Tile> stream_tiles, int column){
        if(playerStatus == PlayerStatus.ADD_SHELF_TILES){
            bookshelf.putTiles(stream_tiles,column);
            bookshelf.checkEOG();
            updateScore();
            this.notifyAll();

        }
    }

    private void updateScore(){
        score = bookshelf.getMyScore();
    }

    public void setBookshelf(Board board){
        bookshelf = new Bookshelf(board);
    }

    public void setPlayerStatus(PlayerStatus playerStatus){
        this.playerStatus = playerStatus;
    }






}
