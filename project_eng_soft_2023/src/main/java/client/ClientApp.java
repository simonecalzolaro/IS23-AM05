package client;

import model.Board;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ClientApp extends UnicastRemoteObject implements ClientHandler {


    protected ClientApp() throws RemoteException {


    }

    public static void main(String[] args) {

    }

    private void startClient() throws RemoteException{

    }


    @Override
    public int enterNUmberOfPlayers() {
        return 0;
    }

    @Override
    public boolean updateBoard(Board board) {
        return false;
    }

    @Override
    public void theGameEnd() {

    }

    @Override
    public void statYourTurn() {

    }

    @Override
    public void endYourTurn() {

    }
}
