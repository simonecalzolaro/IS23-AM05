package controller;

import client.ClientHandler;
import model.Board;
import model.Tile;

import java.io.IOException;
import java.net.Socket;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
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
     * @param board    : unique board
     */
    public RMIControlPlayer(String nickname, Board board, ClientHandler clientHandler) throws RemoteException {

        super(nickname, board);
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
    public Boolean notifyStartYourTurn() throws RemoteException {
        return ch.startYourTurn();
    }

    @Override
    public Boolean notifyEndYourTurn() throws RemoteException {
        return ch.endYourTurn();
    }

    @Override
    public void notifyUpdatedBoard() throws RemoteException{

            if( ! playerStatus.equals(PlayerStatus.NOT_ONLINE)) {

                Map<String, Tile[][]> map= new HashMap<>();

                for(ControlPlayer cp: game.getPlayers()){

                    if(! cp.equals(this))  map.put(cp.getPlayerNickname(), cp.getBookshelf().getBookshelf());

                }

                ch.updateBoard(game.getBoard().getBoard(), this.bookshelf.getBookshelf(), map  );

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

            ch.startPlaying(bookshelf.getPgc().getCardNumber(), game.getBoard().getCommonGoalCard1().getCGCnumber(), game.getBoard().getCommonGoalCard2().getCGCnumber());
            notifyUpdatedBoard();

            //if this player is also the first player to play
            if(game.getPlayers().get(game.getCurrPlayer()).equals(this)) this.notifyStartYourTurn();

    }

    @Override
    public int askNumberOfPlayers() throws IOException {
        return ch.enterNumberOfPlayers();
    }

    @Override
    public boolean askPing() throws RemoteException {
        return ch.pong();
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
    public void setSocket(Socket socket) {
    }

    private static class Settings {
        static int PORT = 5678;
        static String SERVER_NAME = "127.0.0.1";
    }

}
