package controller;

import model.*;
import myShelfieException.*;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public interface GameHandler extends Remote {

    /**
     * method called by the client to check if the tiles he chooses are okay
     */
    void chooseBoardTiles(List<Integer> coord) throws RemoteException, NotConnectedException, InvalidParametersException, NotMyTurnException, InvalidChoiceException;

    /**
     * method called by the client to insert tiles in a column
     */
    void insertShelfTiles( int choosenColumn, List<Integer> coord) throws RemoteException, NotConnectedException, NotMyTurnException, InvalidLenghtException, InvalidChoiceException;

    /**
     * method called by the client to skip his turn
     */
    void passMyTurn() throws RemoteException;


}
