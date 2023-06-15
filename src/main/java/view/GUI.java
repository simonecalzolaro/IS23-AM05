package view;

import javafx.application.Platform;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Map;
import java.util.Timer;

public class GUI extends View {
    private int numOfPlayer;
    private LoginController loginController;
    private GameController gameController;
    private ChatController chatController;
    private Timer timer;

    private Stage chatStage;

    @Override
    public void standardLogin() {
        Platform.runLater(()->{
            loginController.showLogin();
        });
    }


    @Override
    public void showException(String exception) {
        if(gameController!=null){
            Platform.runLater(()-> gameController.showException(exception));
            }
    }

    @Override
    public void plotNewMessage(String Sender,String message) {

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
    public void endGame(Map<String, Integer> results) {
        Platform.runLater(()-> {
            try {
                gameController.endGame(results);
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
        if(checkForBackupFile()){
            backupLogin();
            Platform.runLater(()-> {
                try {
                    while(client==null);
                    loginController.setClient(client);
                    loginController.showGameScene();
                    gameController.showGame();

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        } else {
            standardLogin();
        }
    }

    /**
     *
     */
    @Override
    public void endYourTurn() {
        Platform.runLater(()->gameController.endTurn());
    }

    @Override
    public void startPlay()  {
        try{
            Platform.runLater(()-> {
                try {
                    loginController.showGameScene();
                    gameController.updateAll();
                    gameController.showGame();
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

    public void setChatController(ChatController chatController) {
        this.chatController=chatController;
    }

    public void setChatStage(Stage chatStage) {
        this.chatStage=chatStage;
    }

    public Stage getChatStage(){
        return chatStage;
    }
}
