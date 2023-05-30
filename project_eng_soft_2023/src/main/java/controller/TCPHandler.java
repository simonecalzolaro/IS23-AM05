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




    protected void getServerSettings() {
        try{
            Object o = new JSONParser().parse(new FileReader("C:/Users/Utente/IS23-AM05/project_eng_soft_2023/src/main/config/header.json"));
            JSONObject j =(JSONObject) o;
            Map arg = new LinkedHashMap();
            arg = (Map) j.get("serverSettings");

            hostname = (String) arg.get("hostname");
            PORT_pre = (Long) arg.get("TCPPORT");

            PORT = PORT_pre.intValue();

        } catch (FileNotFoundException e) {
            System.out.println("SocketClient --- FileNotFoundException occurred trying to retrieve server's information from header.json");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("SocketClient --- IOException occurred trying to retrieve server's information from header.json");
            e.printStackTrace();
        } catch (ParseException e) {
            System.out.println("SocketClient --- ParseException occurred trying to retrieve server's information from header.json");
            e.printStackTrace();
        }
    }
}
