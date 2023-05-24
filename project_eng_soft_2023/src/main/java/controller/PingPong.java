package controller;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * check if the player is still connected
 */
public class PingPong implements Runnable{
    ControlPlayer controlPlayer;
    private boolean connected;

    static Lock lock=new ReentrantLock();

    private int counter;

    public PingPong(ControlPlayer cp){

        controlPlayer=cp;
        connected=false;
        counter=0;

    }

    @Override
    public void run() {

        while( true ){

            (new Thread(new PingClient())).start(); //

            try {
                Thread.sleep(1000); //wait for 5 seconds
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            //depending on the result after the sleep I set the status of the player
            if(connected){
                //if connected I change the status only if before he wasn't Online
                if(controlPlayer.getPlayerStatus().equals(PlayerStatus.NOT_ONLINE) ){
                    controlPlayer.setPlayerStatus(PlayerStatus.NOT_MY_TURN);
                }
                counter=0;
            } else{

                if(controlPlayer.getPlayerStatus().equals(PlayerStatus.WAITING_ROOM)){
                    ServerApp.lobby.removeFromWaitingRoom(controlPlayer);
                    System.out.println("    "+controlPlayer.getPlayerNickname()+"  left the waiting room ");
                    break;
                }

                controlPlayer.setPlayerStatus(PlayerStatus.NOT_ONLINE);
                if (counter==0) System.out.println("    "+controlPlayer.getPlayerNickname()+" went offline ");

                counter++;

                //when counter reaches 12( -> 1 minute offline ) I disconnect the player from the game
                if(counter >=12 ){

                    controlPlayer.getGame().removePlayer(controlPlayer);
                    System.out.println("    "+controlPlayer.getPlayerNickname()+" timeout connection: removed from his game");

                    break;
                }
            }


            //only if you acquire the lock u'll check the possible game suspension
            if ( lock.tryLock() && controlPlayer.getPlayerStatus().equals(PlayerStatus.MY_TURN)){
                try {
                    // manipulate protected state
                    verifyGameSuspension();

                } finally {
                    lock.unlock();
                }
            }
        }
    }

    class PingClient implements Runnable{

        @Override
        public void run() {

            try{
                //System.out.println("    ping to " + controlPlayer.getPlayerNickname());
               // connected=controlPlayer.askPing();
            }catch (Exception e){
                connected=false;
            }
        }
    }

    public boolean getConnectionStatus(){
        return connected;
    }


    public void verifyGameSuspension(){

        //se sono presenti meno di due giocatori
        if(controlPlayer.getGame().getPlayers().size()<2){
            //setto lo stato del gioco a "SUSPENDED"
            controlPlayer.getGame().setGameStatus(GameStatus.SUSPENDED);

            //aspetto 20 secondi sperando si riconnetta qualcuno
            try {
                Thread.sleep(20000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            //se non si è riconnesso nessuno il gioco termina e notifico l'utente del termine del gioco
            if(controlPlayer.getGame().getPlayers().size()<2){
                controlPlayer.getGame().setGameStatus(GameStatus.END_GAME);
                controlPlayer.getGame().endTurn();
                System.out.println("   Game "+ controlPlayer.getGame().getGameID() + " ended due to a lack of players");
            }
        }
    }
}

