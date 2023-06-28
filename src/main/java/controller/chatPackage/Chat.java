package controller.chatPackage;

import controller.ControlPlayer;

import java.io.IOException;
import java.util.ArrayList;

public class Chat {

    private ArrayList<Message> conversation;
    //the flag is true when a new message is present
    private boolean flagNewMessageReceived;
    private boolean flagNewMessageToSend;

    public Chat() {

        this.conversation = new ArrayList<>();
        flagNewMessageReceived=false;
        flagNewMessageToSend=true;

        (new Thread(()->newMessageListener())).start();
    }

    /**
     * insert a new Message in the conversation
     * @param sender: the user who sends the message
     * @param message: text message
     * @param recipients: the user/s receiving the message
     */
    public synchronized void addMessage(ControlPlayer sender, String message, ArrayList<ControlPlayer> recipients){

        while(flagNewMessageReceived){
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        conversation.add(new Message(sender, message, recipients ));
        System.out.println("---------added a new message, recipients: "+ recipients.toString());
        flagNewMessageReceived=true;
        flagNewMessageToSend=false;
        notifyAll();

    }

    /**
     * listen to the ArrayList conversation, every time there's a new message, the addition of new messages
     * is suspended and the last message is sent
     */
    public synchronized void newMessageListener(){

        while(true){

            while(flagNewMessageToSend){
                try {
                    wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            Message lastMessage=conversation.get(conversation.size()-1);

            //send the new message to the chosen ControlPlayers
            for(ControlPlayer cp: lastMessage.getRecipients()){

                try {
                    System.out.println("--------SENDING TO "+cp.getPlayerNickname());
                    cp.notifyNewMessage(lastMessage.getSender().getPlayerNickname(), lastMessage.getMessage());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            flagNewMessageReceived=false;
            flagNewMessageToSend=true;
            notifyAll();

        }
    }
}
