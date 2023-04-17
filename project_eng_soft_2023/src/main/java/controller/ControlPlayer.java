package controller;

import model.*;

import java.util.ArrayList;
import java.util.List;

public class ControlPlayer {

    /**
     *Player id
     */
    private final String nickname;

    /**
     * player status
     */

    private PlayerStatus playerStatus;

    /**
     * player bookshelf
     */
    private final Bookshelf bookshelf;

    /**
     * player score
     */
    private int score;


    /**
     * Assign player id
     * Initialize score
     * Set player status as NOT_MY_TURN
     * @param nickname: unique player nickname
     * @param board: unique board
     */
    public ControlPlayer(String nickname,Board board) {

        this.nickname=nickname;
        bookshelf = new Bookshelf(board);
        score = 0;
        playerStatus = PlayerStatus.NOT_MY_TURN;
    }

    /**
     *
     * @param stream_tiles ordered list of tiles to insert
     * @param column column where to insert the tiles
     * @throws NotMyTurnException: the player status is NOT_MY_TURN
     * @throws NotConnectedException: the player is not connected
     * @return always true when
     */
    public boolean insertTiles(ArrayList<Tile> stream_tiles, int column) throws NotMyTurnException, NotConnectedException, NotEnoughSpaceException, InvalidLenghtException {

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
     * @throws NotAvailableTilesException the tiles selected are not catchable
     * @throws NotEnoughSpaceException the player does not have enough space in the bookshelf
     * @throws NotInLineException the tiles selected are not in line
     * @throws NotMyTurnException the player status is NOT_MY_TURN (the move is not allowed)
     * @throws NotConnectedException the player is not connected
     */
    public List<Tile> catchTile(List<Integer> coord) throws InvalidParametersException, NotAvailableTilesException, NotEnoughSpaceException, NotInLineException, NotMyTurnException, NotConnectedException {

        List<Tile> temp= new ArrayList<>();

        if(playerStatus==PlayerStatus.NOT_MY_TURN) {
            throw new NotMyTurnException();
        }

        else if(playerStatus == PlayerStatus.NOT_ONLINE){
            throw new NotConnectedException();
        }

        else if(coord.size()!=2 && coord.size()!=4 && coord.size()!=6) {
             throw new InvalidParametersException();
         }

        else if (coord.size()==2) return bookshelf.getBoard().subTiles(coord.get(0), coord.get(1));
        else if (coord.size()==4) return bookshelf.getBoard().subTiles(coord.get(0), coord.get(1), coord.get(2), coord.get(3), bookshelf);
        else return bookshelf.getBoard().subTiles(coord.get(0), coord.get(1), coord.get(2), coord.get(3),coord.get(4), coord.get(5), bookshelf);
    }

    /**
     *
     * @return playerID
     */
    public String getPlayerNickname() {
        return nickname;
    }

    /**
     *
     * @return playerStatus
     */

    public PlayerStatus getPlayerStatus() {
        return playerStatus;
    }

    /**
     *
     * @return bookshelf
     */

    public Bookshelf getBookshelf() {
        return bookshelf;
    }

    /**
     *
     * @param playerStatus: player status
     */
    public void setPlayerStatus(PlayerStatus playerStatus) {
        this.playerStatus = playerStatus;
    }





}
