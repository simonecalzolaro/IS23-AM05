package view;

import client.ClientModel;

import java.util.Timer;
import java.util.TimerTask;

public class GUI implements View{
    private int numOfPlayer;
    private final LoginController loginController;
    private GameController gameController;

    private Timer timer;
    ClientModel clientModel;
    public GUI(LoginController loginController) {
        this.numOfPlayer = 0;
        this.loginController = loginController;
    }

    public void setClientModel(ClientModel clientModel) {
        this.clientModel = clientModel;
    }

    @Override
    public int getNumOfPlayer() {
        loginController.showEnterNumOfPlayer();
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
            loginController.showException("Time is out!");
        }
        return numOfPlayer;
    }



    @Override
    public void updateBoard() {

    }

    @Override
    public void endGame() {

    }

    @Override
    public void isYourTurn() {

    }

    @Override
    public void startGame() {
        try{
            loginController.showGameScene();
        } catch (Exception ignored){
        }
    }

    @Override
    public void endYourTurn() {

    }
    public void setNumOfPlayer(int n){
        numOfPlayer=n;
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
}
