package controller;

import client.ClientHandler;
import model.Board;
import model.Tile;
import myShelfieException.*;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class SocketControlPlayer extends ControlPlayer {


    Socket socketControlPlayer;

    ObjectOutputStream out;
    ObjectInputStream in;

    static int count;

    static Object cpLock;


    /**
     * Assign player id
     * Initialize score
     * Set player status as NOT_MY_TURN
     *
     * @param nickname : unique player nickname
     * @param board    : unique board
     */
    public SocketControlPlayer(String nickname, Board board, Socket socketControlPlayer) throws RemoteException {
        super(nickname, board);
        this.socketControlPlayer = socketControlPlayer;
    }

    public void startServer() {

        System.out.println("CP Thread num: " + count + " created");
        count++;

        while (true) {
            try {

                in = new ObjectInputStream(socketControlPlayer.getInputStream());
                out = new ObjectOutputStream(socketControlPlayer.getOutputStream());

                String method;


                JSONObject json = null;

                while (json == null) {
                    json = (JSONObject) in.readObject();
                }

                method = (String) json.get("method");


                // gestisci vari metodi

                switch (method) {
                    case "askBoardTiles":
                        TCPaskBoardTiles(json);
                        break;

                    case "askInsertShelfTiles":
                        TCPaskInsertShelfTiles(json);
                        break;

                    case "getMyScore":
                        TCPgetMyScore();
                        break;


                }


            } catch (Exception e) {
                System.out.println("error x");
                e.printStackTrace();

            }
        }

    }

    public void TCPaskBoardTiles(JSONObject json) throws InvalidChoiceException, NotConnectedException, InvalidParametersException, RemoteException, NotMyTurnException {

        List<Tile> choosenTiles = (List<Tile>) json.get("param1");
        List<Integer> coord = (List<Integer>) json.get("param2");

        synchronized (cpLock) {
            choseBoardTiles(choosenTiles, coord);
        }

    }


    public void TCPaskInsertShelfTiles(JSONObject json) throws InvalidChoiceException, NotConnectedException, InvalidLenghtException, RemoteException, NotMyTurnException {

        ArrayList<Tile> choosenTiles = (ArrayList<Tile>) json.get("param1");
        Long choosenColumn = (Long) json.get("param2");
        List<Integer> coord = (List<Integer>) json.get("param3");

        synchronized (cpLock) {
            insertShelfTiles(choosenTiles, choosenColumn.intValue(), coord);
        }

    }

    public void TCPgetMyScore() throws IOException {

        int num;

        synchronized (cpLock) {
            num = getMyScore();
        }

        JSONObject jo = new JSONObject();

        jo.put("method", "getMyScore");
        jo.put("param1", num);

        out.writeObject(jo);
        out.flush();


    }


    /**
     * this method tells to "nextClient" to start his turn, is divided in RMI and socket
     *
     * @param nextClient
     * @return true if everything went fine
     * @throws RemoteException
     */
    public Boolean notifyStartYourTurn(ClientHandler nextClient) throws IOException {

        JSONObject jo = new JSONObject();

        jo.put("method", "notifyStartYourTurn");
        jo.put("param1", nextClient);

        out.writeObject(jo);
        out.flush();

        return true;


    }


    public Boolean notifyEndYourTurn() throws IOException {

        JSONObject jo = new JSONObject();

        jo.put("method", "notifyEndYourTurn");
        jo.put("param1", ch);

        out.writeObject(jo);
        out.flush();

        return true;

    }






    public void notifyUpdatedBoard() throws RemoteException{

        //for each client in the game I push the updated board
        for(ControlPlayer player: game.getPlayers()) {

            if( ! playerStatus.equals(PlayerStatus.NOT_ONLINE)) {


                //NON HO CAPITO
                while (!player.getClientHandler().updateBoard(game.getBoard().getBoard())); //----poco elegante

            }
        }
    }


    public void notifyEndGame() throws RemoteException{

        //for each client in the game I notify the end of the game with the results of the match
        for(ControlPlayer player: game.getPlayers()) {

            if( ! playerStatus.equals(PlayerStatus.NOT_ONLINE)) {

                //NON HO CAPITO
                while( player.getClientHandler().theGameEnd(game.getGameResults()) ); //----poco elegante

            }
        }
    }
}