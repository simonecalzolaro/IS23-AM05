package view;

import client.Client;
import client.RMIClient;
import client.SocketClient;
import model.Bookshelf;
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

    public View() {

    }

    public abstract void getNumOfPlayer() throws RemoteException;
    public abstract void updateBoard();
    public abstract void endGame(Map<String, Integer> results);
    public abstract void isYourTurn() throws IOException, InvalidChoiceException, NotConnectedException, InvalidParametersException, NotMyTurnException;
    public abstract void startGame() throws IOException, LoginException, NotBoundException;
    public abstract void endYourTurn();
    public abstract void startPlay() throws Exception;
    public abstract void showException(String exception);
    public abstract void plotNewMessage(String Sender, String message);
    public abstract  void standardLogin();

    public abstract void continueSession();
    public boolean checkForBackupFile(){
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
    public boolean backupLogin(){

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
