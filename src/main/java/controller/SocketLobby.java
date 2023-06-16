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


    /**
     * Responsible for receiving request, as JSONObjects, then it parse the field 'Action' of the object received that indicates
     * which method of the Lobby/ControlPlayer has to be invoked, then it invokes the method through specific local methods
     * --> These local methods are used for separating the parsing from the invocation of the methods, and for having a cleaner code
     * The method can parse 3 type of request coming from the 2 kind of interfaces: ClientServerHandler, GameHandler
     * @throws IOException thrown from here when fatal errors occurs and force the thread to stop
     * @throws LoginException thrown from here when fatal errors occurs and force the thread to stop
     */
    public synchronized void startServer() throws IOException,ClassNotFoundException {

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


            }



        }
    }


    /**
     * It parses the nickname field and store the value into the nickname String then create an ArrayList<> that stores the input/output streams
     * Then invokes the lobby.login() method passing the nickname and the ArrayList as parameters
     * @param json contains all the parameters to parse
     * @return GameHandler object in order to bind the class to its own ControlPlayer
     * @throws LoginException this exception occurs when the client is unable to login, it forces the ending of the thread
     * @throws IOException this exception may occur for several reasons, it forces the ending of the thread
     */
    public GameHandler TCPLogin(JSONObject json){

        String nickname = (String) json.get("Param1");

        ArrayList<Stream> streams = new ArrayList<>();
        streams.add(outServer);
        streams.add(inServer);

        try{
            return lobby.login(nickname,streams);
        } catch (LoginException e) {
           // --> Da gestire per TUI e GUI
        } catch (RemoteException e) {
            System.out.println("");
        } catch (IOException e) {
            System.out.println("SocketLobby --- IOException encountered trying to read the file");
        }

        return null;

    }


    /**
     * It parses the number of players received from the client and its nickname
     * Then invokes the Lobby.setNumberOfPlayers() for setting the number of players of the game that it's going to be started
     * @param json contains all the parameters to parse
     */
    public void TCPSetNumberOfPlayers(JSONObject json){

        int n = (int) json.get("Param1");
        String nickname = (String) json.get("Param2");

        try{
            lobby.setNumberOfPlayers(n,nickname);
        }
        catch (RemoteException e) {
            System.out.println("");
        }
        catch (IllegalArgumentException e){
            System.out.println("SocketLobby --- Client tried to perform an invalid action");
        }

    }


    /**
     * It parses the nickname of the player that wants to leave the game and the ID of the game that the player wants to leave
     * Then invokes the Lobby.leaveGame() for leaving the game
     * @param json contains all the parameters to parse
     * @throws LoginException occurs when the client is unable to leave the game
     */
    public void TCPLeaveGame(JSONObject json){

        String nickname = (String) json.get("Param1");
        int gameId = (int) json.get("Param2");

        try{
            lobby.leaveGame(nickname,gameId);
        } catch (LoginException e) {
            e.getMessage();
        } catch (RemoteException e){
            System.out.println("");
        }

    }



    /**
     * It parses the nickname of the client who wants to continue the game he was playing before a disconnection/crash, then it parses the gameID of the game he wants to continue
     * Then it calls the Lobby.continueGame()
     * If some errors occurs it invoke the throwLoginException() method which tells the client that he's unable to continue the game and returns null
     * @param json contains all the parameters to parse
     * @return GameHandler object which is the reference of the ControlPlayer used for playing, null if some errors occurs trying to continue the game
     * @throws LoginException thrown when the client is unable to continue the game that he was playing, this is caught in the startServer() in order to invoke the throwLoginException() method
     */
    public GameHandler TCPContinueGame(JSONObject json){

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
            System.out.println("");
        }

        return null;

    }


    /**
     *It parses the coordinates of the tiles that the client has chosen from the board
     *Then it invokes the ControlPlayer.chooseBoardTiles() which will communicate this coordinated to the model
     * @param json contains all the parameters to parse
     * @throws InvalidChoiceException thrown when client picks a wrong tile from the board
     * @throws NotConnectedException thrown when the client is not connected
     * @throws InvalidParametersException thrown when the client asks for wrong parameters
     * @throws NotMyTurnException thrown when a client try to make a move while it's not its turn
     */
    public void TCPChooseBoardTiles(JSONObject json){

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
           System.out.println("");
        } catch (NotMyTurnException e) {
           System.out.println("SocketLobby --- Client performed an action while is not its turn");
        }

    }



    /**
     * It parses the columns of the shelf where the player wants to insert the tiles, then parses the tiles to insert in it
     * Then invokes the ControlPlayer.insertShelfTiles() method
     * @param json contains all the parameters to parse
     * @throws InvalidChoiceException thrown when client picks a wrong tile from the board
     * @throws NotConnectedException thrown when the client is not connected
     * @throws NotMyTurnException thrown when a client try to make a move while it's not its turn
     */
    public void TCPInsertShelfTiles(JSONObject json){

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
           System.out.println("");
        } catch (NotMyTurnException e) {
            System.out.println("SocketLobby --- Client performed an action while is not its turn");
        }

    }


    /**
     * It parses the nickname of the clients who responded the ping with a pong and the gameID he's playing
     * @param json contains all the parameters to parse
     */
    public void TCPPong(JSONObject json){

        //System.out.println("*** tcpPong()");

        try {
            String nickname = (String) json.get("Param1");
            int gameId = (int) json.get("Param2");

            lobby.pong(nickname,gameId);

        } catch (RemoteException e) {
           System.out.println("");
        }

    }



    /**
     * Calls the ControlPlayer.passMyTurn()
     */
    public void TCPPassMyTurn(){

        try{
            cp.passMyTurn();
        } catch (RemoteException e) {
            System.out.println("");
        }

    }



    /**
     * It parses the message and the recipient of the message that client wants to send through the chat abstraction
     * @param json contains all the parameters to parse
     */
    public void TCPPostMessage(JSONObject json){

        String message = (String) json.get("Param1");
        ArrayList<String> recipients = (ArrayList<String>) json.get("Param2");

        try{
            cp.postMessage(message,recipients);
        } catch (RemoteException e) {
            System.out.println("");
        }

    }



    /**
     * This method is used for telling the client that a LoginException occurred in order let him know how to behave
     */
    public void throwLoginException(boolean kind){

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
            System.out.println("SocketLobby --- An Exception caused a fatal error while running the application\n---> Socket stopped\n---> Thread stopped");
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
