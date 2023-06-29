package controller;

import myShelfieException.LoginException;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.util.Scanner;


/**
 * Main class of the server
 */
public class ServerApp {

    public static TCPHandler client_server_bridge;
    public static Lobby lobby;

    /**
     * main program of the server, he will just start the server app with startServer()
     * @param args
     */
    public static void main(String[] args) {

        System.out.println("+-----------------------------+");
        System.out.println("|    Hello from ServerApp!    |" );
        System.out.println("+-----------------------------+");


        (new Thread(new Menu())).start();

        Lobby.initializeServer();

        try {
            lobby = new RMILobby();
            lobby.startServer();
            new Thread(()-> lobby.checkFullWaitingRoom()).start();
            new Thread(()-> {
                    lobby.checkAskNumberOfPlayers();
            }).start();

        } catch (RemoteException e) {
            System.out.println("ServerApp --- RemoteException occurred while starting a new RMIServer");
            throw new RuntimeException();
        } catch (AlreadyBoundException e) {
            System.out.println("ServerApp --- AlreadyBoundException occurred while starting a nee RMIServer");
            throw new RuntimeException();
        }


        try {
            client_server_bridge = new TCPHandler(lobby);
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
            int num;
            boolean flag=true;

            while(flag){

                System.out.println("Command: ");

                action = scan.next();

                switch (action){

                    case "/help":
                        System.out.println("/g: to show lobby's games ");
                        System.out.println("/gd: to show game G details");
                        System.out.println("/wr: to show waiting room's details");
                        System.out.println("/s: to show server's details");
                        System.out.println("/q: to quit");
                        break;

                    case "/g":
                        showGamesDetails(lobby);
                        break;

                    case "/gd":

                        System.out.println(" choose your game:");
                        int i=0;
                        for(Game g: lobby.getGames()){
                            System.out.println(i + ": to show game "+ g.getGameID());
                        }
                        System.out.println("->");
                        num = scan.nextInt();
                        if(num >=0 && num<lobby.getGames().size()) {
                            Game g = lobby.getGames().get(num);
                            showGamesDetails(lobby, g);
                        }
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

                System.out.println("\nGAME: " + g.getGameID() + "   status:" + g.getGameStatus());

                System.out.println("number of players: " + g.getPlayers().size());

                for(ControlPlayer cp: g.getPlayers()){

                    System.out.println("   PLAYER: "+cp.getPlayerNickname());
                    System.out.println("         STATUS: "+ cp.getPlayerStatus());
                    System.out.println("         SHELF : "+ cp.getBookshelf());
                    System.out.println("         SCORE : "+cp.getBookshelf().getMyScore());
                    if(cp instanceof RMIControlPlayer){
                        System.out.println("          CONNECTION: RMI");
                    }
                    if(cp instanceof SocketControlPlayer){
                        System.out.println("          CONNECTION: Socket");
                    }
                }
            }

        }

        public static void showGamesDetails(Lobby lobby, Game g){

            System.out.println("    common board:");
            plotBoard(g.getBoard());

            for(ControlPlayer cp: g.getPlayers()){

                System.out.println("    player " +cp.getPlayerNickname() );
                System.out.println("        status: "+cp.getPlayerStatus());
                System.out.println("        bookshelf: ");
                plotShelf(cp.getBookshelf());

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



    private enum ColorCLI{
        RESET("\u001B[0m"),

        GREEN("\u001B[32m"),
        BLUE("\u001B[34m"),
        WHITE("\u001B[37m"),
        PINK("\u001B[35m"),
        YELLOW("\u001B[33m"),
        LIGHTBLUE("\u001B[36m"),

        BLACK_BACKGROUND("\u001B[40m"),
        GREEN_BACKGROUND("\u001B[42m"),
        BLUE_BACKGROUND("\u001B[44m"),
        WHITE_BACKGROUND("\u001B[47m"),
        PINK_BACKGROUND("\u001B[45m"),
        YELLOW_BACKGROUND("\u001B[43m"),
        LIGHTBLUE_BACKGROUND("\u001B[46m");

        private final String color;

        ColorCLI(String color){
            this.color = color;
        }

        public String toString(){
            return color;
        }
    }


    public static boolean plotBoard(model.Board b) {

        StringBuilder strBoard = new StringBuilder();
        int temp=0;

        model.Tile[][] matr=b.getBoard();

        for(int row=0; row< matr.length ; row++){
            for(int col=0; col< matr.length; col++){

                if(row!=temp) {
                    temp++;
                    strBoard.append("\n");
                }

                switch (matr[row][col]){
                    case GREEN -> strBoard.append(ColorCLI.GREEN_BACKGROUND).append(row).append(";").append(col).append(" ").append(ColorCLI.RESET);
                    case BLUE -> strBoard.append(ColorCLI.BLUE_BACKGROUND).append(row).append(";").append(col).append(" ").append(ColorCLI.RESET);
                    case WHITE -> strBoard.append(ColorCLI.WHITE_BACKGROUND).append(row).append(";").append(col).append(" ").append(ColorCLI.RESET);
                    case PINK -> strBoard.append(ColorCLI.PINK_BACKGROUND).append(row).append(";").append(col).append(" ").append(ColorCLI.RESET);
                    case YELLOW -> strBoard.append(ColorCLI.YELLOW_BACKGROUND).append(row).append(";").append(col).append(" ").append(ColorCLI.RESET);
                    case LIGHTBLUE -> strBoard.append(ColorCLI.LIGHTBLUE_BACKGROUND).append(row).append(";").append(col).append(" ").append(ColorCLI.RESET);
                    case EMPTY, NOTAVAILABLE -> strBoard.append("    ").append(ColorCLI.RESET);
                }
            }
        }

        System.out.println(strBoard.toString());

        return true;
    }

    public static boolean plotShelf(model.Bookshelf bookshelf) {

        StringBuilder strShelf = new StringBuilder();
        int temp=5;

        model.Tile[][] matr=bookshelf.getShelf();

        for(int row=5; row>=0; row--){
            for(int col=0; col< 5; col++){

                if(row!=temp) {
                    temp--;
                    strShelf.append("\n");
                }

                switch (matr[row][col]){

                    case GREEN -> strShelf.append(ColorCLI.GREEN_BACKGROUND).append(row).append(";").append(col).append(" ").append(ColorCLI.RESET);
                    case BLUE -> strShelf.append(ColorCLI.BLUE_BACKGROUND).append(row).append(";").append(col).append(" ").append(ColorCLI.RESET);
                    case WHITE -> strShelf.append(ColorCLI.WHITE_BACKGROUND).append(row).append(";").append(col).append(" ").append(ColorCLI.RESET);
                    case PINK -> strShelf.append(ColorCLI.PINK_BACKGROUND).append(row).append(";").append(col).append(" ").append(ColorCLI.RESET);
                    case YELLOW -> strShelf.append(ColorCLI.YELLOW_BACKGROUND).append(row).append(";").append(col).append(" ").append(ColorCLI.RESET);
                    case LIGHTBLUE -> strShelf.append(ColorCLI.LIGHTBLUE_BACKGROUND).append(row).append(";").append(col).append(" ").append(ColorCLI.RESET);
                    case EMPTY, NOTAVAILABLE -> strShelf.append("    ").append(ColorCLI.RESET);
                }

                if(col==4) strShelf.append("|");
            }
        }

        System.out.println(strShelf.toString());

        return true;
    }

    public static boolean plotPGC() {

        /*
        StringBuilder strPGC = new StringBuilder();

        int temp=5;

        for(int row=5; row>=0; row--){
            for(int col=0; col<5; col++){

                if(row!=temp) {
                    temp--;
                    strPGC.append("\n");
                }

                if(client.getModel().getPgc().getTileFromMap(Tile.GREEN)[0]==row &&  client.getModel().getPgc().getTileFromMap(Tile.GREEN)[1]==col){
                    strPGC.append("|").append(TUI.ColorCLI.GREEN_BACKGROUND).append(row).append(";").append(col).append(TUI.ColorCLI.RESET);
                }else if(client.getModel().getPgc().getTileFromMap(Tile.BLUE)[0]==row &&  client.getModel().getPgc().getTileFromMap(Tile.BLUE)[1]==col){
                    strPGC.append("|").append(TUI.ColorCLI.BLUE_BACKGROUND).append(row).append(";").append(col).append(TUI.ColorCLI.RESET);
                } else if (client.getModel().getPgc().getTileFromMap(Tile.WHITE)[0]==row &&  client.getModel().getPgc().getTileFromMap(Tile.WHITE)[1]==col) {
                    strPGC.append("|").append(TUI.ColorCLI.WHITE_BACKGROUND).append(row).append(";").append(col).append(TUI.ColorCLI.RESET);
                }else if(client.getModel().getPgc().getTileFromMap(Tile.PINK)[0]==row &&  client.getModel().getPgc().getTileFromMap(Tile.PINK)[1]==col){
                    strPGC.append("|").append(TUI.ColorCLI.PINK_BACKGROUND).append(row).append(";").append(col).append(TUI.ColorCLI.RESET);
                }else if(client.getModel().getPgc().getTileFromMap(Tile.YELLOW)[0]==row &&  client.getModel().getPgc().getTileFromMap(Tile.YELLOW)[1]==col){
                    strPGC.append("|").append(TUI.ColorCLI.YELLOW_BACKGROUND).append(row).append(";").append(col).append(TUI.ColorCLI.RESET);
                }else if(client.getModel().getPgc().getTileFromMap(Tile.LIGHTBLUE)[0]==row &&  client.getModel().getPgc().getTileFromMap(Tile.LIGHTBLUE)[1]==col){
                    strPGC.append("|").append(TUI.ColorCLI.LIGHTBLUE_BACKGROUND).append(row).append(";").append(col).append(TUI.ColorCLI.RESET);
                }else{
                    strPGC.append("|   ").append(TUI.ColorCLI.RESET);
                }
                if(col==4) strPGC.append("|");
            }
        }

        System.out.println(strPGC.toString());

        return true;

         */
        return true;
    }


}