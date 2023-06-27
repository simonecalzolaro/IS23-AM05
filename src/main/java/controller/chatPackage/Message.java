package controller.chatPackage;

import controller.ControlPlayer;

import java.util.ArrayList;

/**
 * contains the details of a message
 */
public class Message {

    private final ControlPlayer sender;
    private final String message;
    private final ArrayList<ControlPlayer> recipients;

    public Message(ControlPlayer sender, String message, ArrayList<ControlPlayer> recipients) {
        this.sender = sender;
        this.message = message;
        this.recipients = recipients;
    }

    //----------------------------getters---------------------------------

    public ControlPlayer getSender() {
        return sender;
    }

    public String getMessage() {
        return message;
    }

    public ArrayList<ControlPlayer> getRecipients() {
        return recipients;
    }

}
