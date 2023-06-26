package client;

import myShelfieException.*;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Map;
import model.Tile;

public class AsyncClientInput implements Runnable {


    SocketClient socketClient;

    private Stream outClient;
    private Stream inClient;

    public AsyncClientInput(SocketClient socketClient) {
        this.socketClient = socketClient;
        setStreams();
    }

    private void setStreams() {
        outClient = socketClient.getOutputStream();
        inClient = socketClient.getInputStream();
    }


    /**
     * Receives request from the server, parse the 'Action' field and so choose the action to perform on the client
     */
    private void startServer() {

        JSONObject request = new JSONObject();
        JSONObject response = new JSONObject();

        while (true) {

            String Action = null;

            request.clear();
            response.clear();

            try {
                request = inClient.read();
            } catch (InvalidOperationException e) {
                System.out.println("AsyncClientInput  --- InvalidOperationException occurred trying to read a new request");
            } catch (IOException e) {
                System.out.println("AsyncClientInput  --- IOException occurred trying to read a new request");

            } catch (ClassNotFoundException e) {
                System.out.println("AsyncClientInput  --- ClassNotFoundException occurred trying to read a new request");

            }


            try{
                Action = (String) request.get("Action");
            } catch (Exception e) {
                System.out.println("");
            }


            switch (Action) {

                //enterNumberOfPlayers
                case "askNumberOfPlayers":
                    new Thread(() -> {
                        try {
                            socketClient.enterNumberOfPlayers();
                        } catch (RemoteException e) {
                            throw new RuntimeException(e);
                        }
                    }).start();

                    break;


                //updateBoard
                case "notifyUpdatedBoard":
                    try {

                        Tile[][] board = (Tile[][]) request.get("Param1");
                        Tile[][] shelf = (Tile[][]) request.get("Param2");
                        Map<String, Tile[][]> map = (Map<String, Tile[][]>) request.get("Param3");
                        int score = (int) request.get("Param4");
                        int gameID = (int) request.get("Param5");


                        socketClient.updateBoard(board, shelf, map, score);
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                    break;


                //startPlaying
                case "notifyStartPlaying":

                    try {
                        int pgcNum = (int) request.get("Param1");
                        Map<Tile, Integer[]> cardMap = (Map<Tile, Integer[]>) request.get("Param2");
                        int cgc1Num = (int) request.get("Param3");
                        int cgc2Num = (int) request.get("Param4");
                        int gameID = (int) request.get("Param5");

                        socketClient.startPlaying(pgcNum, cardMap, cgc1Num, cgc2Num, gameID);
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }

                    break;


                //startYourTurn
                case "notifyStartYourTurn":

                    try {
                        socketClient.startYourTurn();
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }

                    break;

                //endYourTurn
                case "notifyEndYourTurn":
                    try {
                        socketClient.endYourTurn();
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }

                    break;

                //endGame
                case "notifyEndGame":
                    try {
                        Map<String, Integer> results = (Map<String, Integer>) request.get("Param1");

                        socketClient.theGameEnd(results);
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                    break;

                //askPing
                case "askPing":
                    try {
                        socketClient.ping();
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                    break;

                //receive Message
                case "notifyNewMessage":
                    String nick = (String) request.get("Param1");
                    String message = (String) request.get("Param2");

                    try {
                        socketClient.receiveMessage(nick, message);
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                    break;


                //restore session
                case "restoreSession":

                    try {
                        Tile[][] board = (Tile[][]) request.get("Param1");
                        Tile[][] shelf = (Tile[][]) request.get("Param2");
                        Map<String, Tile[][]> map = (Map<String, Tile[][]>) request.get("Param3");
                        int score = (int) request.get("Param4");
                        int gameID = (int) request.get("Param5");
                        int pgcNum = (int) request.get("Param6");
                        Map<Tile, Integer[]> cardMap = (Map<Tile, Integer[]>) request.get("Param7");
                        int cgc1 = (int) request.get("Param8");
                        int cgc2 = (int) request.get("Param9");

                        socketClient.restoreSession(board, shelf, map, score, gameID, pgcNum, cardMap, cgc1, cgc2);

                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    break;


                //loginexception
                case "throwLoginException":
                    boolean kind = (boolean) request.get("Param1");
                    socketClient.getExceptionHandler().loginExceptionHandler(kind);
                    break;

            }


        }

    }


    @Override
    public void run() {
        startServer();
    }

}
