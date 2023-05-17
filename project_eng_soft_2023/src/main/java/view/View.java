package view;

import client.Client;
import myShelfieException.*;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public abstract class View {
    public Client client;
    public abstract int getNumOfPlayer() throws RemoteException;
    public abstract void updateBoard();
    public abstract void endGame();
    public abstract void isYourTurn() throws IOException, InvalidChoiceException, NotConnectedException, InvalidParametersException, NotMyTurnException;
    public abstract void startGame() throws IOException, LoginException, NotBoundException;
    public abstract void endYourTurn();
}
