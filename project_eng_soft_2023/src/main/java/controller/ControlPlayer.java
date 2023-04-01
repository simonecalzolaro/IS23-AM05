package controller;

import model.Board;
import model.Bookshelf;

public class ControlPlayer {

    private final int playerID;

    private PlayerStatus playerStatus;

    final private Bookshelf bookshelf;



    public ControlPlayer(Board board, int playerID) {
        bookshelf=new Bookshelf(board);
        this.playerID=playerID;
    }


    public int getPlayerID() {
        return playerID;
    }

    public PlayerStatus getPlayerStatus() {
        return playerStatus;
    }


    public Bookshelf getBookshelf() {
        return bookshelf;
    }

    public void setPlayerStatus(PlayerStatus playerStatus) {
        this.playerStatus = playerStatus;
    }

}
