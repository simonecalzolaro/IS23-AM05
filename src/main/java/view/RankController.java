package view;

import client.Client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import java.util.Map;

/**
 * GuiController for the rank scene
 */
public class RankController extends GUIController {

    public void setClient(Client client) {
        this.client=client;
    }

    @Override
    public void setScene(GUI gui, Stage stage) {
        super.setScene(gui, stage);

        switch (client.getModel().getOtherPlayers().size()){
            case 0:  root.lookup("#player2Group").setVisible(false);
            case 1:  root.lookup("#player3Group").setVisible(false);
            case 2:  root.lookup("#player4Group").setVisible(false);
        }
    }
    @FXML
    private void askExit(ActionEvent actionEvent) {
        System.exit(0);
    }


    /**
     * Method invoked by the GUI View to set the final rank.
     * @param results results of the ended game
     */
    public void setRank(Map<String, Integer> results) {
        int i=0;
        for (String name: results.keySet()){
            switch (i){
                case (0)-> {
                    ((Label) root.lookup("#player1Label")).setText(name);
                    ((Label) root.lookup("#score1Label")).setText(Integer.toString(results.get(name)));
                }
                case (1)-> {
                    ((Label) root.lookup("#player2Label")).setText(name);
                    ((Label) root.lookup("#score2Label")).setText(Integer.toString(results.get(name)));
                }
                case (2)-> {
                    ((Label) root.lookup("#player3Label")).setText(name);
                    ((Label) root.lookup("#score3Label")).setText(Integer.toString(results.get(name)));
                }
                case (3)-> {
                    ((Label) root.lookup("#player4Label")).setText(name);
                    ((Label) root.lookup("#score4Label")).setText(Integer.toString(results.get(name)));
                }
            }
            i++;
        }

    }
}
