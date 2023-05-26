package client;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ClientChat {


    private Map<String, String> conversation;


    public ClientChat() {
        conversation=new HashMap<>();
    }

    public Map<String, String> getConversation() {
        return conversation;
    }

    public void addMessage(String sender, String message){
        conversation.put(sender, message);
        //view.plotNewMessage(); ----Gabbo ---Elena mettere metodo in view per plottare
    }



}
