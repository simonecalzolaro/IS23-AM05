package controller;

import client.ClientHandler;
import myShelfieException.LoginException;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientServerHandler extends Remote {

    /**
     * a player log into the server with his nickname, if he is not already present
     * a ControlPlayer is created with the chosen nickname.
     * If the player is the first to entre a game I'll ask him for the number of players
     * @param nickname
     * @return true if the login request is approved
     */
    GameHandler login(String nickname, ClientHandler ch) throws IOException, LoginException;


    GameHandler continueGame(String nickname, ClientHandler ch) throws RemoteException, LoginException;


    /**
     * a player ask to leave the game he is playing
     * @return true if the request is approved
     */
    boolean leaveGame(ClientHandler ch) throws RemoteException;

}
