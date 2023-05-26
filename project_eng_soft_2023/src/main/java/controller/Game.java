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
        if(players.get(currPlayer).getPlayerStatus()!=PlayerStatus.NOT_ONLINE) players.get(currPlayer).setPlayerStatus(PlayerStatus.NOT_MY_TURN);

        //searching for the next player
        do{
            if (currPlayer < players.size()-1){
                currPlayer++;
            }
            else if (board.getEOG()){
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
                    cp.notifyEndGame();
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
    public Map<Integer, String> getGameResults(){

        ArrayList<Integer> scores = players.stream()
                .map(x->x.getBookshelf().getMyScore())
                .collect(Collectors.toCollection(ArrayList::new));

        ArrayList<String> nicks = players.stream()
                .map(x->x.getPlayerNickname())
                .collect(Collectors.toCollection(ArrayList::new));

        Map<Integer, String> res= new HashMap<>();

        for(int i=0; i<scores.size(); i++){
            res.put(scores.get(i), nicks.get(i));
        }

        return res.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors
                        .toMap(
                                Map.Entry::getKey,
                                Map.Entry::getValue,
                                (oldValue, newValue) -> oldValue, LinkedHashMap::new));


    }

    public void setGameStatus(GameStatus gs){
        this.gameStatus=gs;
    }


}
