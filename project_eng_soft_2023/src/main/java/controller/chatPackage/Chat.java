package controller.chatPackage;

import controller.ControlPlayer;

import java.io.IOException;
import java.util.ArrayList;

public class Chat {

    private ArrayList<Message> conversation;
    //the flag is true when a new message is present
    private boolean flagNewMessage;


    public Chat() {

        this.conversation = new ArrayList<>();
        flagNewMessage=false;

        (new Thread(()->newMessageListener())).start();
    }

    public synchronized void addMessage(ControlPlayer sender, String message, ArrayList<ControlPlayer> recipients){

        while(flagNewMessage){
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        flagNewMessage=true;

        conversation.add(new Message(sender, message, recipients ));

        notifyAll();
    }

    public synchronized void newMessageListener(){

        while(true){

            while(!flagNewMessage){
                try {
                    wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            flagNewMessage=true;

            Message lastMessage=conversation.get(conversation.size()-1);

            //send the new message to the chosen ControlPlayers
            for(ControlPlayer cp: lastMessage.getRecipients()){
                try {
                    cp.notifyNewMessage(lastMessage.getSender().getPlayerNickname(), lastMessage.getMessage());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            notifyAll();

        }
    }
}
