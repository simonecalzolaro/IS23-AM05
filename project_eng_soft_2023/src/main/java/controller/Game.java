package controller;

import model.Board;

import java.util.ArrayList;

public class Game {

    private final int gameID;

    private static int counterID=1;
    private ArrayList<ControlPlayer> players;

    private GameStatus gameStatus;

    private ControlPlayer currPlayer;

    private final Board board;

    public Game( int numOfPlayer) {
        this.gameID = counterID;
        counterID++;
        this.board = new Board();
        board.initializeBoard(numOfPlayer);
        gameStatus=GameStatus.WAIT_PLAYERS;
    }

    public void startGame(){
        currPlayer=players.get(0); //si potrebbe anche fare con int
        currPlayer.setPlayerStatus(PlayerStatus.MY_TURN);
        for (int i =1; i<players.size(); i++){
            players.get(i).setPlayerStatus(PlayerStatus.NOT_MY_TURN);
        }
        gameStatus=GameStatus.PLAYING;

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

    public void addPlayer(ControlPlayer player) {
    }

    public Board getBoard() {
        return board;
    }


}
