package controller;

import client.ClientHandler;
import model.*;
import myShelfieException.*;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public interface GameHandler extends Remote {

    boolean choseBoardTiles(List<Tile> choosenTiles, List<Integer> coord) throws RemoteException, NotConnectedException, NotEnoughSpaceException, NotAvailableTilesException, InvalidParametersException, NotMyTurnException, NotInLineException;

    boolean insertShelfTiles(ArrayList<Tile> choosenTiles, int choosenColumn ) throws RemoteException, NotConnectedException, NotMyTurnException, NotEnoughSpaceException, InvalidLenghtException;

    int getMyScore() throws RemoteException;

}
