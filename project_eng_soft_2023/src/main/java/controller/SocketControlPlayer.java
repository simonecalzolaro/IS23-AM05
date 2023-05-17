package controller;

import client.ClientHandler;
import model.Board;
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
     * @param board    : unique board
     */
    public SocketControlPlayer(String nickname, Board board, ArrayList<Stream> streams) throws RemoteException {
        super(nickname, board);

        setStreams(streams);

    }


    /**
     * this method tells to "nextClient" to start his turn, is divided in RMI and socket
     *
     * @return true if everything went fine
     * @throws RemoteException
     */

    @Override
    public Boolean notifyStartYourTurn() throws IOException {

        JSONObject object = new JSONObject();
        JSONObject response = new JSONObject();

        object.put("Action","Method");
        object.put("Method","notifyStartYourTurn");


        try{
            outCP.reset();
            outCP.write(object);
        } catch (InvalidOperationException e) {
            System.out.println("SocketControlPlayer --- InvalidOperationException occurred trying to reset/write the stream");
            System.out.println("---> Maybe you're trying to reset/write an input stream");
            throw new RuntimeException();
        }

        try{
            System.out.println("SocketControlPlayer --- Waiting a feedback from the client");
            response = inCP.read();
            System.out.println("SocketControlPlayer --- Feedback received from the client");
        } catch (InvalidOperationException e) {
            System.out.println("SocketControlPlayer --- InvalidOperationException occurred trying to read a new request");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("SocketControlPlayer --- IOException occurred trying to read a new request");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("SocketControlPlayer --- ClassNotFoundException occurred trying to read a new request");
            e.printStackTrace();
        }


        String Action = (String) response.get("Action");

        switch (Action){

            case "Method":
                break;

            case "Feedback":
                String Feedback = (String) response.get("Feedback");
                if(!Feedback.equals("OKnotifyStartYourTurn")) throw new IOException();


                break;
        }

        return true;

    }

    @Override
    public Boolean notifyEndYourTurn() throws IOException {

        JSONObject object = new JSONObject();
        JSONObject response = new JSONObject();

        object.put("Action","Method");
        object.put("Method","notifyEndYourTurn");


        try{
            outCP.reset();
            outCP.write(object);
        } catch (InvalidOperationException e) {
            System.out.println("SocketControlPlayer --- InvalidOperationException occurred trying to reset/write the stream");
            System.out.println("---> Maybe you're trying to reset/write an input stream");
            throw new RuntimeException();
        }

        try{
            System.out.println("SocketControlPlayer --- Waiting a feedback from the client");
            response = inCP.read();
            System.out.println("SocketControlPlayer --- Feedback received from the client");
        } catch (InvalidOperationException e) {
            System.out.println("SocketControlPlayer --- InvalidOperationException occurred trying to read a new request");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("SocketControlPlayer --- IOException occurred trying to read a new request");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("SocketControlPlayer --- ClassNotFoundException occurred trying to read a new request");
            e.printStackTrace();
        }


        String Action = (String) response.get("Action");

        switch (Action){

            case "Method":
                break;

            case "Feedback":
                String Feedback = (String) response.get("Feedback");
                if(!Feedback.equals("OKnotifyEndYourTurn")) throw new IOException();


                break;
        }

        return true;

    }






    @Override
    public void notifyUpdatedBoard() throws IOException {


        if( ! playerStatus.equals(PlayerStatus.NOT_ONLINE)) {

            Map<String, Tile[][]> map= new HashMap<>();

            for(ControlPlayer cp: game.getPlayers()){

                if(! cp.equals(this))  map.put(cp.getPlayerNickname(), cp.getBookshelf().getShelf());

            }

            JSONObject object = new JSONObject();
            JSONObject response = new JSONObject();

            object.put("Action","Method");
            object.put("Method","notifyUpdateBoard");
            object.put("Param1",game.getBoard().getBoard());
            object.put("Param2", this.bookshelf.getShelf());
            object.put("Param3", map);

            try{
                outCP.reset();
                outCP.write(object);
            } catch (InvalidOperationException e) {
                System.out.println("SocketControlPlayer --- InvalidOperationException occurred trying to reset/write the stream");
                System.out.println("---> Maybe you're trying to reset/write an input stream");
                throw new RuntimeException();
            }



            try{
                System.out.println("SocketControlPlayer --- Waiting a feedback from the client");
                response = inCP.read();
                System.out.println("SocketControlPlayer --- Feedback received from the client");
            } catch (InvalidOperationException e) {
                System.out.println("SocketControlPlayer --- InvalidOperationException occurred trying to read a new request");
                e.printStackTrace();
            } catch (IOException e) {
                System.out.println("SocketControlPlayer --- IOException occurred trying to read a new request");
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                System.out.println("SocketControlPlayer --- ClassNotFoundException occurred trying to read a new request");
                e.printStackTrace();
            }


            String Action = (String) response.get("Action");

            switch (Action){

                case "Method":
                    break;

                case "Feedback":
                    String Feedback = (String) response.get("Feedback");
                    if(!Feedback.equals("OKnotifyUpdateBoard")) throw new IOException();


                    break;
            }


        }

    }


    @Override
    public boolean askPing() throws IOException {
        return false;
    }


    public void notifyEndGame() throws IOException {

        if( ! playerStatus.equals(PlayerStatus.NOT_ONLINE)) {



            JSONObject object = new JSONObject();
            JSONObject response = new JSONObject();

            object.put("Action","Method");
            object.put("Method","notifyEndGame");
            object.put("Param1",game.getGameResults());


            try{
                outCP.reset();
                outCP.write(object);
            } catch (InvalidOperationException e) {
                System.out.println("SocketControlPlayer --- InvalidOperationException occurred trying to reset/write the stream");
                System.out.println("---> Maybe you're trying to reset/write an input stream");
                throw new RuntimeException();
            }

            try{
                System.out.println("SocketControlPlayer --- Waiting a feedback from the client");
                response = inCP.read();
                System.out.println("SocketControlPlayer --- Feedback received from the client");
            } catch (InvalidOperationException e) {
                System.out.println("SocketControlPlayer --- InvalidOperationException occurred trying to read a new request");
                e.printStackTrace();
            } catch (IOException e) {
                System.out.println("SocketControlPlayer --- IOException occurred trying to read a new request");
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                System.out.println("SocketControlPlayer --- ClassNotFoundException occurred trying to read a new request");
                e.printStackTrace();
            }


            String Action = (String) response.get("Action");

            switch (Action){

                case "Method":
                    break;

                case "Feedback":
                    String Feedback = (String) response.get("Feedback");
                    if(!Feedback.equals("OKnotifyEndGame")) throw new IOException();


                    break;
            }

        }

    }


    @Override
    public int askNumberOfPlayers() throws IOException{

        JSONObject object = new JSONObject();
        JSONObject response = new JSONObject();

        int res = -1;

        object.put("Action","Method");
        object.put("Method","askNumberOfPlayers");

        try{
            outCP.reset();
            outCP.write(object);
        } catch (InvalidOperationException e) {
            System.out.println("SocketControlPlayer --- InvalidOperationException occurred trying to reset/write the stream");
            System.out.println("---> Maybe you're trying to reset/write an input stream");
            throw new RuntimeException();
        }

        try{
            System.out.println("SocketControlPlayer --- Waiting number of players from the client");
            response = inCP.read();
            System.out.println("SocketControlPlayer --- Number of players received from the client");
        } catch (InvalidOperationException e) {
            System.out.println("SocketControlPlayer --- InvalidOperationException occurred trying to read a new request");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("SocketControlPlayer --- IOException occurred trying to read a new request");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("SocketControlPlayer --- ClassNotFoundException occurred trying to read a new request");
            e.printStackTrace();
        }

        String Action = (String) response.get("Action");

        switch (Action){

            case "Method":
                break;

            case "Feedback":
                res = (int) response.get("Feedback");
                break;

        }

        return res;

    }


    @Override
    public void notifyStartPlaying() throws IOException {

        JSONObject object = new JSONObject();
        JSONObject response = new JSONObject();

        object.put("Action","Method");
        object.put("Method","notifyStartPlaying");
        object.put("Param1",bookshelf.getPgc().getCardNumber());
        object.put("Param2",bookshelf.getPgc().getCardMap());
        object.put("Param3",game.getBoard().getCommonGoalCard1().getCGCnumber());
        object.put("Param4",game.getBoard().getCommonGoalCard2().getCGCnumber());

        try{
            outCP.reset();
            outCP.write(object);
        } catch (InvalidOperationException e) {
            System.out.println("SocketControlPlayer --- InvalidOperationException occurred trying to reset/write the stream");
            System.out.println("---> Maybe you're trying to reset/write an input stream");
            throw new RuntimeException();
        }


        try{
            System.out.println("SocketControlPlayer --- Waiting a feedback from the client");
            response = inCP.read();
            System.out.println("SocketControlPlayer --- Feedback received from the client");
        } catch (InvalidOperationException e) {
            System.out.println("SocketControlPlayer --- InvalidOperationException occurred trying to read a new request");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("SocketControlPlayer --- IOException occurred trying to read a new request");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("SocketControlPlayer --- ClassNotFoundException occurred trying to read a new request");
            e.printStackTrace();
        }


        String Action = (String) response.get("Action");

        switch (Action){

            case "Method":
                break;

            case "Feedback":
                String Feedback = (String) response.get("Feedback");
                if(!Feedback.equals("OKnotifyStartPlaying")) throw new IOException();


                break;
        }

        notifyUpdatedBoard();

    }

    @Override
    public void setStreams(ArrayList<Stream> streams) {
        outCP = streams.get(0);
        inCP = streams.get(1);
    }

    @Override
    public void setClientHandler(ClientHandler cliHnd) {
    }
}