package controller;

import model.Board;

import java.util.ArrayList;

public class Game {

    private final int gameID;

    private ArrayList<ControlPlayer> players;

    private GameStatus gameStatus;

    private ControlPlayer currPlayer;

    private final Board board;

    public Game() {
        this.gameID = 0; // come determino il gameID?
        this.board = null;
    }


    public int getGameID() {
        return gameID;
    }

    public ArrayList<ControlPlayer> getPlayers() {
        return players;
    }

    public ControlPlayer getCurrPlayer() {
        return currPlayer;
    }

    public Board getBoard() {
        return board;
    }


}
