package controller;

import model.*;
import myShelfieException.*;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public interface GameHandler extends Remote {

    boolean chooseBoardTiles(List<Tile> choosenTiles, List<Integer> coord) throws RemoteException, NotConnectedException, InvalidParametersException, NotMyTurnException, InvalidChoiceException;

    boolean insertShelfTiles(ArrayList<Tile> choosenTiles, int choosenColumn, List<Integer> coord) throws RemoteException, NotConnectedException, NotMyTurnException, InvalidLenghtException, InvalidChoiceException;

    int getMyScore() throws RemoteException;

}
