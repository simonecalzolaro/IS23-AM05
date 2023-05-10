package controller;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TCPHandlerLobby {

    static int PORT = 1235;

    public TCPHandlerLobby() {

        ServerSocket serverSocket;

        try{
            serverSocket = new ServerSocket(PORT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        new Thread(() -> startServer(serverSocket));
        System.out.println("----TCPHandler ready----");
    }

    public void startServer(ServerSocket serverSocket){

        ExecutorService executor = Executors.newCachedThreadPool();

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
