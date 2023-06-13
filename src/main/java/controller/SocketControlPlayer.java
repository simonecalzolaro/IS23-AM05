package controller;

import client.ClientHandler;
import model.Tile;
import myShelfieException.*;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SocketControlPlayer extends ControlPlayer {


    Stream outCP;
    Stream inCP;




    /**
     * Assign player id
     * Initialize score
     * Set player status as NOT_MY_TURN
     *
     * @param nickname : unique player nickname
     */
    public SocketControlPlayer(String nickname, ArrayList<Stream> streams) throws RemoteException {
        super(nickname);
        setStreams(streams);
    }


    /**
     * this method tells to "nextClient" to start his turn, is divided in RMI and socket
     *
     * @return true if everything went fine
     * @throws RemoteException
     */

    @Override
    public void notifyStartYourTurn() throws RemoteException {

        System.out.println("    ->notify StartYourTurn to "+ nickname);

        JSONObject object = new JSONObject();

        object.put("Action","notifyStartYourTurn");

        try{
            outCP.reset();
            outCP.write(object);
        } catch (InvalidOperationException e) {
            System.out.println("SocketControlPlayer --- InvalidOperationException occurred in notifyStartYourTurn trying to reset/write the stream");
            System.out.println("---> Maybe you're trying to reset/write an input stream");
            throw new RuntimeException();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    @Override
    public void notifyEndYourTurn() throws IOException {

        JSONObject object = new JSONObject();

        object.put("Action","notifyEndYourTurn");

        try{
            outCP.reset();
            outCP.write(object);
        } catch (InvalidOperationException e) {
            System.out.println("SocketControlPlayer --- InvalidOperationException occurred in notifyStartYourTurn trying to reset/write the stream");
            System.out.println("---> Maybe you're trying to reset/write an input stream");
            throw new RuntimeException();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }



    }






    @Override
    public void notifyUpdatedBoard() throws RemoteException{

        if( ! playerStatus.equals(PlayerStatus.NOT_ONLINE)) {

            Map<String, Tile[][]> map= new HashMap<>();

            //in this for I'm creating the map to send, I'm NOT notifying each player
            for(ControlPlayer cp: game.getPlayers()){

                if(! cp.equals(this))  map.put(cp.getPlayerNickname(), cp.getBookshelf().getShelf());

            }


            JSONObject object = new JSONObject();
            object.put("Action","notifyUpdatedBoard");
            object.put("Param1",game.getBoard().getBoard());
            object.put("Param2",this.bookshelf.getShelf());
            object.put("Param3",map);
            object.put("Param4",bookshelf.getMyScore());
            object.put("Param5",game.getGameID());

            try{
                outCP.reset();
                outCP.write(object);
            } catch (InvalidOperationException e) {
                System.out.println("SocketControlPlayer --- InvalidOperationException occurred in notifyUpdateBoard trying to reset/write the stream");
                System.out.println("---> Maybe you're trying to reset/write an input stream");
                throw new RuntimeException();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }



    public void notifyEndGame() throws IOException {

        if( ! playerStatus.equals(PlayerStatus.NOT_ONLINE)) {

            JSONObject object = new JSONObject();

            object.put("Action","notifyEndGame");
            object.put("Param1",game.getGameResults());

            try{
                outCP.reset();
                outCP.write(object);
            } catch (InvalidOperationException e) {
                System.out.println("SocketControlPlayer --- InvalidOperationException occurred in notifyStartYourTurn trying to reset/write the stream");
                System.out.println("---> Maybe you're trying to reset/write an input stream");
                throw new RuntimeException();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }

    }


    /**
     * Invoked by the thread lobby.checkAskNumberOfPlayers() in order to ask to the first connected client the number of players
     * of the game
     * It creates a JSONObject filling the 'Action' field with the string 'askNumberOfPlayers', then send all to the client who'll receive the object on a separated thread
     */
    @Override
    public void askNumberOfPlayers() {

        JSONObject object = new JSONObject();
        object.put("Action","askNumberOfPlayers");

        try{
            outCP.reset();
            outCP.write(object);
        } catch (InvalidOperationException e) {
            System.out.println("SocketControlPlayer --- InvalidOperationException occurred in askNumberOfPlayers trying to reset/write the stream");
            System.out.println("---> Maybe you're trying to reset/write an input stream");
            throw new RuntimeException();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void notifyStartPlaying() throws RemoteException {

        System.out.println("    ->notify startPlaying to "+ nickname);

        JSONObject object = new JSONObject();

        object.put("Action", "notifyStartPlaying");
        object.put("Param1",bookshelf.getPgc().getCardNumber());
        object.put("Param2",bookshelf.getPgc().getCardMap());
        object.put("Param3",game.getBoard().getCommonGoalCard1().getCGCnumber());
        object.put("Param4",game.getBoard().getCommonGoalCard2().getCGCnumber());
        object.put("Param5",game.getGameID());


        try{
            outCP.reset();
            outCP.write(object);
        } catch (InvalidOperationException e) {
            System.out.println("SocketControlPlayer --- InvalidOperationException occurred in  trying to reset/write the stream");
            System.out.println("---> Maybe you're trying to reset/write an input stream");
            throw new RuntimeException();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    @Override
    public void restoreSession(){

        if( ! playerStatus.equals(PlayerStatus.NOT_ONLINE)) {

            Map<String, Tile[][]> map= new HashMap<>();

            //in this for I'm creating the map to send, I'm NOT notifying each player
            for(ControlPlayer cp: game.getPlayers()){

                if(! cp.equals(this))  map.put(cp.getPlayerNickname(), cp.getBookshelf().getShelf());

            }


            JSONObject object = new JSONObject();
            object.put("Action","restoreSession");
            object.put("Param1",game.getBoard().getBoard());
            object.put("Param2",this.bookshelf.getShelf());
            object.put("Param3",map);
            object.put("Param4",bookshelf.getMyScore());
            object.put("Param5",game.getGameID());
            object.put("Param6", bookshelf.getPgc().getCardNumber());
            object.put("Param7", bookshelf.getPgc().getCardMap());
            object.put("Param8", game.getBoard().getCommonGoalCard1().getCGCnumber());
            object.put("Param9",game.getBoard().getCommonGoalCard2().getCGCnumber());

            try{
                outCP.reset();
                outCP.write(object);
            } catch (InvalidOperationException e) {
                System.out.println("SocketControlPlayer --- InvalidOperationException occurred in notifyUpdateBoard trying to reset/write the stream");
                System.out.println("---> Maybe you're trying to reset/write an input stream");
                throw new RuntimeException();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }



    @Override
    public void askPing() throws IOException {

        //System.out.println("*** soket askPing()");

        JSONObject object = new JSONObject();

        object.put("Action","askPing");
        try{
            outCP.reset();
            outCP.write(object);
        } catch (InvalidOperationException e) {
            System.out.println("SocketControlPlayer --- InvalidOperationException occurred in trying to reset/write the stream");
            System.out.println("---> Maybe you're trying to reset/write an input stream");
            throw new RuntimeException();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void setStreams(ArrayList<Stream> streams) {
        outCP = streams.get(0);
        inCP = streams.get(1);
    }

    @Override
    public void notifyNewMessage(String nick, String message) throws IOException {


        JSONObject object = new JSONObject();

        object.put("Action","notifyNewMessage");
        object.put("Param1",nick);
        object.put("Param2",message);

        try{
            outCP.reset();
            outCP.write(object);
        } catch (InvalidOperationException e) {
            System.out.println("SocketControlPlayer --- InvalidOperationException occurred in notifyStartPlaying trying to reset/write the stream");
            System.out.println("---> Maybe you're trying to reset/write an input stream");
            throw new RuntimeException();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }





    @Override
    public void setClientHandler(ClientHandler cliHnd) {
    }
}