package client;


import myShelfieException.LoginException;

import java.io.IOException;

/**
 * check if the player is still connected.
 * Every 10 seconds checks if in the past 10 seconds he received a ping from the server.
 */
public class PingFromServer implements Runnable{

    Client client;
    private boolean connected;
    private int counter;
    private boolean flag;

    /**
     * PingFromServer constructor.
     * @param client the associated client
     */
    public PingFromServer(Client client){

        this.client=client;
        connected=false;
        counter=0;
        flag=true;

    }


    @Override
    public void run() {

        counter=0;

        //System.out.println("pong process starts");
        while( flag  ){

            connected=false;

            try {
                Thread.sleep(12000); //wait for 12 seconds
            } catch (InterruptedException e) {
                client.getView().showException("--- error occurred while waiting ping from server");
            }

            //depending on the result after the sleep I set the status of the player
            if(connected){
                if(counter>1) client.getView().showException("---you are online again");
                counter=0;
            }
            else{

                counter++;

                if(client.isGameStarted() && !client.isGameEnded()) {

                    //when counter reaches 6 ( 6 -> 60 sec offline ) I disconnect the player from the game
                    if(counter ==1 ) {
                        client.getView().showException("---ops...you are offline");
                        client.setMyTurn(false);
                    }

                    client.getView().showException("---trying to reconnect...");

                }
            }
        }
        System.out.println("ping stopped");
    }

    /**
     * @return the ConnectionStatus (true= you are online/server up, false= you are offline/server down
     */
    public boolean getConnectionStatus(){
        return connected;
    }

    /**
     * set the connection status
     * @param connected attribute
     */
    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    /**
     * method to stop the ping process
     */
    public void stopPingProcess(){
        flag=false;
    }
}