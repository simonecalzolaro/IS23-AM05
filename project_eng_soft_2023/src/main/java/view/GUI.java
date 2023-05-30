package view;

import client.ClientModel;

import javafx.application.Platform;


import java.io.IOException;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class GUI extends View {
    private int numOfPlayer;
    private LoginController loginController;
    private GameController gameController;

    private Timer timer;
    ClientModel clientModel;
    private RankController rankController;

    @Override
    public void showException(String exception) {

    }

    @Override
    public void plotNewMessage(String message) {

    }

    public GUI() {
        this.numOfPlayer = 0;
    }

    /**
     * asks the player to enter the number of players in the game
     */
    @Override
    public void getNumOfPlayer() {
        Platform.runLater(()-> loginController.showEnterNumOfPlayer());
    }



    @Override
    public void updateBoard() {
        if(gameController!=null){
            Platform.runLater(()->gameController.updateAll());
        }

    }

    @Override
    public void endGame(Map< Integer, String> results) {
        Platform.runLater(()-> {
            try {
                gameController.endGame();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

    }

    @Override
    public void isYourTurn() {
        Platform.runLater(()->gameController.startTurn());
    }

    @Override
    public void startGame() {
    }

    /**
     *
     */
    @Override
    public void endYourTurn() {
        Platform.runLater(()->gameController.endTurn());
    }

    @Override
    public void startPlay() {
        try{
            Platform.runLater(()-> {
                try {
                    loginController.showGameScene();
                    gameController.updateAll();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (Exception ignored){
        }
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


    public void setRankController(RankController rankController) {
        this.rankController=rankController;
    }
}
