package view;

import client.RMIClient;
import client.SocketClient;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import myShelfieException.LoginException;

import java.io.IOException;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;


public class LoginController extends GUIController {
    @FXML
    public Label waitingLabel;
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
    public ImageView image1;
    @FXML
    public ImageView image2;
    @FXML
    public ImageView image3;
    @FXML
    public ImageView image4;

    private Timer timer1;
    private Timer timer2;


    public void timer(){
        timer1 = new Timer();
        timer2= new Timer();
        TimerTask task1=new TimerTask() {
            private int n=0;

            @Override
            public void run() {
                switch (n%4) {
                    case 0 -> {
                        Platform.runLater(() -> {image1.setVisible(false);
                            image2.setVisible(true);});

                    }
                    case 1 -> {
                        Platform.runLater(() -> {image2.setVisible(false);
                            image3.setVisible(true);});

                    }
                    case 2 -> {
                        Platform.runLater(() -> {image3.setVisible(false);
                            image4.setVisible(true);});

                    }
                    case 3 -> {
                        Platform.runLater(() -> {image4.setVisible(false);
                                               image1.setVisible(true);});

                    }
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
                    case 0 -> {
                        Platform.runLater(() -> waitingLabel.setText("Waiting for players"));
                    }
                    case 1 -> {
                        Platform.runLater(() -> waitingLabel.setText("Waiting for players."));
                    }
                    case 2 -> {
                        Platform.runLater(() -> waitingLabel.setText("Waiting for players.."));
                    }
                    case 3 -> {
                        Platform.runLater(() -> waitingLabel.setText("Waiting for players..."));
                    }
                }
                num++;
                if(num==800) timer2.cancel();
            }

        };
        timer1.schedule(task1, 3000, 3000);
        timer2.schedule(task2, 750, 750);
    }
    public void chooseProtocol(ActionEvent actionEvent) {

        loginExceptionLabel.setDisable(true);
        loginExceptionLabel.setVisible(false);

        try{
            if(actionEvent.getSource().equals(rmiButton)){
                client = new RMIClient();
            } else {client = new SocketClient();
            }
            client.startClient();
        } catch (Exception e){
            showException("Error! Try again!");
        }


        rmiButton.isDisable();
        socketButton.isDisable();
        rmiButton.setVisible(false);
        socketButton.setVisible(false);
        chooseLabel.isDisable();
        chooseLabel.setVisible(false);

        enterNicknameLabel.setVisible(true);
        enterNicknameLabel.setDisable(false);
        nameField.setDisable(false);
        nameField.setVisible(true);
        loginButton.setDisable(false);
        loginButton.setVisible(true);

    }
    public void enterNickname(ActionEvent actionEvent) throws IOException {
        loginExceptionLabel.setDisable(true);
        loginExceptionLabel.setVisible(false);
        String nickname = nameField.getText();
        nameField.clear();
        try{
            client.askLogin(nickname);
        } catch (LoginException e){
            showException("This nickname is not available! Try again!");
        } catch (Exception e){
            showException("Error! Try again!");

        }
        //timer se non viene invocato enter num of player mostro la waitingScene
        showEnterNumOfPlayer(); //da cambiare

    }

    public void showEnterNumOfPlayer(){
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
    }

    public void enterNumOfPlayer(ActionEvent actionEvent) {
        if(gui.getTimer()!=null){
            gui.getTimer().cancel();
            gui.setTimer(null);
            loginExceptionLabel.setDisable(true);
            loginExceptionLabel.setVisible(false);
            int n=Integer.parseInt(numField.getText());
            if(n>1&&n<=4){
                gui.setNumOfPlayer(n);
            } else {
                showException("Error! You can choose from 2 to 4 player!");
                gui.getNumOfPlayer();
            }
        }
        try {
            showWaitingScene(); //da cambiare
        } catch (IOException e){
            System.out.println("Qualcosa non funziona con il file");
        } catch (Exception e){
            System.out.println("Qualcosa non funziona e basta");
        }
    }


    public void showWaitingScene() throws IOException {
        enterNicknameLabel.setVisible(false);
        enterNicknameLabel.isDisable();

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

    public void showGameScene() throws IOException {
        timer1.cancel();
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("game.fxml")));
        Scene scene= new Scene(root);
        GameController gameController=new GameController();
        gameController.setScene(gui,stage);
        stage.setFullScreen(true);
        stage.setScene(scene);
    }

}
