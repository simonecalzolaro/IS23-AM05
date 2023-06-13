package view;

import client.Client;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import myShelfieException.LoginException;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Map;

import static javafx.application.Platform.exit;

public class RankController extends GUIController {
    @FXML
    private Label score1Label;
    @FXML
    private Label score2Label;
    @FXML
    private Label score3Label;
    @FXML
    private Label score4Label;
    @FXML
    private Label player1Label;
    @FXML
    private Label player2Label;
    @FXML
    private Label player3Label;
    @FXML
    private Label player4Label;
    @FXML
    private Group player2Group;
    @FXML
    private Group player3Group;
    @FXML
    private Group player4Group;

    public void setClient(Client client) {
        this.client=client;
    }

    @Override
    public void setScene(GUI gui, Stage stage) {
        super.setScene(gui, stage);

        switch (client.getModel().getOtherPlayers().size()){
            case 0:  player2Group.setVisible(false);
            case 1:  player3Group.setVisible(false);
            case 2:  player4Group.setVisible(false);
        }
    }

    public void askExit(ActionEvent actionEvent) {
        exit();
    }

    public void startNewGame(ActionEvent actionEvent) throws IOException {
        try {
            client.askLogin(client.getModel().getNickname());
        } catch (LoginException e) {
            throw new RuntimeException(e);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        FXMLLoader fxmlLoader = new FXMLLoader(GUIApplication.class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1250,650);
        stage.setScene(scene);
        LoginController controller=fxmlLoader.getController();
        gui.setLoginController(controller);
        controller.setScene(gui,stage);
        controller.showWaitingScene();
    }

    public void setRank(Map<String, Integer> results) {
        int i=0;
        for (String name: results.keySet()){
            switch (i){
                case (0)-> {
                    player1Label.setText(name);
                    score1Label.setText(Integer.toString(results.get(name)));
                }
                case (1)-> {
                    player2Label.setText(name);
                    score2Label.setText(Integer.toString(results.get(name)));
                }
                case (2)-> {
                    player3Label.setText(name);
                    score3Label.setText(Integer.toString(results.get(name)));
                }
                case (3)-> {
                    player4Label.setText(name);
                    score4Label.setText(Integer.toString(results.get(name)));
                }
            }
            i++;
        }

    }
}
