package controller;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * Crucial component of the server architecture
 * It waits for socket TCP connection from the client in order to create a new SocketClient for every connection
 */
public class TCPHandler {

    private int PORT;

    public Lobby lobby;

    public TCPHandler(Lobby lobby){
        this.lobby = lobby;
    }


    /**
     * This method is always listening requests of connection from the clients
     * It implements a server-socket functionality
     * When a client try to connect to a server it creates a new socket then it creates a new SocketLobby linked to the client through the socket just created
     * The threads are cached into CachedThreadPool
     * It necessary in order to create a client-server connection via socket
     */

    public void startServer(){

        getServerSettings();

        System.out.println("--- TCPHandler ready ---");

        ExecutorService executor = Executors.newCachedThreadPool();
        ServerSocket serverSocket;

        try{
            serverSocket = new ServerSocket(PORT);
        } catch (IOException e) {
            System.out.println("TCPHandler --- IOException occurred trying to initialize the server socket");
            throw new RuntimeException();
        }


        while(true){

            try{
                Socket socket = serverSocket.accept();
                executor.submit(new SocketLobby(lobby,socket));
            }catch (IOException e){
                System.out.println("TCPHandler --- IOException occurred trying to create a new SocketLobby");
            }
        }

    }

    private void getServerSettings() {
        try{
            Long PORT_pre;
            Object o = new JSONParser().parse(new FileReader(System.getProperty("user.dir")+"/config/header.json"));
            JSONObject j =(JSONObject) o;
            Map arg;
            arg = (Map) j.get("serverSettings");

            PORT_pre = (Long) arg.get("TCPPORT");

            PORT = PORT_pre.intValue();

        } catch (FileNotFoundException e) {
            System.out.println(" --- FileNotFoundException occurred trying to retrieve server's information from header.json");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println(" --- IOException occurred trying to retrieve server's information from header.json");
            e.printStackTrace();
        } catch (ParseException e) {
            System.out.println(" --- ParseException occurred trying to retrieve server's information from header.json");
            e.printStackTrace();
        }
    }
}
