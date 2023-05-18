package client;



import myShelfieException.*;
import view.View;


import java.io.*;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

/**
 * Client Application
 */
public abstract class Client extends UnicastRemoteObject implements ClientHandler, Serializable {

    //----- tutti sti attributi sono da spostare in classi o sottoclassi più specifiche
    protected String hostname;
    protected int PORT;

    protected boolean left;
    protected int myScore;

    protected ClientModel model;
    private View view;


    protected boolean myTurn;
    protected boolean gameEnded;


    /**
     * constructor of ClientApp
     * @throws RemoteException
     */
    protected Client(View view) throws RemoteException {

        super();

        this.view=view;
        model=new ClientModel();
        myTurn=false;
        gameEnded=false;
        left=false;

    }


    abstract public void initializeClient() throws  IOException, NotBoundException;


    /**
     * method called by the server to ask the first player to entre the number of players he wants in his match
     * @return the chosen number of players
     * @throws RemoteException
     */
    @Override
    public int enterNumberOfPlayers() throws RemoteException{

        return view.getNumOfPlayer();

    }


    /**
     * method called by the server to update the board of this client
     * @param board
     * @throws RemoteException
     */
    @Override
    public boolean updateBoard(model.Tile[][] board, model.Tile[][] myShelf, Map<String, model.Tile[][]> otherShelf) throws RemoteException{

        Map<String, Matrix> otherPlayersMatr= new HashMap<>();
        for(String nick: otherShelf.keySet()){
            otherPlayersMatr.put(nick, new Matrix(otherShelf.get(nick)));
        }

        model.initializeMatrixes(new Matrix(board), new Matrix(myShelf), otherPlayersMatr );

        view.updateBoard();

        return true;

    }


    /**
     * method called by the server to show the user an ordered rank of players
     * and to end the match
     * @throws RemoteException
     */
    @Override
    public boolean theGameEnd(Map< Integer, String> results) throws RemoteException{

        gameEnded=true;
        view.endGame(results);
        return true;

    }


    /**
     * method called by the server to notify the user that his turn has started
     * @throws RemoteException
     */
    @Override
    public boolean startYourTurn() throws RemoteException{

        myTurn=true;
        try {
            view.isYourTurn();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InvalidChoiceException e) {
            throw new RuntimeException(e);
        } catch (NotConnectedException e) {
            throw new RuntimeException(e);
        } catch (InvalidParametersException e) {
            throw new RuntimeException(e);
        } catch (NotMyTurnException e) {
            throw new RuntimeException(e);
        }

        return true;
    }


    /**
     *  method called by the server to notify the user that his turn is ended
     * @throws RemoteException
     */
    @Override
    public boolean endYourTurn() throws RemoteException{

        view.endYourTurn();
        myTurn=false;
        return true;

    }


    /**
     *  method called by the server to notify the user that the match has started
     * @throws RemoteException
     */
    @Override
    public boolean startPlaying(int pgcNum, Map<model.Tile, Integer[]> pgcMap, int cgc1num, int cgc2num) throws RemoteException {


        model.initializeCards(new Matrix(pgcMap), pgcNum, cgc1num, cgc2num );

        view.startPlay();

        return true;

    }


    /**
     * @return always true
     * @throws RemoteException RMI exception
     */
    @Override
    public boolean pong() throws RemoteException {
        return true;
    }



    /**
     * set up the servers' ports and hostname
     */
    abstract public void getServerSettings();

    /**
     * @return clientModel
     */
    public ClientModel getModel(){
        return model;
    }

    /**
     * @return true if is your turn
     */
    public boolean isMyTurn() {
        return myTurn;
    }

    /**
     * @return true if game ended
     */
    public boolean GameEnded() {
        return gameEnded;
    }

    //-------------------------------------- RMI vs Socket layer --------------------------------------


    /**
     * asks the server to log in, is divided in RMI and socket
     * @return GameHandler interface
     * @throws LoginException
     * @throws IOException
     * @throws RemoteException
     */
    public abstract void askLogin(String nick) throws LoginException, IOException, RemoteException;


    /**
     * asks the server to continue a game, is divided in RMI and socket
     * @return GameHandler interface
     * @throws LoginException
     * @throws RemoteException
     */
    public abstract void askContinueGame() throws LoginException, IOException;


    /**
     * asks the server to leave the game I'm playing, is divided in RMI and socket
     * @return true if everything went fine
     * @throws RemoteException
     */
    public abstract boolean askLeaveGame() throws IOException, LoginException;


    /**
     * asks the server to leave the game I'm playing, is divided in RMI and socket
     * @param coord
     * @return true if everything went fine
     * @throws InvalidChoiceException
     * @throws NotConnectedException
     * @throws InvalidParametersException
     * @throws RemoteException
     * @throws NotMyTurnException
     */
    public abstract boolean askBoardTiles( List<Integer> coord) throws InvalidChoiceException, NotConnectedException, InvalidParametersException, IOException, NotMyTurnException;


    /**
     * asks the server to insert some tiles in my shelf, is divided in RMI and socket
     * @param choosenColumn is the column where to insert the tiles
     * @param coord board coordinates
     * @return true if everything went fine
     * @throws IOException
     * @throws NotConnectedException
     * @throws NotMyTurnException
     * @throws InvalidChoiceException
     * @throws InvalidLenghtException
     */
    public abstract boolean askInsertShelfTiles( int choosenColumn, List<Integer> coord) throws IOException, NotConnectedException, NotMyTurnException, InvalidChoiceException, InvalidLenghtException;


    /**
     * asks the server my current score, is divided in RMI and socket
     * @return the score
     * @throws IOException
     */
    abstract public int askGetMyScore() throws IOException;

    /**
     * verifies the server is up
     * @return true if the server is online
     */
    public abstract boolean askPing();

    abstract public void askCheckFullWaitingRoom() throws IOException;

}
