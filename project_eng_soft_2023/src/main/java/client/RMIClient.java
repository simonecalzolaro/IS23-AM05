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
    protected ClientServerHandler clientServerHandler;
    protected GameHandler gameHandler;


    /**
     * constructor of ClientApp
     *
     * @throws RemoteException
     */
    public RMIClient() throws RemoteException {
        super();
    }

    @Override
    public void startClient() throws RemoteException {

        // Getting the registry
        Registry registry;
        registry = LocateRegistry.getRegistry(Settings.SERVER_NAME, Settings.PORT);

        // Looking up the registry for the remote object
        try {

            this.clientServerHandler = (ClientServerHandler) registry.lookup("ServerAppService");

        } catch (NotBoundException e) {

            throw new RuntimeException(e);

        }

        do {

            int num;
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

            //-------questa parte andrà migliorata in GUI e TUI

            Scanner scan = new Scanner(System.in);

            do {
                System.out.println("0 --> start a new game");
                System.out.println("1 --> continue a Game");
                num = scan.nextInt();

            }while (num!=0 &&  num!=1);

            try {

                switch (num) {

                    case 0:
                        System.out.println("nickname: ");
                        nickName = br.readLine();
                        System.out.println("-----------------------");

                        //login of the new player
                        this.gameHandler = askLogin();
                        System.out.println("-----login successfully");
                        break;

                    case 1:
                        System.out.println("nickname: ");
                        nickName = br.readLine();
                        System.out.println("-------------------");

                        //login of the new player
                        this.gameHandler = askContinueGame();
                        System.out.println("-----reconnected successfully");
                        break;
                }

            } catch (Exception e) {

                System.out.println(e.getMessage());
                e.printStackTrace();
                System.out.println("try again...");
                nickName="";

            }

        }while(nickName.equals(""));

    }



    /**
     * asks the server to log in, is divided in RMI and socket
     * @return GameHandler interface
     * @throws LoginException
     * @throws IOException
     * @throws RemoteException
     */
    public GameHandler askLogin() throws LoginException, IOException, RemoteException{

        return clientServerHandler.login( this.nickName, this);

    }


    /**
     * asks the server to continue a game, is divided in RMI and socket
     * @return GameHandler interface
     * @throws LoginException
     * @throws RemoteException
     */
    public GameHandler askContinueGame() throws LoginException, RemoteException {

        return clientServerHandler.continueGame(nickName, this);

    }


    /**
     * asks the server to leave the game I'm playing, is divided in RMI and socket
     * @return true if everything went fine
     * @throws RemoteException
     */
    public boolean askLeaveGame() throws RemoteException, LoginException {


        return clientServerHandler.leaveGame(nickName);

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
