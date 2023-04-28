package controller;

import client.ClientHandler;
import model.Board;

import java.rmi.RemoteException;

public class RMIControlPlayer extends ControlPlayer{
    /**
     * Assign player id
     * Initialize score
     * Set player status as NOT_MY_TURN
     *
     * @param nickname : unique player nickname
     * @param board    : unique board
     */
    public RMIControlPlayer(String nickname, Board board) throws RemoteException {
        super(nickname, board);
    }

    /**
     * this method tells to "nextClient" to start his turn, is divided in RMI and socket
     * @param nextClient
     * @return true if everything went fine
     * @throws RemoteException
     */
    public Boolean notifyStartYourTurn(ClientHandler nextClient) throws RemoteException {


        return nextClient.startYourTurn();


    }


    public Boolean notifyEndYourTurn() throws RemoteException {


        return ch.endYourTurn();

    }

    public void notifyUpdatedBoard() throws RemoteException{

        //for each client in the game I push the updated board
        for(ControlPlayer player: game.getPlayers()) {

            if( ! playerStatus.equals(PlayerStatus.NOT_ONLINE)) {


                while (!player.getClientHandler().updateBoard(game.getBoard().getBoard())); //----poco elegante


            }
        }
    }


    public void notifyEndGame() throws RemoteException{

        //for each client in the game I notify the end of the game with the results of the match
        for(ControlPlayer player: game.getPlayers()) {

            if( ! playerStatus.equals(PlayerStatus.NOT_ONLINE)) {

                    //NON HO CAPITO
                    while( player.getClientHandler().theGameEnd(game.getGameResults()) ); //----poco elegante

            }
        }
    }
}
