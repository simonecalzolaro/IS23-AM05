package client;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;

public interface ClientHandler extends Remote {
    int enterNumberOfPlayers() throws IOException;
    boolean updateBoard(model.Tile[][] board, model.Tile[][] myShelf, Map<String, model.Tile[][]> otherShelf) throws RemoteException;
    boolean theGameEnd(Map<Integer, String> results) throws RemoteException;
    void startYourTurn() throws RemoteException;
    boolean endYourTurn() throws RemoteException;
    boolean startPlaying(int pgcNum, Map<model.Tile, Integer[]> pgcMap, int cgc1num, int cgc2num) throws RemoteException;
    boolean pong() throws RemoteException;
}
