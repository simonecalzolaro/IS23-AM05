package client;

import client.Client;
import client.RMIClient;
import client.SocketClient;

import java.io.File;

public class ExceptionHandler {



    Client client;


    public ExceptionHandler(Client client){

        this.client = client;

    }

    /**
     *
     * @param kind = false --> from login;
     *             = true --> from continueGame
     */
    public void loginExceptionHandler(boolean kind){

        if(kind){
            System.out.println("Unable to find the game you left --> go and start a new game");
            File file = new File("src/main/config/backup.json");
            file.delete();
            client.getView().standardLogin();

        }
        else{

        }

    }

}
