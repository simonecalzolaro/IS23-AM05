package client;

import model.Tile;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Map;

public class ClientSocketInputCP extends SocketClient implements Runnable{
    /**
     * constructor of ClientApp
     *
     * @throws RemoteException
     */
    protected ClientSocketInputCP() throws RemoteException {
        super();
    }

    @Override
    public void run() {
        JSONObject jo = new JSONObject();
        jo = null;

        while(true){

            jsonInCP.clear();
            jsonInCP = null;


            try{
                while(jsonInCP == null){
                    jsonInCP = (JSONObject) inLobby.readObject();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }

            String method;

            method = (String) jsonInCP.get("method");

            switch (method){

                case "askNumberOfPlayers":
                    try {
                        int num = enterNumberOfPlayers();

                        jo.put("method","enterNumberOfPlayers");
                        jo.put("param1",num);

                        outCP.writeObject(jo);
                        outCP.flush();

                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    jo.clear();
                    jo = null;
                    break;

                case "startPlaying":
                      int pgcNum= (int) jsonInCP.get("param1");
                      int cgc1Num = (int) jsonInCP.get("param2");
                      int cgc2Num = (int) jsonInCP.get("param3");

                    try {
                        startPlaying(pgcNum,cgc1Num,cgc2Num);
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }


                    jo.put("method","startPlayingOK");

                    try {
                        outCP.writeObject(jo);
                        outCP.flush();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    jo.clear();
                    jo = null;


                    break;

                case "updateBoard":

                    Tile[][] board = (Tile[][]) jsonInCP.get("param1");
/*
                    try {
                        updateBoard(board);
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }

 */

                    jo.put("method","updateBoardOK");

                    try {
                        outCP.writeObject(jo);
                        outCP.flush();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    jo.clear();
                    jo = null;

                    break;


                case "notifyStartYourTurn":

                    try{
                        startYourTurn();
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }

                    jo.put("method","startYourTurnOK");

                    try {
                        outCP.writeObject(jo);
                        outCP.flush();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    jo.clear();
                    jo = null;

                    break;

                case "notifyEndYourTurn":

                    try{
                        endYourTurn();
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }

                    jo.put("method","endYourTurnOK");

                    try {
                        outCP.writeObject(jo);
                        outCP.flush();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    jo.clear();
                    jo = null;

                    break;


                case "endGame":
                    Map<Integer,String> results = (Map<Integer,String>) jo.get("param1");

                    try{
                        theGameEnd(results);
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }

                    jo.put("method", "endGameOK");

                    try {
                        outCP.writeObject(jo);
                        outCP.flush();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    jo.clear();
                    jo = null;

                    break;

                case "askPing":

                    try{
                        pong();
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }

                    jo.put("method", "askPingOK");

                    try {
                        outCP.writeObject(jo);
                        outCP.flush();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    jo.clear();
                    jo = null;

                    break;


            }

        }
    }
}
