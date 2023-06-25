package view;

import client.Client;
import client.ClientApp;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import myShelfieException.LoginException;

import java.io.IOException;
import java.rmi.NotBoundException;

import static com.sun.javafx.application.PlatformImpl.exit;

public abstract class GUIController {
    protected GUI gui;
    protected Stage stage;
    protected Client client;

    /**
     * Initialization of a new scene
     * @param gui GUI view
     * @param stage current stage
     */
    public void setScene(GUI gui, Stage stage) {
        this.gui=gui;
        this.stage=stage;

    }

    /**
     * Method invoke by the GUI View to show graphically an exception
     * @param exception string that will be printed to describe the exception
     */
    public void showException(String exception) {

    }
    @FXML
    private void highlightButton(MouseEvent mouseEvent) {
        Button button = (Button) mouseEvent.getSource();
        if(mouseEvent.getEventType().getName().equals("MOUSE_ENTERED")){
            button.setStyle("-fx-background-color: #ffd900; -fx-border-color: #ffad00;");
        } else {
            button.setStyle("-fx-background-color: #ffd896; -fx-border-color: #ffac37;");
        }
    }
    @FXML
    private void showChat(ActionEvent actionEvent) {
        gui.getChatStage().show();
    }



    public void setClient(Client client) {
        this.client = client;
    }
}
