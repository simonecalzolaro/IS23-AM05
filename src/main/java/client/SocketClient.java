package client;

import myShelfieException.*;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.*;

public class SocketClient extends Client{

    Socket socketClient;

    private Stream outClient;
    private Stream inClient;

    static JSONObject jsonIn = null;

    private boolean isPaused;


    /**
     * constructor of SocketClient
     *
     * @throws RemoteException
     */
    public SocketClient() throws RemoteException {

        super();
        model.setConnectionType(true);
    }


    /**
     * This method initializes the connection with the server creating the socket binding between itself and the server
     * and instantiating the input/output stream for communicate
     * @throws IOException when the streams are reset or corrupted
     */
    @Override
    public void initializeClient() throws IOException {

        System.out.println("--- Initializing the SocketClient ...");


        //Getting server's information about IPAddress and PORT
        getServerSettings();
        isPaused = false;
        boolean goon = false;
        boolean firstAttempt = true;

        while(!goon){
            try{
                socketClient = new Socket(hostname,PORT);
                goon = true;
            } catch (Exception e) {
                if(firstAttempt) System.out.println("Server is down ---> Wait for reconnection or close the application");
                firstAttempt = false;
                goon = false;
            }
        }


        try{
            outClient = new Stream(socketClient,0);
        } catch (InvalidParametersException e) {
            System.out.println("SocketClient --- InvalidParameterException occurred trying to create the output stream");
            System.out.println("---> Change it with a valid one");
            throw new RuntimeException();
        }

        try{
            inClient = new Stream(socketClient,1);
        } catch (InvalidParametersException e) {
            System.out.println("SocketClient --- InvalidParameterException occurred trying to create the input stream");
            System.out.println("---> Change it with a valid one");
            throw new RuntimeException();
        }

        Thread inputReader = new Thread(new AsyncClientInput(this));
        inputReader.start();

        System.out.println("--- SocketClient ready ---");


    }


    /**
     * asks the server to log in, is divided in RMI and socket
     * @return GameHandler interface
     * @throws IOException when the streams are corrupted
     * @throws RemoteException occurs in SocketClient
     */
    @Override
    public synchronized void askLogin(String nick) throws IOException, RemoteException {

        JSONObject object = new JSONObject();

        object.put("Interface","ClientServerHandler");
        object.put("Action","login");
        object.put("Param1",nick);

        try{
            outClient.reset();
            outClient.write(object);
        } catch (InvalidOperationException e) {
            System.out.println("SocketClient --- InvalidOperationException occurred in trying to reset/write the stream");
            System.out.println("---> Maybe you're trying to reset/write an input stream");
            throw new RuntimeException();
        }

    }








    /**
     * asks the server to continue a game, is divided in RMI and socket
     * @return GameHandler interface
     */

    @Override
    public synchronized void askContinueGame() {

        JSONObject object = new JSONObject();

        object.put("Interface","ClientServerHandler");
        object.put("Action","continueGame");
        object.put("Param1",model.getNickname());
        object.put("Param2",model.getGameID());

        try{
            outClient.reset();
            outClient.write(object);
        } catch (InvalidOperationException | IOException e) {
            System.out.println("SocketClient --- InvalidOperationException occurred in trying to reset/write the stream");
            System.out.println("---> Maybe you're trying to reset/write an input stream");
            throw new RuntimeException();
        }

    }


    /**
     * asks the server to leave the game I'm playing, is divided in RMI and socket
     * @throws RemoteException not handled in SocketClient
     */

    @Override
    public void askLeaveGame() throws RemoteException{

        JSONObject object = new JSONObject();

        object.put("Interface","ClientServerHandler");
        object.put("Action","leaveGame");
        object.put("Param1",model.getNickname());
        object.put("Param2",model.getGameID());

        try{
            outClient.reset();
            outClient.write(object);
        } catch (InvalidOperationException | IOException e) {
            System.out.println("SocketClient --- InvalidOperationException occurred in trying to reset/write the stream");
            System.out.println("---> Maybe you're trying to reset/write an input stream");
            throw new RuntimeException();
        }
    }



    /**
     * asks the server to leave the game I'm playing, is divided in RMI and socket
     * @param coord
     * @return true if everything went fine
     * @throws InvalidChoiceException
     * @throws NotConnectedException
     * @throws InvalidParametersException
     * @throws RemoteException
     * @throws NotMyTurnException
     */

    @Override
    public void askBoardTiles( List<Integer> coord) throws RemoteException {

        JSONObject object = new JSONObject();

        object.put("Interface","GameHandler");
        object.put("Action","chooseBoardTiles");
        object.put("Param1",coord);
      ;

        try{
            outClient.reset();
            outClient.write(object);
        } catch (InvalidOperationException | IOException e) {
            System.out.println("SocketClient --- InvalidOperationException occurred in trying to reset/write the stream");
            System.out.println("---> Maybe you're trying to reset/write an input stream");
            throw new RuntimeException();
        }

    }


    /**
     * Communicates the server the chosen tiles and the columns of the shelf
     * @param choosenColumn is the column where to insert the tiles
     * @param coord board coordinates
     * @throws RemoteException not handled in SocketClient
     */
    @Override
    public void askInsertShelfTiles( int choosenColumn, List<Integer> coord) throws RemoteException{

        JSONObject object = new JSONObject();

        object.put("Interface","GameHandler");
        object.put("Action","insertShelfTiles");
        object.put("Param1",choosenColumn);
        object.put("Param2",coord);

        try{
            outClient.reset();
            outClient.write(object);
        } catch (InvalidOperationException | IOException e) {
            System.out.println("SocketClient --- InvalidOperationException occurred in trying to reset/write the stream");
            System.out.println("---> Maybe you're trying to reset/write an input stream");
            throw new RuntimeException();
        }


    }


    /**
     * Communicates the server to pass the turn
     */
    @Override
    public void askPassMyTurn() {


        JSONObject object = new JSONObject();

        object.put("Interface","GameHandler");
        object.put("Action","passMyTurn");


        try{
            outClient.reset();
            outClient.write(object);
        } catch (InvalidOperationException | IOException e) {
            System.out.println("SocketClient --- InvalidOperationException occurred in trying to reset/write the stream");
            System.out.println("---> Maybe you're trying to reset/write an input stream");
            throw new RuntimeException();
        }

    }


    /**
     * Communicated the number of players to the server
     * @param n chosen number of players
     * @param nick your nickname
     */
    @Override
    public void askSetNumberOfPlayers(int n, String nick) {

        JSONObject object = new JSONObject();
        object.put("Interface","ClientServerHandler");
        object.put("Action","setNumberOfPlayers");
        object.put("Param1",n);
        object.put("Param2",nick);

        try{
            outClient.reset();
            outClient.write(object);
        } catch (InvalidOperationException | IOException e) {
            System.out.println("SocketClient --- InvalidOperationException occurred trying to reset/write the stream");
            System.out.println("---> Maybe you're trying to reset/write an input stream");
            throw new RuntimeException();
        }

    }


    /**
     * Notify the pong to the client
     */
    @Override
    public void notifyPong() {

        //System.out.println("*** socket notifyPong()");

        JSONObject object = new JSONObject();
        object.put("Interface","ClientServerHandler");
        object.put("Action","pong");
        object.put("Param1",model.getNickname());
        object.put("Param2",model.getGameID());

        try{
            outClient.reset();
            outClient.write(object);
        } catch (InvalidOperationException | IOException e) {
            System.out.println("SocketClient --- InvalidOperationException occurred trying to reset/write the stream");
            System.out.println("---> Maybe you're trying to reset/write an input stream");
            throw new RuntimeException();
        }


    }


    /**
     * Send the message to the server which will deliver it to the right client/clients
     * @param Message string to send to the
     * @param recipients 
     */
    @Override
    public void askPostMessage(String Message, ArrayList<String> recipients) {

        JSONObject object = new JSONObject();
        object.put("Interface","GameHandler");
        object.put("Action","postMessage");
        object.put("Param1",Message);
        object.put("Param2", recipients);

        try{
            outClient.reset();
            outClient.write(object);
        } catch (InvalidOperationException | IOException e) {
            System.out.println("SocketClient --- InvalidOperationException occurred trying to reset/write the stream");
            System.out.println("---> Maybe you're trying to reset/write an input stream");
            throw new RuntimeException();
        }
    }


    @Override
    public void getServerSettings() {

        try{

            Long PORT_pre;
            Object o = new JSONParser().parse(new FileReader(System.getProperty("user.dir")+"/config/header.json"));
            JSONObject j =(JSONObject) o;
            Map arg = new LinkedHashMap();
            arg = (Map) j.get("serverSettings");

            hostname = (String) arg.get("hostname");
            PORT_pre = (Long) arg.get("TCPPORT");

            PORT = PORT_pre.intValue();

        } catch (FileNotFoundException e) {
            System.out.println("SocketClient --- FileNotFoundException occurred trying to retrieve server's information from header.json");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("SocketClient --- IOException occurred trying to retrieve server's information from header.json");
            e.printStackTrace();
        } catch (ParseException e) {
            System.out.println("SocketClient --- ParseException occurred trying to retrieve server's information from header.json");
            e.printStackTrace();
        }
    }



    public Stream getInputStream(){
        return inClient;
    }

    public Stream getOutputStream(){
        return outClient;
    }





}
