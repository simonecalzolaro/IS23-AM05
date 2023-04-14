package client;


import controller.ClientServerHandler;
import controller.GameHandler;

import model.Tile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Map;
import java.util.Scanner;

/**
 * Client Application
 */
public class ClientApp extends UnicastRemoteObject implements ClientHandler {

    private Tile board[][];
    private Tile shelf[][];

    private String nickName;
    private boolean myTurn;
    private boolean gameEnded;

    private ClientServerHandler clientServerHandler;
    private GameHandler gameHandler;

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
            this.gameHandler= (GameHandler) registry.lookup("ServerAppService");

        } catch (NotBoundException e) {
            throw new RuntimeException(e);
        }

        do {

            try {

                System.out.println("nickname: ");
                BufferedReader br = new BufferedReader (new InputStreamReader(System.in));
                nickName = br.readLine ();
                System.out.println("-------------------");
                //login of the new player
                clientServerHandler.login(nickName, this );

            }catch (Exception e) {

                System.out.println(e.getMessage());
                e.printStackTrace();
                System.out.println("try again...");
                nickName="";
            }

        }while(nickName.equals(""));

        System.out.println("...you are now in the waiting room..." );

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
    public void updateBoard(Tile[][] board) throws RemoteException{

        for(int row=0; row< board.length; row++){
            for(int col=0; col< board[0].length; col++){
                this.board[row][col]= board[row][col]; //------------- gli elementi sono oggetti, facendo così copio il puntatore, devo usare una clone
            }
        }
        try {
            gameHandler.getMyScore(this);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * method called by the server to show the user an ordered rank of players
     * @param results rank of players and their scores
     * @throws RemoteException
     */
    @Override
    public void showResults(Map< Integer, String> results) throws RemoteException{

        for(Integer key: results.keySet()){
            System.out.println(key + " ->   "+ results.get(key));
        }

    }

    /**
     * method called by the server to end the match
     * @throws RemoteException
     */
    @Override
    public void theGameEnd() throws RemoteException{

        gameEnded=true;

    }

    /**
     * method called by the server to notify the user that his turn has started
     * @throws RemoteException
     */
    @Override
    public void startYourTurn() throws RemoteException{
        myTurn=true;
    }

    /**
     *  method called by the server to notify the user that his turn is ended
     * @throws RemoteException
     */
    @Override
    public void endYourTurn() throws RemoteException{
        myTurn=false;
    }

    /**
     *  method called by the server to notify the user that the match has started
     * @throws RemoteException
     */
    @Override
    public void startPlaying() throws RemoteException {

        System.out.println("let's go!! The game has started ! ");
        System.out.println("is your turn? make a move =) ");
        //qua dentro verrà poi lanciata la gui o la cli

    }

}
