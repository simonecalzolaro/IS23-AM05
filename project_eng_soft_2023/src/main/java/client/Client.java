package client;


import controller.ClientServerHandler;
import controller.GameHandler;

import model.Tile;
import myShelfieException.*;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Timestamp;
import java.util.*;

/**
 * Client Application
 */
public abstract class Client extends UnicastRemoteObject implements ClientHandler, Serializable {

    //----- tutti sti attributi sono da spostare in classi o sottoclassi più specifiche
    static String localhost;
    static Long PORT;
    static int PORTX;
    static Long TCPPORT;
    static int TCPPORTX;
    static Long TCPPORTCP;
    static int TCPPORTCPX;



    protected ClientModel model;
    protected boolean myTurn;
    protected boolean gameEnded;


    /**
     * constructor of ClientApp
     * @throws RemoteException
     */
    protected Client() throws RemoteException {

        super();

        model=new ClientModel();
        myTurn=false;
        gameEnded=false;

        getServerSettings();

    }

    public void startClient(){


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
    public boolean updateBoard(Tile[][] board, Tile[][] myShelf, Map<String, Tile[][]> otherShelf) throws RemoteException{

        Map<String, Matrix> otherPlayersMatr= new HashMap<>();
        for(String nick: otherShelf.keySet()){
            otherPlayersMatr.put(nick, new Matrix(otherShelf.get(nick)));
        }

        model.initializeMatrixes(new Matrix(board), new Matrix(myShelf), otherPlayersMatr );


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

        System.out.println(" make a move "+ model.getNickname() + " is your turn");
        myTurn=true;
        return true;

    }


    /**
     *  method called by the server to notify the user that his turn is ended
     * @throws RemoteException
     */
    @Override
    public boolean endYourTurn() throws RemoteException{

        System.out.println("...your turn is ended ! ");
        myTurn=false;
        return true;

    }


    /**
     *  method called by the server to notify the user that the match has started
     * @throws RemoteException
     */
    @Override
    public boolean startPlaying(int pgcNum, Map<Tile, Integer[]> pgcMap, int cgc1num, int cgc2num) throws RemoteException {


        model.initializeCards(new Matrix(pgcMap), pgcNum, cgc1num, cgc2num );

        //----UImethod()
        System.out.print("\033[H\033[2J");
        System.out.flush();
        System.out.println("let's go!! The game has started ! ");
        System.out.println("is your turn? make a move =) ");

        return true;

    }


    /**
     * @return always true
     * @throws RemoteException RMI exception
     */
    @Override
    public boolean pong() throws RemoteException {

        System.out.println("    pong");
        return true;
    }



    /**
     * set up the servers' ports and hostname
     */
    public void getServerSettings(){

        try{
            Object o = new JSONParser().parse(new FileReader("header.json"));
            JSONObject j =(JSONObject) o;
            Map arg = new LinkedHashMap();
            arg = (Map) j.get("serverSettings");

            localhost = (String) arg.get("hostname");
            PORT = (Long) arg.get("PORT");

            PORTX = PORT.intValue();

            TCPPORT = (Long) arg.get("TCPPORT");
            TCPPORTX = TCPPORT.intValue();
            TCPPORTCP = (Long) arg.get("TCPPORTCP");
            TCPPORTCPX = TCPPORTCP.intValue();

            System.out.println(localhost+" "+PORTX);

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @return clientModel
     */
    public ClientModel getModel(){
        return model;
    }

    /**
     * @return true if is your turn
     */
    public boolean isMyTurn() {
        return myTurn;
    }

    /**
     * @return true if game ended
     */
    public boolean GameEnded() {
        return gameEnded;
    }

    //-------------------------------------- RMI vs Socket layer --------------------------------------


    /**
     * asks the server to log in, is divided in RMI and socket
     * @return GameHandler interface
     * @throws LoginException
     * @throws IOException
     * @throws RemoteException
     */
    public abstract void askLogin(String nick) throws LoginException, IOException, RemoteException;


    /**
     * asks the server to continue a game, is divided in RMI and socket
     * @return GameHandler interface
     * @throws LoginException
     * @throws RemoteException
     */
    public abstract void askContinueGame() throws LoginException, IOException;


    /**
     * asks the server to leave the game I'm playing, is divided in RMI and socket
     * @return true if everything went fine
     * @throws RemoteException
     */
    public abstract boolean askLeaveGame() throws IOException, LoginException;


    /**
     * asks the server to leave the game I'm playing, is divided in RMI and socket
     * @param chosenTiles
     * @param coord
     * @return true if everything went fine
     * @throws InvalidChoiceException
     * @throws NotConnectedException
     * @throws InvalidParametersException
     * @throws RemoteException
     * @throws NotMyTurnException
     */
    public abstract boolean askBoardTiles(List<Tile> chosenTiles, List<Integer> coord) throws InvalidChoiceException, NotConnectedException, InvalidParametersException, IOException, NotMyTurnException;


    /**
     * asks the server to insert some tiles in my shelf, is divided in RMI and socket
     * @param choosenTiles are the tiles to insert in a column
     * @param choosenColumn is the column where to insert the tiles
     * @param coord board coordinates
     * @return true if everything went fine
     * @throws IOException
     * @throws NotConnectedException
     * @throws NotMyTurnException
     * @throws InvalidChoiceException
     * @throws InvalidLenghtException
     */
    abstract boolean askInsertShelfTiles(ArrayList<Tile> choosenTiles, int choosenColumn, List<Integer> coord) throws IOException, NotConnectedException, NotMyTurnException, InvalidChoiceException, InvalidLenghtException;


    /**
     * asks the server my current score, is divided in RMI and socket
     * @return the score
     * @throws IOException
     */
    abstract int askGetMyScore() throws IOException;

    /**
     * verifies the server is up
     * @return true if the server is online
     */
    abstract boolean askPing();

}
