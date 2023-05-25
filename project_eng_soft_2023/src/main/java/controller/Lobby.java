package controller;

import client.ClientHandler;
import model.*;
import myShelfieException.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;

import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Stream;


/**
 * ServerApp is the running program of the server, it handles the remote clients and
 * all the actions each client wants to take
 */
public abstract class Lobby implements  ClientServerHandler {

    static String hostname;
    static Long PORT_pre;
    static int PORT;
    static int currNoP=0;


    private static Object nowLoggingClient;
    protected static ArrayList<ControlPlayer> clients;
    protected static ArrayList< Game > games;
    protected static int attendedPlayers;
    protected static Board tempBoard;
    protected static ArrayList<ControlPlayer> tempPlayers;

    private static boolean flagNoP;
    private static boolean flagLogin;
    private static boolean flagWR;


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
        flagLogin=true;
        flagNoP=false;
        flagWR=false;

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
    public synchronized GameHandler login(String nickname, Object client) throws RemoteException, IOException, LoginException {

        //questo while è a mutua esclusione con quello all'interno di checkFullWaitingRoom()

        while ( !flagLogin ) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        flagLogin=false;

        nowLoggingClient=client;

        //check if the nickname is available
        if (tempPlayers.stream().map(x -> x.getPlayerNickname()).toList().contains(nickname))
            throw new LoginException("this nickname is not available at the moment");

        else {

            ControlPlayer pl = null;

            //se la waiting room è vuota o non è ancora stato chiesto il numero di giocatori
            if (tempBoard==null) {
                tempBoard = new Board();
                System.out.println("\n  ...a new board has been created... " + tempBoard);
            }

            try {

                //create a ControlPlayer
                if (client instanceof ArrayList<?>) {
                    pl = new SocketControlPlayer(nickname, (ArrayList<controller.Stream>) client);
                } else if (client instanceof ClientHandler) {
                    pl = new RMIControlPlayer(nickname, (ClientHandler) client);
                } else {
                    throw new IllegalArgumentException("Object client must only be instanceof ArratList<> or ClientHandler");
                }

            }finally {
                if(pl==null) return null;
            }


            //add to the map "clients" the ClientHandler interface and the associated ControlPlayer
            clients.add(pl);
            //tempPlayers is like a waiting room
            //adding the player to the waiting room, so now tempPlayers.size() è uguale a old( tempPlayers.size() ) + 1
            tempPlayers.add(pl);
            pl.setPlayerStatus(PlayerStatus.WAITING_ROOM);
            System.out.println("-->player " + nickname + " entered the game. Waiting room now contains " + tempPlayers.size() + "/" + (attendedPlayers<0? "0": attendedPlayers));

            flagNoP=true;
            notifyAll();

            return pl;
        }
    }


    public synchronized void checkAskNuberOfPlayers(){

        System.out.println("-> checkAskNuberOfPlayers active");
        //if there isn't any waiting room it means that "client" is the first player
        while(true) {

                while(!flagNoP){
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                flagNoP=false;

                if (attendedPlayers == -1 ) {

                    attendedPlayers = -2;
                    ControlPlayer pl = tempPlayers.get(0);

                    //server asks client how many players he wants in his match
                    System.out.println("    ...asking the number of players to "+pl.getPlayerNickname());
                    pl.askNumberOfPlayers();
                   // System.out.println("-> " + pl.getPlayerNickname() + " chooses " + attendedPlayers + " number of players");

                    //setting the status of this Player as nOfPlayerAsked
                    pl.setPlayerStatus(PlayerStatus.nOfPlayerAsked);

                }

                flagWR=true;
                notifyAll();
        }
    }

    /**
     * loop function to check the status of the waiting room.
     * If the waiting room size is equal to the number of attended players a new Game starts and all the players are notified
     */
    public synchronized void checkFullWaitingRoom() {

        System.out.println("-> checkFullWaitingRoom active");

        while(true) {

            while( !flagWR){
                try {
                    wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            flagWR=false;


            //System.out.println("... checkFullWaitingRoom, tempPlayers.size():"+ tempPlayers.size() + ", attendedPlayers:"+attendedPlayers);
            //once the waiting room (tempPlayers) is full the Game is created and all the players are notified
            if (tempPlayers.size() >= attendedPlayers && attendedPlayers>0) {

                System.out.println("    ...Loading game , participants: " + tempPlayers.stream().map(ControlPlayer::getPlayerNickname));

                try {

                    ArrayList<ControlPlayer> newPlayers= new ArrayList<>();

                    //creating a list with the first "attendedPlayer" players in tempPlayers
                    for(int i=0; i<attendedPlayers; i++){
                        newPlayers.add(tempPlayers.get(0));
                        tempPlayers.remove(0);
                    }

                    //initializing the board with the chosen number of players
                    tempBoard.initializeBoard(attendedPlayers);
                    System.out.println("    ...creating a game with " + attendedPlayers + " players...");


                    Game g = new Game( newPlayers, tempBoard);
                    games.add(g);

                    //initializing each client, this for CAN'T be inside the next one otherwise when notifyUpdatedBoard()
                    //is launched for the first client the others are not initialized yet (NULL pointer exc.)
                    for (ControlPlayer cp : g.getPlayers()) {
                        cp.initializeControlPlayer(tempBoard);
                        cp.setGame(g);
                        cp.getBookshelf().initializePGC(tempBoard);
                    }


                    for (ControlPlayer cp : g.getPlayers()) {

                        try {
                            cp.notifyUpdatedBoard();
                            cp.notifyStartPlaying();
                            if (cp.equals(newPlayers.get(0))){
                                cp.setPlayerStatus(PlayerStatus.MY_TURN);
                                cp.notifyStartYourTurn();
                            }
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    System.out.println("    ...The game has been created, participants: " + g.getPlayers().stream().map(x -> x.getPlayerNickname()));

                } catch (IOException e) {
                    System.out.println("----Exception occurred while initializing a Game");
                    e.printStackTrace();
                }

                tempBoard=null;
                attendedPlayers = -1;

            }

            flagLogin=true;
            notifyAll();

        }
    }

    /**
     * Method called by a client who wants to continue a game he was playing before the disconnection/client's crash
     * @param nickname
     * @return GameHandler remote interface
     * @throws RemoteException
     */
    @Override
    public synchronized GameHandler continueGame(String nickname, Object client) throws RemoteException, LoginException {

            //checking if exists a player called "nickname" now offline
            Stream<ControlPlayer> cp= null;
            cp = clients.stream()
                        .filter(x -> x.getPlayerNickname().equals(nickname));

            if(cp.count()<1) throw new LoginException("This nickname does not exists");

            if(cp.count()!=1) throw new LoginException("try again");

            ControlPlayer controlPlayer = cp.toList().get(0);

            if( ! controlPlayer.getPlayerStatus().equals(PlayerStatus.NOT_ONLINE) ) throw new LoginException("This nickname results to be still online");

            //if exists I'll create a new one, remove the current ClientHandler-ControlPlayer pair from the map, set a new ClientHandler on the ControlPlayer

            if (client instanceof ArrayList<?>) {

                controlPlayer.setStreams((ArrayList<controller.Stream>) client);

            }
            else if (client instanceof ClientHandler){

                controlPlayer.setClientHandler((ClientHandler) client);

            }

            //searching the correspondent Game and return the GameHandler interface
            return controlPlayer;

    }

    /**
     * method called by a client to leave the game he is playing
     * @return true if the client nickname left the game correctly
     */
    @Override
    public synchronized void leaveGame(String nickname, int ID) throws LoginException, RemoteException {

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

    }


    /**
     * method to set the number of players
     * @param n number of players the players wants
     * @param nick nickname of the client
     * @throws RemoteException
     */
    @Override
    public void setNumberOfPlayers(int n, String nick) throws RemoteException{

        //searching the controlPlayer called "nick" int the waiting room and if I found him I'll set attendedPlayers to n
        //System.out.println("...setting new number of players...");
        if(tempPlayers.get(0).getPlayerNickname().equals(nick) && tempPlayers.size()>0){
            if (n>=2 && n<=4) {
                attendedPlayers = n;
                System.out.println("--> new number of attendedPlayers:"+attendedPlayers);
                tempPlayers.get(0).setPlayerStatus(PlayerStatus.WAITING_ROOM);
            }
        }
    }


    @Override
    public void pong(String nickname, int gameID) throws RemoteException{


        if(gameID<=0 ){ // if gameId is less than 0 it means that we still are in the waiting room
            for(ControlPlayer cp: tempPlayers){
                if(cp.getPlayerNickname().equals(nickname) ){
                    cp.getPingClass().setConnected();
                    return;
                }
            }
        }else{
            for(ControlPlayer cp: clients){
                if(cp.getPlayerNickname().equals(nickname) && gameID==cp.getGame().getGameID()){
                    cp.getPingClass().setConnected();
                    return;
                }
            }
        }
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

    /**
     * @return clients present in the lobby
     */
    public static ArrayList<ControlPlayer> getClients() {
        return clients;
    }

    /**
     * removes Player cp from his waiting room and, if cp is the only one present, the waiting room il reinitialized
     * @param cp ControlPlayer to be removed from the waiting room
     */
    public synchronized void removeFromWaitingRoom (ControlPlayer cp){

        //se il primo se ne va ancor prima di chedergli in nuber Of PLayers:
        if(cp.equals(tempPlayers.get(0))){
            attendedPlayers=-1;
        }

        tempPlayers.remove(cp);
        clients.remove(cp);

        //se non è rimasto nessuno nella waiting room :
        if(tempPlayers.size()==0){
            attendedPlayers=-1;
            return;
        }

        //se il primo se ne va ma da lui mi aspettavo il NoP:
        if(cp.getPlayerStatus().equals(PlayerStatus.nOfPlayerAsked)){
            attendedPlayers = -2;
            tempPlayers.get(0).setPlayerStatus(PlayerStatus.nOfPlayerAsked);
            tempPlayers.get(0).askNumberOfPlayers();
        }
    }
}
