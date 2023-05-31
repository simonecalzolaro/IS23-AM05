package client;

import view.View;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * class to store and use the chat
 */
public class ClientChat {

    private Map<String, String> conversation;
    private static View view;

    /**
     * constructor method
     */
    public ClientChat(View view) {
        conversation=new HashMap<>();
        this.view=view;
    }

    /**
     * @return all the conversation as a Map<sender, message>
     */
    public Map<String, String> getConversation() {
        return conversation;
    }

    /**
     * inser a message in the conversation
     * @param sender: nickname of the person sending the message
     * @param message: message to insert in the chat
     */
    public void addMessage(String sender, String message){
        conversation.put(sender, message);
    }

}
