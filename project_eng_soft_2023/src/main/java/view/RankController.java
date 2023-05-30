package view;

import client.Client;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.stage.Stage;

import static com.sun.javafx.application.PlatformImpl.exit;
import static com.sun.javafx.application.PlatformImpl.isCaspian;

public class RankController extends GUIController {
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

    public void startNewGame(ActionEvent actionEvent) {
    }

}
