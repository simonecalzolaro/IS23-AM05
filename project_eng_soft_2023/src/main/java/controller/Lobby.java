package controller;

import client.ClientHandler;
import model.*;
import myShelfieException.*;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.stream.Stream;

/**
 * ServerApp is the running program of the server, it handles the remote clients and
 * all the actions each client wants to take
 */
public class Lobby extends UnicastRemoteObject implements ClientServerHandler {

    private Map<ClientHandler, ControlPlayer> clients;
    private ArrayList< Game > games;
    private static int attendedPlayers;
    private Board tempBoard;
    private ArrayList<ControlPlayer> tempPlayers;

    /**
     * constructor for the ServerApp
     * @throws RemoteException
     */
    protected Lobby() throws RemoteException {

        super();

        clients = new HashMap<>();
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

            new Lobby().startServer();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * start the server app and create a registry "ServerAppService" bound with "this" object
     * @throws RemoteException
     */
    private void startServer() throws RemoteException {

        Registry register = LocateRegistry.createRegistry(Settings.PORT);

        try {
            register.bind("ServerAppService", this);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Server ready");

    }

    public static class Settings {
        static int PORT = 1234;
        static String SERVER_NAME = "127.0.0.1";
    }

    /**
     * Method called by the client to log into the server to start a new game.
     * @param nickname is the name chosen by the client, if ti is already used throws IllegalArgumentException
     * @param ch is the client interface. I need it to associate each client interface to a ControlPlayer
     * @throws RemoteException
     */
    @Override
    public GameHandler login(String nickname, ClientHandler ch, Boolean connectionType) throws RemoteException, IOException, LoginException {

        if( clients.values().stream().map(x -> x.getPlayerNickname()).toList().contains(nickname) ) throw new LoginException("this nickname is not available at the moment");

        else {

            if(attendedPlayers==-1){ //if the isn't any waiting room it means that ch is the first player

                tempBoard = new Board();
                System.out.println("...a new board has been created...");

                do{
                    try {
                        //server asks client how many players he wants in his match
                        attendedPlayers=askNumberOfPlayers(ch, connectionType );
                    } catch (RemoteException e) {
                        attendedPlayers = -1;
                        throw new RuntimeException(e);
                    }
                } while(attendedPlayers<2 || attendedPlayers>4);

                //initializing the board with the chosen number of players
                tempBoard.initializeBoard(attendedPlayers);
                System.out.println("...player "+ nickname+ " created a game with "+ attendedPlayers+" players...");

            }

            //create a ControlPlayer
            ControlPlayer pl= new ControlPlayer(nickname, tempBoard);
            pl.setClientHandler(ch);
            pl.setConnectionType(connectionType);
            System.out.println("...player "+ nickname+ " entered the game ");

            //add to the map "clients" the ClientHandler interface and the associated ControlPlayer
            clients.put(ch, pl);
            //tempPlayers is like a waiting room
            tempPlayers.add(pl);

            //once the waiting room (tempPlayers) is full the Game is created and all the players are notified
            if(tempPlayers.size() == attendedPlayers){

                attendedPlayers = -1;
                Game g = new Game( tempPlayers , tempBoard );
                games.add( g );
                tempPlayers.clear();


                notifyStartPlaying(g, connectionType);

                System.out.println("...The game has been created, participants: "+ g.getPlayers());

            }

            return pl;
        }
    }

    /**
     * Method called by a client who wants to continue a game he was playing before the disconnection/client's crash
     * @param nickname
     * @param ch ClientHandler of the remote User
     * @return GameHandler remote interface
     * @throws RemoteException
     */
    @Override
    public GameHandler continueGame(String nickname, ClientHandler ch, Boolean connectionType) throws RemoteException, LoginException {

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
        controlPlayer.setConnectionType(connectionType);
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
     * @param connectionType
     * @return the chosen number of players
     * @throws RemoteException
     */
    public int askNumberOfPlayers(ClientHandler ch, Boolean connectionType) throws RemoteException {

        if(connectionType){ //if RMI connection type
            //RMI calling
            return ch.enterNumberOfPlayers();

        }
        else{
            //socket calling
            return -1; //---SimoSocket,  metodo per chiedere all'utente

        }

    }

    /**
     * this method tells to all users that the game has started and that they aren't anymore in the waiting room, is divided in RMI and socket
     * @param g
     * @param connectionType
     * @throws RemoteException
     */
    public void notifyStartPlaying(Game g, Boolean connectionType) throws RemoteException {

        for(ControlPlayer player: g.getPlayers()){
            try {
                if(connectionType){ //if RMI connection type
                    //RMI calling
                    player.setGame(g);
                    ClientHandler clih=player.getClientHandler();

                    clih.startPlaying(player.getBookshelf().getPgc().getCardNumber(), g.getBoard().getCommonGoalCard1().getCGCnumber(), g.getBoard().getCommonGoalCard2().getCGCnumber());
                    clih.updateBoard(g.getBoard().getBoard()); //----------timer che aspetta il return true

                    if(g.getPlayers().get(g.getCurrPlayer()).equals(player)) clih.startYourTurn();
                }
                else{

                    //socket calling
                    //---SimoSocket,  metodo per chiedere all'utente

                }

            } catch (RemoteException e) { throw new RuntimeException(e); }
        }


    }


}
