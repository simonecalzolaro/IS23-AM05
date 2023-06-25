package view;

import client.RMIClient;
import client.SocketClient;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import myShelfieException.LoginException;

import java.awt.*;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;


public class LoginController extends GUIController {
    @FXML
    private Label waitingLabel;
    @FXML
    private Button rmiButton;
    @FXML
    private Button socketButton;
    @FXML
    private Label chooseLabel;
    @FXML
    private Label enterNicknameLabel;
    @FXML
    private TextField nameField;
    @FXML
    private Button loginButton;
    @FXML
    private Label loginExceptionLabel;
    @FXML
    private Button numButton;
    @FXML
    private TextField numField;
    @FXML
    private ImageView image1;
    @FXML
    private ImageView image2;
    @FXML
    private ImageView image3;
    @FXML
    private ImageView image4;

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
                    case 0 -> Platform.runLater(() -> {image1.setVisible(false);
                        image2.setVisible(true);});
                    case 1 -> Platform.runLater(() -> {image2.setVisible(false);
                        image3.setVisible(true);});
                    case 2 -> Platform.runLater(() -> {image3.setVisible(false);
                        image4.setVisible(true);});
                    case 3 -> Platform.runLater(() -> {image4.setVisible(false);
                                           image1.setVisible(true);});
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
                    case 0 -> Platform.runLater(() -> waitingLabel.setText("Waiting for players"));
                    case 1 -> Platform.runLater(() -> waitingLabel.setText("Waiting for players."));
                    case 2 -> Platform.runLater(() -> waitingLabel.setText("Waiting for players.."));
                    case 3 -> Platform.runLater(() -> waitingLabel.setText("Waiting for players..."));
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
        stage.show();
    }
    @FXML
    private void chooseProtocol(ActionEvent actionEvent) {

        loginExceptionLabel.setDisable(true);
        loginExceptionLabel.setVisible(false);

        try{
            if(actionEvent.getSource().equals(rmiButton)){

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


        rmiButton.isDisable();
        socketButton.isDisable();
        rmiButton.setVisible(false);
        socketButton.setVisible(false);
        chooseLabel.isDisable();
        chooseLabel.setVisible(false);

        showEnterNickname();

    }

    private void showEnterNickname(){
        enterNicknameLabel.setText("Enter your nickname:");
        enterNicknameLabel.setVisible(true);
        enterNicknameLabel.setDisable(false);
        nameField.setDisable(false);
        nameField.setVisible(true);
        loginButton.setDisable(false);
        loginButton.setVisible(true);
        numField.setVisible(false);
        numField.setDisable(true);
        numButton.setDisable(true);
        numButton.setVisible(false);
    }
    @FXML
    private void enterNickname(ActionEvent actionEvent){
        loginExceptionLabel.setDisable(true);
        loginExceptionLabel.setVisible(false);
        String nickname = nameField.getText();
        nameField.clear();
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

        if(timer1!=null){
            timer2.cancel();
            timer1.cancel();
            timer1=null;
            timer2=null;
        }


        enterNicknameLabel.setVisible(true);
        enterNicknameLabel.setDisable(false);
        waitingLabel.setVisible(false);
        waitingLabel.setDisable(true);

        loginExceptionLabel.setDisable(true);
        loginExceptionLabel.setVisible(false);
        enterNicknameLabel.setText("Enter number of player to create a new game:");
        nameField.setDisable(true);
        nameField.setVisible(false);
        loginButton.setDisable(true);
        loginButton.setVisible(false);

        numField.setDisable(false);
        numField.setVisible(true);
        numButton.setDisable(false);
        numButton.setVisible(true);


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
            loginExceptionLabel.setDisable(true);
            loginExceptionLabel.setVisible(false);
            int n=Integer.parseInt(numField.getText());
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
        chooseLabel.setVisible(false);
        chooseLabel.setDisable(true);

        nameField.setVisible(false);
        nameField.setDisable(true);

        rmiButton.setVisible(false);
        rmiButton.setDisable(true);

        socketButton.setVisible(false);
        socketButton.setDisable(true);

        enterNicknameLabel.setVisible(false);
        enterNicknameLabel.setDisable(true);
        nameField.setVisible(false);
        nameField.isDisable();

        loginButton.setVisible(false);
        loginButton.isDisable();

        numButton.isDisable();
        numButton.setVisible(false);

        numField.setVisible(false);
        numField.setDisable(true);

        image1.setVisible(true);
        waitingLabel.setVisible(true);
        waitingLabel.setDisable(false);

        timer();
    }



    public void showException(String e){
        loginExceptionLabel.setDisable(false);
        loginExceptionLabel.setVisible(true);
        loginExceptionLabel.setText(e);
    }

    /**
     * Method invoked by the GUI View when the game start. The Game Scene is shown.
     * @throws IOException
     */
    public void showGameScene() throws IOException {
        if(timer1!=null){
            timer1.cancel();
            timer1=null;
        }

        FXMLLoader fxmlLoader1 = new FXMLLoader(GUIApplication.class.getResource("game.fxml"));
        Scene scene = new Scene(fxmlLoader1.load(), 1250,650);
        GameController gameController=fxmlLoader1.getController();
        gameController.setClient(client);
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
