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
     *
     * @param nickname : unique player nickname
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
     * @return true if everything went fine
     * @throws RemoteException
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
     * @throws RemoteException
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

    @Override
    public void askPing() throws IOException {

        ch.ping();

    }

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

    @Override
    public void askNumberOfPlayers() {
        try {
            ch.enterNumberOfPlayers();
        } catch (Exception e) {
            System.out.println(" --- an error occured while asking enterNumberOfPlayers() to "+ nickname);
        }
    }



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

    @Override
    public void setStreams(ArrayList<Stream> streams) { }

}
