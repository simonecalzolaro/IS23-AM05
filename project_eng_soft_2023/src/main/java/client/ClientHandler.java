package client;

import model.Board;
import model.Tile;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;

public interface ClientHandler extends Remote {
    int enterNumberOfPlayers() throws RemoteException;
    boolean updateBoard(Tile[][] board)throws RemoteException;
    boolean theGameEnd(Map<Integer, String> results)throws RemoteException;
    boolean startYourTurn()throws RemoteException;
    boolean endYourTurn()throws RemoteException;
    boolean startPlaying() throws RemoteException;

}
