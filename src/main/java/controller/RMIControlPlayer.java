package controller;

import client.ClientHandler;
import model.Tile;

import java.io.IOException;
import java.rmi.RemoteException;
import java.security.spec.ECField;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RMIControlPlayer extends ControlPlayer{

    private ClientHandler ch;

    /*-------------------------------
    private Registry registry = null;
    private ClientServerHandler stub = null;
    */

    /**
     * Assign player id
     * Initialize score
     * Set player status as NOT_MY_TURN
     * @param nickname of the player who logged in the game
     * @param clientHandler reference/RMI stream for communicating with the client
     * @throws RemoteException thrown when a network error occurs
     */
    public RMIControlPlayer(String nickname, ClientHandler clientHandler) throws RemoteException {

        super(nickname);
        ch=clientHandler;
        /* ----------------------------

        stub = (GameHandler) UnicastRemoteObject.exportObject(this, Settings.PORT);
        registry = LocateRegistry.createRegistry(Settings.PORT);
        try {
            registry.bind(nickname+"playerService" , stub);
        } catch (AlreadyBoundException e) {
            throw new RuntimeException(e);
        }

         */

        System.out.println("----RMIControlPlayer ready----");

    }

    /**
     * this method tells to "nextClient" to start his turn, is divided in RMI and socket
     * @throws RemoteException thrown when a network error occurs
     */
    @Override
    public void notifyStartYourTurn() throws RemoteException {

        if( !playerStatus.equals(PlayerStatus.NOT_ONLINE)){
            try{
                System.out.println("    ->notify StartYourTurn to "+ nickname);
                ch.startYourTurn();
            }catch(Exception e){
                System.out.println("---something went wrong while notifyStartYourTurn() to "+ nickname);
            }
        }
    }


    /**
     * This method tells the client to end its turn
     * @throws RemoteException thrown when a network error occurs
     */
    @Override
    public void notifyEndYourTurn() throws RemoteException {

        if(!playerStatus.equals(PlayerStatus.NOT_ONLINE)){
            try{
                ch.endYourTurn();
            }catch(Exception e){
                System.out.println("---error: something went wrong while notifyEndYourTurn() to "+ nickname);
            }
        }
    }


    /**
     * This method update the board of every client playing the game
     * @throws RemoteException thrown when a network error occurs
     */
    @Override
    public void notifyUpdatedBoard() throws RemoteException{

        if(!playerStatus.equals(PlayerStatus.NOT_ONLINE)){
            try{
                Map<String, Tile[][]> map= new HashMap<>();
                //in this for I'm creating the map to send, I'm NOT notifying each player
                for(ControlPlayer cp: game.getPlayers()){
                    if(! cp.equals(this))  map.put(cp.getPlayerNickname(), cp.getBookshelf().getShelf());
                }
                ch.updateBoard(game.getBoard().getBoard(), this.bookshelf.getShelf(), map , bookshelf.getMyScore());

            }catch(Exception e){
                System.out.println("---error: something went wrong while notifyUpdatedBoard() to "+ nickname);
            }
        }
    }


    /**
     * This method tells the client to end the game
     * @throws RemoteException thrown when a network error occurs
     */
    @Override
    public void notifyEndGame() throws RemoteException{

        if( ! playerStatus.equals(PlayerStatus.NOT_ONLINE)) {
            try{
                ch.theGameEnd(game.getGameResults()) ;
            }catch (RemoteException e){
                System.out.println("---error: something went wrong while notifyEndGame() to "+ nickname);
            }
        }
    }

    /**
     * this method tells to all users that the game has started and that they aren't anymore in the waiting room, is divided in RMI and socket
     * @throws RemoteException thrown when a network error occurs
     */
    @Override
    public void notifyStartPlaying() throws RemoteException {
        if( !playerStatus.equals(PlayerStatus.NOT_ONLINE)) {
            try {
                System.out.println("    ->notify startPlaying to " + nickname);
                ch.startPlaying(bookshelf.getPgc().getCardNumber(), bookshelf.getPgc().getCardMap(), game.getBoard().getCommonGoalCard1().getCGCnumber(), game.getBoard().getCommonGoalCard2().getCGCnumber(), game.getGameID());
            }catch (RemoteException e){
                System.out.println("---error: something went wrong while notifyStartPlaying() to "+ nickname);
            }
        }
    }



    /**
     * This method send the ping to the client in order to verify if it's online or not
     * @throws IOException thrown when a network error occurs
     */
    @Override
    public void askPing() throws IOException {

        ch.ping();

    }


    /**
     * This method is used in order to warn the client about a new message received from another client
     * @param nick: nickname of the sender
     * @param message: text message
     * @throws IOException thrown when a network error occurs
     */
    @Override
    public void notifyNewMessage(String nick, String message) throws IOException {

        if( !playerStatus.equals(PlayerStatus.NOT_ONLINE)) {
            try {
                ch.receiveMessage(nick, message);
            }catch (RemoteException e){
                System.out.println("---error: something went wrong while notifyNewMessage() to "+ nickname);
            }
        }
    }


    /**
     * This method tells the client to insert the number of players of the game
     * That means that the current client is the first player
     */
    @Override
    public void askNumberOfPlayers() {
        try {
            ch.enterNumberOfPlayers();
        } catch (Exception e) {
            System.out.println(" --- an error occured while asking enterNumberOfPlayers() to "+ nickname);
        }
    }



    /**
     * This method is invoked when a client tries to continue a game and succeed in it, so it's necessary that he receives the updated board and its attributes stored in the server
     * @throws RemoteException thrown when a network error occurs
     */
    @Override
    public void restoreSession() throws RemoteException {

        if( ! playerStatus.equals(PlayerStatus.NOT_ONLINE)) {

            Map<String, Tile[][]> map= new HashMap<>();

            //in this for I'm creating the map to send, I'm NOT notifying each player
            for(ControlPlayer cp: game.getPlayers()){

                if(! cp.equals(this))  map.put(cp.getPlayerNickname(), cp.getBookshelf().getShelf());

            }

            ch.restoreSession(game.getBoard().getBoard(),this.bookshelf.getShelf(),map,bookshelf.getMyScore(),game.getGameID(),bookshelf.getPgc().getCardNumber(),bookshelf.getPgc().getCardMap(),game.getBoard().getCommonGoalCard1().getCGCnumber(),game.getBoard().getCommonGoalCard2().getCGCnumber());

        }
    }



    /**
     * @return client handler
     */
    public ClientHandler getClientHandler(){
        return ch;
    }


    /**
     * @param cliHnd of type ClientHandler
     */
    @Override
    public void setClientHandler(ClientHandler cliHnd){
        this.ch=cliHnd;
    }


    /**
     * Not used in RMIControlPlayer
     * @param streams not important here
     */
    @Override
    public void setStreams(ArrayList<Stream> streams) { }

}
