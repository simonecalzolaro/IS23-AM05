package controller;

import model.*;
import myShelfieException.*;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public interface GameHandler extends Remote {

    void chooseBoardTiles(List<Integer> coord) throws RemoteException, NotConnectedException, InvalidParametersException, NotMyTurnException, InvalidChoiceException;

    void insertShelfTiles( int choosenColumn, List<Integer> coord) throws RemoteException, NotConnectedException, NotMyTurnException, InvalidLenghtException, InvalidChoiceException;

    void passMyTurn() throws RemoteException;

}
