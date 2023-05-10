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
     * @return true if everything went fine
     * @throws RemoteException
     */

    @Override
    public Boolean notifyStartYourTurn() throws IOException {

        JSONObject jo = new JSONObject();

        jo.put("method","notifyStartYourTurn");

        out.writeObject(jo);
        out.flush();

        try{
            while(jo == null){
                jo = (JSONObject) in.readObject();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        if(jo.get("method").equals("startYourTurnOK")){
            return true;
        }

        return false;

    }

    @Override
    public Boolean notifyEndYourTurn() throws IOException {

        JSONObject jo = new JSONObject();

        jo.put("method", "notifyEndYourTurn");

        out.writeObject(jo);
        out.flush();

        try{
            while(jo == null){
                jo = (JSONObject) in.readObject();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        if(jo.get("method").equals("endYourTurnOK")){
            return true;
        }




        return false;

    }






    @Override
    public void notifyUpdatedBoard() throws IOException {

        JSONObject jo = new JSONObject();

        if( ! playerStatus.equals(PlayerStatus.NOT_ONLINE)) {


            jo.put("method","updateBoard");
            jo.put("param1",game.getBoard().getBoard());

            out.writeObject(jo);
            out.flush();

            jo.clear();
            jo = null;

            try{
                while(jo == null){
                    jo = (JSONObject) in.readObject();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }

            if(jo.get("method").equals("updateBoardOK")) {

                jo.clear();
                jo = null;
            }



        }
    }


    @Override
    public boolean askPing() throws IOException {
        JSONObject jo = new JSONObject();

        jo.put("method","askPing");

        out.writeObject(jo);
        out.flush();;


        jo.clear();
        jo = null;

        try{
            while(jo == null){
                jo = (JSONObject) in.readObject();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        if(jo.get("method").equals("askPingOK")) {

            jo.clear();
            jo = null;

            return true;
        }

        return false;


    }


    public void notifyEndGame() throws IOException {

        JSONObject jo = new JSONObject();


        if( ! playerStatus.equals(PlayerStatus.NOT_ONLINE)) {

           jo.put("method","endGame");
           jo.put("param1",game.getGameResults());

           out.writeObject(jo);
           out.flush();

            try{
                while(jo == null){
                    jo = (JSONObject) in.readObject();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }

            if(jo.get("method").equals("endGameOK")) {

                jo.clear();
                jo = null;
            }





        }

    }


    @Override
    public int askNumberOfPlayers() throws IOException{

        int res=-1;
        JSONObject jo = new JSONObject();

        jo.put("method","askNumberOfPlayers");

        out.writeObject(jo);
        out.flush();

        jo = null;



        try{
            while(jo == null){
                jo = (JSONObject) in.readObject();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        if(jo.get("method").equals("enterNumberOfPlayers")){
            res = (int) jo.get("param1");
        }

        return res;

    }


    @Override
    public void notifyStartPlaying() throws IOException {

        JSONObject jo = new JSONObject();

        jo.put("method","startPlaying");
        jo.put("param1",bookshelf.getPgc().getCardNumber());
        jo.put("param2",game.getBoard().getCommonGoalCard1().getCGCnumber());
        jo.put("param3",game.getBoard().getCommonGoalCard2().getCGCnumber());

        out.writeObject(jo);
        out.flush();

        jo.clear();
        jo = null;

        try{
            while(jo == null){
                jo = (JSONObject) in.readObject();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        if(jo.get("method").equals("startPlayingOK")){
            jo.clear();
            jo = null;

            jo.put("method","updateBoard");
            jo.put("param1",game.getBoard().getBoard());

            out.writeObject(jo);
            out.flush();

            try{
                while(jo == null){
                    jo = (JSONObject) in.readObject();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }

            if(jo.get("method").equals("updateBoardOK")){

                jo.clear();
                jo = null;

                if(game.getPlayers().get(game.getCurrPlayer()).equals(this)){
                    jo.put("method","notifyStartYourTurn");

                    out.writeObject(jo);
                    out.flush();

                    try{
                        while(jo == null){
                            jo = (JSONObject) in.readObject();
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }

                    if(jo.get("method").equals("startYourTurnOK")){
                        return;
                    }
                }

            }
        }
    }

    @Override
    public void setSocket(Socket socket) {
        socketControlPlayer = socket;
    }

    @Override
    public void setClientHandler(ClientHandler cliHnd) {
    }
}