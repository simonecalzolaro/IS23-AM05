package controller;


import client.ClientHandler;
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

public class SocketLobby implements Runnable{

    Socket socketServer;
    Stream outServer;
    Stream inServer;
    GameHandler cp;

    private static Lobby lobby;


    /**
     * SocketLobby is responsible for all the requests coming from the client, it receive a JSONObject then execute the parsing of the Object's fields
     * in order to individuate the action invoked by the client with the respective parameters, then invokes the method on the Lobby through the lobby attribute
     * The constructor assigns the two parameters lobby and socket in order to execute request from the client on the server and sending/receiving data from the client
     * through the Socket
     * The input/output streams are abstracted with a Stream object which contains all method and attribute that enable the communication between client and the server over
     * the network
     *
     * @param lobby reference to the lobby, so that every request for the server will invokes a method on this attribute
     * @param socket created by the TCPHandler() this links the communication from the client to the server
     * @throws IOException
     */
    public SocketLobby(Lobby lobby, Socket socket) throws IOException {

        this.lobby = lobby;

        this.socketServer = socket;
        System.out.println("----SocketServer ready----");

        try{
            outServer = new Stream(socketServer,0);
        } catch (InvalidParametersException e) {
            System.out.println("SocketClient --- InvalidParameterException occurred trying to create the output stream");
            System.out.println("---> Change it with a valid one");
            throw new RuntimeException();
        }

        try{
            inServer = new Stream(socketServer,1);
        } catch (InvalidParametersException e) {
            System.out.println("SocketClient --- InvalidParameterException occurred trying to create the input stream");
            System.out.println("---> Change it with a valid one");
            throw new RuntimeException();
        }
    }


    /**
     * Responsible for receiving request, as JSONObjects, then it parse the field 'Action' of the object received that indicates
     * which method of the Lobby/ControlPlayer has to be invoked and then it invokes the method through specific local methods
     * --> These local methods are used for separating the parsing from the invokation of the methods, and for having a cleaner code
     *
     * The method can parse 3 type of request coming from the 3 interfaces ClientServerHandler, ControllerAksNotify and GameHandler
     *  --> Before to parse the Action it parses the Interface field in order to individuate the correct branch of the incoming request
     * @throws RemoteException
     */
    public synchronized void startServer() throws RemoteException {

        JSONObject request = new JSONObject();
        JSONObject response = new JSONObject();

        while(true) {

            if (!request.equals(null)) request.clear();
            if (!response.equals(null)) response.clear();

            try {
                request = inServer.read();
            } catch (InvalidOperationException e) {
                System.out.println("SocketLobby --- InvalidOperationException occurred trying to read a new request");
                e.printStackTrace();
            } catch (IOException e) {
                System.out.println("SocketLobby --- IOException occurred trying to read a new request");
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                System.out.println("SocketLobby --- ClassNotFoundException occurred trying to read a new request");
                e.printStackTrace();
            }

            String Interface = (String) request.get("Interface");
            String Action = (String) request.get("Action");

            switch (Interface){



                //ClientServerHandler
                case "ClientServerHandler":

                    switch (Action){

                        //login
                        case "login":
                            cp = TCPLogin(request);
                            break;

                        //setNumberOfPlayers
                        case "setNumberOfPlayers":
                            TCPSetNumberOfPlayers(request);
                            break;

                        //leaveGame
                        case "leaveGame":
                            TCPLeaveGame(request);
                            break;

                        //continueGame
                        case "continueGame":
                            TCPContinueGame(request);
                            break;

                        //pong
                        case "pong":
                            TCPPong(request);
                            break;

                    }

                    break;


                //ControllerAskNotify
                case "ControllerAskNotify":

                    switch (Action){

                    }

                    break;


                //GameHandler
                case "GameHandler":

                    switch (Action){

                        //chooseBoardTiles
                        case "chooseBoardTiles":
                            TCPChooseBoardTiles(request);
                            break;

                        //insertShelfTiles
                        case "insertShelfTiles":
                            TCPInsertShelfTiles(request);
                            break;

                        //passMyTurn
                        case "passMyTurn":
                            TCPPassMyTurn();
                            break;



                    }

                    break;


            }



        }
    }


    /**
     * It parses the nickname field and store the value into the nickname String then create an ArrayList<> that stores the input/output streams
     * Then invokes the lobby.login() method passing the nickname and the ArrayList as parameters
     * @param json object received from the client, it has been passed to this method in order to parse the nickname chose by the client
     *
     * @return GameHandler object in order to bind the class to its own ControlPlayer
     */
    public GameHandler TCPLogin(JSONObject json){

        String nickname = (String) json.get("Param1");

        ArrayList<Stream> streams = new ArrayList<>();
        streams.add(outServer);
        streams.add(inServer);

        try{
            return lobby.login(nickname,streams);
        } catch (LoginException e) {
            throw new RuntimeException(e);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    public void TCPSetNumberOfPlayers(JSONObject json){

        int n = (int) json.get("Param1");
        String nickname = (String) json.get("Param2");

        try{
            lobby.setNumberOfPlayers(n,nickname);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

    }


    public void TCPLeaveGame(JSONObject json){

        String nickname = (String) json.get("Param1");
        int gameId = (int) json.get("Param2");

        try{
            lobby.leaveGame(nickname,gameId);
        } catch (LoginException e) {
            throw new RuntimeException(e);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

    }


    public void TCPContinueGame(JSONObject json){

        // --> Work-in-progress

    }


    public void TCPChooseBoardTiles(JSONObject json){

        List<Integer> coord = (List<Integer>) json.get("Param1");

        try{
            cp.chooseBoardTiles(coord);
        } catch (InvalidChoiceException e) {
            throw new RuntimeException(e);
        } catch (NotConnectedException e) {
            throw new RuntimeException(e);
        } catch (InvalidParametersException e) {
            throw new RuntimeException(e);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        } catch (NotMyTurnException e) {
            throw new RuntimeException(e);
        }

    }




    public void TCPInsertShelfTiles(JSONObject json){

        int column = (int) json.get("Param1");
        List<Integer> coord = (List<Integer>) json.get("Param2");

        try{
            cp.insertShelfTiles(column,coord);
        } catch (InvalidChoiceException e) {
            throw new RuntimeException(e);
        } catch (NotConnectedException e) {
            throw new RuntimeException(e);
        } catch (InvalidLenghtException e) {
            throw new RuntimeException(e);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        } catch (NotMyTurnException e) {
            throw new RuntimeException(e);
        }

    }


    public void TCPPong(JSONObject json){

        //System.out.println("*** tcpPong()");

        try {
            String nickname = (String) json.get("Param1");
            int gameId = (int) json.get("Param2");

            lobby.pong(nickname,gameId);

        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

    }



    public void TCPPassMyTurn(){

        try{
            cp.passMyTurn();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

    }






    @Override
    public void run() {
        try {
            startServer();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

}
