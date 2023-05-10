package client;

import controller.GameHandler;
import model.Tile;
import myShelfieException.*;
import org.json.simple.JSONObject;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SocketClient extends Client{

    Socket socketLobby;
    Socket socketControlPlayer;

    static ObjectInputStream inLobby;
    static ObjectOutputStream outLobby;

    static ObjectInputStream inCP;
    static ObjectOutputStream outCP;
    static JSONObject jsonIn = null;
    static JSONObject jsonInCP = null;

    /**
     * constructor of ClientApp
     *
     * @throws RemoteException
     */
    protected SocketClient() throws RemoteException {
        super();
    }

    @Override
    public void startClient(){

        System.out.println("Hello from SocketClient");

        getServerSettings();

        try{

            socketLobby = new Socket(localhost,TCPPORTX);

            outCP = new ObjectOutputStream(socketControlPlayer.getOutputStream());
            inCP = new ObjectInputStream(socketControlPlayer.getInputStream());


            outLobby = new ObjectOutputStream(socketLobby.getOutputStream());
            inLobby = new ObjectInputStream(socketLobby.getInputStream());



            Thread threadInputLobby = new Thread(new ClientSocketInputLobby());
            threadInputLobby.start();
            Thread threadInputCP = new Thread(new ClientSocketInputCP());
            threadInputCP.start();


            do {

                int num;
                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

                //-------questa parte andrà migliorata in GUI e TUI

                Scanner scan = new Scanner(System.in);

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
                            askLogin();
                            System.out.println("-----login successfully");
                            break;

                        case 1:
                            System.out.println("nickname: ");
                            nickName = br.readLine();
                            System.out.println("-------------------");

                            //login of the new player
                            askContinueGame();
                            System.out.println("-----reconnected successfully");
                            break;
                    }

                }catch (Exception e) {

                    System.out.println(e.getMessage());
                    e.printStackTrace();
                    System.out.println("try again...");
                    nickName="";
                }

            }while(nickName.equals(""));

        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    //---SimoSocket: is eguente metodo esiste già in Client, non va sovrascritto
    /*
    @Override
    public int enterNumberOfPlayers() throws IOException {

        JSONObject jo = new JSONObject();
        jo.put("method","enterNumberOfPlayers");
        jo.put("param1", enterNumberOfPlayers() );
        jo.put("param2",null);

        outLobby.writeObject(jo);
        outLobby.flush();

        return 0;


    }
    */


    /**
     * asks the server to log in, is divided in RMI and socket
     * @return GameHandler interface
     * @throws LoginException
     * @throws IOException
     * @throws RemoteException
     */
    public GameHandler askLogin() throws LoginException, IOException, RemoteException {

        JSONObject jo = new JSONObject();

        jo.put("method", "login");
        jo.put("param1", nickName);
        jo.put("param2", this);
        jo.put("param3",socketControlPlayer);

        System.out.println(jo);

        outLobby.writeObject(jo);
        outLobby.flush();

        return null;


    }



    /**
     * asks the server to continue a game, is divided in RMI and socket
     * @return GameHandler interface
     * @throws LoginException
     * @throws RemoteException
     */
    public GameHandler askContinueGame() throws LoginException, IOException {

        JSONObject jo = new JSONObject();

        jo.put("method", "continueGame");
        jo.put("param1", nickName);

        System.out.println(jo);

        outLobby.writeObject(jo);
        outLobby.flush();

        return null;
    }


    /**
     * asks the server to leave the game I'm playing, is divided in RMI and socket
     * @return true if everything went fine
     * @throws RemoteException
     */
    public boolean askLeaveGame() throws IOException {

        JSONObject jo = new JSONObject();

        jo.put("method", "leaveGame");
        jo.put("param1", nickName);


        System.out.println(jo);

        outLobby.writeObject(jo);
        outLobby.flush();

        return true;

    }



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
    public boolean askBoardTiles(List<Tile> chosenTiles, List<Integer> coord) throws InvalidChoiceException, NotConnectedException, InvalidParametersException, IOException, NotMyTurnException {

        JSONObject jo = new JSONObject();

        jo.put("method", "askBoardTiles");
        jo.put("param1", chosenTiles);
        jo.put("param2",coord);


        System.out.println(jo);

        outCP.writeObject(jo);
        outCP.flush();

        return true;

    }


    boolean askInsertShelfTiles(ArrayList<Tile> choosenTiles, int choosenColumn, List<Integer> coord) throws IOException, NotConnectedException, NotMyTurnException, InvalidChoiceException, InvalidLenghtException{

        JSONObject jo = new JSONObject();

        jo.put("method", "askInsertShelfTiles");
        jo.put("param1", choosenTiles);
        jo.put("param2",choosenColumn);
        jo.put("param3",coord);


        System.out.println(jo);

        outCP.writeObject(jo);
        outCP.flush();

        return true;

    }


    int askGetMyScore() throws IOException {

        JSONObject jo = new JSONObject();

        jo.put("method", "askGetMyScore");



        System.out.println(jo);

        outCP.writeObject(jo);
        outCP.flush();

        return 0;


    }







}
