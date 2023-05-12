package controller;

import java.util.Scanner;

public class ServerApp {

    static Lobby lobby;
    static TCPHandlerLobby SocketLobby;

    /**
     * main program of the server, he will just start the server app with startServer()
     * @param args
     */
    public static void main(String[] args) {

        System.out.println( "Hello from ServerApp!" );

        try {

            lobby = new RMIServer();
            lobby.startServer();

            //SocketLobby = new TCPHandlerLobby();
            //new Thread(() -> SocketLobby.startServer()); //---- potrebbe dare problemi con gli indirizzi di memoria (parla con Simo)
            //SocketLobby.startServer();

           // new Thread(() -> new TCPHandlerLobby());

        } catch (Exception e) {
            e.printStackTrace();
        }

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

        System.out.println("server active?!?!  I don't know what else to write");

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
