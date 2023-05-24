package controller;

import client.ClientHandler;
import myShelfieException.LoginException;

import java.io.IOException;
import java.net.Socket;
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
    GameHandler login(String nickname, Object obj) throws RemoteException, IOException, LoginException;

    /**
     * Method called by a user who wants tu continue a game after a web or client's server crash
     * @param nickname of the user, the same used in an old game
     * @return the GameHandler of his local ControlPlayer
     * @throws RemoteException
     * @throws LoginException
     */
    GameHandler continueGame(String nickname, Object client)  throws RemoteException, LoginException;


    /**
     * a player ask to leave the game he is playing
     */
    void leaveGame(String nickname, int ID) throws LoginException, RemoteException;

    /**
     * method called by the client to set the number of players
     */
    void setNumberOfPlayers(int n, String nick) throws RemoteException;

}
