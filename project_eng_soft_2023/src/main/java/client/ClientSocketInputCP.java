package client;

import model.Tile;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.rmi.RemoteException;

public class ClientSocketInputCP extends SocketClient implements Runnable{
    /**
     * constructor of ClientApp
     *
     * @throws RemoteException
     */
    protected ClientSocketInputCP() throws RemoteException {
        super();
    }

    @Override
    public void run() {
        while(true){

            try{
                while(jsonInCP == null){
                    jsonInCP = (JSONObject) inLobby.readObject();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }

            String method;

            method = (String) jsonInCP.get("method");

            switch (method){

                case "getMyScore":
                    score = ((Long) jsonInCP.get("param1")).intValue();
                    break;

                case "notifyStartYourTurn":
                    try {
                        startYourTurn();
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                    break;

                case "notifyEndYourTurn":
                    try{
                        endYourTurn();
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                    break;


            }

        }
    }
}
