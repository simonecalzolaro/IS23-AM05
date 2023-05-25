package view;

import client.Client;
import javafx.event.ActionEvent;

import static com.sun.javafx.application.PlatformImpl.exit;

public class RankController extends GUIController {
    public void setClient(Client client) {
        this.client=client;
    }
    //TODO set rank

    public void newGame(ActionEvent actionEvent) {

        //TODO start new game
    }

    public void askExit(ActionEvent actionEvent) {
        exit();
    }
}
