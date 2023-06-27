package client;

import view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * class to store and use the chat
 */
public class ClientChat {

    private ArrayList<Message> conversation;
    private int lastReadMessage;

    /**
     * constructor method
     */
    public ClientChat() {
        conversation=new ArrayList<>();
        lastReadMessage=0;

    }

    /**
     * @return all the conversation as a Map<sender, message>
     */
    public ArrayList<ArrayList <String>> getConversation() {

        ArrayList<ArrayList <String>> conv= new ArrayList<>();

        for(int i=lastReadMessage; i< conversation.size(); i++){

            Message m= conversation.get(i);
            conv.add(new ArrayList<>());
            conv.get(i).add(m.getSender());
            conv.get(i).add(m.getMessage());
            lastReadMessage++;
        }

        return conv;
    }

    /**
     * inser a message in the conversation
     * @param sender: nickname of the person sending the message
     * @param message: message to insert in the chat
     */
    public void addMessage(String sender, String message){
        conversation.add(new Message(sender, message));
    }


}

/**
 * contains the details of a message
 */
class Message{

    private final String sender;
    private final String message;

    public Message(String sender, String message) {
        this.sender = sender;
        this.message = message;
    }

    public String getSender() {
        return sender;
    }

    public String getMessage() {
        return message;
    }
}
