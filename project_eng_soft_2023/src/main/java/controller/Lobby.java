package controller;

import client.ClientHandler;
import model.*;
import myShelfieException.*;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.Socket;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.stream.Stream;

/**
 * ServerApp is the running program of the server, it handles the remote clients and
 * all the actions each client wants to take
 */
public abstract class Lobby implements ClientServerHandler {

    static String hostname;
    static Long PORT_pre;
    static int PORT;

    static Object lock;
    protected static ArrayList<ControlPlayer> clients;
    protected static ArrayList< Game > games;
    protected static int attendedPlayers;
    protected static Board tempBoard;
    protected static ArrayList<ControlPlayer> tempPlayers;

    /**
     * constructor for the ServerApp
     * @throws RemoteException
     */
    protected Lobby() throws RemoteException {
        super();
    }


    public static void initializeServer(){

        clients = new ArrayList<>();
        games = new ArrayList<>();
        tempPlayers= new ArrayList<>();
        attendedPlayers = -1;
        tempBoard=null;
        lock = new Object();
    }


    public abstract void startServer() throws RemoteException, AlreadyBoundException;



    //-------------------------------- inherited methods --------------------------------

    /**
     * Method called by the client to log into the server to start a new game.
     * @param nickname is the name chosen by the client, if ti is already used throws IllegalArgumentException
     * @param  client
     * @throws RemoteException
     */
    @Override
    public GameHandler login(String nickname, Object client) throws RemoteException, IOException, LoginException {

        synchronized(lock) {

            //check if the nickname is available
            if (clients.stream().map(x -> x.getPlayerNickname()).toList().contains(nickname))
                throw new LoginException("this nickname is not available at the moment");

            else {

                ControlPlayer pl = null;

                if (attendedPlayers == -1) { //if the isn't any waiting room it means that ch is the first player

                    tempBoard = new Board();
                    System.out.println("\n\n...a new board has been created... " + tempBoard);

                }

                //create a ControlPlayer
                if (client instanceof ArrayList<?>) {
                    pl = new SocketControlPlayer(nickname, tempBoard, (ArrayList<controller.Stream>) client);
                } else if (client instanceof ClientHandler) {
                    pl = new RMIControlPlayer(nickname, tempBoard, (ClientHandler) client);
                } else{
                    throw new IllegalArgumentException("Object client must only be instanceof ArratList<> or ClientHandler");
                }

                if (attendedPlayers == -1) { //if there isn't any waiting room it means that "client" is the first player

                    do {
                        try {
                            //server asks client how many players he wants in his match
                            attendedPlayers = pl.askNumberOfPlayers();
                            System.out.println("-> " + nickname + "chooses " + attendedPlayers + " number of players");

                            //attendedPlayers == -1 when the user is slow
                            if(attendedPlayers==-1){
                                throw new LoginException("OPSSS...you have been too slow, try to login again... ");
                            }

                        } catch (RemoteException e) {
                            attendedPlayers = -1;
                            throw new RuntimeException(e);
                        }
                    } while (attendedPlayers < 2 || attendedPlayers > 4); //eccezione da gestire

                    //setting the status of this Player as MY_TURN
                    pl.setPlayerStatus(PlayerStatus.MY_TURN);

                    //initializing the board with the chosen number of players
                    tempBoard.initializeBoard(attendedPlayers);
                    System.out.println("-> ...player " + nickname + " created a game with " + attendedPlayers + " players...");

                }

                //add to the map "clients" the ClientHandler interface and the associated ControlPlayer
                clients.add(pl);
                //tempPlayers is like a waiting room
                tempPlayers.add(pl);

                System.out.println("-> ...player " + nickname + " entered the game. Waiting room now contains "+ tempPlayers.size()+"/" + attendedPlayers);


                return pl;
            }
        }
    }


    @Override
    public void checkFullWaitingRoom() throws IOException {

        synchronized (lock){
            //once the waiting room (tempPlayers) is full the Game is created and all the players are notified
            if (tempPlayers.size() >= attendedPlayers) {

                attendedPlayers = -1;

                Game g = new Game(tempPlayers, tempBoard);
                games.add(g);

                for (ControlPlayer cp : tempPlayers) {

                    cp.setGame(g);
                    cp.getBookshelf().initializePGC(tempBoard);
                    cp.notifyStartPlaying();

                    if(cp.getPlayerStatus().equals(PlayerStatus.MY_TURN)) cp.notifyStartYourTurn();

                }

                tempPlayers.clear();
                System.out.println(" -> ...The game has been created, participants: " + g.getPlayers());

            }
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

        synchronized(lock){

            //checking if exists a player called "nickname" now offline

            Stream<ControlPlayer> cp= null;
            cp = clients.stream()
                        .filter(x -> x.getPlayerNickname().equals(nickname));

            if(cp.count()<1) throw new LoginException("This nickname does not exists");

            if(cp.count()!=1) throw new LoginException("try again");

            ControlPlayer controlPlayer = cp.toList().get(0);

            if( ! controlPlayer.getPlayerStatus().equals(PlayerStatus.NOT_ONLINE) ) throw new LoginException("This nickname results to be still online");

            //if exists I'll create a new one, remove the current ClientHandler-ControlPlayer pair from the map, set a new ClientHandler on the ControlPlayer

            if (client instanceof ArrayList<?>){

                controlPlayer.setStreams((ArrayList<controller.Stream>) client);

            }
            else if (client instanceof ClientHandler){

                controlPlayer.setClientHandler((ClientHandler) client);

            }

            //searching the correspondent Game and return the GameHandler interface
            return controlPlayer;
        }
    }

    /**
     * method called by a client to leave the game he is playing
     * @return true if the client nickname left the game correctly
     */
    @Override
    public boolean leaveGame(String nickname) throws LoginException, RemoteException {

        synchronized(lock) {

            //checking if exists a player called "nickname" now offline
            Stream<ControlPlayer> cp = null;
            cp = clients.stream()
                    .filter(x -> x.getPlayerNickname().equals(nickname));


            if (cp.count() < 1) throw new LoginException("This nickname does not exists");

            if (cp.count() != 1) throw new LoginException("try again");

            ControlPlayer controlPlayer = cp.toList().get(0);

            System.out.println("User " + controlPlayer.getPlayerNickname());

            //searching the game controlPlayer was playing
            Game g = null;
            for (Game g1 : games) {

                for (ControlPlayer conti : g1.getPlayers()) {

                    if (conti.getPlayerNickname().equals(nickname)) {
                        g = g1;
                        break;
                    }
                }
            }


            boolean res = ((g != null) ? g.removePlayer(controlPlayer) : false);
            clients.remove(controlPlayer);

            if (res) {
                System.out.println(" successfully ");
            } else System.out.println("tried to");

            System.out.println(" leave the game: " + g.getGameID());

            return res;
        }
    }

    @Override
    public boolean pong() throws RemoteException{
        return true;
    }

    //-------------------------------- getter and setter--------------------------------

    /**
     * @return ArrayList of all Game
     */
    public ArrayList<Game> getGames() { return games; }

    /**
     * @return ArrayList of the ControlPlayer now int the waiting room
     */
    public ArrayList<ControlPlayer> getWaitingRoom(){
        return tempPlayers;
    }

    /**
     * @return number of attended Players to play the last Game
     */
    public int getAttendedPlayers(){
        return attendedPlayers;
    }



}
