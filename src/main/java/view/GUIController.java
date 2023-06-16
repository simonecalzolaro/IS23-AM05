package view;

import client.Client;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public abstract class GUIController {
    protected GUI gui;
    protected Stage stage;
    protected Client client;


    public void setScene(GUI gui, Stage stage) {
        this.gui=gui;
        this.stage=stage;

    }

    public void highlightButton(MouseEvent mouseEvent) {
        Button button = (Button) mouseEvent.getSource();
        if(mouseEvent.getEventType().getName().equals("MOUSE_ENTERED")){
            button.setStyle("-fx-background-color: #ffd900; -fx-border-color: #ffad00;");
        } else {
            button.setStyle("-fx-background-color: #ffd896; -fx-border-color: #ffac37;");
        }
    }

    public void showChat(ActionEvent actionEvent) {
        gui.getChatStage().show();
    }

    public void showException() {
    }

    public void setClient(Client client) {
        this.client = client;
    }
}
