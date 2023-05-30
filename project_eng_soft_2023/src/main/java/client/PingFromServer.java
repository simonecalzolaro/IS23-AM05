package client;


import myShelfieException.LoginException;

import java.io.IOException;

/**
 * check if the player is still connected
 */
public class PingFromServer implements Runnable{

    Client client;
    private boolean connected;
    private int counter;
    private boolean flag;

    public PingFromServer(Client client){

        this.client=client;
        connected=false;
        counter=0;
        flag=true;

    }

    @Override
    public void run() {

        counter=0;

        while( flag  ){

            connected=false;

            try {
                Thread.sleep(10000); //wait for 5 seconds
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            //depending on the result after the sleep I set the status of the player
            if(connected){
                if(counter>0) client.getView().showException("---you are online again");
                counter=0;
            }

            else{

                counter++;

                if(client.isGameStarted()) {

                    //when counter reaches 6 ( 6 -> 60 sec offline ) I disconnect the player from the game
                    if(counter ==2 ){
                        client.getView().showException("---ops... the server is offline, wait for the reconnection...");
                        //System.out.println("    OPSSS... the server is offline, wait for the reconnection...");
                    }

                    if (counter >= 6) {

                        System.out.println("   trying to reconnect");
                        try {
                            client.askContinueGame();
                        } catch (LoginException e) {
                            client.getView().showException(e.getMessage());
                            throw new RuntimeException(e);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        } finally {
                            client.getView().showException("---ops ...reconnection failed...");
                            System.out.println("    reconnection failed");
                        }
                    }
                }
            }
        }
        System.out.println("ping stopped");
    }

    public boolean getConnectionStatus(){
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    public void stopPingProcess(){
        flag=false;
    }
}