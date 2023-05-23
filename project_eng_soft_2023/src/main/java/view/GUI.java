package view;

import client.Client;
import client.ClientModel;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import myShelfieException.LoginException;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class GUI extends View {
    private int numOfPlayer;
    private LoginController loginController;
    private GameController gameController;
    private String[] args;

    private Timer timer;
    ClientModel clientModel;
    public GUI() {
        this.numOfPlayer = 0;

    }


    @Override
    public int getNumOfPlayer() {
        Platform.runLater(loginController::showEnterNumOfPlayer);
        timer=new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if (timer != null) {
                    numOfPlayer = -1;
                    timer.cancel();
                    timer = null;
                }
            }
        };
        timer.schedule(task, 30000);
        while(numOfPlayer==0){
        }
        if(numOfPlayer==-1) {
            Platform.runLater(()-> loginController.showException("Time is out!"));
        }
        return numOfPlayer;
    }



    @Override
    public void updateBoard() {
        //TODO when update from game controller is complete
    }

    @Override
    public void endGame(Map< Integer, String> results) {
        //TODO when showrank from controller is complete


    }

    @Override
    public void isYourTurn() {
        Platform.runLater(()->gameController.startTurn());
    }

    @Override
    public void startGame() {
        //TODO start GUI application ???
    }

    @Override
    public void endYourTurn() {
        Platform.runLater(()->gameController.endTurn());
    }

    @Override
    public void startPlay() {
        try{
            loginController.showGameScene();
        } catch (Exception ignored){
        }
    }

    public void setNumOfPlayer(int n){
        numOfPlayer=n;
    }
    public void setLoginController(LoginController loginController) {
        this.loginController=loginController;
    }

    public void setGameController(GameController gameController) {
        this.gameController=gameController;
    }

    public Timer getTimer(){
        return timer;
    }

    public void setTimer(Timer timer) {
        this.timer=timer;
    }


    public void login (Client client,String nickName) {
        try{
            client.askLogin(nickName);
        } catch (LoginException e) {
            throw new RuntimeException(e);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
