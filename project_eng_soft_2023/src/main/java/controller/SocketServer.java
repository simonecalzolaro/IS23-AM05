package controller;


import client.ClientHandler;
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
            login(nick, socketCP);
        }

    }

    public void TCPContinueGame(JSONObject json) throws LoginException, RemoteException {

        String nick = (String) json.get("param1");


        synchronized (loginLock){
            continueGame(nick, socket);
        }

    }

    public void TCPLeaveGame(JSONObject json) throws LoginException {

        String nick = (String) json.get("param1");

        synchronized (loginLock){
            leaveGame(nick);
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
