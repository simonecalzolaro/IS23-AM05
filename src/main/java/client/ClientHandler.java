package client;

import model.Tile;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;

public interface ClientHandler extends Remote {
    void enterNumberOfPlayers() throws IOException;
    void updateBoard(Tile[][] board, Tile[][] myShelf, Map<String, Tile[][]> otherShelf, int myScore) throws RemoteException;
    void theGameEnd(Map<String, Integer> results) throws RemoteException;
    void startYourTurn() throws RemoteException;
    void endYourTurn() throws RemoteException;
    void startPlaying(int pgcNum, Map<model.Tile, Integer[]> pgcMap, int cgc1num, int cgc2num, int GameID) throws RemoteException;
    void ping() throws RemoteException;
    void receiveMessage(String sender, String message) throws RemoteException;

    void restoreSession(model.Tile[][] board, model.Tile[][] myShelf, Map<String, model.Tile[][]> otherShelf, int myScore,int gameID, int pgcNum, Map<model.Tile, Integer[]> pgcMap, int cgc1num, int cgc2num) throws RemoteException;

}