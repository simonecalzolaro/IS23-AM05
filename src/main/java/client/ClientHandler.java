package client;

import model.Tile;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;

public interface ClientHandler extends Remote {

    /**
     * method called by the server to ask the user to insert the number of players
     */
    void enterNumberOfPlayers() throws IOException;

    /**
     * method called by the server to update the users's board after a turn
     * @param board: update board of tiles
     * @param myShelf: your updated shelf
     * @param otherShelf: other's shelf
     * @param myScore: your updated score
     * @throws RemoteException: RMI error
     */
    void updateBoard(Tile[][] board, Tile[][] myShelf, Map<String, Tile[][]> otherShelf, int myScore) throws RemoteException;

    /**
     * method called by the server to notify the end of a game
     * @param results: ranking of the participants
     * @throws RemoteException: RMI error
     */
    void theGameEnd(Map<String, Integer> results) throws RemoteException;

    /**
     * method called by the server to tell the user to start his turn
     * @throws RemoteException: RMI error
     */
    void startYourTurn() throws RemoteException;

    /**
     * method called by the server to tell the user to end his turn
     * @throws RemoteException: RMI error
     */
    void endYourTurn() throws RemoteException;

    /**
     * function called by the server to tell the user to start playing
     * @param pgcNum: personal goal card number
     * @param pgcMap: map representing the personal goal card tile's disposition
     * @param cgc1num: first common goal card number
     * @param cgc2num: second common goal card number
     * @param GameID: ID of the game you are playing
     * @throws RemoteException
     */
    void startPlaying(int pgcNum, Map<model.Tile, Integer[]> pgcMap, int cgc1num, int cgc2num, int GameID) throws RemoteException;

    /**
     * method called by the server to ping this client, the client will then call a pong() on the server
     * @throws RemoteException: RMI error
     */
    void ping() throws RemoteException;

    /**
     * method called by the server to add a message to user's conversation
     * @param sender: nickname of the player who sent the message
     * @param message: text message
     * @throws RemoteException: RMI error
     */
    void receiveMessage(String sender, String message) throws RemoteException;

    /**
     * method called by the server to restore the status of a game in a client after a break or a disconnection
     * @param board: game board
     * @param myShelf: user shelf
     * @param otherShelf: other shelf
     * @param myScore: user score
     * @param gameID: game ID
     * @param pgcNum: personal goal card number
     * @param pgcMap: map representing the personal goal card tile's disposition
     * @param cgc1num: first common goal card number
     * @param cgc2num: second common goal card number
     * @throws RemoteException: RMI error
     */
    void restoreSession(model.Tile[][] board, model.Tile[][] myShelf, Map<String, model.Tile[][]> otherShelf, int myScore,int gameID, int pgcNum, Map<model.Tile, Integer[]> pgcMap, int cgc1num, int cgc2num) throws RemoteException;

}
