package client;

import controller.GameHandler;
import model.Tile;
import myShelfieException.*;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import view.View;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.*;

public class SocketClient extends Client{

    Socket socketClient;

    private Stream outClient;
    private Stream inClient;

    static JSONObject jsonIn = null;

    private boolean isPaused;


    /**
     * constructor of ClientApp
     *
     * @throws RemoteException
     */
    public SocketClient(View view) throws RemoteException {

        super( view);
    }

    @Override
    public void initializeClient() throws IOException {

        System.out.println("--- Initializing the SocketClient ...");


        //Getting server's information about IPAddress and PORT
        getServerSettings();
        isPaused = false;

        socketClient = new Socket(hostname,PORT);

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
     * @throws LoginException
     * @throws IOException
     * @throws RemoteException
     */
    @Override
    public synchronized void askLogin(String nick) throws LoginException, IOException, RemoteException {

        JSONObject object = new JSONObject();

        object.put("Action","Method");
        object.put("Method","login");
        object.put("Param1",nick);

        try{
            outClient.reset();
            outClient.write(object);
        } catch (InvalidOperationException e) {
            System.out.println("SocketClient --- InvalidOperationException occurred in askLogin() trying to reset/write the stream");
            System.out.println("---> Maybe you're trying to reset/write an input stream");
            throw new RuntimeException();
        }


        redLight();

        while(isPaused){
            try {
                wait();
            } catch (InterruptedException e) {
                System.out.println("SocketClient --- InterruptedException occurred trying to wait the execution flow");
                throw new RuntimeException();
            }
        }



    }



    public synchronized void askCheckFullWaitingRoom() throws IOException {

        JSONObject object = new JSONObject();

        object.put("Action","Method");
        object.put("Method","checkFullWaitingRoom");

        try{
            outClient.reset();
            outClient.write(object);
        } catch (InvalidOperationException e) {
            System.out.println("SocketClient --- InvalidOperationException occurred in askLogin() trying to reset/write the stream");
            System.out.println("---> Maybe you're trying to reset/write an input stream");
            throw new RuntimeException();
        }

        redLight();

        while(isPaused){
            try {
                wait();
            } catch (InterruptedException e) {
                System.out.println("SocketClient --- InterruptedException occurred trying to wait the execution flow");
                throw new RuntimeException();
            }
        }
    }





    /**
     * asks the server to continue a game, is divided in RMI and socket
     * @return GameHandler interface
     * @throws LoginException
     * @throws RemoteException
     */

    @Override
    public synchronized void askContinueGame() throws LoginException, IOException {

    }


    /**
     * asks the server to leave the game I'm playing, is divided in RMI and socket
     * @return true if everything went fine
     * @throws RemoteException
     */

    @Override
    public synchronized void askLeaveGame() throws IOException {

        JSONObject object = new JSONObject();

        object.put("Action","Method");
        object.put("Method","leaveGame");
        object.put("Param1",model.getNickname());

        try{
            outClient.reset();
            outClient.write(object);
        } catch (InvalidOperationException e) {
            System.out.println("SocketClient --- InvalidOperationException occurred trying to reset/write the stream");
            System.out.println("---> Maybe you're trying to reset/write an input stream");
            throw new RuntimeException();
        }


        redLight();

        while(isPaused){
            try {
                wait();
            } catch (InterruptedException e) {
                System.out.println("SocketClient --- InterruptedException occurred trying to wait the execution flow");
                throw new RuntimeException();
            }
        }

        //return left; --- SimoSocket

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
    public synchronized void askBoardTiles( List<Integer> coord) throws InvalidChoiceException, NotConnectedException, InvalidParametersException, IOException, NotMyTurnException {

        JSONObject object = new JSONObject();

        object.put("Action","Method");
        object.put("Method","chooseBoardTiles");
        //object.put("Param1",chosenTiles); ---- SimoSocket
        object.put("Param2",coord);

        try{
            outClient.reset();
            outClient.write(object);
        } catch (InvalidOperationException e) {
            System.out.println("SocketClient --- InvalidOperationException occurred in trying to reset/write the stream");
            System.out.println("---> Maybe you're trying to reset/write an input stream");
            throw new RuntimeException();
        }


        redLight();

        while(isPaused){
            try {
                wait();
            } catch (InterruptedException e) {
                System.out.println("SocketClient --- InterruptedException occurred trying to wait the execution flow");
                throw new RuntimeException();
            }
        }


    }



    @Override
    public synchronized void askInsertShelfTiles( int choosenColumn, List<Integer> coord) throws IOException, NotConnectedException, NotMyTurnException, InvalidChoiceException, InvalidLenghtException{

        JSONObject object = new JSONObject();

        object.put("Action","Method");
        object.put("Method","insertShelfTiles");
        //object.put("Param1",choosenTiles); ---SimoSocket
        object.put("Param2",choosenColumn);
        object.put("Param3",coord);

        try{
            outClient.reset();
            outClient.write(object);
        } catch (InvalidOperationException e) {
            System.out.println("SocketClient --- InvalidOperationException occurred in trying to reset/write the stream");
            System.out.println("---> Maybe you're trying to reset/write an input stream");
            throw new RuntimeException();
        }


        redLight();

        while(isPaused){
            try {
                wait();
            } catch (InterruptedException e) {
                System.out.println("SocketClient --- InterruptedException occurred trying to wait the execution flow");
                throw new RuntimeException();
            }
        }

    }

    @Override
    public void askSetNumberOfPlayers(int n, String nick) {
        //---SimoSocket
    }

    @Override
    public void askPassMyTurn() {
//---SimoSocket
    }



    @Override
    public void getServerSettings() {

        try{

            Long PORT_pre;

            Object o = new JSONParser().parse(new FileReader("header.json"));
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


    public synchronized void redLight(){
        isPaused = true;
    }

    public synchronized void greenLight(){
        isPaused = false;
        notifyAll();
    }


}
