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
                Thread.sleep(12000); //wait for 5 seconds
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
                    if(counter ==1 ) client.getView().showException("---ops...you are offline");
                        //System.out.println("    OPSSS... the server is offline, wait for the reconnection...");

                        /*
                    if (counter >= 3) {

                        System.out.println("   trying to reconnect");

                        try {
                            client.askContinueGame();
                        } catch (LoginException | IOException e) {
                            client.getView().showException(e.getMessage());
                            client.getView().showException("---ops ...reconnection failed...");
                        }

                         */
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