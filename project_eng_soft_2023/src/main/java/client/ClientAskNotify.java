package client;

import myShelfieException.*;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public interface ClientAskNotify {

    /**
     * asks the server to log in, is implemented in RMI and socket
     * @return GameHandler interface
     * @throws LoginException
     * @throws IOException
     * @throws RemoteException
     */
    void askLogin(String nick) throws LoginException, IOException, RemoteException;

    /**
     * asks the server to continue a game, is implemented in RMI and socket
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
     * asks the server to leave the game I'm playing, is implemented in RMI and socket
     * @param coord
     * @throws InvalidChoiceException
     * @throws NotConnectedException
     * @throws InvalidParametersException
     * @throws RemoteException
     * @throws NotMyTurnException
     */
    void askBoardTiles( List<Integer> coord) throws InvalidChoiceException, NotConnectedException, InvalidParametersException, IOException, NotMyTurnException;

    /**
     * asks the server to insert some tiles in my shelf, is implemented in RMI and socket
     * @param choosenColumn is the column where to insert the tiles
     * @param coord board coordinates
     * @throws IOException
     * @throws NotConnectedException
     * @throws NotMyTurnException
     * @throws InvalidChoiceException
     * @throws InvalidLenghtException
     */
    void askInsertShelfTiles( int choosenColumn, List<Integer> coord) throws IOException, NotConnectedException, NotMyTurnException, InvalidChoiceException, InvalidLenghtException;

    /**
     * asks the server to set the number of players, is implemented in RMI and socket
     * @param n chosen number of players
     * @param nick your nickname
     */
    void askSetNumberOfPlayers(int n, String nick);

    /**
     * asks the server to pass my turn to the next player int the game
     */
    void askPassMyTurn();

    /**
     * tells the client that a ping has been received
     * @throws RemoteException
     */
    void notifyPong() throws RemoteException;

    /**
     * asks the server to post a message in the chat
     * @param Message
     * @param recipients
     */
    void askPostMessage(String Message, ArrayList<String> recipients);

}
