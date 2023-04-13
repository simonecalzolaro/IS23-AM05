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

public class ServerApp extends UnicastRemoteObject implements GameHandler, ClientServerHandler {

    private Map<ClientHandler, ControlPlayer> clients;

    private ArrayList< Game > games;

    private int attendedPlayers;
    private Board tempBoard;
    private ArrayList<ControlPlayer> tempPlayers;

    protected ServerApp() throws RemoteException {

        clients = new HashMap<>();
        games = new ArrayList<>();
        tempPlayers= new ArrayList<>();
        attendedPlayers = -1;

    }

    public static void main(String[] args) {

        System.out.println( "Hello from ServerApp!" );

        try {
            new ServerApp().startServer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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

    @Override
    public GameHandler login(String nickname, ClientHandler ch) {

        if( clients.values().stream().map(x -> x.getPlayerNickname()).toList().contains(nickname) ) throw new IllegalArgumentException("this nickname is not available at the moment");

        else {

            if(attendedPlayers==-1){

                tempBoard = new Board();

                do{
                    attendedPlayers=ch.enterNUmberOfPlayers();
                }while(attendedPlayers<2 || attendedPlayers>4);
            }

            System.out.println("player "+ nickname+ ", create game with"+ attendedPlayers);

            ControlPlayer pl= new ControlPlayer(nickname, tempBoard);

            clients.put(ch, pl);
            tempPlayers.add(pl);

            if(tempPlayers.size()==attendedPlayers){

                attendedPlayers = -1;
                games.add(new Game(tempPlayers, tempBoard ));
                tempPlayers.clear();
            }
        }

        return this;
    }

    @Override
    public boolean leaveGame(ClientHandler ch) {

        System.out.println("User "+ clients.get(ch).getPlayerNickname());
        clients.remove(ch);

        Game g= (Game)games.stream().filter(x->x.getPlayers().contains(clients.get(ch)));

        boolean res = g.removePlayer( clients.get(ch));

        if( res ) System.out.println(" successfully ");
        else System.out.println("tried to");

        System.out.println(" leave the game:"+ games.stream()
                                                   .filter(x->x.getPlayers().contains(clients.get(ch)))
                                                   .map(x -> x.getGameID()));

        return res;
    }

    @Override
    public boolean chooseBoardTiles(List<Tile> chosenTiles, List<Integer> coord, ClientHandler ch) {

        try {

            if( clients.get(ch).catchTile(coord).equals(chosenTiles) )return true;
            else return false;

        } catch (NotInLineException | NotConnectedException | NotEnoughSpaceException | NotAvailableTilesException |
                 InvalidParametersException | NotMyTurnException e) {

            return false;
        }

    }

    @Override
    public boolean insertShelfTiles(ArrayList<Tile> choosenTiles, int choosenColumn , ClientHandler ch) throws NotEnoughSpaceException, InvalidLenghtException {

        return clients.get(ch).getBookshelf().putTiles(choosenTiles, choosenColumn);

    }

    @Override
    public int getMyScore( ClientHandler ch ) {

        return clients.get(ch).getBookshelf().getMyScore();

    }
}
