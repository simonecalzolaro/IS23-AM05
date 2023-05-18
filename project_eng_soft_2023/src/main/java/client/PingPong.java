package client;


import myShelfieException.LoginException;

import java.io.IOException;

/**
 * check if the player is still connected
 */
public class PingPong implements Runnable{

    Client client;
    private boolean connected;
    private int counter;

    public PingPong(Client client){

        this.client=client;
        connected=false;
        counter=0;

    }

    @Override
    public void run() {

        counter=0;

        while( true  ){

            (new Thread(new PingServer())).start(); //

            try {
                Thread.sleep(1000); //wait for 5 seconds
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            //depending on the result after the sleep I set the status of the player
            if(connected){
                if(counter > 0 ) System.out.println("     u r connected again! ");
                counter=0;
            }

            else{

                counter++;
                //when counter reaches 12( -> 1 minute offline ) I disconnect the player from the game
                if(counter ==4 ){
                    System.out.println("    OPSSS... the server is offline, wait for the reconnection...");
                }

                if(counter >=4 && counter <=6 ){
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

    class PingServer implements Runnable{

        @Override
        public void run() {

            try{
               // System.out.println("    ping to Server");
                connected=client.askPing();
            } catch (Exception e){
                connected=false;
            }
        }
    }

    public boolean getConnectionStatus(){
        return connected;
    }
}