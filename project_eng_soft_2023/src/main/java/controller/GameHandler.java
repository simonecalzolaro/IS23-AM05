package controller;

import client.ClientHandler;
import model.Tile;

import java.rmi.Remote;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface GameHandler extends Remote {

    boolean chooseBoardTiles(List<Tile> choosenTiles, List<Integer> coord, ClientHandler ch);

    int insertShelfTiles(ArrayList<Tile> choosenTiles, int choosenColumn , ClientHandler ch);

    int getMyScore(ClientHandler ch );

}
