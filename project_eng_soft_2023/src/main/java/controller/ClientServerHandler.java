package controller;

import client.ClientHandler;

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
    void login(String nickname, ClientHandler ch) throws RemoteException;


    /**
     * a player ask to leave the game he is playing
     * @return true if the request is approved
     */
    boolean leaveGame(ClientHandler ch) throws RemoteException;

}