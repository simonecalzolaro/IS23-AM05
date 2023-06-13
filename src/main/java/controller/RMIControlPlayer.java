package controller;

import client.ClientHandler;
import model.Tile;

import java.io.IOException;
import java.rmi.RemoteException;
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

        System.out.println("    ->notify StartYourTurn to "+ nickname);
        ch.startYourTurn();

    }

    @Override
    public void notifyEndYourTurn() throws RemoteException {
        ch.endYourTurn();
    }

    @Override
    public void notifyUpdatedBoard() throws RemoteException{

        if( ! playerStatus.equals(PlayerStatus.NOT_ONLINE)) {

            Map<String, Tile[][]> map= new HashMap<>();

            //in this for I'm creating the map to send, I'm NOT notifying each player
            for(ControlPlayer cp: game.getPlayers()){

                if(! cp.equals(this))  map.put(cp.getPlayerNickname(), cp.getBookshelf().getShelf());

            }
            ch.updateBoard(game.getBoard().getBoard(), this.bookshelf.getShelf(), map , bookshelf.getMyScore());
        }
    }


    @Override
    public void notifyEndGame() throws RemoteException{

        if( ! playerStatus.equals(PlayerStatus.NOT_ONLINE)) {

            ch.theGameEnd(game.getGameResults()) ;

        }
    }

    /**
     * this method tells to all users that the game has started and that they aren't anymore in the waiting room, is divided in RMI and socket
     * @throws RemoteException
     */
    @Override
    public void notifyStartPlaying() throws RemoteException {

        System.out.println("    ->notify startPlaying to "+ nickname);
        ch.startPlaying(bookshelf.getPgc().getCardNumber(), bookshelf.getPgc().getCardMap(), game.getBoard().getCommonGoalCard1().getCGCnumber(), game.getBoard().getCommonGoalCard2().getCGCnumber(), game.getGameID());

    }

    @Override
    public void askPing() throws IOException {
            ch.ping();
    }

    @Override
    public void notifyNewMessage(String nick, String message) throws IOException {

        ch.receiveMessage(nick, message);

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

    /*
    @Override
    public void askPing() throws RemoteException {
        ch.pong();
    }

     */


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
