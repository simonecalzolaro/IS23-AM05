package view;

import client.ClientChat;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Objects;

public class ChatController extends GUIController{
    @FXML
    private AnchorPane chatPane;
    @FXML
    private final Font font = new Font( 14);
    private double height = 20;
    @FXML
    private void plotMyMessage(ActionEvent event){
        if(((TextField) root.lookup("#textField")).getText().equals("")){
            return;
        }
        ArrayList<String> recipients = new ArrayList<>();
        if(((CheckBox) root.lookup("#broadcastCheckBox")).isSelected()){
            recipients.addAll(client.getModel().getOtherPlayers().keySet());
        } else {
            if (((CheckBox) root.lookup("#player1CheckBox")).isSelected()&&!((CheckBox) root.lookup("#player1CheckBox")).isDisabled()) recipients.add(((CheckBox) root.lookup("#player1CheckBox")).getText());
            if (((CheckBox) root.lookup("#player2CheckBox")).isSelected()&&!((CheckBox) root.lookup("#player2CheckBox")).isDisabled()) recipients.add(((CheckBox) root.lookup("#player2CheckBox")).getText());
            if (((CheckBox) root.lookup("#player3CheckBox")).isSelected()&&!((CheckBox) root.lookup("#player3CheckBox")).isDisabled()) recipients.add(((CheckBox) root.lookup("#player3CheckBox")).getText());
        }

        try {
            client.askPostMessage(((TextField) root.lookup("#textField")).getText(), recipients);
            plotMessage("You", ((TextField) root.lookup("#textField")).getText());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        ((TextField) root.lookup("#textField")).clear();
    }

    /**
     * Method invoked by the GUI View to plot the message
     * @param player Nickname of the player who sent the message
     * @param message Text of the message
     */
    public void plotMessage(String player, String message){

        Text text = new Text(player + ": " +message);
        chatPane.getChildren().add(text);
        text.setLayoutX(5);
        text.setLayoutY(height);
        text.setFont(font);
        text.setWrappingWidth(230);
        text.setStyle("-fx-fill: #623e00;");


        Bounds bounds = text.getLayoutBounds();
        double textHeight = bounds.getHeight();

        height=textHeight+10+height;
        if(chatPane.getHeight()<190) {
            chatPane.setPrefHeight(190);
        }

        if (height >= chatPane.getHeight()){
            chatPane.setPrefHeight(height+10);
            ((ScrollPane) root.lookup("#scrollPane")).setVvalue(((ScrollPane) root.lookup("#scrollPane")).getVmax());
        }

    }
    @FXML
    private void setRecipient(ActionEvent event){
            if(event.getSource()==((CheckBox) root.lookup("#broadcastCheckBox"))){
                ((CheckBox) root.lookup("#player1CheckBox")).setSelected(false);
                ((CheckBox) root.lookup("#player2CheckBox")).setSelected(false);
                ((CheckBox) root.lookup("#player3CheckBox")).setSelected(false);
            } else {
                ((CheckBox) root.lookup("#broadcastCheckBox")).setSelected(false);
            }

        if(!((CheckBox) root.lookup("#player1CheckBox")).isSelected()&&!((CheckBox) root.lookup("#player2CheckBox")).isSelected()&&!((CheckBox) root.lookup("#player3CheckBox")).isSelected()){
            ((CheckBox) root.lookup("#broadcastCheckBox")).setSelected(true);
        }

    }

    @FXML
    private void setTextField(InputMethodEvent event){
        ((Button) root.lookup("#enterButton")).setDisable(Objects.equals(((TextField) root.lookup("#textField")).getText(), ""));
    }

    @Override
    public void setScene(GUI gui, Stage stage) {
        super.setScene(gui, stage);
        chatPane.setMinHeight(190);

    }


    protected void initializeChat(){
        switch (client.getModel().getNumOtherPlayers()){
            case 1:{
                ((CheckBox) root.lookup("#player2CheckBox")).setVisible(false);
                ((CheckBox) root.lookup("#player2CheckBox")).setDisable(true);

            }

            case 2: {
                ((CheckBox) root.lookup("#player3CheckBox")).setDisable(true);
                ((CheckBox) root.lookup("#player3CheckBox")).setVisible(false);
            }
        }
        int i=0;
        for(String s: client.getModel().getOtherPlayers().keySet()){
            switch (i){
                case 0-> ((CheckBox) root.lookup("#player1CheckBox")).setText(s);
                case 1-> ((CheckBox) root.lookup("#player2CheckBox")).setText(s);
                case 2-> ((CheckBox) root.lookup("#player3CheckBox")).setText(s);
            }

            i++;
        }
        
    }


}
