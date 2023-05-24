package client;

import myShelfieException.*;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.List;

public interface ClientAskNotify {

    /**
     * asks the server to log in, is divided in RMI and socket
     * @return GameHandler interface
     * @throws LoginException
     * @throws IOException
     * @throws RemoteException
     */
    void askLogin(String nick) throws LoginException, IOException, RemoteException;

    /**
     * asks the server to continue a game, is divided in RMI and socket
     * @return GameHandler interface
     * @throws LoginException
     * @throws RemoteException
     */
    void askContinueGame() throws LoginException, IOException;

    /**
     * asks the server to leave the game I'm playing, is divided in RMI and socket
     * @return true if everything went fine
     * @throws RemoteException
     */
    void askLeaveGame() throws IOException, LoginException;

    /**
     * asks the server to leave the game I'm playing, is divided in RMI and socket
     * @param coord
     * @return true if everything went fine
     * @throws InvalidChoiceException
     * @throws NotConnectedException
     * @throws InvalidParametersException
     * @throws RemoteException
     * @throws NotMyTurnException
     */
    void askBoardTiles( List<Integer> coord) throws InvalidChoiceException, NotConnectedException, InvalidParametersException, IOException, NotMyTurnException;

    /**
     * asks the server to insert some tiles in my shelf, is divided in RMI and socket
     * @param choosenColumn is the column where to insert the tiles
     * @param coord board coordinates
     * @return true if everything went fine
     * @throws IOException
     * @throws NotConnectedException
     * @throws NotMyTurnException
     * @throws InvalidChoiceException
     * @throws InvalidLenghtException
     */
    void askInsertShelfTiles( int choosenColumn, List<Integer> coord) throws IOException, NotConnectedException, NotMyTurnException, InvalidChoiceException, InvalidLenghtException;

    void askSetNumberOfPlayers(int n, String nick);

    void askPassMyTurn();


}
