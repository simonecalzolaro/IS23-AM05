package controller;

import client.ClientHandler;
import model.NotAvailableTilesException;
import model.NotEnoughSpaceException;
import model.NotInLineException;
import model.Tile;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface GameHandler extends Remote {

    boolean chooseBoardTiles(List<Tile> choosenTiles, List<Integer> coord, ClientHandler ch) throws RemoteException, NotInLineException, NotConnectedException, NotEnoughSpaceException, NotAvailableTilesException, InvalidParametersException, NotMyTurnException;

    void insertShelfTiles(ArrayList<Tile> choosenTiles, int choosenColumn , ClientHandler ch) throws RemoteException, NotConnectedException, NotMyTurnException;

    int getMyScore(ClientHandler ch ) throws RemoteException;

}
