package controller;

import client.ClientHandler;
import model.*;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface GameHandler extends Remote {

    boolean chooseBoardTiles(List<Tile> choosenTiles, List<Integer> coord, ClientHandler ch) throws RemoteException, NotConnectedException, NotEnoughSpaceException, NotAvailableTilesException, InvalidParametersException, NotMyTurnException, NotInLineException;

    void insertShelfTiles(ArrayList<Tile> choosenTiles, int choosenColumn , ClientHandler ch) throws RemoteException, NotConnectedException, NotMyTurnException;

    int getMyScore(ClientHandler ch ) throws RemoteException;

}
