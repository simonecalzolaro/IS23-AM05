package controller;

import model.Board;

import java.util.ArrayList;
import java.util.Objects;

public class Game extends Thread{

    private int expectedPlayers;
    private int connectedPlayers;

    private ArrayList<ControlPlayer> gamePlayers;

    public static int counter;

    public int gameID;

    private Board board;

    private ControlPlayer currPlayer;




    public Game(ControlPlayer player, int expectedPlayers){

        gameID = counter+1;
        counter++;

        this.expectedPlayers = expectedPlayers;
        initGame();
        addPlayer(player);

    }



    public void run(){

        int circularIndex=0;

        while(!board.getEOG() && circularIndex != 0){

            while(!isFull()); //wait that all the players are connected

            currPlayer = gamePlayers.get(circularIndex);

            for(ControlPlayer p: gamePlayers){
                if(p != currPlayer) p.setPlayerStatus(PlayerStatus.NOT_MY_TURN);

            }

            currPlayer.setPlayerStatus(PlayerStatus.CHOOSE_BOARD_TILES);

            try{
                currPlayer.wait();
            }
            catch (InterruptedException e){
                e.printStackTrace();
            }

            currPlayer.setPlayerStatus(PlayerStatus.ADD_SHELF_TILES);

            try{
                currPlayer.wait();
            }
            catch (InterruptedException e){
                e.printStackTrace();
            }

            currPlayer.setPlayerStatus(PlayerStatus.NOT_MY_TURN);

            circularIndex++;

            if(circularIndex == expectedPlayers) circularIndex = 0;




        }

    }


    private boolean isFull(){
        return connectedPlayers == expectedPlayers;
    }

    private void initGame(){
        board = new Board();
        board.initializeBoard(expectedPlayers);
    }

    public int getExpectedPlayers() {
        return expectedPlayers;
    }

    public int getConnectedPlayers() {
        return connectedPlayers;
    }

    public void addPlayer(ControlPlayer player){
        player.setBookshelf(board);
        gamePlayers.add(player);
        //per provare assumo che tutti i giocatori siano sempre connessi
        connectedPlayers++;
    }
}
