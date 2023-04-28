package client;


import controller.ClientServerHandler;
import controller.GameHandler;

import model.CommonGoalCard;
import model.PersonalGoalCard;
import model.Tile;
import myShelfieException.*;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.Socket;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

/**
 * Client Application
 */
public abstract class ClientApp extends UnicastRemoteObject implements ClientHandler {

    static String localhost;
    static Long PORT;
    static int PORTX;
    static Long TCPPORT;
    static int TCPPORTX;

    static Long TCPPORTCP;
    static int TCPPORTCPX;

    Socket socketLobby;
    Socket socketControlPlayer;


    protected Tile board[][];
    protected Tile shelf[][];

    protected  int pgc;
    protected int cgc1;
    protected int cgc2;

    protected String nickName;
    protected boolean myTurn;
    protected boolean gameEnded;
    protected boolean connectionType;

    //-------------- RMI attributes --------------
    protected ClientServerHandler clientServerHandler;
    protected GameHandler gameHandler;

    protected int score;

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
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String read = null;
        int readInt;

        int n=0;

        try{

            //ciclo finche non inserisco i valori corretti



            do{
                System.out.println("Select connection: '0'--> RMI; '1' --> Socket");
                read = in.readLine();

            }while(!read.equals("0") && !read.equals("1"));

            readInt = Integer.parseInt(read);

            //lancio il client con la connessione scela

            switch (readInt){

                case 0:
                    new RMIClient().startClient();
                    break;

                case 1:
                    new SocketClient().startClient();
                    break;



            }


        }catch (IOException e){
            e.printStackTrace();
        }

    }

    /**
     * asks user his nickname, load the remote interfaces and log the client into the server
     * @throws RemoteException
     */


    //DONE
    public abstract void startClient() throws RemoteException;


    public static class Settings {

        static int PORT = 1234;
        static String SERVER_NAME = "127.0.0.1";
    }

    /**
     * method called by the server to ask the first player to entre the number of players he wants in his match
     * @return the chosen number of players
     * @throws RemoteException
     */


    //DONE
    @Override
    public abstract int enterNumberOfPlayers() throws IOException;

    /**
     * method called by the server to update the board of this client
     * @param board
     * @throws RemoteException
     */


    //DONE
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


    //DONE
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
     *
     */

    //DONE
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


    //DONE
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

    //DONE
    public abstract GameHandler askLogin() throws LoginException, IOException, RemoteException;

    /**
     * asks the server to continue a game, is divided in RMI and socket
     * @return GameHandler interface
     * @throws LoginException
     * @throws RemoteException
     */

    //DONE
    public abstract GameHandler askContinueGame() throws LoginException, IOException;


    /**
     * asks the server to leave the game I'm playing, is divided in RMI and socket
     * @return true if everything went fine
     * @throws RemoteException
     */

    //DONE
    public abstract boolean askLeaveGame() throws IOException;


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

    //DONE
    public abstract boolean askBoardTiles(List<Tile> chosenTiles, List<Integer> coord) throws InvalidChoiceException, NotConnectedException, InvalidParametersException, IOException, NotMyTurnException;


    //DONE
    abstract boolean askInsertShelfTiles(ArrayList<Tile> choosenTiles, int choosenColumn, List<Integer> coord) throws IOException, NotConnectedException, NotMyTurnException, InvalidChoiceException, InvalidLenghtException;


    //DONE
    abstract int askGetMyScore() throws IOException;



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



}
