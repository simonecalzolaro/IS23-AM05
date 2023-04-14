package controller;

import client.ClientHandler;
import model.*;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * ServerApp is the running program of the server, it handles the remote clients and
 * all the actions each client wants to take
 */
public class ServerApp extends UnicastRemoteObject implements GameHandler, ClientServerHandler {

    private Map<ClientHandler, ControlPlayer> clients;
    private ArrayList< Game > games;
    private static int attendedPlayers;
    private Board tempBoard;
    private ArrayList<ControlPlayer> tempPlayers;

    /**
     * constructor for the ServerApp
     * @throws RemoteException
     */
    protected ServerApp() throws RemoteException {

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

            new ServerApp().startServer();

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
    public void login(String nickname, ClientHandler ch) throws RemoteException{

        if( clients.values().stream().map(x -> x.getPlayerNickname()).toList().contains(nickname) ) throw new IllegalArgumentException("this nickname is not available at the moment");

        else {

            if(attendedPlayers==-1){ //if the isn't any waiting room it means that ch is the first player

                tempBoard = new Board();
                System.out.println("...a new board has been created...");

                do{
                    try {
                        //server asks client how many players he wants in his match
                        attendedPlayers=ch.enterNumberOfPlayers();
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
            System.out.println("...player "+ nickname+ " entered the game ");

            //add to the map "clients" the ClientHandler interface and the associated ControlPlayer
            clients.put(ch, pl);
            //tempPlayers is like a waiting room
            tempPlayers.add(pl);

            //once the waiting room (tempPlayers) is full the Game is created and all the players are notified
            if(tempPlayers.size() == attendedPlayers){

                attendedPlayers = -1;
                games.add( new Game( tempPlayers , tempBoard ) );
                tempPlayers.clear();


                //Game g = (Game) games.stream().filter(x->x.getPlayers().contains(clients.get(ch))) ;
                Game g =games.get(games.size()-1);

                for(ControlPlayer player: g.getPlayers()){
                    try {

                        ClientHandler clih=getKey(clients, player);
                        clih.updateBoard(g.getBoard().getBoard());
                        clih.startPlaying();

                    } catch (RemoteException e) { throw new RuntimeException(e); }
                }

                System.out.println("...The game has been created, participants: "+ tempPlayers);

            }
        }
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
     * method called by the clients to choose tiles from the board of their game
     * @param chosenTiles List of tiles chosen by the client
     * @param coord coordinates of the chosen tiles
     * @param ch client interface
     * @return true if the chosen tiles are valid
     */
    @Override
    public boolean chooseBoardTiles(List<Tile> chosenTiles, List<Integer> coord, ClientHandler ch) throws NotInLineException, NotConnectedException , NotEnoughSpaceException , NotAvailableTilesException , InvalidParametersException, NotMyTurnException{

        if (clients.get(ch).catchTile(coord).equals(chosenTiles)){

            Game g= (Game)games.stream().filter(x->x.getPlayers().contains(clients.get(ch)));

            for (ControlPlayer cp: g.getPlayers() ) {

               try{
                   getKey(clients, cp).updateBoard(g.getBoard().getBoard());
               }catch (Exception e){
                   return false;
               }

            }
            return true;
        }
        else return false;


    }

    /**
     * method called by clients to insert the tiles they choose from the board into their bookshelf
     * @param chosenTiles ordered list of tiles, from the lowest one to the top one
     * @param choosenColumn column where to insert the tiles
     * @param ch client interface
     * @throws NotConnectedException
     * @throws NotMyTurnException
     */
    @Override
    public void insertShelfTiles(ArrayList<Tile> chosenTiles, int choosenColumn , ClientHandler ch) throws NotConnectedException, NotMyTurnException {

        //inserting the tiles in the bookshelf
        try {
            if( ! clients.get(ch).insertTiles(chosenTiles, choosenColumn)) return ;
        } catch (NotEnoughSpaceException e) {
            throw new RuntimeException(e);
        }

        //tell to ch that his turn is completed
        try {
            ch.endYourTurn();
        } catch (RemoteException e) { throw new RuntimeException(e); }

       //Game.endTurn() is called to update the turn to the next player and the game status
        Game g= (Game)games.stream().filter(x->x.getPlayers().contains(clients.get(ch)));
        g.endTurn();


        ControlPlayer nextPlayer= g.getPlayers().get(g.getCurrPlayer());
        ClientHandler nextClient=getKey(clients, nextPlayer);

        //notify the updated board to all the clients participating in the same game of ch
        for(ControlPlayer player: g.getPlayers()){
            try {
                getKey(clients, player).updateBoard(g.getBoard().getBoard());
            } catch (RemoteException e) { throw new RuntimeException(e); }
        }

        //if the game is ended every player is notified and the results are shown
        if(g.getGameStatus().equals(GameStatus.END_GAME)){

            for(ControlPlayer player: g.getPlayers()){
                try {
                    ClientHandler clih=getKey(clients, player);
                    assert clih != null;//-------------
                    clih.theGameEnd();
                    clih.showResults(getGameResults(g));
                } catch (RemoteException e) { throw new RuntimeException(e); }
            }
        }

        try {
            nextClient.startYourTurn();
        } catch (RemoteException e) { throw new RuntimeException(e); }

    }

    /**
     * @param ch
     * @return the score of the player ch
     * @throws RemoteException
     */
    @Override
    public int getMyScore( ClientHandler ch ) throws RemoteException{

        return clients.get(ch).getBookshelf().getMyScore();

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
     * @param game
     * @return a map of ordered players from the player with the highest score to the player with the lowest
     */
    private static Map<Integer, String> getGameResults(Game game){

        ArrayList<Integer> scores = game.getPlayers().stream()
                                        .map(x->x.getBookshelf().getMyScore())
                                        .collect(Collectors.toCollection(ArrayList::new));

        ArrayList<String> nicks = game.getPlayers().stream()
                                        .map(x->x.getPlayerNickname())
                                        .collect(Collectors.toCollection(ArrayList::new));

        Map<Integer, String> res= new HashMap<>();

        for(int i=0; i<scores.size(); i++){
            res.put(scores.get(i), nicks.get(i));
        }

        return res.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors
                            .toMap(
                            Map.Entry::getKey,
                            Map.Entry::getValue,
                            (oldValue, newValue) -> oldValue, LinkedHashMap::new));


    }

}

