package controller;

import client.ClientHandler;
import model.*;
import myShelfieException.*;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public abstract class ControlPlayer extends UnicastRemoteObject implements GameHandler{

    /**
     *Player's id
     */
    protected final String nickname;

    /**
     * player's status
     */
    protected PlayerStatus playerStatus;

    /**
     * player's bookshelf
     */
    protected final Bookshelf bookshelf;

    /**
     * player's score
     */
    protected int score;


    protected Game game;





    /**
     * Assign player id
     * Initialize score
     * Set player status as NOT_MY_TURN
     * @param nickname: unique player nickname
     * @param board: unique board
     */
    public ControlPlayer(String nickname,Board board) throws RemoteException{

        this.nickname = nickname;
        bookshelf = new Bookshelf(board);
        score = 0;
        playerStatus = PlayerStatus.NOT_MY_TURN;

    }

    /**
     *
     * @param stream_tiles ordered list of tiles to insert
     * @param column column where to insert the tiles
     * @throws NotMyTurnException : the player status is NOT_MY_TURN
     * @throws NotConnectedException : the player is not connected
     * @return always true when
     */
    public boolean insertTiles(ArrayList<Tile> stream_tiles, int column) throws NotMyTurnException, NotConnectedException, InvalidLenghtException, InvalidChoiceException {

        if(playerStatus == PlayerStatus.NOT_MY_TURN){
            throw new NotMyTurnException();
        }
        else if(playerStatus == PlayerStatus.NOT_ONLINE){
            throw new NotConnectedException();
        }
        else{
            bookshelf.putTiles(stream_tiles,column);
            updateScore();
            return true;
        }


    }


    private void updateScore(){
        score = bookshelf.getMyScore();
    }

    /**
     * if everything it's ok, remove the selected tiles from the board
     * @param coord list
     * @return List of catch tiles
     * @throws InvalidParametersException the list of coordinates is not compliant
     * @throws InvalidChoiceException the tiles selected are not catchable, the player have no enough space in his bookshelf or tiles are not in line
     * @throws NotMyTurnException the player status is NOT_MY_TURN (the move is not allowed)
     * @throws NotConnectedException the player is not connected
     */
    public List<Tile> catchTile(List<Integer> coord) throws InvalidParametersException, InvalidChoiceException, NotMyTurnException, NotConnectedException {


        if (playerStatus == PlayerStatus.NOT_MY_TURN) {
            throw new NotMyTurnException();
        } else if (playerStatus == PlayerStatus.NOT_ONLINE) {
            throw new NotConnectedException();
        } else {
            return switch (coord.size()) {
                case 2 -> bookshelf.getBoard().chooseTiles(coord.get(0), coord.get(1));
                case 4 ->
                        bookshelf.getBoard().chooseTiles(coord.get(0), coord.get(1), coord.get(2), coord.get(3), bookshelf);
                case 6 ->
                        bookshelf.getBoard().chooseTiles(coord.get(0), coord.get(1), coord.get(2), coord.get(3), coord.get(4), coord.get(5), bookshelf);
                default -> throw new InvalidParametersException();
            };
        }
    }





    //-------------------------------------- GameHandler implemented methods --------------------------------------

    /**
     * method called by the clients to choose tiles from the board of their game
     * @param chosenTiles List of tiles chosen by the client
     * @param coord coordinates of the chosen tiles
     * @return true if the chosen tiles are valid
     */
    @Override
    public boolean choseBoardTiles(List<Tile> chosenTiles, List<Integer> coord) throws RemoteException, NotConnectedException, InvalidParametersException, NotMyTurnException, InvalidChoiceException {

        if ( catchTile(coord).equals(chosenTiles)){

            /* updating the Board of all clients is not necessary here, I can do it in insertShelfTiles() only

            Game g= (Game)games.stream().filter(x->x.getPlayers().contains(clients.get(ch)));

            for (ControlPlayer cp: g.getPlayers() ) {

                try{
                    getKey(clients, cp).updateBoard(g.getBoard().getBoard()); //----------timer che aspetta il return true
                }catch (RemoteException e){
                    e.printStackTrace();
                    return false;
                }

            } */
            return true;
        }
        else return false;


    }

    /**
     * method called by clients to insert the tiles they choose from the board into their bookshelf
     * @param chosenTiles ordered list of tiles, from the lowest one to the top one
     * @param choosenColumn column where to insert the tiles
     * @param coord tile coordinates
     * @throws NotConnectedException
     * @throws NotMyTurnException
     */
    @Override
    public boolean insertShelfTiles(ArrayList<Tile> chosenTiles, int choosenColumn, List<Integer> coord) throws RemoteException, NotConnectedException, NotMyTurnException, InvalidLenghtException, InvalidChoiceException {

        //inserting the tiles in the bookshelf

        if( ! insertTiles(chosenTiles, choosenColumn)) return false;

        //and subtracting the same tiles from the board
        game.getBoard().subTiles(coord);

        //tell to ch that his turn is completed
        try {
            notifyEndYourTurn();
        } catch (IOException e) { throw new RuntimeException(e); }

        //Game.endTurn() is called to update the turn to the next player and update the game status

        game.endTurn();

        //notify the updated board to all the clients participating in the same game of ch
        try {

            for(ControlPlayer cp: game.getPlayers()){
                cp.notifyUpdatedBoard();
            }

        } catch (RemoteException e) { throw new RuntimeException(e); }


        //if the game is ended every player is notified and the results are shown
        if(game.getGameStatus().equals(GameStatus.END_GAME)){

            try {

                for(ControlPlayer cp: game.getPlayers()){
                    cp.notifyEndGame();
                }

            } catch (RemoteException e) { throw new RuntimeException(e); }

        }

        try {

            ControlPlayer nextPlayer= game.getPlayers().get(game.getCurrPlayer());
            nextPlayer.notifyStartYourTurn();

        } catch (IOException e) { throw new RuntimeException(e); }

        return true;

    }

    /**
     * @return the score of the player ch
     * @throws RemoteException
     */
    @Override
    public int getMyScore() throws RemoteException{

        return bookshelf.getMyScore();

    }




    //-------------------------------------- RMI vs Socket layer --------------------------------------


    /**
     * this method tells to "nextClient" to start his turn, is divided in RMI and socket
     * @return true if everything went fine
     * @throws RemoteException
     */
    public abstract Boolean notifyStartYourTurn() throws IOException;

    /**
     * this method tells to the current user that his turn is finished, is divided in RMI and socket
     * @return true if everything went fine
     * @throws RemoteException
     */
    public abstract Boolean notifyEndYourTurn() throws IOException;

    /**
     * this method tells to all users to update their board to the new one, is divided in RMI and socket
     * @throws RemoteException
     */
    public abstract void notifyUpdatedBoard() throws RemoteException;

    /**
     * this method tells to all users that the game they're playing is ended, is divided in RMI and socket
     * @throws RemoteException
     */
    public abstract void notifyEndGame() throws RemoteException;


    /**
     * this method tells to all users that the game has started and that they aren't anymore in the waiting room, is divided in RMI and socket
     * @throws RemoteException
     */
    public  abstract void notifyStartPlaying() throws RemoteException;


    /**
     * this method tells asks a user how many players he wants in his game, is divided in RMI and socket
     * @return the chosen number of players
     * @throws RemoteException
     */
    public abstract int askNumberOfPlayers() throws IOException;

    //-------------------------------------- getter and setter methods --------------------------------------

    /**
     *
     * @return playerID
     */
    public String getPlayerNickname() {
        return nickname;
    }
    /**
     *
     * @return playerStatus
     */

    public PlayerStatus getPlayerStatus() {
        return playerStatus;
    }

    /**
     *
     * @return bookshelf
     */

    public Bookshelf getBookshelf() {
        return bookshelf;
    }



    /**
     * @return true if it's used RMI connection, false otherwise
     */


    /**
     * @param g of tupe Game
     */
    public void setGame(Game g){
        this.game=g;
    }


    /**
     * @param playerStatus: player status
     */
    public void setPlayerStatus(PlayerStatus playerStatus) {
        this.playerStatus = playerStatus;
    }


    abstract public void setClientHandler(ClientHandler cliHnd);


}
