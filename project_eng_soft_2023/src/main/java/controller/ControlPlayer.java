package controller;

import client.ClientHandler;
import model.*;
import myShelfieException.*;

import java.io.IOException;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public abstract class ControlPlayer implements GameHandler, Serializable {

    /**
     *Player's id
     */
    protected final String nickname;

    /**
     * player's status
     */
    protected PlayerStatus playerStatus;

    /**
     * player's bookshelf
     */
    protected final Bookshelf bookshelf;

    /**
     * player's score
     */
    protected int score;


    protected Game game;


    /**
     * Assign player id
     * Initialize score
     * Set player status as NOT_MY_TURN
     * @param nickname: unique player nickname
     * @param board: unique board
     */
    public ControlPlayer(String nickname, Board board) throws RemoteException{

        this.nickname = nickname;
        bookshelf = new Bookshelf(board);
        score = 0;
        playerStatus = PlayerStatus.NOT_MY_TURN;

        (new Thread(new PingPong(this))).start(); //starting PinPonging
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
            bookshelf.putTiles(stream_tiles,column);
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
    public boolean chooseBoardTiles(List<Integer> coord) throws RemoteException, NotConnectedException, InvalidParametersException, NotMyTurnException, InvalidChoiceException {

            catchTile(coord);
            return true;
    }

    /**
     * method called by clients to insert the tiles they choose from the board into their bookshelf
     * @param choosenColumn column where to insert the tiles
     * @param coord tile coordinates
     * @throws NotConnectedException
     * @throws NotMyTurnException
     */
    @Override
    public boolean insertShelfTiles( int choosenColumn, List<Integer> coord) throws RemoteException, NotConnectedException, NotMyTurnException, InvalidLenghtException, InvalidChoiceException {

        ArrayList<Tile> choosenTiles=new ArrayList<>();

        for(int i=0; i<coord.size()/2; i+=2){
            choosenTiles.add(game.getBoard().getBoard()[i][i+1]);
        }

        //inserting the tiles in the bookshelf
        if( ! insertTiles(choosenTiles, choosenColumn)) return false;

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
            } catch (IOException e) { throw new RuntimeException(e); }
        }

        //tell the next player to start his turn
        try {
            ControlPlayer nextPlayer= game.getPlayers().get(game.getCurrPlayer());
            nextPlayer.notifyStartYourTurn();
        } catch (IOException e) { throw new RuntimeException(e); }

        return true;

    }

    /**
     * @return the score of the player ch
     * @throws RemoteException
     */
    @Override
    public int getMyScore() throws RemoteException{

        return bookshelf.getMyScore();

    }



    //-------------------------------------- RMI vs Socket layer --------------------------------------


    /**
     * this method tells to "nextClient" to start his turn, is divided in RMI and socket
     * @return true if everything went fine
     * @throws RemoteException
     */
    public abstract Boolean notifyStartYourTurn() throws IOException;

    /**
     * this method tells to the current user that his turn is finished, is divided in RMI and socket
     * @return true if everything went fine
     * @throws RemoteException
     */
    public abstract Boolean notifyEndYourTurn() throws IOException;

    /**
     * this method tells to all users to update their board to the new one, is divided in RMI and socket
     * @throws RemoteException
     */
    public abstract void notifyUpdatedBoard() throws IOException;

    /**
     * this method tells to all users that the game they're playing is ended, is divided in RMI and socket
     * @throws RemoteException
     */
    public abstract void notifyEndGame() throws IOException;


    /**
     * this method tells to all users that the game has started and that they aren't anymore in the waiting room, is divided in RMI and socket
     * @throws RemoteException
     */
    public  abstract void notifyStartPlaying() throws IOException;


    /**
     * this method tells asks a user how many players he wants in his game, is divided in RMI and socket
     * @return the chosen number of players
     * @throws RemoteException
     */
    public abstract int askNumberOfPlayers() throws IOException;


    /**
     * @return true if the client is connected
     * @throws RemoteException RMI exception
     */
    public abstract boolean askPing() throws IOException;



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
     * @param g of tupe Game
     */
    public void setGame(Game g){
        this.game=g;
    }

    /**
     * @return game
     */
    public controller.Game getGame() {
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

            default:
                System.out.println(" impossible to set "+nickname+" status from "+this.playerStatus + " to " + ps);
        }
    }


    abstract public void setClientHandler(ClientHandler cliHnd);

    abstract public void setStreams(ArrayList<Stream> streams);


}
