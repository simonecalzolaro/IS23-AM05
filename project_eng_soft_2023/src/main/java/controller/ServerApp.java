package controller;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.util.Scanner;

public class ServerApp {

    public static TCPHandler client_server_bridge;
    public static Lobby lobby;

    /**
     * main program of the server, he will just start the server app with startServer()
     * @param args
     */
    public static void main(String[] args) {

        System.out.println( "Hello from ServerApp!" );

        (new Thread(new Menu())).start();

        Lobby.initializeServer();

        try {
            lobby = new RMILobby();
            lobby.startServer();
        } catch (RemoteException e) {
            System.out.println("ServerApp --- RemoteException occurred while starting a new RMIServer");
            throw new RuntimeException();
        } catch (AlreadyBoundException e) {
            System.out.println("ServerApp --- AlreadyBoundException occurred while starting a nee RMIServer");
            throw new RuntimeException();
        }

        try {
            client_server_bridge = new TCPHandler();
            client_server_bridge.startServer();
        } catch (Exception e) {
            System.out.println("ServerApp --- Unexptected exception occurred trying to start the TCPHandler ");
            throw new RuntimeException();
        }


    }


    static class Menu implements Runnable{

        @Override
        public void run(){

            Scanner scan = new Scanner(System.in);
            String action;
            boolean flag=true;

            while(flag){

                System.out.println("Command: ");

                action = scan.next();

                switch (action){

                    case "/help":
                        System.out.println("/g: to show lobby's games details");
                        System.out.println("/wr: to show waiting room's details");
                        System.out.println("/s: to show server's details");
                        System.out.println("/q: to quit");
                        break;

                    case "/g":
                        showGamesDetails(lobby);
                        break;

                    case "/wr":
                        showWaitingRoomDetails();
                        break;

                    case "/s":
                        showServerDetails();
                        break;

                    case "/q":
                        flag=false;
                        break;
                }
            }
        }


        /**
         * plot lobby's games, games' players and players' details
         * @param lobby from witch collect the information
         */
        public static void showGamesDetails(Lobby lobby){

            System.out.println("number of games: " + lobby.getGames().size());

            for(Game g: lobby.getGames()){

                System.out.println("GAME: " + g.getGameID() + "   status:" + g.getGameStatus());

                System.out.println("number of players: " + g.getPlayers().size());

                for(ControlPlayer cp: g.getPlayers()){

                    System.out.println("    PLAYER: "+cp.getPlayerNickname());
                    System.out.println("         STATUS: "+ cp.getPlayerStatus());
                    System.out.println("          SHELF: "+ cp.getBookshelf());
                    if(cp instanceof RMIControlPlayer){
                        System.out.println("          CONNECTION: RMI");
                    }
                    if(cp instanceof SocketControlPlayer){
                        System.out.println("          CONNECTION: Socket");
                    }
                }
            }

        }


        /**
         * plot server's details
         */
        public static void showServerDetails(){

            System.out.println("all present clients and their status");
            for(ControlPlayer cp : lobby.getClients()){
                System.out.println(cp.getPlayerNickname() + " status: "+ cp.getPlayerStatus());
            }
        }

        /**
         * plot waiting room's details
         */
        public static void showWaitingRoomDetails(){

            System.out.println("WAITING ROOM:");
            System.out.println("attended number of players:"+lobby.getAttendedPlayers());
            System.out.println("current number of players:"+lobby.getWaitingRoom().size());

            for(ControlPlayer cp: lobby.getWaitingRoom()){
                System.out.println("    PLAYER: "+cp.getPlayerNickname());
                if(cp instanceof RMIControlPlayer){
                    System.out.println("    CONNECTION: RMI");
                }
                if(cp instanceof SocketControlPlayer){
                    System.out.println("    CONNECTION: Socket");
                }
            }
        }


    }

}
