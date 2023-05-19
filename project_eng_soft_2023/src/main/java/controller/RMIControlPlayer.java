package controller;

import client.ClientHandler;
import model.Board;
import model.Tile;
import myShelfieException.InvalidChoiceException;
import myShelfieException.InvalidParametersException;
import myShelfieException.NotConnectedException;
import myShelfieException.NotMyTurnException;

import java.io.IOException;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

        System.out.println("notify StartYourTurn to "+ nickname);
        ch.startYourTurn();
        return true;
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

                    if(! cp.equals(this))  map.put(cp.getPlayerNickname(),cp.getBookshelf().getShelf());

                }

                ch.updateBoard(game.getBoard().getBoard(), this.bookshelf.getShelf(), map  );

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

        System.out.println("notify startPlaying to "+ nickname);
        ch.startPlaying(bookshelf.getPgc().getCardNumber(), bookshelf.getPgc().getCardMap(), game.getBoard().getCommonGoalCard1().getCGCnumber(), game.getBoard().getCommonGoalCard2().getCGCnumber());
        notifyUpdatedBoard();
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
    public void setStreams(ArrayList<Stream> streams) { }


}
