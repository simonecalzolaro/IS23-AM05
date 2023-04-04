package controller;

import client.ClientHandler;
import model.Board;
import model.Tile;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class ServerApp extends UnicastRemoteObject implements GameHandler, ClientServerHandler {

    private Map<ControlPlayer, ClientHandler> clients;

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

        if( clients.keySet().stream().map(x -> x.getPlayerNickname()).toList().contains(nickname) ) throw new IllegalArgumentException("this nickname is not available");

        else {

            if(attendedPlayers==-1){
                tempBoard = new Board();
                attendedPlayers=ch.enterNUmberOfPlayers();
            }

            ControlPlayer pl= new ControlPlayer(nickname, tempBoard);

            clients.put( pl , ch);
            tempPlayers.add(pl);
            attendedPlayers++;

        }

        return this;

    }

    @Override
    public boolean leaveGame() {
        return false;
    }

    @Override
    public boolean chooseBoardTiles(Map<Tile, ArrayList<Integer>> choosenTiles) {
        return false;
    }

    @Override
    public boolean insertShelfTiles(ArrayList<Tile> choosenTiles, int choosenColumn) {
        return false;
    }

    @Override
    public int getMyScore() {
        return 0;
    }
}
