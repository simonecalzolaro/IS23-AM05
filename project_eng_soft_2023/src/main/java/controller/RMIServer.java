package controller;

import client.ClientHandler;
import model.Board;
import myShelfieException.LoginException;

import java.io.IOException;
import java.net.Socket;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class RMIServer extends Lobby{
    /**
     * constructor for the ServerApp
     *
     * @throws RemoteException
     */
    protected RMIServer() throws RemoteException {
    }

    @Override
    public void startServer(){
        Registry registry = null;
        ClientServerHandler stub = null;

        try{
            stub = (ClientServerHandler) UnicastRemoteObject.exportObject(this, Settings.PORT);
            registry = LocateRegistry.createRegistry(Settings.PORT);
            registry.bind("ServerAppService",stub);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        } catch (AlreadyBoundException e) {
            throw new RuntimeException(e);
        }

        System.out.println("RMIServer ready");
    }


    @Override
    public int askNumberOfPlayers(ClientHandler ch) throws IOException {

        return ch.enterNumberOfPlayers(); //dubbio che serva lo stub per comunicare con l'interfaccia remota del client


    }


    /**
     * Method called by the client to log into the server to start a new game.
     * @param nickname is the name chosen by the client, if ti is already used throws IllegalArgumentException
     * @param ch is the client interface. I need it to associate each client interface to a ControlPlayer
     * @throws RemoteException
     */

    public GameHandler login(String nickname, ClientHandler ch, Socket socketCP) throws RemoteException, IOException, LoginException {

        if( clients.values().stream().map(x -> x.getPlayerNickname()).toList().contains(nickname) ) throw new LoginException("this nickname is not available at the moment");

        else {

            if(attendedPlayers==-1){ //if the isn't any waiting room it means that ch is the first player

                tempBoard = new Board();
                System.out.println("...a new board has been created...");

                do{
                    try {
                        //server asks client how many players he wants in his match
                        attendedPlayers = askNumberOfPlayers(ch);
                    } catch (RemoteException e) {
                        attendedPlayers = -1;
                        throw new RuntimeException(e);
                    }
                } while(attendedPlayers<2 || attendedPlayers>4); //eccezione da gestire

                //initializing the board with the chosen number of players
                tempBoard.initializeBoard(attendedPlayers);
                System.out.println("...player "+ nickname+ " created a game with "+ attendedPlayers+" players...");

            }

            //create a ControlPlayer
            ControlPlayer pl= new RMIControlPlayer(nickname, tempBoard);
            pl.setClientHandler(ch);
            System.out.println("...player "+ nickname+ " entered the game ");

            //add to the map "clients" the ClientHandler interface and the associated ControlPlayer
            clients.put(ch, pl);
            //tempPlayers is like a waiting room
            tempPlayers.add(pl);

            //once the waiting room (tempPlayers) is full the Game is created and all the players are notified
            if(tempPlayers.size() == attendedPlayers){

                attendedPlayers = -1;
                Game g = new Game( tempPlayers , tempBoard );
                games.add( g );
                tempPlayers.clear();


                notifyStartPlaying(g);

                System.out.println("...The game has been created, participants: "+ g.getPlayers());

            }

            return pl;
        }
    }

    /**
     * this method tells to all users that the game has started and that they aren't anymore in the waiting room, is divided in RMI and socket
     * @param g
     * @throws RemoteException
     */
    public void notifyStartPlaying(Game g) throws RemoteException {

        for(ControlPlayer player: g.getPlayers()){
            try {

                player.setGame(g);
                ClientHandler clih=player.getClientHandler();
                clih.startPlaying(player.getBookshelf().getPgc().getCardNumber(), g.getBoard().getCommonGoalCard1().getCGCnumber(), g.getBoard().getCommonGoalCard2().getCGCnumber());
                clih.updateBoard(g.getBoard().getBoard()); //----------timer che aspetta il return true

                if(g.getPlayers().get(g.getCurrPlayer()).equals(player)) clih.startYourTurn();

            } catch (RemoteException e) { throw new RuntimeException(e); }
        }


    }
}
