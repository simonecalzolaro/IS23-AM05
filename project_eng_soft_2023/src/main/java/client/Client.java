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
public abstract class Client extends UnicastRemoteObject implements ClientHandler, ClientAskNotify, Serializable {

    //----- tutti sti attributi sono da spostare in classi o sottoclassi più specifiche
    protected String hostname;
    protected int PORT;

    protected boolean left;
    protected int myScore;

    protected ClientModel model;
    private View view;

    private PingFromServer pingChecker;
    private Thread pingThread;

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
       // pingChecker=new PingFromServer(this);
        // pingThread=new Thread(pingChecker);

    }

    /**
     * initialize client's parameters
     * @throws IOException
     * @throws NotBoundException
     */
    abstract public void initializeClient() throws  IOException, NotBoundException;

    /**
     * method called by the server to ask the first player to entre the number of players he wants in his match
     * @return the chosen number of players
     * @throws RemoteException
     */
    @Override
    public void enterNumberOfPlayers() throws RemoteException{

        view.getNumOfPlayer();

    }

    /**
     * method called by the server to update the board of this client
     * @param board
     * @throws RemoteException
     */
    @Override
    public void updateBoard(model.Tile[][] board, model.Tile[][] myShelf, Map<String, model.Tile[][]> otherShelf, int myScore) throws RemoteException{

        Map<String, Matrix> otherPlayersMatr= new HashMap<>();
        for(String nick: otherShelf.keySet()){
            otherPlayersMatr.put(nick, new Matrix(otherShelf.get(nick)));
        }

        model.initializeMatrixes(new Matrix(board), new Matrix(myShelf), otherPlayersMatr );
        model.setMyScore(myScore);

        view.updateBoard();

    }

    /**
     * method called by the server to show the user an ordered rank of players
     * and to end the match
     * @throws RemoteException
     */
    @Override
    public void theGameEnd(Map< Integer, String> results) throws RemoteException{

        gameEnded=true;
        view.endGame(results);

    }

    /**
     * method called by the server to notify the user that his turn has started
     *
     * @throws RemoteException
     */
    @Override
    public void startYourTurn() throws RemoteException{

        myTurn=true;

        new Thread(() -> {
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
                        }).start();


    }

    /**
     *  method called by the server to notify the user that his turn is ended
     * @throws RemoteException
     */
    @Override
    public void endYourTurn() throws RemoteException{

        view.endYourTurn();
        myTurn=false;

    }

    /**
     *  method called by the server to notify the user that the match has started
     * @throws RemoteException
     */
    @Override
    public void startPlaying(int pgcNum, Map<model.Tile, Integer[]> pgcMap, int cgc1num, int cgc2num, int GameID) throws RemoteException {

        //start pinging the server
        //pingThread.start();

        model.initializeCards(new Matrix(pgcMap), pgcNum, cgc1num, cgc2num );
        model.setGameID(GameID);
        System.out.println("+++++++++++++++++++++++++++++");
        view.startPlay();


    }

    /**
     * @return always true
     * @throws RemoteException RMI exception
     */
    @Override
    public boolean pong() throws RemoteException {

        pingChecker.setConnected(true);
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






}
