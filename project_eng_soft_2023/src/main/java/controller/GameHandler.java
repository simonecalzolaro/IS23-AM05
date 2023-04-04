package controller;

import model.Tile;

import java.rmi.Remote;
import java.util.ArrayList;
import java.util.Map;

public interface GameHandler extends Remote {

    boolean chooseBoardTiles(Map< Tile, ArrayList<Integer>> choosenTiles );

    boolean insertShelfTiles(ArrayList<Tile> choosenTiles, int choosenColumn );

    int getMyScore();

}
