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
    private ScrollPane scrollPane;
    @FXML
    private TextField textField;
    @FXML
    private CheckBox player3CheckBox;
    @FXML
    private CheckBox player2CheckBox;
    @FXML
    private CheckBox player1CheckBox;
    @FXML
    private CheckBox broadcastCheckBox;
    @FXML
    private AnchorPane chatPane;
    @FXML
    private Button enterButton;
    @FXML
    private final Font font = new Font( 14);
    private double height = 20;
    @FXML
    private void plotMyMessage(ActionEvent event){
        if(textField.getText().equals("")){
            return;
        }
        ArrayList<String> recipients = new ArrayList<>();
        if(broadcastCheckBox.isSelected()){
            recipients.addAll(client.getModel().getOtherPlayers().keySet());
        } else {
            if (player1CheckBox.isSelected()&&!player1CheckBox.isDisabled()) recipients.add(player1CheckBox.getText());
            if (player2CheckBox.isSelected()&&!player2CheckBox.isDisabled()) recipients.add(player2CheckBox.getText());
            if (player3CheckBox.isSelected()&&!player3CheckBox.isDisabled()) recipients.add(player3CheckBox.getText());
        }

        try {
            client.askPostMessage(textField.getText(), recipients);
            plotMessage("You", textField.getText());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        textField.clear();
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
            scrollPane.setVvalue(scrollPane.getVmax());
        }

    }
    @FXML
    private void setRecipient(ActionEvent event){
            if(event.getSource()==broadcastCheckBox){
                player1CheckBox.setSelected(false);
                player2CheckBox.setSelected(false);
                player3CheckBox.setSelected(false);
            } else {
                broadcastCheckBox.setSelected(false);
            }

        if(!player1CheckBox.isSelected()&&!player2CheckBox.isSelected()&&!player3CheckBox.isSelected()){
            broadcastCheckBox.setSelected(true);
        }

    }

    @FXML
    private void setTextField(InputMethodEvent event){
        enterButton.setDisable(Objects.equals(textField.getText(), ""));
    }

    @Override
    public void setScene(GUI gui, Stage stage) {
        super.setScene(gui, stage);
        chatPane.setMinHeight(190);

    }


    protected void initializeChat(){
        switch (client.getModel().getNumOtherPlayers()){
            case 1:{
                player2CheckBox.setVisible(false);
                player2CheckBox.setDisable(true);

            }

            case 2: {
                player3CheckBox.setDisable(true);
                player3CheckBox.setVisible(false);
            }
        }
        int i=0;
        for(String s: client.getModel().getOtherPlayers().keySet()){
            switch (i){
                case 0-> player1CheckBox.setText(s);
                case 1-> player2CheckBox.setText(s);
                case 2-> player3CheckBox.setText(s);
            }

            i++;
        }

    }


}
