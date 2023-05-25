package controller;

import client.ClientHandler;
import model.*;
import myShelfieException.*;

import java.io.IOException;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public abstract class ControlPlayer extends UnicastRemoteObject implements GameHandler, ControllerAskNotify, Serializable {

    /**
     *Player's id
     */
    protected final String nickname;

    /**
     * Player's status
     */
    protected PlayerStatus playerStatus;

    /**
     * Player's bookshelf
     */
     Bookshelf bookshelf;

    /**
     * Player's score
     */
    protected int score;

    /**
     * Player's game
     */
    protected Game game;


    /**
     * Assign player id
     * Initialize score
     * Set player status as NOT_MY_TURN
     * @param nickname: unique player nickname
     */
    public ControlPlayer(String nickname) throws RemoteException{

        this.nickname = nickname;
        playerStatus = PlayerStatus.NOT_MY_TURN;

        //(new Thread(new PingPong(this))).start(); //starting PinPonging
    }

    public void initializeControlPlayer(Board board){

        System.out.println("    "+nickname+" player is initialized, bookshelf:"+ bookshelf);
        bookshelf = new Bookshelf(board);
        score = 0;
        playerStatus = PlayerStatus.NOT_MY_TURN;
        System.out.println("    "+nickname+" player is initialized, bookshelf:"+ bookshelf);

    }

    /**
     *
     * @param stream_tiles ordered list of tiles to insert
     * @param column column where to insert the tiles
     * @throws NotMyTurnException : the player status is NOT_MY_TURN
     * @throws NotConnectedException : the player is not connected
     * @return always true when
     */
    public boolean insertTiles(ArrayList<Tile> stream_tiles, int column) throws NotMyTurnException, NotConnectedException, InvalidLenghtException, InvalidChoiceException {

        if(playerStatus == PlayerStatus.NOT_MY_TURN){
            throw new NotMyTurnException();
        }
        else if(playerStatus == PlayerStatus.NOT_ONLINE){
            throw new NotConnectedException();
        }
        else{
            bookshelf.putTiles(stream_tiles, column);
            updateScore();
            return true;
        }

    }


    private void updateScore(){
        score = bookshelf.getMyScore();
    }

    /**
     * if everything it's ok, remove the selected tiles from the board
     * @param coord list
     * @return List of catch tiles
     * @throws InvalidParametersException the list of coordinates is not compliant
     * @throws InvalidChoiceException the tiles selected are not catchable, the player have no enough space in his bookshelf or tiles are not in line
     * @throws NotMyTurnException the player status is NOT_MY_TURN (the move is not allowed)
     * @throws NotConnectedException the player is not connected
     */
    public List<Tile> catchTile(List<Integer> coord) throws InvalidParametersException, InvalidChoiceException, NotMyTurnException, NotConnectedException {

        System.out.println("    "+nickname+" catchTile bookshelf:"+ bookshelf);

        if (playerStatus == PlayerStatus.NOT_MY_TURN) {
            throw new NotMyTurnException();
        } else if (playerStatus == PlayerStatus.NOT_ONLINE) {
            throw new NotConnectedException();
        } else {
            return switch (coord.size()) {
                case 2 -> bookshelf.getBoard().chooseTiles(coord.get(0), coord.get(1));
                case 4 ->
                        bookshelf.getBoard().chooseTiles(coord.get(0), coord.get(1), coord.get(2), coord.get(3), bookshelf);
                case 6 ->
                        bookshelf.getBoard().chooseTiles(coord.get(0), coord.get(1), coord.get(2), coord.get(3), coord.get(4), coord.get(5), bookshelf);
                default -> throw new InvalidParametersException();
            };
        }
    }




    //-------------------------------------- GameHandler implemented methods --------------------------------------

    /**
     * method called by the clients to choose tiles from the board of their game
     * @param coord coordinates of the chosen tiles
     * @return true if the chosen tiles are valid
     */
    @Override
    public void chooseBoardTiles(List<Integer> coord) throws RemoteException, NotConnectedException, InvalidParametersException, NotMyTurnException, InvalidChoiceException {

            catchTile(coord);

    }

    /**
     * method called by clients to insert the tiles they choose from the board into their bookshelf
     * @param choosenColumn column where to insert the tiles
     * @param coord tile coordinates
     * @throws NotConnectedException
     * @throws NotMyTurnException
     */
    @Override
    public void insertShelfTiles( int choosenColumn, List<Integer> coord) throws RemoteException, NotConnectedException, NotMyTurnException, InvalidLenghtException, InvalidChoiceException {

        ArrayList<Tile> choosenTiles=new ArrayList<>();
        boolean goOn;

        for(int i=0; i<coord.size()/2; i+=2){
            choosenTiles.add(game.getBoard().getBoard()[i][i+1]);
        }

        //inserting the tiles in the bookshelf
        goOn=insertTiles(choosenTiles, choosenColumn);
        if( !goOn  ) return;

        //and subtracting the same tiles from the board
        game.getBoard().subTiles(coord);

        //tell to ch that his turn is completed
        try {
            notifyEndYourTurn();
        } catch (IOException e) { throw new RuntimeException(e); }

        //Game.endTurn() is called to update the turn to the next player and update the game status
        game.endTurn();

        //notify the updated board to all the clients participating in the same game of ch
        try {

            for(ControlPlayer cp: game.getPlayers()){
                //if player cp is online ill update his board
                if(!cp.getPlayerStatus().equals(PlayerStatus.NOT_ONLINE)) cp.notifyUpdatedBoard();
            }

        } catch (IOException e) { throw new RuntimeException(e); }


        //if the game is ended every player is notified and the results are shown
        if(game.getGameStatus().equals(GameStatus.END_GAME)){
            try {
                for(ControlPlayer cp: game.getPlayers()){
                    cp.notifyEndGame();
                }
            }catch (IOException e) { throw new RuntimeException(e); }
        }

        //tell the next player to start his turn
        try {
            ControlPlayer nextPlayer= game.getPlayers().get(game.getCurrPlayer());
            nextPlayer.notifyStartYourTurn();
        } catch (IOException e) { throw new RuntimeException(e); }

    }


    /**
     * method called by the client to pass his turn to the next player (like timeout or others)
     * @throws RemoteException
     */
    @Override
    public void passMyTurn() throws RemoteException{

        game.endTurn();

        //notify the updated board to all the clients participating in the same game of ch
        try {

            for(ControlPlayer cp: game.getPlayers()){
                //if player cp is online ill update his board
                if(!cp.getPlayerStatus().equals(PlayerStatus.NOT_ONLINE)) cp.notifyUpdatedBoard();
            }

        } catch (IOException e) { throw new RuntimeException(e); }


        //if the game is ended every player is notified and the results are shown
        if(game.getGameStatus().equals(GameStatus.END_GAME)){
            try {
                for(ControlPlayer cp: game.getPlayers()){
                    cp.notifyEndGame();
                }
            }catch (IOException e) { throw new RuntimeException(e); }
        }

        //tell the next player to start his turn
        try {
            ControlPlayer nextPlayer= game.getPlayers().get(game.getCurrPlayer());
            nextPlayer.notifyStartYourTurn();
        } catch (IOException e) { throw new RuntimeException(e); }

    }

    //-------------------------------------- RMI vs Socket layer --------------------------------------




    //-------------------------------------- getter and setter methods --------------------------------------

    /**
     * @return playerID
     */
    public String getPlayerNickname() {
        return nickname;
    }

    /**
     * @return playerStatus
     */
    public PlayerStatus getPlayerStatus() {
        return playerStatus;
    }

    /**
     * @return bookshelf
     */
    public Bookshelf getBookshelf() {
        return bookshelf;
    }

    /**
     * @param g of type Game
     */
    public void setGame(Game g){
        this.game=g;
    }

    /**
     * @return game
     */
    public Game getGame() {
        return game;
    }

    /**
     * @param ps: player status
     */
    public void setPlayerStatus(PlayerStatus ps) {


        switch (this.getPlayerStatus()){

            case MY_TURN :
                this.playerStatus = ps;
                //se va offline durante il suo turno devo cercare il client successivo
                if(ps.equals(PlayerStatus.NOT_ONLINE)) {
                    game.endTurn();
                }
                break;

            case NOT_MY_TURN :
                this.playerStatus = ps;
                break;

            case NOT_ONLINE:
                if(ps.equals(PlayerStatus.NOT_MY_TURN)){
                    this.playerStatus = ps;
                }
                break;

            case WAITING_ROOM:

                    this.playerStatus = ps;

                break;

            case nOfPlayerAsked:
                if(ps.equals(PlayerStatus.MY_TURN)){
                    this.playerStatus = ps;
                }
                break;

            default:
                System.out.println(" impossible to set "+nickname+" status from "+this.playerStatus + " to " + ps);
        }
    }

    abstract public void setClientHandler(ClientHandler cliHnd);

    abstract public void setStreams(ArrayList<Stream> streams);


}
