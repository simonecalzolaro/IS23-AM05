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

    protected ClientModel model;
    private static View view;
    private PingFromServer pingChecker;
    protected boolean myTurn;
    protected boolean gameEnded;
    private boolean gameStarted;


    /**
     * constructor of ClientApp
     * @throws RemoteException
     */
    protected Client() throws RemoteException {

        super();
        model= new ClientModel();
        myTurn=false;
        gameEnded=false;
        left=false;
        gameStarted=false;
        pingChecker=new PingFromServer(this);
        (new Thread(pingChecker)).start();

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

            new Thread(()-> {
                try {
                    view.getNumOfPlayer();
                } catch (RemoteException e) {
                    //System.out.println("--- ops... a remote exception occurred while communicating number of players to the server");
                    view.showException("--- ops... a remote exception occurred while communicating number of players to the server");
                } catch (NullPointerException e){
                    System.out.println("--- error: view is null");
                }
            }).start();
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

        try {
            model.initializeMatrixes(new Matrix(board), new Matrix(myShelf), otherPlayersMatr);
            model.setMyScore(myScore);
        }catch (Exception e){
            view.showException("---ops... something went wrong while updating the board and other's bookshelf");
        }

        view.updateBoard();

    }

    /**
     * method called by the server to show the user an ordered rank of players
     * and to end the match
     * @throws RemoteException
     */
    @Override
    public void theGameEnd(Map< String, Integer> results) throws RemoteException{

        gameEnded=true;
        pingChecker.stopPingProcess();
        view.endGame(results);
        System.setProperty("java.rmi.server.hostname","192.168.0.1" );

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
                            } catch (InvalidChoiceException | IOException | NotConnectedException |
                                     InvalidParametersException | NotMyTurnException e) {
                                view.showException("---ops...something went wrong during your turn");
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
        gameStarted=true;
        view.startPlay();


    }

    /**
     * @throws RemoteException RMI exception
     */
    @Override
    public void ping() throws RemoteException{

        pingChecker.setConnected(true);
       // System.out.println("ping() received");

        try{
            //System.out.println("*** pong() the server");
            notifyPong();
        } catch (RemoteException e) {
            view.showException("---ops..."+e.getMessage());
        }
    }

    @Override
    public void receiveMessage(String sender, String message) throws RemoteException{
        model.getMyChat().addMessage(sender, message);
        view.plotNewMessage(sender, message );
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
     * @return the view (GUI/TUI)
     */
    public View getView() {
        return view;
    }


    public static void setView(View view1){
        view=view1;
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

    /**
     * @return pingChecker
     */
    public PingFromServer getPingChecker() {
        return pingChecker;
    }

    /**
     * @return true if the game has started
     */
    public boolean isGameStarted() {
        return gameStarted;
    }

    //-------------------------------------- RMI vs Socket layer --------------------------------------

}
