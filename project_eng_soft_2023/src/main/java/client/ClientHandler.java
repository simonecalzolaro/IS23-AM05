package client;

import model.Board;
import model.Tile;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;

public interface ClientHandler extends Remote {
    int enterNumberOfPlayers() throws RemoteException;
    void updateBoard(Tile[][] shelf)throws RemoteException;
    void showResults(Map<Integer, String> results)throws RemoteException;
    void theGameEnd()throws RemoteException;
    void startYourTurn()throws RemoteException;
    void endYourTurn()throws RemoteException;
    void startPlaying() throws RemoteException;

}
