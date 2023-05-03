package controller;

import client.ClientHandler;
import model.*;
import myShelfieException.*;

import java.io.IOException;
import java.net.Socket;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

/**
 * ServerApp is the running program of the server, it handles the remote clients and
 * all the actions each client wants to take
 */
public abstract class Lobby implements ClientServerHandler {


    protected static ArrayList<ControlPlayer> clients;
    protected static ArrayList< Game > games;
    protected static int attendedPlayers;
    protected static Board tempBoard;
    protected static ArrayList<ControlPlayer> tempPlayers;

    /**
     * constructor for the ServerApp
     * @throws RemoteException
     */
    protected Lobby() throws RemoteException { //---- questo costruttore potrebbe dare problemi? viene eseguito due volte, uno per RMIServer e una per SocketServer.
                                                    // credo di no perchè gli attributi sono static (M)

        super();

        clients = new ArrayList<>(); //thread-safe
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

    public abstract void startServer() throws RemoteException, AlreadyBoundException;

    /**
     * Method called by the client to log into the server to start a new game.
     * @param nickname is the name chosen by the client, if ti is already used throws IllegalArgumentException
     * @param  client
     * @throws RemoteException
     */

    public GameHandler login(String nickname, Object client) throws RemoteException, IOException, LoginException {

        if( clients.stream().map(x -> x.getPlayerNickname()).toList().contains(nickname) ) throw new LoginException("this nickname is not available at the moment");

        else {

            ControlPlayer pl=null;

            //create a ControlPlayer
            if (client instanceof Socket){
                pl= new SocketControlPlayer(nickname, tempBoard, (Socket) client );
            }
            else if (client instanceof ClientHandler){

                pl= new RMIControlPlayer(nickname, tempBoard, (ClientHandler)client);

            }

            System.out.println("...player "+ nickname+ " entered the game ");

            //add to the map "clients" the ClientHandler interface and the associated ControlPlayer
            clients.add(pl);
            //tempPlayers is like a waiting room
            tempPlayers.add(pl);

            if(attendedPlayers==-1){ //if the isn't any waiting room it means that ch is the first player

                tempBoard = new Board();
                System.out.println("...a new board has been created...");

                do{
                    try {
                        //server asks client how many players he wants in his match
                        if (client instanceof Socket) attendedPlayers = pl.askNumberOfPlayers();

                    } catch (RemoteException e) {
                        attendedPlayers = -1;
                        throw new RuntimeException(e);
                    }
                } while(attendedPlayers<2 || attendedPlayers>4); //eccezione da gestire

                //initializing the board with the chosen number of players
                tempBoard.initializeBoard(attendedPlayers);
                System.out.println("...player "+ nickname+ " created a game with "+ attendedPlayers+" players...");

            }


            //once the waiting room (tempPlayers) is full the Game is created and all the players are notified
            if(tempPlayers.size() == attendedPlayers){

                attendedPlayers = -1;

                for(ControlPlayer cp: tempPlayers ){

                     cp.notifyStartPlaying();

                }

                Game g = new Game( tempPlayers , tempBoard );
                games.add( g );
                tempPlayers.clear();

                System.out.println("...The game has been created, participants: "+ g.getPlayers());

            }

            return pl;
        }
    }


    /**
     * Method called by a client who wants to continue a game he was playing before the disconnection/client's crash
     * @param nickname
     * @return GameHandler remote interface
     * @throws RemoteException
     */
    @Override
    public GameHandler continueGame(String nickname, Object client) throws RemoteException, LoginException {

        //checking if exists a player called "nickname" now offline

        Stream<ControlPlayer> cp= null;
        cp = clients.stream()
                    .filter(x -> x.getPlayerNickname().equals(nickname));

        if(cp.count()<1) throw new LoginException("This nickname does not exists");

        if(cp.count()!=1) throw new LoginException("try again");

        ControlPlayer controlPlayer = cp.toList().get(0);

        if( ! controlPlayer.getPlayerStatus().equals(PlayerStatus.NOT_ONLINE) ) throw new LoginException("This nickname results to be still online");

        //if exists I'll create a new one, remove the current ClientHandler-ControlPlayer pair from the map, set a new ClientHandler on the ControlPlayer

        if (client instanceof Socket){

            //SimoSocket : controlPlayer.setSocket() ;

        }
        else if (client instanceof ClientHandler){

            controlPlayer.setClientHandler((ClientHandler) client);

        }

        //searching the correspondent Game and return the GameHandler interface
        return controlPlayer;

    }

    /**
     * method called by a client to leave the game he is playing
     * @return
     */
    @Override
    public boolean leaveGame(String nickname) throws LoginException {


        //checking if exists a player called "nickname" now offline
        Stream<ControlPlayer> cp= null;
        cp = clients.stream()
                    .filter(x -> x.getPlayerNickname().equals(nickname));


        if(cp.count()<1) throw new LoginException("This nickname does not exists");

        if(cp.count()!=1) throw new LoginException("try again");

        ControlPlayer controlPlayer = cp.toList().get(0);

        System.out.println("User "+ controlPlayer.getPlayerNickname());

        //searching the game controlPlayer was playing
        Game g=null;
        for( Game g1: games){

            for (ControlPlayer conti : g1.getPlayers()) {

                if (conti.getPlayerNickname().equals(nickname)) {
                    g = g1;
                    break;
                }
            }
        }


        boolean res = ((g != null )? g.removePlayer(controlPlayer) : false );
        clients.remove(controlPlayer);

        if( res ){
            System.out.println(" successfully ");
        }
        else System.out.println("tried to");

        System.out.println(" leave the game: "+ g.getGameID());

        return res;
    }

    public static ArrayList<Game> getGames() {
        return games;
    }



}
