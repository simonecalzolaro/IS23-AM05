package controller;

import client.ClientHandler;
import model.*;
import myShelfieException.*;

import java.io.IOException;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

/**
 * ServerApp is the running program of the server, it handles the remote clients and
 * all the actions each client wants to take
 */
public abstract class Lobby implements ClientServerHandler {


    protected static Map<ClientHandler, ControlPlayer> clients;
    protected static ArrayList< Game > games;
    protected static int attendedPlayers;
    protected static Board tempBoard;
    protected ArrayList<ControlPlayer> tempPlayers;

    /**
     * constructor for the ServerApp
     * @throws RemoteException
     */
    protected Lobby() throws RemoteException {

        super();

        clients = new ConcurrentHashMap<>(); //thread-safe
        games = new ArrayList<>();
        tempPlayers= new ArrayList<>();
        attendedPlayers = -1;

    }

    /**
     * main program of the server, he will just start the server app with startServer()
     * @param args
     */
    public static void main(String[] args) {

        System.out.println( "Hello from ServerApp!" );

        try {

            new RMIServer().startServer();
            new TCPHandlerLobby().startServer();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * start the server app and create a registry "ServerAppService" bound with "this" object
     * @throws RemoteException
     */


    public static class Settings {
        static int PORT = 1234;
        static String SERVER_NAME = "127.0.0.1";
    }

    public abstract void startServer() throws RemoteException;

    /**
     * Method called by the client to log into the server to start a new game.
     * @param nickname is the name chosen by the client, if ti is already used throws IllegalArgumentException
     * @param ch is the client interface. I need it to associate each client interface to a ControlPlayer
     * @throws RemoteException
     */

    public abstract GameHandler login(String nickname, ClientHandler ch , Socket socketCP) throws RemoteException, IOException, LoginException ;

    /**
     * Method called by a client who wants to continue a game he was playing before the disconnection/client's crash
     * @param nickname
     * @param ch ClientHandler of the remote User
     * @return GameHandler remote interface
     * @throws RemoteException
     */
    @Override
    public GameHandler continueGame(String nickname, ClientHandler ch) throws RemoteException, LoginException {

        //checking if exists a player called "nickname" now offline

        Stream<ControlPlayer> cp= null;
        cp = clients.values().stream()
                    .filter(x -> x.getPlayerNickname().equals(nickname));

        if(cp.count()<1) throw new LoginException("This nickname does not exists");

        if(cp.count()!=1) throw new LoginException("try again");

        ControlPlayer controlPlayer = cp.toList().get(0);

        if( ! controlPlayer.getPlayerStatus().equals(PlayerStatus.NOT_ONLINE) ) throw new LoginException("This nickname results to be still online");

        //if exists I'll create a new one, remove the current ClientHandler-ControlPlayer pair from the map, set a new ClientHandler on the ControlPlayer
        controlPlayer.setClientHandler(ch);
        clients.remove(getKey(clients, controlPlayer));
        clients.put(ch, controlPlayer);


        //searching the correspondent Game and return the GameHandler interface
        return controlPlayer;
    }

    /**
     * method called by a client to leave the game he is playing
     * @param ch is the client interface how wants to leave the game
     * @return
     */
    @Override
    public boolean leaveGame(ClientHandler ch) {

        System.out.println("User "+ clients.get(ch).getPlayerNickname());

        Game g= (Game)games.stream().filter(x->x.getPlayers().contains(clients.get(ch)));

        boolean res = g.removePlayer( clients.get(ch));

        if( res ){
            System.out.println(" successfully ");
            clients.remove(ch);
        }
        else System.out.println("tried to");

        System.out.println(" leave the game:"+ games.stream()
                .filter(x->x.getPlayers().contains(clients.get(ch)))
                .map(x -> x.getGameID()));

        return res;
    }

    /**
     * get the correspondent key of a given value
     * @param map
     * @param value
     * @return the key associated to a value in a map
     * @param <K>
     * @param <V>
     */
    private static <K, V> K getKey(Map<K, V> map, V value) {
        for (Map.Entry<K, V> entry: map.entrySet())
        {
            if (value.equals(entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }

    /**
     * this method tells asks a user how many players he wants in his game, is divided in RMI and socket
     * @param ch
     * @return the chosen number of players
     * @throws RemoteException
     */
    public abstract int askNumberOfPlayers(ClientHandler ch) throws IOException;

    /**
     * this method tells to all users that the game has started and that they aren't anymore in the waiting room, is divided in RMI and socket
     * @param g
     * @throws RemoteException
     */
    public  abstract void notifyStartPlaying(Game g) throws RemoteException;


}
