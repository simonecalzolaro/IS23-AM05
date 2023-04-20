package controller;

import client.ClientHandler;
import model.*;
import myShelfieException.*;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class ControlPlayer extends UnicastRemoteObject implements GameHandler{

    /**
     *Player id
     */
    private final String nickname;

    /**
     * player status
     */

    private PlayerStatus playerStatus;

    /**
     * player bookshelf
     */
    private final Bookshelf bookshelf;

    /**
     * player score
     */
    private int score;

    /**
     * connectionType is:
     * true if RMI,
     * false if socket
     */
    private boolean connectionType;

    //RMI attributes-----------------------------
    private Game game;
    private ClientHandler ch;


    //soket attributes--------------------------
    //...




    /**
     * Assign player id
     * Initialize score
     * Set player status as NOT_MY_TURN
     * @param nickname: unique player nickname
     * @param board: unique board
     */
    public ControlPlayer(String nickname,Board board) throws RemoteException{

        this.nickname=nickname;
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
    public boolean insertTiles(ArrayList<Tile> stream_tiles, int column) throws NotMyTurnException, NotConnectedException, NotEnoughSpaceException, InvalidLenghtException {

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
     * @throws NotAvailableTilesException the tiles selected are not catchable
     * @throws NotEnoughSpaceException the player does not have enough space in the bookshelf
     * @throws NotInLineException the tiles selected are not in line
     * @throws NotMyTurnException the player status is NOT_MY_TURN (the move is not allowed)
     * @throws NotConnectedException the player is not connected
     */
    public List<Tile> catchTile(List<Integer> coord) throws InvalidParametersException, NotAvailableTilesException, NotEnoughSpaceException, NotInLineException, NotMyTurnException, NotConnectedException {

        List<Tile> temp= new ArrayList<>();

        if(playerStatus==PlayerStatus.NOT_MY_TURN) {
            throw new NotMyTurnException();
        }

        else if(playerStatus == PlayerStatus.NOT_ONLINE){
            throw new NotConnectedException();
        }

        else if(coord.size()!=2 && coord.size()!=4 && coord.size()!=6) {
             throw new InvalidParametersException();
         }

        else if (coord.size()==2) return bookshelf.getBoard().subTiles(coord.get(0), coord.get(1));
        else if (coord.size()==4) return bookshelf.getBoard().subTiles(coord.get(0), coord.get(1), coord.get(2), coord.get(3), bookshelf);
        else return bookshelf.getBoard().subTiles(coord.get(0), coord.get(1), coord.get(2), coord.get(3),coord.get(4), coord.get(5), bookshelf);
    }





    //-------------------------------------- GameHandler implemented methods --------------------------------------

    /**
     * method called by the clients to choose tiles from the board of their game
     * @param chosenTiles List of tiles chosen by the client
     * @param coord coordinates of the chosen tiles
     * @return true if the chosen tiles are valid
     */
    @Override
    public boolean choseBoardTiles(List<Tile> chosenTiles, List<Integer> coord) throws RemoteException, NotConnectedException, NotEnoughSpaceException, NotAvailableTilesException, InvalidParametersException, NotMyTurnException, NotInLineException {

        if ( catchTile(coord).equals(chosenTiles)){

            /* updating the Board of all clients is not necessary here, i can do it in insertShelfTiles() only

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
     * @throws NotConnectedException
     * @throws NotMyTurnException
     */
    @Override
    public boolean insertShelfTiles(ArrayList<Tile> chosenTiles, int choosenColumn ) throws RemoteException, NotConnectedException, NotMyTurnException, NotEnoughSpaceException, InvalidLenghtException {

        //inserting the tiles in the bookshelf

        if( ! insertTiles(chosenTiles, choosenColumn)) return false;

        //tell to ch that his turn is completed
        try {
            notifyEndYourTurn();
        } catch (RemoteException e) { throw new RuntimeException(e); }

        //Game.endTurn() is called to update the turn to the next player and update the game status

        game.endTurn();

        //notify the updated board to all the clients participating in the same game of ch
        try {
            notifyUpdatedBoard();
        } catch (RemoteException e) { throw new RuntimeException(e); }


        //if the game is ended every player is notified and the results are shown
        if(game.getGameStatus().equals(GameStatus.END_GAME)){

            try {
                notifyEndGame();
            } catch (RemoteException e) { throw new RuntimeException(e); }

        }

        try {
            ControlPlayer nextPlayer= game.getPlayers().get(game.getCurrPlayer());
            notifyStartYourTurn(nextPlayer.getClientHandler());
        } catch (RemoteException e) { throw new RuntimeException(e); }

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
     * @param nextClient
     * @return true if everything went fine
     * @throws RemoteException
     */
    public Boolean notifyStartYourTurn(ClientHandler nextClient) throws RemoteException {

        if(connectionType){
            //RMI calling
            return nextClient.startYourTurn();
        }
        else{
            //socket calling
            return false; //----SimoSocket
        }
    }

    /**
     * this method tells to the current user that his turn is finished, is divided in RMI and socket
     * @return true if everything went fine
     * @throws RemoteException
     */
    public Boolean notifyEndYourTurn() throws RemoteException {

        if(connectionType){
            //RMI calling
            return ch.endYourTurn();
        }
        else{
            //socket calling
            return false; //----SimoSocket
        }
    }

    /**
     * this method tells to all users to update their board to the new one, is divided in RMI and socket
     * @throws RemoteException
     */
    public void notifyUpdatedBoard() throws RemoteException{

        //for each client in the game I push the updated board
        for(ControlPlayer player: game.getPlayers()) {

            if( ! playerStatus.equals(PlayerStatus.NOT_ONLINE)) {

                if (connectionType) {
                    //RMI calling
                    while (!player.getClientHandler().updateBoard(game.getBoard().getBoard())); //----poco elegante

                } else {
                    //socket calling
                    //----SimoSocket
                }
            }
        }
    }

    /**
     * this method tells to all users that the game they're playing is ended, is divided in RMI and socket
     * @throws RemoteException
     */
    public void notifyEndGame() throws RemoteException{

        //for each client in the game I notify the end of the game with the results of the match
        for(ControlPlayer player: game.getPlayers()) {

            if( ! playerStatus.equals(PlayerStatus.NOT_ONLINE)) {

                if (connectionType) {
                    //RMI calling
                    while( player.getClientHandler().theGameEnd(game.getGameResults()) ); //----poco elegante

                } else {
                    //socket calling
                    //----SimoSocket
                }
            }
        }
    }




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
     * @return client handler
     */
    public ClientHandler getClientHandler(){
        return ch;
    }

    /**
     * @return true if it's used RMI connection, false otherwise
     */
    public boolean getConnectionType() {
        return connectionType;
    }

    /**
     * @param g of tupe Game
     */
    public void setGame(Game g){
        this.game=g;
    }

    /**
     * @param cliHnd of type ClientHandler
     */
    public void setClientHandler(ClientHandler cliHnd){
        this.ch=cliHnd;
    }

    /**
     * @param playerStatus: player status
     */
    public void setPlayerStatus(PlayerStatus playerStatus) {
        this.playerStatus = playerStatus;
    }

    /**
     * @param RMIconnection type boolean
     */
    public void setConnectionType(boolean RMIconnection) {
        this.connectionType = RMIconnection;
    }

}
