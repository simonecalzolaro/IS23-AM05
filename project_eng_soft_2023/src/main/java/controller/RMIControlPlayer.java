package controller;

import client.ClientHandler;
import model.Board;

import java.io.IOException;
import java.net.Socket;
import java.rmi.RemoteException;

public class RMIControlPlayer extends ControlPlayer{

    protected ClientHandler ch;


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

                ch.updateBoard(game.getBoard().getBoard());

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
            ch.updateBoard(game.getBoard().getBoard()); //----------timer che aspetta il return true

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
    public void setClientHandler(ClientHandler cliHnd){
        this.ch=cliHnd;
    }

    @Override
    public void setSocket(Socket socket) {}
}
