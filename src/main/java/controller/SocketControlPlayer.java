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


/**
 * Receive requests from the SocketClient in order to perform game's actions
 */
public class SocketControlPlayer extends ControlPlayer {

    Stream outCP;
    Stream inCP;

    int contPing=0;

    /**
     * Assign player id
     * Initialize score
     * Set player status as NOT_MY_TURN
     * @param nickname of the player who logged in the game
     * @param streams reference/Socket stream for communicating with the client
     * @throws RemoteException thrown when a network error occurs
     */
    public SocketControlPlayer(String nickname, ArrayList<Stream> streams) throws RemoteException {
        super(nickname);
        setStreams(streams);
    }


    /**
     * this method tells to "nextClient" to start his turn, is divided in RMI and socket
     * @throws RemoteException thrown when a network error occurs
     */
    @Override
    public synchronized void notifyStartYourTurn() throws RemoteException {

        if (!playerStatus.equals(PlayerStatus.NOT_ONLINE)) {

            System.out.println("    ->notify StartYourTurn to "+ nickname);

            JSONObject object = new JSONObject();

            object.put("Action","notifyStartYourTurn");

            try{
                outCP.reset();
                outCP.write(object);
            } catch (InvalidOperationException e) {
                System.out.println("SocketControlPlayer --- InvalidOperationException occurred in notifyStartYourTurn trying to reset/write the stream");
                System.out.println("---> Maybe you're trying to reset/write an input stream");

            } catch (IOException e) {
                System.out.println("SocketControlPlayer --- IOException occurred in notifyStartYourTurn");
            }

        }


    }


    /**
     * This method tells the client to end its turn
     */
    @Override
    public synchronized void notifyEndYourTurn() {

       if(!playerStatus.equals(PlayerStatus.NOT_ONLINE)){
           JSONObject object = new JSONObject();

           object.put("Action","notifyEndYourTurn");

           try{
               outCP.reset();
               outCP.write(object);
           } catch (InvalidOperationException e) {
               System.out.println("SocketControlPlayer --- InvalidOperationException occurred in notifyStartYourTurn trying to reset/write the stream");
               System.out.println("---> Maybe you're trying to reset/write an input stream");

           } catch (IOException e) {
               System.out.println("SocketControlPlayer --- IOException occurred in notifyEndYourTurn");

           }
       }



    }






    /**
     * This method update the board of every client playing the game
     * @throws RemoteException thrown when a network error occurs
     */
    @Override
    public synchronized void notifyUpdatedBoard() throws RemoteException{

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

            try{
                outCP.reset();
                outCP.write(object);
            } catch (InvalidOperationException e) {
                System.out.println("SocketControlPlayer --- InvalidOperationException occurred in notifyUpdateBoard trying to reset/write the stream");
                System.out.println("---> Maybe you're trying to reset/write an input stream");

            } catch (IOException e) {
                System.out.println("SocketControlPlayer --- IOException occurred in notifyUpdateboard");
            }
        }
    }



    /**
     * This method tells the client to end the game
     */
    public synchronized void notifyEndGame(){

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
            } catch (IOException e) {
                System.out.println("SocketControlPlayer --- IOException occurred in notifyEndGame");
            }

        }

    }


    /**
     * This method tells the client to insert the number of players of the game
     * That means that the current client is the first player
     */
    @Override
    public synchronized void askNumberOfPlayers() {

        JSONObject object = new JSONObject();
        object.put("Action","askNumberOfPlayers");


           try{
               outCP.reset();
               outCP.write(object);
           } catch (InvalidOperationException e) {
               System.out.println("SocketControlPlayer --- InvalidOperationException occurred in askNumberOfPlayers trying to reset/write the stream");
               System.out.println("---> Maybe you're trying to reset/write an input stream");
           } catch (IOException e) {
               System.out.println("SocketControlPlayer --- IOException occurred in askNumberOfPlayers");
           }
       }



    /**
     * this method tells to all users that the game has started and that they aren't anymore in the waiting room, is divided in RMI and socket
     * @throws RemoteException thrown when a network error occurs
     */
    @Override
    public synchronized void notifyStartPlaying() throws RemoteException {


            if(!playerStatus.equals(PlayerStatus.NOT_ONLINE)){
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


                } catch (IOException e) {
                    System.out.println("SocketControlPlayer --- IOException occurred in notifyStartPlaying" );
                }
            }

    }


    /**
     * This method is invoked when a client tries to continue a game and succeed in it, so it's necessary that he receives the updated board and its attributes stored in the server
     */
    @Override
    public synchronized void restoreSession(){

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
            } catch (IOException e) {
                System.out.println("SocketControlPlayer --- IOException occurred in restoreSession");
            }
        }

    }



    /**
     * This method sends the ping to the client in order to verify if it's online or not
     */
    @Override
    public synchronized void askPing() {

        //System.out.println("*** soket askPing()");

        JSONObject object = new JSONObject();

        object.put("Action","askPing");
        try{
            outCP.reset();
            outCP.write(object);
        } catch (InvalidOperationException e) {
            System.out.println("SocketControlPlayer --- InvalidOperationException occurred in trying to reset/write the stream");
            System.out.println("---> Maybe you're trying to reset/write an input stream");
        } catch (IOException e) {
            System.out.println("SocketControlPlayer --- IOException occurred in askPing");
            contPing++;
            if(contPing == 13){
                pingClass.stopPingProcess();
                contPing = 0;

            }

        }

    }


    /**
     * Set the socket streams used for communicating with the client
     * @param streams to set in order to communicate with the client
     */
    @Override
    public synchronized void setStreams(ArrayList<Stream> streams) {
        outCP = streams.get(0);
        inCP = streams.get(1);
    }


    /**
     * This method is used in order to warn the client about a new message received from another client
     * @param nick: nickname of the sender
     * @param message: text message
     */
    @Override
    public synchronized void notifyNewMessage(String nick, String message) {


       if(!playerStatus.equals(PlayerStatus.NOT_ONLINE)){
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
           } catch (IOException e) {

               System.out.println("SocketControlPlayer --- Unable to send the message");
           }

       }

    }




    /**
     * @param cliHnd of type ClientHandler
     */
    @Override
    public void setClientHandler(ClientHandler cliHnd) {
    }
}