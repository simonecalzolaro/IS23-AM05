package controller;

import client.ClientHandler;
import model.Board;
import myShelfieException.LoginException;

import java.io.IOException;
import java.net.Socket;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class RMIServer extends Lobby{
    /**
     * constructor for the ServerApp
     *
     * @throws RemoteException
     */
    public RMIServer() throws RemoteException {

    }

    @Override
    public void startServer() throws RemoteException, AlreadyBoundException {

        Registry registry = null;
        ClientServerHandler stub = null;

        stub = (ClientServerHandler) UnicastRemoteObject.exportObject(this, Settings.PORT);
        registry = LocateRegistry.createRegistry(Settings.PORT);
        registry.bind("ServerAppService",stub);

        System.out.println("RMIServer ready");
    }


}
