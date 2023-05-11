package client;

import controller.ClientServerHandler;
import controller.GameHandler;
import model.Tile;
import myShelfieException.*;

import javax.swing.plaf.PanelUI;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class RMIClient extends Client {


    //-------------- RMI attributes --------------
    private ClientServerHandler clientServerHandler;
    private GameHandler gameHandler;
    private Registry registry;

    /**
     * constructor of ClientApp
     * @throws RemoteException
     */
    public RMIClient() throws RemoteException {

        super();

        // Getting the registry
        registry = LocateRegistry.getRegistry(Settings.SERVER_NAME, Settings.PORT);

        // Looking up the registry for the remote object
        try {

            this.clientServerHandler = (ClientServerHandler) registry.lookup("ServerAppService");

        } catch (NotBoundException e) {

            throw new RuntimeException(e);

        }

    }


    /**
     * asks the server to log in, is divided in RMI and socket
     * @return GameHandler interface
     * @throws LoginException
     * @throws IOException
     * @throws RemoteException
     */
    public void askLogin(String nick) throws LoginException, IOException, RemoteException{

        this.gameHandler= clientServerHandler.login(nick , this);
    }


    /**
     * asks the server to continue a game, is divided in RMI and socket
     * @return GameHandler interface
     * @throws LoginException
     * @throws RemoteException
     */
    public void askContinueGame() throws LoginException, RemoteException {

        this.gameHandler= clientServerHandler.continueGame(model.getNickname(), this);
    }


    /**
     * asks the server to leave the game I'm playing, is divided in RMI and socket
     * @return true if everything went fine
     * @throws RemoteException
     */
    public boolean askLeaveGame() throws RemoteException, LoginException {

        return clientServerHandler.leaveGame(model.getNickname());
    }


    /**
     * asks the server to leave the game I'm playing, is divided in RMI and socket
     * @param chosenTiles
     * @param coord
     * @return true if everything went fine
     * @throws InvalidChoiceException
     * @throws NotConnectedException
     * @throws InvalidParametersException
     * @throws RemoteException
     * @throws NotMyTurnException
     */
    public boolean askBoardTiles(List<Tile> chosenTiles, List<Integer> coord) throws InvalidChoiceException, NotConnectedException, InvalidParametersException, RemoteException, NotMyTurnException {

        return gameHandler.choseBoardTiles(chosenTiles, coord);
    }



    boolean askInsertShelfTiles(ArrayList<Tile> choosenTiles, int choosenColumn, List<Integer> coord) throws RemoteException, NotConnectedException, NotMyTurnException, InvalidChoiceException, InvalidLenghtException{

        return gameHandler.insertShelfTiles(choosenTiles, choosenColumn, coord);
    }


    int askGetMyScore() throws RemoteException{

        return gameHandler.getMyScore();
    }

}
