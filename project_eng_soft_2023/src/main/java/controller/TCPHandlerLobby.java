package controller;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TCPHandlerLobby {

    static int PORT = 1236;

    public void startServer(){

        ExecutorService executor = Executors.newCachedThreadPool();
        System.out.println("Hello from TCPHandler");

        ServerSocket serverSocket;

        try{
            serverSocket = new ServerSocket(PORT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println("TCPHandler ready");

        while(true){

            try{

                Socket socket = serverSocket.accept();
                executor.submit(new SocketServer(socket));
            } catch (IOException e) {
                System.out.println("error");
                e.printStackTrace();
            }
        }

    }
}
