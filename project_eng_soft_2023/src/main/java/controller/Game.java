package controller;

import model.Board;

import java.util.ArrayList;
import java.util.List;

public class Game {

    private final int gameID;

    private static int counterID=1;
    private ArrayList<ControlPlayer> players;

    private GameStatus gameStatus;

    private int currPlayer;

    private final Board board;

    public Game(List<ControlPlayer> players) {
        this.gameID = counterID;
        counterID++;
        this.board = new Board();
        board.initializeBoard(players.size());
        for(int i=0; i<players.size(); i++){
            addPlayer(players.get(i));
        }

    }

    public void startGame(){
        currPlayer=0;
        players.get(0).setPlayerStatus(PlayerStatus.MY_TURN);
        for (int i=1; i<players.size(); i++){
            players.get(i).setPlayerStatus(PlayerStatus.NOT_MY_TURN);
        }
        gameStatus=GameStatus.PLAYING;
    }



    public void endTurn(){
        board.updateBoard();
        players.get(currPlayer).setPlayerStatus(PlayerStatus.NOT_MY_TURN);
        do{
            if (currPlayer<players.size()-1){
                currPlayer++;
            }
            else if (board.getEOG()){
                gameStatus=GameStatus.END_GAME;

            }
            else {
                currPlayer=0;
            }
        } while(gameStatus!=GameStatus.END_GAME&&players.get(currPlayer).getPlayerStatus()==PlayerStatus.NOT_ONLINE);

        if(gameStatus!=GameStatus.END_GAME) players.get(currPlayer).setPlayerStatus(PlayerStatus.MY_TURN);
    }

    public int getGameID() {
        return gameID;
    }

    public ArrayList<ControlPlayer> getPlayers() {
        return players;
    }

    public GameStatus getGameStatus() {
        return gameStatus;
    }



    public int getCurrPlayer() {
        return currPlayer;
    }

    public void addPlayer(ControlPlayer player){
        player.setBookshelf(board);
        players.add(player);

    }


    public Board getBoard() {
        return board;
    }


}
