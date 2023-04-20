package client;


import controller.ClientServerHandler;
import controller.GameHandler;

import model.CommonGoalCard;
import model.PersonalGoalCard;
import model.Tile;
import myShelfieException.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Client Application
 */
public class ClientApp extends UnicastRemoteObject implements ClientHandler {

    private Tile board[][];
    private Tile shelf[][];

    private  int pgc;
    private int cgc1;
    private int cgc2;

    private String nickName;
    private boolean myTurn;
    private boolean gameEnded;
    private boolean connectionType;

    //-------------- RMI attributes --------------
    private ClientServerHandler clientServerHandler;
    private GameHandler gameHandler;

    //-------------- socket attributes --------------
    //... ----SimoSocket


    /**
     * constructor of ClientApp
     * @throws RemoteException
     */
    protected ClientApp() throws RemoteException {

        super();

        board = new Tile[9][9]; //------------------------si può fare meglio
        shelf = new Tile[6][5]; //------------------------si può fare meglio

        clientServerHandler=null;
        gameHandler=null;

        nickName = new String();
        myTurn=false;
        gameEnded=false;

    }

    /**
     * main program
     * @param args
     */
    public static void main(String[] args) {

        System.out.println( "Hello from ClientApp" );

        try {

            new ClientApp().startClient();

        } catch (Exception e) {

            e.printStackTrace();

        }

    }

    /**
     * asks user his nickname, load the remote interfaces and log the client into the server
     * @throws RemoteException
     */
    private void startClient() throws RemoteException{

        // Getting the registry
        Registry registry;
        registry = LocateRegistry.getRegistry(Settings.SERVER_NAME, Settings.PORT);
        // Looking up the registry for the remote object
        try {
            this.clientServerHandler = (ClientServerHandler) registry.lookup("ServerAppService");

        } catch (NotBoundException e) {
            throw new RuntimeException(e);
        }

        do {
                int num;
                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

                //-------questa parte andrà migliorata in GUI e TUI

                System.out.println("0 --> socket");
                System.out.println("1 --> RMI");
                Scanner scan = new Scanner(System.in);
                connectionType = (scan.nextInt()%2 == 0) ? false : true;

                do {
                    System.out.println("0 --> start a new game");
                    System.out.println("1 --> continue a Game");
                    num = scan.nextInt();

                }while (num!=0 &&  num!=1);

            try {

                switch (num) {

                    case 0:
                        System.out.println("nickname: ");
                        nickName = br.readLine();
                        System.out.println("-----------------------");

                        //login of the new player
                        this.gameHandler = askLogin();
                        System.out.println("-----login successfully");

                        break;

                    case 1:
                        System.out.println("nickname: ");
                        nickName = br.readLine();
                        System.out.println("-------------------");

                        //login of the new player
                        this.gameHandler = askContinueGame();
                        System.out.println("-----login successfully");
                        break;
                }

            }catch (Exception e) {

                System.out.println(e.getMessage());
                e.printStackTrace();
                System.out.println("try again...");
                nickName="";
            }

        }while(nickName.equals(""));


    }

    public static class Settings {

        static int PORT = 1234;
        static String SERVER_NAME = "127.0.0.1";
    }

    /**
     * method called by the server to ask the first player to entre the number of players he wants in his match
     * @return the chosen number of players
     * @throws RemoteException
     */
    @Override
    public int enterNumberOfPlayers() throws RemoteException{

        int num;
        Scanner scan = new Scanner(System.in);

        do {

            System.out.print("Enter the number of players: ");
            // This method reads the number provided using keyboard
            num = scan.nextInt();

            if(num<2 || num >4) System.out.println("The number of players must be between 2 and 4");

        }while(num<2 || num >4);

        return num;

    }

    /**
     * method called by the server to update the board of this client
     * @param board
     * @throws RemoteException
     */
    @Override
    public boolean updateBoard(Tile[][] board) throws RemoteException{

        for(int row=0; row< board.length; row++){
            for(int col=0; col< board[0].length; col++){
                this.board[row][col]= board[row][col]; //------------- gli elementi sono oggetti, facendo così copio il puntatore, devo usare una clone
            }
        }

         //   gameHandler.getMyScore();
        System.out.println("the board has been updated...");

        return true;

    }


    /**
     * method called by the server to show the user an ordered rank of players
     * and to end the match
     * @throws RemoteException
     */
    @Override
    public boolean theGameEnd(Map< Integer, String> results) throws RemoteException{

        for(Integer key: results.keySet()){
            System.out.println(key + " ->   "+ results.get(key));
        }

        gameEnded=true;

        return true;

    }

    /**
     * method called by the server to notify the user that his turn has started
     * @throws RemoteException
     */
    @Override
    public boolean startYourTurn() throws RemoteException{

        System.out.println(" make a move "+ nickName + " is your turn");
        myTurn=true;
        return true;

    }

    /**
     *  method called by the server to notify the user that his turn is ended
     * @throws RemoteException
     */
    @Override
    public boolean endYourTurn() throws RemoteException{

        myTurn=false;
        return true;

    }

    /**
     *  method called by the server to notify the user that the match has started
     * @throws RemoteException
     */
    @Override
    public boolean startPlaying( int pgc, int cgc1, int cgc2) throws RemoteException {

        System.out.print("\033[H\033[2J");
        System.out.flush();
        System.out.println("let's go!! The game has started ! ");
        System.out.println("is your turn? make a move =) ");

        this.pgc=pgc;
        this.cgc1=cgc1;
        this.cgc2=cgc2;

        //----qua dentro verrà poi lanciata la gui o la cli

        return true;

    }



    //-------------------------------------- RMI vs Socket layer --------------------------------------


    /**
     * asks the server to log in, is divided in RMI and socket
     * @return GameHandler interface
     * @throws LoginException
     * @throws IOException
     * @throws RemoteException
     */
    public GameHandler askLogin() throws LoginException, IOException, RemoteException{

        if(connectionType){
            //RMI calling
            return clientServerHandler.login(nickName, this, true);
        }
        else{
            //socket calling
            return null; //----SimoSocket
        }
    }

    /**
     * asks the server to continue a game, is divided in RMI and socket
     * @return GameHandler interface
     * @throws LoginException
     * @throws RemoteException
     */
    public GameHandler askContinueGame() throws LoginException, RemoteException {

        if(connectionType){
            //RMI calling
            return clientServerHandler.continueGame(nickName, this, true);
        }
        else{
            //socket calling
            return null; //----SimoSocket
        }
    }

    /**
     * asks the server to leave the game I'm playing, is divided in RMI and socket
     * @return true if everything went fine
     * @throws RemoteException
     */
    public boolean askLeaveGame() throws RemoteException {

        if(this.connectionType){
            //RMI calling
            return clientServerHandler.leaveGame(this);
        }
        else{
            //socket calling
            return false; //----SimoSocket
        }
    }

    /**
     * asks the server to leave the game I'm playing, is divided in RMI and socket
     * @param chosenTiles
     * @param coord
     * @return true if everything went fine
     * @throws NotInLineException
     * @throws NotConnectedException
     * @throws NotEnoughSpaceException
     * @throws NotAvailableTilesException
     * @throws InvalidParametersException
     * @throws RemoteException
     * @throws NotMyTurnException
     */
    public boolean askBoardTiles(List<Tile> chosenTiles, List<Integer> coord) throws NotInLineException, NotConnectedException, NotEnoughSpaceException, NotAvailableTilesException, InvalidParametersException, RemoteException, NotMyTurnException {

        if(this.connectionType){
            //RMI calling
            return gameHandler.choseBoardTiles(chosenTiles, coord);
        }
        else{
            //socket calling
            return false; //----SimoSocket
        }
    }

    boolean askInsertShelfTiles(ArrayList<Tile> choosenTiles, int choosenColumn ) throws RemoteException, NotConnectedException, NotMyTurnException, NotEnoughSpaceException, InvalidLenghtException{

        if(this.connectionType){
            //RMI calling
            return gameHandler.insertShelfTiles(choosenTiles, choosenColumn);
        }
        else{
            //socket calling
            return false; //----SimoSocket
        }

    }

    int askGetMyScore() throws RemoteException{

        if(this.connectionType){
            //RMI calling
            return gameHandler.getMyScore();
        }
        else{
            //socket calling
            return -1; //----SimoSocket
        }
    }



}
