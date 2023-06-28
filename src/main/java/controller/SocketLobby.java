package controller;



import myShelfieException.*;
import org.json.simple.JSONObject;
import java.io.IOException;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class SocketLobby implements Runnable{

    Socket socketServer;
    Stream outServer;
    Stream inServer;

    JSONObject request;
    JSONObject response;

    GameHandler cp;

    private static Lobby lobby;


    /**
     * SocketLobby is responsible for all the requests coming from the client, it receives a JSONObject then execute the parsing of the Object's fields
     * in order to individuate the action invoked by the client with the respective parameters, then invokes the method on the Lobby through the lobby attribute
     * The constructor assigns the two parameters lobby and socket in order to execute request from the client on the server and sending/receiving data from the client
     * through the Socket
     * The input/output streams are abstracted with a Stream object which contains all method and attribute that enable the communication between client and the server over
     * the network
     *
     * @param lobby reference to the lobby, so that every request for the server will invoke a method on this attribute
     * @param socket created by the TCPHandler() this links the communication from the client to the server
     * @throws IOException thrown when a fatal error occurs, it forces the thread to stop
     */
    public SocketLobby(Lobby lobby, Socket socket) throws IOException {

        this.lobby = lobby;

        this.socketServer = socket;
        System.out.println("----SocketServer ready----");

        try{
            outServer = new Stream(socketServer,0);
            inServer = new Stream(socketServer,1);
        }catch (InvalidParametersException e){
            System.out.println("SocketLobby --- Fatal error encountered while setting up the socket stream\n---> Socket closed\n---> Thread stopped");
            socket.close();
        }


    }


    private synchronized void startServer() throws IOException,ClassNotFoundException {

        request = new JSONObject();
        response = new JSONObject();



        while(true) {

            request.clear();
            response.clear();

            try {
                request = inServer.read();
            } catch (InvalidOperationException e) {
                System.out.println("SocketLobby --- InvalidOperationException occurred trying to read a new request\n---> Application keeps running");
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
                                cp = TCPContinueGame(request);
                                cp.restoreSession();

                                break;

                            //pong
                            case "pong":
                                TCPPong(request);
                                break;

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

                            //postMessage
                            case "postMessage":
                                TCPPostMessage(request);
                                break;



                        }

                        break;

                    default:
                        System.out.println("SocketLobby --- Unknown request received from client ---> "+Interface+" "+Action);
                        break;


                }

            }
        
    }


    private GameHandler TCPLogin(JSONObject json){

        String nickname = (String) json.get("Param1");

        ArrayList<Stream> streams = new ArrayList<>();
        streams.add(outServer);
        streams.add(inServer);

        try{
            return lobby.login(nickname,streams);
        } catch (LoginException e) {
           throwLoginException(false);
        } catch (RemoteException e) {
            System.out.println("SocketLobby --- RemoteException occurred");
        } catch (IOException e) {
            System.out.println("SocketLobby --- IOException encountered trying to read the file");
        }

        return null;

    }


    private void TCPSetNumberOfPlayers(JSONObject json){

        int n = (int) json.get("Param1");
        String nickname = (String) json.get("Param2");

        try{
            lobby.setNumberOfPlayers(n,nickname);
        }
        catch (RemoteException e) {
            System.out.println("SocketLobby --- RemoteException occurred");
        }
        catch (IllegalArgumentException e){
            System.out.println("SocketLobby --- Client tried to perform an invalid action");
        }

    }


    private void TCPLeaveGame(JSONObject json){

        String nickname = (String) json.get("Param1");
        int gameId = (int) json.get("Param2");

        try{
            lobby.leaveGame(nickname,gameId);
        } catch (LoginException e) {
            e.getMessage();
        } catch (RemoteException e){
            System.out.println("SocketLobby --- RemoteException occurred");
        }

    }


    private GameHandler TCPContinueGame(JSONObject json){

        String nickname = (String) json.get("Param1");
        int gameID = (int) json.get("Param2");

        ArrayList<Stream> streams = new ArrayList<>();
        streams.add(outServer);
        streams.add(inServer);

        try{
            return lobby.continueGame(nickname,streams,gameID);
        } catch (LoginException e) {
            throwLoginException(true);
        } catch (RemoteException e) {
            System.out.println("SocketLobby --- RemoteException occurred");
        }

        return null;

    }



    private void TCPChooseBoardTiles(JSONObject json){

        List<Integer> coord = (List<Integer>) json.get("Param1");

        try{
            cp.chooseBoardTiles(coord);
        } catch (InvalidChoiceException e) {
            System.out.println("SocketLobby --- Invalid choice performed by the client");
        } catch (NotConnectedException e) {
            System.out.println("SocketLobby --- Client not connected");
        } catch (InvalidParametersException e) {
            System.out.println("SocketLobby --- Invalid parameter inserted by the client");
        } catch (RemoteException e) {
            System.out.println("SocketLobby --- RemoteException occurred");
        } catch (NotMyTurnException e) {
           System.out.println("SocketLobby --- Client performed an action while is not its turn");
        }

    }




    private void TCPInsertShelfTiles(JSONObject json){

        int column = (int) json.get("Param1");
        List<Integer> coord = (List<Integer>) json.get("Param2");

        try{
            cp.insertShelfTiles(column,coord);
        } catch (InvalidChoiceException e) {
            System.out.println("SocketLobby --- Invalid choice performed by the client");
        } catch (NotConnectedException e) {
            System.out.println("SocketLobby --- Client not connected");
        } catch (InvalidLenghtException e) {
            System.out.println("SocketLobby --- Invalid length !!!");
        } catch (RemoteException e) {
            System.out.println("SocketLobby --- RemoteException occurred");
        } catch (NotMyTurnException e) {
            System.out.println("SocketLobby --- Client performed an action while is not its turn");
        }

    }


    private void TCPPong(JSONObject json){

        //System.out.println("*** tcpPong()");

        try {
            String nickname = (String) json.get("Param1");
            int gameId = (int) json.get("Param2");

            lobby.pong(nickname,gameId);

        } catch (RemoteException e) {
            System.out.println("SocketLobby --- RemoteException occurred");
        }

    }


    private void TCPPassMyTurn(){

        try{
            cp.passMyTurn();
        } catch (RemoteException e) {
            System.out.println("SocketLobby --- RemoteException occurred");
        }

    }


    private void TCPPostMessage(JSONObject json){

        String message = (String) json.get("Param1");
        ArrayList<String> recipients = (ArrayList<String>) json.get("Param2");

        try{
            cp.postMessage(message,recipients);
        } catch (RemoteException e) {
            System.out.println("SocketLobby --- RemoteException occurred");
        }

    }


    private void throwLoginException(boolean kind){

        JSONObject object = new JSONObject();

        object.put("Action","throwLoginException");
        object.put("Param1",kind);

        try{
            outServer.reset();
            outServer.write(object);
        } catch (InvalidOperationException e) {
            System.out.println("SocketControlPlayer --- InvalidOperationException occurred in notifyStartPlaying trying to reset/write the stream");
            System.out.println("---> Maybe you're trying to reset/write an input stream");
        } catch (IOException e) {
            System.out.println("SocketLobby --- IOException occurred trying to flush throwLoginException through the channel");
        }

    }





    /**
     * Starts the SocketLobby thread for each client who wants to play
     */
    @Override
    public void run() {
        try {
            startServer();
        }
        catch (IOException | ClassNotFoundException e){
            System.out.println("SocketLobby --- An Exception caused a fatal error while running the application by \n---> Socket stopped\n---> Thread stopped");
        }
        finally {
            try {
                socketServer.close();
            } catch (IOException e) {
                System.out.println("SocketLobby --- Unable to close the socket stream safely");
            }
        }
    }

}
