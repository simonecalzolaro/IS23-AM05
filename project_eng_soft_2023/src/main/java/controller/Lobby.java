package controller;

import client.ClientHandler;
import model.*;
import myShelfieException.*;

import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;

import java.util.*;
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
    private static ArrayList<ControlPlayer> clients;
    private static ArrayList< Game > games;
    private static int attendedPlayers;
    private static Board tempBoard;
    private static ArrayList<ControlPlayer> tempPlayers;

    private static boolean flagLogin;
    private static boolean flagNoP;
    private static boolean flagWR;


    /**
     * constructor for the ServerApp
     * @throws RemoteException
     */
    protected Lobby() throws RemoteException {
        super();
    }

    /**
     * initialize the Lobby's server
     */
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

        while ( flagNoP || flagWR ) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

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

            //once the waiting room (tempPlayers) is full the Game is created and all the players are notified
            flagWR=true;
            flagNoP=true;
            //flagLogin=false;

            notifyAll();
            return pl;
        }

    }

    /**
     * Method to check if the server has to ask a client the number of players he wants in his game.
     * It is always executed immediately after the login() method.
     */
    public synchronized void checkAskNumberOfPlayers(){

        System.out.println("-> checkAskNuberOfPlayers active");
        //if there isn't any waiting room it means that "client" is the first player
        while(true) {

            System.out.println("->while checkAskNuberOfPlayers");

            while(!flagNoP){
                try {
                    wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            if (attendedPlayers == -1 && tempPlayers.size()>0 ) {

                attendedPlayers = -2;
                ControlPlayer pl = tempPlayers.get(0);

                //server asks client how many players he wants in his match
                pl.askNumberOfPlayers();
                System.out.println("    ...asking the number of players to "+pl.getPlayerNickname());
               // System.out.println("-> " + pl.getPlayerNickname() + " chooses " + attendedPlayers + " number of players");

                //setting the status of this Player as nOfPlayerAsked
                pl.setPlayerStatus(PlayerStatus.nOfPlayerAsked);

            }

            flagNoP=false;
            notifyAll();
        }
    }

    /**
     * If the waiting room size is equal to the number of attended players a new Game starts and all the players are notified
     * It is always executed immediately after the checkAskNumberOfPlayers() method.
     */
    public synchronized void checkFullWaitingRoom() {

        System.out.println("-> checkFullWaitingRoom active");


        while(true) {

            System.out.println("->while checkFullWaitingRoom");

            while( !flagWR){
                try {
                    wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            if (tempPlayers.size() >= attendedPlayers && attendedPlayers>0){

                System.out.println("    ...Loading game , participants: " + tempPlayers.stream().map(ControlPlayer::getPlayerNickname));

                try {

                    ArrayList<ControlPlayer> newPlayers = new ArrayList<>();

                    //creating a list with the first "attendedPlayer" players in tempPlayers
                    for (int i = 0; i < attendedPlayers; i++) {
                        newPlayers.add(tempPlayers.get(0));
                        tempPlayers.remove(0);
                    }

                    //initializing the board with the chosen number of players
                    tempBoard.initializeBoard(attendedPlayers);
                    System.out.println("    ...creating a game with " + attendedPlayers + " players...");


                    Game g = new Game(newPlayers, tempBoard);
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
                            if (cp.equals(newPlayers.get(0))) {
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

                tempBoard = null;
                attendedPlayers = -1;

            }

            flagWR=false;
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
    public synchronized GameHandler continueGame(String nickname, Object client, int ID) throws RemoteException, LoginException {

        //checking if exists a player called "nickname" now offline inside Game signed ID
        Game myGame=null;
        for(Game g: games){
            if(g.getGameID()==ID){
                myGame=g;
                break;
            }
        }

        if(myGame==null){
            throw new LoginException("---error: occurred in continueGame(), does not exists Game ID="+ID);
        }

        for(ControlPlayer cp: myGame.getPlayers()){
            if(cp.getPlayerNickname().equals(nickname)){
                if(cp.getPlayerStatus().equals(PlayerStatus.NOT_ONLINE) ){
                    if (client instanceof ArrayList<?>) {

                        cp.setStreams((ArrayList<controller.Stream>) client);

                    }
                    else if (client instanceof ClientHandler){

                        cp.setClientHandler((ClientHandler) client);

                    }
                    else{
                        return null;
                    }
                    cp.setPlayerStatus(PlayerStatus.NOT_MY_TURN);

                    return cp;
                }else{
                    throw new LoginException("This nickname results to be still online");
                }
            }
        }
        throw new LoginException("---error: occurred in continueGame(), nickname "+nickname+" does not exists inside Game ID="+ID);

    }

    /**
     * method called by a client to leave the game he is playing
     * @return true if the client nickname left the game correctly
     */
    @Override
    public synchronized void leaveGame(String nickname, int ID) throws LoginException, RemoteException {

        if(ID==-1){
            ControlPlayer myPlayer=getPlayerFromNickInWaitingRoom(nickname);
            if(myPlayer!=null){
                removeFromWaitingRoom(myPlayer);
            }
            return;

        }

        //checking if exists a player called "nickname" now offline inside Game ID
        Game myGame=getGameByID(ID);
        if(myGame==null){
            throw new LoginException("---error: does not exists Game ID="+ID);
        }

        ControlPlayer myPlayer=myGame.getPlayerByNickname(nickname);
        if(myPlayer==null){
            throw new LoginException("---error: does not exists player ''"+nickname+"'' inside Game ID="+ID);
        }

        System.out.println("--> player "+ nickname+ " wants to leave the Game ID="+ID);

        /*
        if( !myGame.getGameStatus().equals(GameStatus.SUSPENDED)) quitGameIDandNotify(myGame);
        else{
            myGame.removePlayer(myPlayer);
            myPlayer.getPingClass().stopPingProcess();
            if(myGame.getPlayers().size()<2){
                myGame.removePlayer(myGame.getPlayers().get(0));
                myPlayer.getPingClass().stopPingProcess();
                games.remove(myGame);
            }
        }*/

        switch (myGame.getGameStatus()){

            case PLAYING, SUSPENDED: {

                quitGameIDandNotify(myGame);

                break;
            }

            case END_GAME :{

                myGame.removePlayer(myPlayer);
                myPlayer.getPingClass().stopPingProcess();
                clients.remove(myPlayer);

                if(myGame.getPlayers().size()<2){
                    myGame.removePlayer(myGame.getPlayers().get(0));
                    myPlayer.getPingClass().stopPingProcess();
                    clients.remove(myPlayer);
                    games.remove(myGame);
                }

                break;
            }
        }
    }


    /**
     * method to set the number of players
     * @param n number of players the players wants
     * @param nick nickname of the client
     * @throws RemoteException
     */
    @Override
    public synchronized void setNumberOfPlayers(int n, String nick) throws RemoteException{

        //searching the controlPlayer called "nick" int the waiting room and if I found him I'll set attendedPlayers to n
        //System.out.println("...setting new number of players...");
        if(tempPlayers.get(0).getPlayerNickname().equals(nick) && tempPlayers.get(0).getPlayerStatus().equals(PlayerStatus.nOfPlayerAsked)  && tempPlayers.size()>0){
            if (n>=2 && n<=4) {
                attendedPlayers = n;
                System.out.println("--> new number of attendedPlayers:"+attendedPlayers);
                tempPlayers.get(0).setPlayerStatus(PlayerStatus.WAITING_ROOM);
                flagWR=true;
                notifyAll();
            }
            else throw new IllegalArgumentException("---error: invalid number of attendedPlayers");
        }
        else throw new IllegalArgumentException("---error: invalid action");
    }

    /**
     * method called by the client to notify the server that he received the ping
     * @param nickname: nickname of the client that notify the ping
     * @param gameID: the game's ID he is part of
     * @throws RemoteException
     */
    @Override
    public void pong(String nickname, int gameID) throws RemoteException{

        //System.out.println("pong() from "+nickname);
        if(gameID<=0 ){ // if gameId is less than 0 it means that we are still in the waiting room
            for(ControlPlayer cp: tempPlayers){
                if(cp.getPlayerNickname().equals(nickname) ){
                    //System.out.println("*** setConnected()");
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

    public Game getGameByID(int ID){
        for(Game g: games){
            if( g.getGameID()==ID ){
                return g;
            }
        }
        return null;
    }
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
     * @param nick: nickname of the player I want
     * @return the ControlPlayer obj with nickname equals to "nick"
     */
    public ControlPlayer getPlayerFromNickInWaitingRoom(String nick){

        for(ControlPlayer cp: tempPlayers){
            if(cp.getPlayerNickname().equals(nick)) return cp;
        }

        return null;
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
        cp.getPingClass().stopPingProcess();

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

    /**
     * interrupt game "myGame" and tells to all the players that the game is ended, remove each player from "clients" and
     * at the end delete myGame from "games".
     * @param myGame: the Game I want to interrupt
     */
    public void quitGameIDandNotify(Game myGame){

        for(ControlPlayer cp: myGame.getPlayers()){
            try {
                cp.getPingClass().stopPingProcess();
                cp.notifyEndGame();
            }catch(Exception e){
                System.out.println("---error: something went wrong while notifying the end of the game:"+myGame.getGameID()+" to "+cp.getPlayerNickname());
            }
        }

        int size=myGame.getPlayers().size();
        for(int i=0; i<size; i++){
            try {
                clients.remove(myGame.getPlayers().get(0));
                myGame.removePlayer(myGame.getPlayers().get(0));
            }catch(Exception e){
                System.out.println("---error: something went wrong while removing a player from game:"+myGame.getGameID());
            }
        }

        System.out.println("game ID="+myGame.getGameID()+" removed from lobby");
        games.remove(myGame);

    }

}
