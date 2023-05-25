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

    public PingFromServer(Client client){

        this.client=client;
        connected=false;
        counter=0;

    }

    @Override
    public void run() {

        counter=0;

        while( true  ){

            connected=false;

            try {
                Thread.sleep(10000); //wait for 5 seconds
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            //depending on the result after the sleep I set the status of the player
            if(connected){
                counter=0;
            }

            else{

                counter++;
                //when counter reaches 12( -> 1 minute offline ) I disconnect the player from the game
                if(counter ==2 ){
                    System.out.println("    OPSSS... the server is offline, wait for the reconnection...");
                }

                if(counter >=2 && counter <=6 ){
                    System.out.println("    ...    ");
                }

                if(counter >= 7){

                    System.out.println("   trying to reconnect");

                    try {
                        client.askContinueGame();
                    } catch (LoginException e) {
                        throw new RuntimeException(e);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }finally {
                        System.out.println("    reconnection failed");
                    }
                }
            }
        }
    }

    public boolean getConnectionStatus(){
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

}