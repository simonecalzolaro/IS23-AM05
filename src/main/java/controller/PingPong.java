package controller;

import myShelfieException.LoginException;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

/**
 * check if the player is still connected
 */
public class PingPong implements Runnable{
    private ControlPlayer controlPlayer;
    private boolean connected;
    private boolean flag;
    private int counter;
    private static Lock gameSusp= new ReentrantLock();

    public PingPong(ControlPlayer cp){

        controlPlayer=cp;
        connected=false;
        counter=0;
        flag=true;

    }

    @Override
    public void run() {

        while( flag ){

            try{
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

                    if (counter==1){

                        if(controlPlayer.getPlayerStatus().equals(PlayerStatus.WAITING_ROOM) || controlPlayer.getPlayerStatus().equals(PlayerStatus.nOfPlayerAsked)){
                            ServerApp.lobby.removeFromWaitingRoom(controlPlayer);
                            System.out.println("    "+controlPlayer.getPlayerNickname()+"  left the waiting room ");
                            stopPingProcess();
                            return;
                        }else{
                            controlPlayer.setPlayerStatus(PlayerStatus.NOT_ONLINE);
                            System.out.println("    "+controlPlayer.getPlayerNickname()+" went offline ");
                            new Thread(()->gameSuspension()).start();
                        }
                    }
                    counter++;
                }
            }catch(NullPointerException npe){
                System.out.println(" --- error: nullPointerException occurred");
            }
        }

        System.out.println(controlPlayer.getPlayerNickname()+" pingPong process stopped");

    }

    public void gameSuspension(){

        synchronized (gameSusp){

            System.out.println("    check game suspension, current number of online players:" + controlPlayer.getGame().getPlayers()
                    .stream()
                    .filter(x -> !x.getPlayerStatus().equals(PlayerStatus.NOT_ONLINE))
                    .toList()
                    .size() );

            //se sono presenti meno di due giocatori se il gioco è in corso...
            if( controlPlayer.getGame().getPlayers()
                    .stream()
                    .filter(x -> !x.getPlayerStatus().equals(PlayerStatus.NOT_ONLINE))
                    .toList()
                    .size() < 2
                    && controlPlayer.getGame().getGameStatus().equals((GameStatus.PLAYING))) {

                //setto lo stato del gioco a "SUSPENDED"
                System.out.println("-----setting game ID="+controlPlayer.getGame().getGameID()+" to SUSPENDED with "+controlPlayer.getPlayerNickname());
                controlPlayer.getGame().setGameStatus(GameStatus.SUSPENDED);

                //aspetto 10 secondi sperando si riconnetta qualcuno
                try {
                    System.out.println("-----waiting for someone to reconnect");
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                //se non si è riconnesso nessuno negli scorsi 10 secondi il gioco termina e notifico l'utente del termine del gioco
                if( controlPlayer.getGame().getPlayers()
                        .stream()
                        .filter(x->!x.getPlayerStatus().equals(PlayerStatus.NOT_ONLINE))
                        .toList()
                        .size()<2 ){
                    try {

                        System.out.println("-----Game " + controlPlayer.getGame().getGameID() + " ended due to a lack of players");
                        //controlPlayer.getGame().endTurn();
                        ServerApp.lobby.quitGameIDandNotify(controlPlayer.getGame());

                    }catch(Exception e){
                        System.out.println("---error: impossible to end the Game "+controlPlayer.getGame().getGameID()+" in verifyGameSuspension()!");
                    }
                } else{
                    controlPlayer.getGame().setGameStatus(GameStatus.PLAYING);
                    //controlPlayer.getGame().endTurn();
                }
            }
        }
    }

    public boolean getConnectionStatus(){
        return connected;
    }

    public void setConnected() {
        this.connected = true;
    }

    public void stopPingProcess(){
        flag=false;
    }

}

