package client;

import model.Tile;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.rmi.RemoteException;

public class ClientSocketInputLobby extends SocketClient implements Runnable{
    /**
     * constructor of ClientApp
     *
     * @throws RemoteException
     */
    protected ClientSocketInputLobby() throws RemoteException {
        super();
    }

    @Override
    public void run() {
        
        while(true){
            
           try{
               while(jsonIn == null){
                   jsonIn = (JSONObject) inLobby.readObject();
               }
           } catch (IOException e) {
               throw new RuntimeException(e);
           } catch (ClassNotFoundException e) {
               throw new RuntimeException(e);
           }
           
           String method;
           
           method = (String) jsonIn.get("method");

           switch (method){
               case "askNumberOfPlayers":
                   try {
                       enterNumberOfPlayers();
                   } catch (RemoteException e) {
                       throw new RuntimeException(e);
                   } catch (IOException e) {
                       throw new RuntimeException(e);
                   }
                   break;

               case "notifyStartPlaying":
                   try {
                       Long pgc = (Long) jsonIn.get("param1");
                       Long cgc1 = (Long) jsonIn.get("param2");
                       Long cgc2 = (Long) jsonIn.get("param3");
                       startPlaying(pgc.intValue(),cgc1.intValue(),cgc2.intValue());
                   } catch (RemoteException e) {
                       throw new RuntimeException(e);
                   }
                   break;

               case "updateBoard":
                   try{
                       Tile[][] board = (Tile[][]) jsonIn.get("param1");
                       updateBoard(board);
                   } catch (RemoteException e) {
                       throw new RuntimeException(e);
                   }

               case "startYourTurn":
                   try{
                       startYourTurn();
                   } catch (RemoteException e) {
                       throw new RuntimeException(e);
                   }

           }

        }
        
    }
}
