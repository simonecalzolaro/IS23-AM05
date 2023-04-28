package controller;

import client.ClientApp;
import client.ClientHandler;
import model.Board;
import myShelfieException.LoginException;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.RemoteException;

public class SocketServer extends Lobby implements Runnable{

    Socket socket;
    static int count;

    ObjectOutputStream out;
    ObjectInputStream in;

    static Object loginLock;

    /**
     * constructor for the ServerApp
     *
     * @throws RemoteException
     */
    protected SocketServer(Socket socket) throws RemoteException {

        this.socket = socket;
    }

    @Override
    public void startServer() throws RemoteException {


        System.out.println("Thread num: " + count + " created");
        count++;

        while(true){
            try {

                in = new ObjectInputStream(socket.getInputStream());
                out = new ObjectOutputStream(socket.getOutputStream());

                String method;


                JSONObject json = null;

                while (json == null) {
                    json = (JSONObject) in.readObject();
                }

                method = (String) json.get("method");


                // gestisci vari metodi

                switch (method){
                    case "login":

                        TCPLogin(json);
                        break;

                    case "continueGame":
                        TCPContinueGame(json);
                        break;

                    case "leaveGame":
                        TCPLeaveGame(json);
                        break;




                }






            } catch (Exception e) {
                System.out.println("error x");
                e.printStackTrace();

            }
        }

    }

    //TCPLOGIN

    //synchronized static per evitare che più thread chiamino lo stesso metodo in contemporanea
    public void TCPLogin(JSONObject json) throws LoginException, IOException {

        String nick = (String) json.get("param1");
        ClientHandler ch = (ClientHandler) json.get("param2");
        Socket socketCP = (Socket) json.get("param3");

        synchronized (loginLock){
            login(nick, ch,socketCP);
        }


    }

    public void TCPContinueGame(JSONObject json) throws LoginException, RemoteException {

        String nick = (String) json.get("param1");
        ClientHandler ch = (ClientHandler) json.get("param2");

        synchronized (loginLock){
            continueGame(nick, ch);
        }


    }

    public void TCPLeaveGame(JSONObject json){
        ClientHandler ch = (ClientHandler) json.get("param1");

        synchronized (loginLock){
            leaveGame(ch);
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


    //Sono arrivato qui: finisci
    @Override
    public int askNumberOfPlayers(ClientHandler ch) throws IOException {

        int res = -1;


        JSONObject jo = new JSONObject();
        jo.put("method","askNumberOfPlayers");
        jo.put("param1",null);
        jo.put("param2",null);

        System.out.println(jo);

        out.writeObject(jo);
        out.flush();

        JSONObject json = null;

        while(json == null){
            try{
                json = (JSONObject) in.readObject();
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }

        }

        if(json.get("method").equals("enterNumberOfPlayers")){
            Long num = (Long) json.get("param1");
            res = num.intValue();
        } else throw new IOException();

        return  res;


    }



    /**
     * Method called by the client to log into the server to start a new game.
     * @param nickname is the name chosen by the client, if ti is already used throws IllegalArgumentException
     * @param ch is the client interface. I need it to associate each client interface to a ControlPlayer
     * @throws RemoteException
     */

    public GameHandler login(String nickname, ClientHandler ch,Socket socketCP) throws RemoteException, IOException, LoginException {

        if( clients.values().stream().map(x -> x.getPlayerNickname()).toList().contains(nickname) ) throw new LoginException("this nickname is not available at the moment");

        else {

            if(attendedPlayers==-1){ //if the isn't any waiting room it means that ch is the first player

                tempBoard = new Board();
                System.out.println("...a new board has been created...");

                do{
                    try {
                        //server asks client how many players he wants in his match
                        attendedPlayers = askNumberOfPlayers(ch);
                    } catch (RemoteException e) {
                        attendedPlayers = -1;
                        throw new RuntimeException(e);
                    }
                } while(attendedPlayers<2 || attendedPlayers>4); //eccezione da gestire

                //initializing the board with the chosen number of players
                tempBoard.initializeBoard(attendedPlayers);
                System.out.println("...player "+ nickname+ " created a game with "+ attendedPlayers+" players...");

            }

            //create a ControlPlayer
            ControlPlayer pl= new SocketControlPlayer(nickname, tempBoard,socketCP);
            pl.setClientHandler(ch);

            System.out.println("...player "+ nickname+ " entered the game ");

            //add to the map "clients" the ClientHandler interface and the associated ControlPlayer
            clients.put(ch, pl);
            //tempPlayers is like a waiting room
            tempPlayers.add(pl);

            //once the waiting room (tempPlayers) is full the Game is created and all the players are notified
            if(tempPlayers.size() == attendedPlayers){

                attendedPlayers = -1;
                Game g = new Game( tempPlayers , tempBoard );
                games.add( g );
                tempPlayers.clear();


                notifyStartPlaying(g);

                System.out.println("...The game has been created, participants: "+ g.getPlayers());

            }

            return pl;
        }
    }



    /**
     * this method tells to all users that the game has started and that they aren't anymore in the waiting room, is divided in RMI and socket
     * @param g
     * @throws RemoteException
     */
    public void notifyStartPlaying(Game g) throws RemoteException {

        for(ControlPlayer player: g.getPlayers()){
            try {
                    //RMI calling
                    player.setGame(g);
                    ClientHandler clih=player.getClientHandler();

                    JSONObject jo = new JSONObject();
                    jo.put("method","notifyStartPlaying");
                    jo.put("param1",player.getBookshelf().getPgc().getCardNumber());
                    jo.put("param2",g.getBoard().getCommonGoalCard1().getCGCnumber());
                    jo.put("param3",g.getBoard().getCommonGoalCard2().getCGCnumber());

                    System.out.println(jo);

                    out.writeObject(jo);
                    out.flush();

                    jo.clear();

                    jo.put("method","updateBoard");
                    jo.put("param1",g.getBoard().getBoard());

                    System.out.println(jo);

                    out.writeObject(jo);
                    out.flush();

                    if(g.getPlayers().get(g.getCurrPlayer()).equals(player)) {
                        jo.put("method","startYourTurn");
                        System.out.println(jo);

                        out.writeObject(jo);
                        out.flush();
                    }


            } catch (RemoteException e) { throw new RuntimeException(e); }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
        }


    }


}
