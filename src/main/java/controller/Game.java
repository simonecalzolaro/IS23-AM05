package controller;

import controller.chatPackage.Chat;
import model.Board;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;


public class Game implements Serializable {

    /**
     * Unique Game ID
     *
     */

    private final int gameID;

    /**
     * Created game counter
     */
    private static int counterID=1;

    /**
     * Sorted list of player
     */
    private ArrayList<ControlPlayer> players;

    /**
     * Game status (PLAYING, END_GAME)
     */
    private GameStatus gameStatus;

    /**
     * Index of the player who is in his turn to play
     */
    private int currPlayer;

    /**
     * Game Board
     */
    private final Board board;

    private final Chat chatRoom;

    public Game(){
        board=null;
        chatRoom=null;
        gameID=-999;
    }

    /**
     * Assign GameID and increase game count
     * Create the game board and initialize it
     * Add the player to the game
     * Invoke startGame
     * @param players list of the player joining the game
     */
    public Game(List<ControlPlayer> players, Board board) throws IOException {

        this.gameID = counterID;
        counterID++;
        this.board = board;
        board.initializeBoard(players.size());
        this.players = new ArrayList<>();
        (this.players).addAll(players);
        this.chatRoom=new Chat();

        startGame();
    }

    /**
     * Set the first connected player as the currPlayer and his status as MY_TURN
     * Set the Game Status as PLAYING
     */
    public void startGame(){

        currPlayer=0;
        while (players.get(currPlayer).getPlayerStatus()==PlayerStatus.NOT_ONLINE) {
            if(currPlayer==players.size()-1){
                currPlayer=0;
            } else {
                currPlayer++;
            }
        }
        players.get(currPlayer).setPlayerStatus(PlayerStatus.MY_TURN);
        gameStatus=GameStatus.PLAYING;

    }

    /**
     * Fill the board if necessary
     * Set the currPlayer status as NOT_MY_TURN (if still connected)
     * set the currPlayer on the next connected player
     * If the currPlayer were the last of the player list, check if the game is over
     * If the game is not over yet set the currPlayer status as MY_TURN
     */
    public void endTurn(){

        //updating the board
        board.updateBoard();

        //if online, setting the current player to NOT_MY_TURN
        if(players.get(currPlayer).getPlayerStatus()!=PlayerStatus.NOT_ONLINE && currPlayer<players.size()) players.get(currPlayer).setPlayerStatus(PlayerStatus.NOT_MY_TURN);

        //searching for the next player
        do{
            if (currPlayer < players.size()-1){
                currPlayer++;
            }
            else if (board.getEOG()){
                System.out.println("set the game "+gameID+"status to END_GAME");
                gameStatus=GameStatus.END_GAME;
            }
            else {
                currPlayer=0;
            }

        } while(gameStatus!=GameStatus.END_GAME && players.get(currPlayer).getPlayerStatus()==PlayerStatus.NOT_ONLINE);

        if(gameStatus!=GameStatus.END_GAME){
            players.get(currPlayer).setPlayerStatus(PlayerStatus.MY_TURN);
        }
        else{
            try {
                for(ControlPlayer cp: players){
                    System.out.println("    notify end game to "+cp.getPlayerNickname());
                    if( !cp.getPlayerStatus().equals(PlayerStatus.NOT_ONLINE))  cp.notifyEndGame();
                }
            } catch (IOException e) { throw new RuntimeException(e); }
        }
    }

    /**
     * @return gameID
     */
    public int getGameID() {
        return gameID;
    }

    /**
     * @return list of players
     */
    public ArrayList<ControlPlayer> getPlayers() {
        return players;
    }

    public ControlPlayer getPlayerByNickname(String nick){

        for(ControlPlayer cp: players){
            if(cp.getPlayerNickname().equals(nick)){
                return cp;
            }
        }
        return null;
    }
    /**
     * @return gameStatus
     */
    public GameStatus getGameStatus() {
        return gameStatus;
    }

    /**
     * @return currPlayer index
     */

    public int getCurrPlayer() {
        return currPlayer;
    }


    /**
     * @return game board
     */

    public Board getBoard() {
        return board;
    }


    public Chat getChatRoom() {
        return chatRoom;
    }

    /**
     * remove a specific ControlPlayer from the game
     * @param player to exclude from the game
     * @return true if the player is present in the game and is correctly deleted
     */
    public boolean removePlayer(ControlPlayer player){

        try{
            return players.remove(player);
        }catch (Exception e){
            return false;
        }

    }


    /**
     * @return a map of ordered players from the player with the highest score to the player with the lowest
     */
    public Map<String, Integer> getGameResults(){

        Map<String, Integer> res= new HashMap<>();
        for(int i=0; i<players.size(); i++){
            res.put(players.get(i).getPlayerNickname(), players.get(i).getBookshelf().getMyScore());
        }

        Map<String, Integer> sortedMap = sortMapByValue(res);

        //if the game is not ended and I'm pushing this map because someone left the game I'll set all the scores to their negative module
        if(getGameStatus().equals(GameStatus.END_GAME)) return sortedMap;

        res= new HashMap<>();
        //setting everything to negatives
        for (Map.Entry<String, Integer> entry : sortedMap.entrySet()) {
            res.put(entry.getKey(), - entry.getValue());
        }

        return res;
    }

    /**
     * @param res : map to sort
     * @return sorted map by values
     */
    public Map<String, Integer> sortMapByValue( Map<String, Integer> res){

        // Create a list of map entries
        List<Map.Entry<String, Integer>> entryList = new ArrayList<>(res.entrySet());

        // Sort the list by value using a reverseOrder Comparator
        entryList.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));

        // Create a LinkedHashMap to preserve the order of sorted entries
        Map<String, Integer> sortedMap = new LinkedHashMap<>();

        // Populate the sorted map with sorted entries
        for (Map.Entry<String, Integer> entry : entryList) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }

    public void setGameStatus(GameStatus gs){
        this.gameStatus=gs;
    }


}
