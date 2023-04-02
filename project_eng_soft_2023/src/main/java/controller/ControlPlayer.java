package controller;

import model.*;

import java.util.ArrayList;
import java.util.List;

public class ControlPlayer {

    private final int playerID;

    private PlayerStatus playerStatus;

    final private Bookshelf bookshelf;



    public ControlPlayer(Board board, int playerID) {
        bookshelf=new Bookshelf(board);
        this.playerID=playerID;
    }


    public List<Tile> catchTile(List<Integer> coord) throws InvalidParameters, NotAvailableTiles, NotEnoughSpace, NotInLine {
        List<Tile> temp= new ArrayList<>();
         if(coord.size()!=2&&coord.size()!=4&&coord.size()!=6) {
             throw new InvalidParameters();
         }
        else if (coord.size()==2) return bookshelf.getBoard().subTiles(coord.get(0), coord.get(1));
        else if (coord.size()==4) return bookshelf.getBoard().subTiles(coord.get(0), coord.get(1), coord.get(2), coord.get(3), bookshelf);
        else return bookshelf.getBoard().subTiles(coord.get(0), coord.get(1), coord.get(2), coord.get(3),coord.get(4), coord.get(5), bookshelf);
    }
    public int getPlayerID() {
        return playerID;
    }

    public PlayerStatus getPlayerStatus() {
        return playerStatus;
    }


    public Bookshelf getBookshelf() {
        return bookshelf;
    }

    public void setPlayerStatus(PlayerStatus playerStatus) {
        this.playerStatus = playerStatus;
    }


}
