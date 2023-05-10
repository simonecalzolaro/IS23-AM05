package client;

import model.Board;
import model.CommonGoalCard;
import model.PersonalGoalCard;
import model.Tile;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;

public interface ClientHandler extends Remote {
    int enterNumberOfPlayers() throws IOException;
    boolean updateBoard(Tile[][] board, Tile[][] myShelf, Map<String, Tile[][]> otherShelf) throws RemoteException;
    boolean theGameEnd(Map<Integer, String> results)throws RemoteException;
    boolean startYourTurn()throws RemoteException;
    boolean endYourTurn()throws RemoteException;
    boolean startPlaying(int pgc, int cgc1, int cgc2) throws RemoteException;
    boolean pong() throws RemoteException;
}
