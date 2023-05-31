package controller;

import java.io.IOException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * check if the player is still connected
 */
public class PingPong implements Runnable{
    ControlPlayer controlPlayer;
    private boolean connected;
    private boolean flag;

    static Lock lock=new ReentrantLock();

    private int counter;

    public PingPong(ControlPlayer cp){

        controlPlayer=cp;
        connected=false;
        counter=0;
        flag=true;

    }

    @Override
    public void run() {


        while( flag ){

            //set the connection to false and sending a ping request
            connected=false;
            try {
                //System.out.println("*** ask ping to "+controlPlayer.getPlayerNickname());
                controlPlayer.askPing();
            } catch (Exception e) {
                if(counter==1) System.out.println(" --- error: impossible to ask ping to "+controlPlayer.getPlayerNickname());
            }


            //waiting a little
            try {
                Thread.sleep(5000); //wait for 5 seconds
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

            }else{

                if(controlPlayer.getPlayerStatus().equals(PlayerStatus.WAITING_ROOM) || controlPlayer.getPlayerStatus().equals(PlayerStatus.nOfPlayerAsked)){
                    ServerApp.lobby.removeFromWaitingRoom(controlPlayer);
                    System.out.println("    "+controlPlayer.getPlayerNickname()+"  left the waiting room ");
                    break;
                }

                controlPlayer.setPlayerStatus(PlayerStatus.NOT_ONLINE);
                if (counter==1) System.out.println("    "+controlPlayer.getPlayerNickname()+" went offline ");

                counter++;

            }


            //only if you acquire the lock u'll check the possible game suspension
            if ( !controlPlayer.getPlayerStatus().equals(PlayerStatus.NOT_ONLINE)){
                //System.out.println(" check game suspension");
                new Thread(this::verifyGameSuspension);

            }
        }

        System.out.println(controlPlayer.getPlayerNickname()+" pingPong process stopped");

    }


    public boolean getConnectionStatus(){
        return connected;
    }

    public void setConnected() {
        this.connected = true;
    }

    public void verifyGameSuspension(){

        //System.out.println(controlPlayer.getPlayerNickname()+" verifyGameSuspension()");
        //se sono presenti meno di due giocatori
        if( controlPlayer.getGame().getPlayers()
                                    .stream()
                                    .filter(x->!x.getPlayerStatus().equals(PlayerStatus.NOT_ONLINE))
                                    .toList()
                                    .size() < 2 ){

            //setto lo stato del gioco a "SUSPENDED"
            System.out.println("-----setting game ID="+controlPlayer.getGame().getGameID()+" to SUSPENDED with "+controlPlayer.getPlayerNickname());
            controlPlayer.getGame().setGameStatus(GameStatus.SUSPENDED);

            //aspetto 20 secondi sperando si riconnetta qualcuno
            try {
                System.out.println("-----waiting for someone to reconnect");
                Thread.sleep(20000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            //se non si è riconnesso nessuno il gioco termina e notifico l'utente del termine del gioco
            if( controlPlayer.getGame().getPlayers()
                                        .stream()
                                        .filter(x->!x.getPlayerStatus().equals(PlayerStatus.NOT_ONLINE))
                                        .toList()
                                        .size()<2 ){
                try {

                    System.out.println("   Game " + controlPlayer.getGame().getGameID() + " ended due to a lack of players");
                    controlPlayer.getGame().setGameStatus(GameStatus.END_GAME);
                    controlPlayer.getGame().endTurn();
                    ServerApp.lobby.quitGameIDandNotify(controlPlayer.getGame());

                }catch(Exception e){
                    System.out.println("---error: impossible to end the Game "+controlPlayer.getGame().getGameID()+" in verifyGameSuspension()!");
                }
            }
            else{
                controlPlayer.getGame().setGameStatus(GameStatus.PLAYING);
            }
        }
    }

    public void stopPingProcess(){
        flag=false;
    }

}

