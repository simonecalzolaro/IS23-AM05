package view;

import client.RMIClient;
import client.SocketClient;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import javafx.stage.Stage;
import myShelfieException.LoginException;


import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * GuiController for the login scene
 */
public class LoginController extends GUIController {
    private Timer timer1;
    private Timer timer2;

    private Timer enterNumTimer;


    private void timer(){
        timer1 = new Timer();
        timer2= new Timer();
        TimerTask task1=new TimerTask() {
            private int n=0;

            @Override
            public void run() {
                switch (n%4) {
                    case 0 -> Platform.runLater(() -> {
                        root.lookup("#image1").setVisible(false);
                        root.lookup("#image2").setVisible(true);});
                    case 1 -> Platform.runLater(() -> {
                        root.lookup("#image2").setVisible(false);
                        root.lookup("#image3").setVisible(true);});
                    case 2 -> Platform.runLater(() -> {
                        root.lookup("#image3").setVisible(false);
                        root.lookup("#image4").setVisible(true);});
                    case 3 -> Platform.runLater(() -> {
                        root.lookup("#image4").setVisible(false);
                        root.lookup("#image1").setVisible(true);});
                }
                n++;
                if(n==200) timer1.cancel();
            }
        };
        TimerTask task2=new TimerTask(){
            int num = 0;
            @Override
            public void run() {
                switch (num%4){
                    case 0 -> Platform.runLater(() -> ((Label) root.lookup("#waitingLabel")).setText("Waiting for players"));
                    case 1 -> Platform.runLater(() -> ((Label) root.lookup("#waitingLabel")).setText("Waiting for players."));
                    case 2 -> Platform.runLater(() -> ((Label) root.lookup("#waitingLabel")).setText("Waiting for players.."));
                    case 3 -> Platform.runLater(() -> ((Label) root.lookup("#waitingLabel")).setText("Waiting for players..."));
                }
                num++;
                if(num==800) timer2.cancel();
            }

        };
        timer1.schedule(task1, 3000, 3000);
        timer2.schedule(task2, 750, 750);
    }

    /**
     * Method invoked by the GUI View to show the login scene if there are no active game to restore
     */
    public void showLoginScene(){
        if(client==null){
            stage.show();
        } else {
            showEnterNickname();
            showException("This nickname already exists!");
        }

    }
    @FXML
    private void chooseProtocol(ActionEvent actionEvent) {

        root.lookup("#loginExceptionLabel").setDisable(true);
        root.lookup("#loginExceptionLabel").setVisible(false);

        try{
            if(actionEvent.getSource().equals(root.lookup("#rmiButton"))){

                client = new RMIClient();
                RMIClient.setView(gui);

            } else {
                client = new SocketClient();
                SocketClient.setView(gui);
            }
            client.initializeClient();
            gui.getChatController().setClient(client);
        } catch (Exception e){
            showException("Error! Try again!");
            return;
        }


        root.lookup("#rmiButton").isDisable();
        root.lookup("#socketButton").isDisable();
        root.lookup("#rmiButton").setVisible(false);
        root.lookup("#socketButton").setVisible(false);
        root.lookup("#chooseLabel").isDisable();
        root.lookup("#chooseLabel").setVisible(false);

        showEnterNickname();

    }

    private void showEnterNickname(){

        hideWaitingScene();
        root.lookup("#image1").setVisible(false);
        ((Label) root.lookup("#enterNicknameLabel")).setText("Enter your nickname:");
        root.lookup("#enterNicknameLabel").setVisible(true);
        root.lookup("#enterNicknameLabel").setDisable(false);
        root.lookup("#nameField").setDisable(false);
        root.lookup("#nameField").setVisible(true);
        root.lookup("#loginButton").setDisable(false);
        root.lookup("#loginButton").setVisible(true);
        root.lookup("#numField").setVisible(false);
        root.lookup("#numField").setDisable(true);
        root.lookup("#numButton").setDisable(true);
        root.lookup("#numButton").setVisible(false);
    }
    @FXML
    private void enterNickname(ActionEvent actionEvent){
        root.lookup("#loginExceptionLabel").setDisable(true);
        root.lookup("#loginExceptionLabel").setVisible(false);
        String nickname = ((TextField) root.lookup("#nameField")).getText();
        ((TextField) root.lookup("#nameField")).clear();
        try{
            client.getModel().setNickname(nickname);
            client.askLogin(nickname);
            showWaitingScene();

        } catch (LoginException e){
            showException("This nickname is not available! Try again!");
        } catch (Exception e){
            showException("Error! Try again!");
        }
    }

    /**
     * Method invoked by the GUI View to ask the user to enter the number of player to create a new game
     */
    public void showEnterNumOfPlayer(){

        hideWaitingScene();

        root.lookup("#enterNicknameLabel").setVisible(true);
        root.lookup("#enterNicknameLabel").setDisable(false);
        root.lookup("#loginExceptionLabel").setDisable(true);
        root.lookup("#loginExceptionLabel").setVisible(false);
        ((Label) root.lookup("#enterNicknameLabel")).setText("Enter number of player to create a new game:");
        root.lookup("#nameField").setDisable(true);
        root.lookup("#nameField").setVisible(false);
        root.lookup("#loginButton").setDisable(true);
        root.lookup("#loginButton").setVisible(false);

        root.lookup("#numField").setDisable(false);
        root.lookup("#numField").setVisible(true);
        root.lookup("#numButton").setDisable(false);
        root.lookup("#numButton").setVisible(true);


        if(enterNumTimer!=null){
            enterNumTimer.cancel();
            enterNumTimer=null;
        }

        enterNumTimer=new Timer();
        TimerTask showWaitingScemeTask = new TimerTask() {
            @Override
            public void run() {
                try {
                    Platform.runLater(()->{
                        showEnterNickname();
                        showException("Time is up! Login again!");
                    });
                    client.askLeaveGame();
                } catch (IOException | LoginException ignore) {
                }
            }
        };

        enterNumTimer.schedule(showWaitingScemeTask, 30000);
    }
    @FXML
    private void enterNumOfPlayer(ActionEvent actionEvent) {
            if(enterNumTimer!=null){
                enterNumTimer.cancel();
                enterNumTimer=null;
            }
        root.lookup("#loginExceptionLabel").setDisable(true);
        root.lookup("#loginExceptionLabel").setVisible(false);
            int n=Integer.parseInt(((TextField) root.lookup("#numField")).getText());
            if(n>1&&n<=4){
                client.askSetNumberOfPlayers(n, client.getModel().getNickname());
            } else {
                showException("Error! You can choose from 2 to 4 player!");
                gui.getNumOfPlayer();
            }

            showWaitingScene();

    }

    /**
     * Method invoked to show the user that the game is waiting for other players
     */
    public void showWaitingScene() {
        root.lookup("#chooseLabel").setVisible(false);
        root.lookup("#chooseLabel").setDisable(true);

        root.lookup("#nameField").setVisible(false);
        root.lookup("#nameField").setDisable(true);

        root.lookup("#rmiButton").setVisible(false);
        root.lookup("#rmiButton").setDisable(true);

        root.lookup("#socketButton").setVisible(false);
        root.lookup("#socketButton").setDisable(true);

        root.lookup("#enterNicknameLabel").setVisible(false);
        root.lookup("#enterNicknameLabel").setDisable(true);
        root.lookup("#nameField").setVisible(false);
        root.lookup("#nameField").isDisable();

        root.lookup("#loginButton").setVisible(false);
        root.lookup("#loginButton").isDisable();

        root.lookup("#numButton").isDisable();
        root.lookup("#numButton").setVisible(false);

        root.lookup("#numField").setVisible(false);
        root.lookup("#numField").setDisable(true);

        root.lookup("#image1").setVisible(true);
        root.lookup("#waitingLabel").setVisible(true);
        root.lookup("#waitingLabel").setDisable(false);

        timer();
    }

    private void hideWaitingScene(){
        if(timer1!=null){
            timer2.cancel();
            timer1.cancel();
            timer1=null;
            timer2=null;
        }


        root.lookup("#waitingLabel").setVisible(false);
        root.lookup("#waitingLabel").setDisable(true);

    }
    @Override
    public void showException(String e){
        root.lookup("#loginExceptionLabel").setDisable(false);
        root.lookup("#loginExceptionLabel").setVisible(true);
        ((Label) root.lookup("#loginExceptionLabel")).setText(e);
    }

    /**
     * Method invoked by the GUI View when the game start. The Game Scene is shown.
     * @throws IOException from FXMLLoader load
     */
    public void showGameScene() throws IOException {
        if(timer1!=null){
            timer1.cancel();
            timer1=null;
        }

        FXMLLoader fxmlLoader1 = new FXMLLoader(GUIApplication.class.getResource("game.fxml"));
        Parent root= fxmlLoader1.load();
        Scene scene = new Scene(root, 1250,650);
        GameController gameController=fxmlLoader1.getController();
        gameController.setClient(client);
        gameController.setRoot(root);
        gameController.setScene(gui,stage);
        gui.setGameController(gameController);
        stage.setScene(scene);
    }

    @Override
    public void setScene(GUI gui, Stage stage) {
        gui.setLoginController(this);
        super.setScene(gui, stage);
    }


}
