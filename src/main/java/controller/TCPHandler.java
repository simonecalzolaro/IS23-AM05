package controller;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TCPHandler {

    private String hostname;
    private Long PORT_pre;
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
        ServerSocket serverSocket = null;

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




    /**
     * This method set the server properties set the PORT where the server wants to receive the request from the client
     */
    protected void getServerSettings() {
        try{
            Object o = new JSONParser().parse(new FileReader("src/main/config/header.json")); //C:/Users/Utente/IS23-AM05/project_eng_soft_2023/
            JSONObject j =(JSONObject) o;
            Map arg = new LinkedHashMap();
            arg = (Map) j.get("serverSettings");

            hostname = (String) arg.get("hostname");
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
