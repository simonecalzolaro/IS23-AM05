package controller;

/**
 * check if the player is still connected
 */
public class PingPong implements Runnable{

    ControlPlayer controlPlayer;
    boolean connected;

    public PingPong(ControlPlayer cp){

        controlPlayer=cp;
        connected=false;

    }

    @Override
    public void run() {

        while(true){

            (new Thread(new PingClient())).start(); //

            try {
                Thread.sleep(5000); //wait for 5 seconds
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            //depending on the result after the sleep I set the status of the player
            if(connected) controlPlayer.setPlayerStatus(PlayerStatus.NOT_MY_TURN);
            else controlPlayer.setPlayerStatus(PlayerStatus.NOT_ONLINE);

        }
    }

    class PingClient implements Runnable{

        @Override
        public void run() {
            try{
                connected=controlPlayer.askPing();
            }catch (Exception e){
                connected=false;
            }
        }
    }
}

