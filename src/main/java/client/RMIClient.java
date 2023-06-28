package client;

import controller.ClientServerHandler;
import controller.GameHandler;
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
import java.util.*;

/**
 * this class extends the mor generic class Client, overriding its methods
 * to an RMI connection type.
 */
public class RMIClient extends Client {


    //-------------- RMI attributes --------------
    private ClientServerHandler clientServerHandler;
    private GameHandler gameHandler;
    private Registry registry;

    /**
     * constructor of RMIClientApp
     * @throws RemoteException
     */

    public RMIClient() throws RemoteException {

        super();
        model.setConnectionType(false);

    }


    @Override
    public void initializeClient() throws IOException, NotBoundException {

        //System.out.println("--- initialize the RMI Client --- ");

        getServerSettings();
        boolean goon = false;
        boolean firstAttempt = true;

        // Getting the registry


        while(!goon){
            try{

                registry = LocateRegistry.getRegistry(hostname, PORT);

                // Looking up the registry for the remote object
                this.clientServerHandler = (ClientServerHandler) registry.lookup("ServerAppService");

                goon = true;
            } catch (Exception e) {
                if(firstAttempt) System.out.println("Server is down ---> Wait for reconnection or close the application");
                firstAttempt = false;
                goon = false;
            }
        }

    }

    @Override
    public void getServerSettings() {

        Long PORT_pre;

        try{
            Object o = new JSONParser().parse(new FileReader(System.getProperty("user.dir")+"/config/header.json"));
            JSONObject j =(JSONObject) o;
            Map arg = new LinkedHashMap();
            arg = (Map) j.get("serverSettings");

            hostname = (String) arg.get("hostname");
            PORT_pre = (Long) arg.get("RMIPORT");

            PORT = PORT_pre.intValue();

        }catch (FileNotFoundException e) {
            System.out.println("RMIClient --- FileNotFoundException occurred trying to retrieve server's information from header.json");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("RMIClient --- IOException occurred trying to retrieve server's information from header.json");
            e.printStackTrace();
        } catch (ParseException e) {
            System.out.println("RMIClient --- ParseException occurred trying to retrieve server's information from header.json");
            e.printStackTrace();
        }

    }


    /**
     * asks the server to log in, is divided in RMI and socket
     * @throws LoginException
     * @throws IOException
     * @throws RemoteException
     */
    @Override
    public void askLogin(String nick) throws LoginException, IOException, RemoteException{

        try{
            this.gameHandler= clientServerHandler.login(nick , this);
        }catch (RemoteException e){
            getView().showException("ops... something went wrong while Login");
            throw e;
        }catch (LoginException e){
            getView().showException(e.getMessage());
            throw e;
        }
    }


    /**
     * asks the server to continue a game, is divided in RMI and socket
     * @throws LoginException
     * @throws RemoteException
     */
    @Override
    public void askContinueGame() throws LoginException, RemoteException {

        this.gameHandler= clientServerHandler.continueGame(model.getNickname(), this, model.getGameID());

        gameHandler.restoreSession();

    }


    /**
     * asks the server to leave the game I'm playing, is divided in RMI and socket
     * @throws RemoteException
     */
    @Override
    public void askLeaveGame() throws RemoteException, LoginException {

        clientServerHandler.leaveGame(model.getNickname(), model.getGameID());
        super.getPingChecker().stopPingProcess();


    }


    /**
     * asks the server to leave the game I'm playing, is divided in RMI and socket
     * @param coord
     * @throws InvalidChoiceException
     * @throws NotConnectedException
     * @throws InvalidParametersException
     * @throws RemoteException
     * @throws NotMyTurnException
     */
    @Override
    public void askBoardTiles( List<Integer> coord) throws InvalidChoiceException, NotConnectedException, InvalidParametersException, RemoteException, NotMyTurnException {

        gameHandler.chooseBoardTiles( coord );

    }

    @Override
    public void askInsertShelfTiles( int choosenColumn, List<Integer> coord) throws RemoteException, NotConnectedException, NotMyTurnException, InvalidChoiceException, InvalidLenghtException{

        gameHandler.insertShelfTiles( choosenColumn, coord);
    }

    @Override
    public void askSetNumberOfPlayers(int n, String nick) {

        try {
            clientServerHandler.setNumberOfPlayers(n, nick);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void askPassMyTurn() {

        try {
            System.out.println(" timeout!");
            gameHandler.passMyTurn();
        } catch (RemoteException e) {
            System.out.println("---error occurred when asking to skip the turn");
            throw new RuntimeException(e);
        }

    }

    @Override
    public void notifyPong() throws RemoteException {

        //System.out.println("notifyPong()");
        clientServerHandler.pong(model.getNickname(), model.getGameID());

    }

    @Override
    public void askPostMessage(String message, ArrayList<String> recipients){
        try {
            gameHandler.postMessage(message, recipients);
        } catch (RemoteException e) {
            System.out.println("---error occurred while posting the message");
        }
    }


}
