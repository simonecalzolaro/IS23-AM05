package view;

import client.Client;
import client.RMIClient;
import client.SocketClient;
import myShelfieException.*;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.FileReader;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Map;

public abstract class View {

    protected boolean connectionType;
    public Client client;

    /**
     * Method invoked to ask the player to choose the number of player to create a new game
     * @throws RemoteException
     */
    public abstract void getNumOfPlayer() throws RemoteException;

    /**
     * Method invoked to updates the game
     */
    public abstract void updateBoard();

    /**
     * Method invoked when the game is over to show the final rank.
     * @param results
     */
    public abstract void endGame(Map<String, Integer> results);

    /**
     * Method invoked to notify the player that his turn starts.
     * @throws IOException
     * @throws InvalidChoiceException
     * @throws NotConnectedException
     * @throws InvalidParametersException
     * @throws NotMyTurnException
     */
    public abstract void isYourTurn() throws IOException, InvalidChoiceException, NotConnectedException, InvalidParametersException, NotMyTurnException;

    /**
     * Method invoked to start the game when the type of UI is chosen
     * @throws IOException
     * @throws LoginException
     * @throws NotBoundException
     */
    public abstract void startGame() throws IOException, LoginException, NotBoundException;

    /**
     * Method invoked to end the player's turn
     */
    public abstract void endYourTurn();

    /**
     * Method invoked when the play has begun.
     * @throws Exception
     */
    public abstract void startPlay() throws Exception;

    /**
     * Method invoked to show exception
     * @param exception String that describe the problem to the player
     */
    public abstract void showException(String exception);

    /**
     * Method invoked to show a new message in the chat
     * @param Sender nickname of the player who sent the message
     * @param message text of the message
     */
    public abstract void plotNewMessage(String Sender, String message);

    /**
     * Method invoked when there is no active game to restore
     */
    public abstract  void standardLogin();

    /**
     *  Method invoked when there is an active game to restore
     */
    public abstract void continueSession();

    protected boolean checkForBackupFile(){
        JSONObject j = new JSONObject();
        try{
            Object o = new JSONParser().parse(new FileReader(System.getProperty("user.dir")+"/config/backup.json"));
            j =(JSONObject) o;

            connectionType = (boolean) j.get("connection");

            return true;


        } catch (Exception e) {
            System.out.println("No cached file available restoring the session");

            return false;

        }
    }

    protected boolean backupLogin(){

        JSONObject j = new JSONObject();

        if(!connectionType){

            try {
                client.setView(this);
                client = new RMIClient();
            } catch (RemoteException e) {
                System.out.println("Error encountered while starting the application --> try to reboot the application");
                return false;
            }

        }
        else{
            try {
                client.setView(this);
                client = new SocketClient();
            } catch (RemoteException e) {
                System.out.println("Error encountered while starting the application --> try to reboot the application");
                return false;
            }
        }

        try {

            try{
                Object o = new JSONParser().parse(new FileReader(System.getProperty("user.dir")+"/config/backup.json"));

                j = (JSONObject) o;

            } catch (ParseException e) {
                System.out.println("Error encountered while reading the file!");
            }
            Long num_pre;
            Long num_other;
            client.initializeClient();
            client.getModel().setNickname((String) j.get("nickname"));
            num_pre = (Long) j.get("gameID");
            client.getModel().setGameID(num_pre.intValue());
            num_other = (Long) j.get("numOtherPlayers");
            client.getModel().setNumOtherPlayers(num_other.intValue());

        } catch (IOException | NotBoundException e) {
            System.out.println("Error encountered while starting the application --> try to reboot the application");
        }

        try{
            client.askContinueGame();

        } catch (LoginException e) {
            client.getExceptionHandler().loginExceptionHandler(true);
            return false;
        } catch (IOException e) {
            System.out.println("Error encountered while starting the application --> try to reboot the application");
        }

        return true;
    }
}
