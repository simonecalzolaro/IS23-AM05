package controller;

import client.ClientHandler;
import client.RMIClient;
import client.SocketClient;
import model.Board;
import model.Bookshelf;
import model.Tile;
import myShelfieException.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import view.TUI;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ControlPlayerTest {

    ClientHandler ch= (ClientHandler) new RMIClient();

    ControlPlayerTest() throws RemoteException {
    }


    /**
     * This test is similar to putTiles() test. It analyzes the new exception thrown by the method
     * and the exception thrown by the model
     *
     * @throws NotConnectedException
     * @throws InvalidChoiceException
     * @throws NotMyTurnException
     * @throws InvalidLenghtException
     */


    @Test
    void catchTile() throws RemoteException {


        ControlPlayer cp = new RMIControlPlayer("Ciro",new RMIClient());

        cp.setPlayerStatus(PlayerStatus.NOT_MY_TURN);
        List<Integer> coord = new ArrayList<>();
        coord.add(1);
        coord.add(2);

        Assertions.assertThrows(NotMyTurnException.class, ()->{
            cp.catchTile(coord);
        });

        cp.setPlayerStatus(PlayerStatus.NOT_ONLINE);

        Assertions.assertThrows(NotConnectedException.class, ()->{
            cp.catchTile(coord);
        });



    }


    @Test
    void chooseBoardTiles() throws InvalidChoiceException, NotConnectedException, InvalidParametersException, NotMyTurnException, RemoteException {

        ControlPlayer cp = new RMIControlPlayer("Ciro",new RMIClient());

        cp.setPlayerStatus(PlayerStatus.NOT_MY_TURN);
        List<Integer> coord = new ArrayList<>();
        coord.add(1);
        coord.add(2);

        Assertions.assertThrows(NotMyTurnException.class, ()->{
            cp.catchTile(coord);
        });

        cp.setPlayerStatus(PlayerStatus.NOT_ONLINE);

        Assertions.assertThrows(NotConnectedException.class, ()->{
            cp.catchTile(coord);
        });


    }




    @Test
    void insertTiles() throws RemoteException {
        Board board = new Board();
        board.initializeBoard(4);


        ControlPlayer cp = new RMIControlPlayer("Ciro",new RMIClient());

        cp.initializeControlPlayer(board);

        ArrayList<Tile> tiles = new ArrayList<>();
        tiles.add(Tile.BLUE);
        tiles.add(Tile.BLUE);

        tiles.add(Tile.BLUE);
        tiles.add(Tile.BLUE);


        cp.setPlayerStatus(PlayerStatus.MY_TURN);

        tiles.add(Tile.BLUE);
        tiles.add(Tile.BLUE);

        Assertions.assertThrows(InvalidLenghtException.class, ()->{
            cp.insertTiles(tiles,0);
        });


        tiles.clear();
        assertEquals(tiles.size(),0);

        Assertions.assertThrows(InvalidLenghtException.class, ()->{
            cp.insertTiles(tiles,0);
        });

        Assertions.assertThrows(InvalidLenghtException.class, ()->{
            cp.insertTiles(tiles,5);
        });


        Assertions.assertThrows(InvalidLenghtException.class, ()->{
            cp.insertTiles(tiles,-1);
        });





        cp.setPlayerStatus(PlayerStatus.NOT_MY_TURN);

        Assertions.assertThrows(NotMyTurnException.class, ()->{
            cp.insertTiles(tiles,0);
        });

        cp.setPlayerStatus(PlayerStatus.NOT_ONLINE);

        Assertions.assertThrows(NotConnectedException.class, ()->{
            cp.insertTiles(tiles,0);
        });

    }


    @Test
    void insertShelfTiles() throws RemoteException {


        Board board = new Board();
        board.initializeBoard(4);


        ControlPlayer cp = new RMIControlPlayer("Ciro",new RMIClient());

        cp.initializeControlPlayer(board);

        ArrayList<Integer> coord = new ArrayList<>();





        cp.setPlayerStatus(PlayerStatus.MY_TURN);



        Assertions.assertThrows(InvalidLenghtException.class, ()->{
            cp.insertShelfTiles(0,coord);
        });


        coord.clear();
        assertEquals(coord.size(),0);

        Assertions.assertThrows(InvalidLenghtException.class, ()->{
            cp.insertShelfTiles(0,coord);
        });

        Assertions.assertThrows(InvalidLenghtException.class, ()->{
            cp.insertShelfTiles(5,coord);
        });


        Assertions.assertThrows(InvalidLenghtException.class, ()->{
            cp.insertShelfTiles(-1,coord);
        });





        cp.setPlayerStatus(PlayerStatus.NOT_MY_TURN);

        Assertions.assertThrows(NotMyTurnException.class, ()->{
            cp.insertShelfTiles(0,coord);
        });

        cp.setPlayerStatus(PlayerStatus.NOT_ONLINE);

        Assertions.assertThrows(NotConnectedException.class, ()->{
            cp.insertShelfTiles(0,coord);
        });


        assertTrue(!cp.getPlayerStatus().equals(PlayerStatus.NOT_MY_TURN));




    }




    /*

    @Test
    void insertTiles() throws NotConnectedException, NotEnoughSpaceException, NotMyTurnException, InvalidLenghtException {
        Board board = new Board();
        board.initializeBoard(4);
        ControlPlayer cp = new ControlPlayer("Tony", board);

        ArrayList<Tile> arr1= new ArrayList<>(){{add(Tile.BLUE); add(Tile.GREEN); add(Tile.WHITE);}};


        ArrayList<Tile> arr0= new ArrayList<>(){{}};
        ArrayList<Tile> arrEx= new ArrayList<>(){{add(Tile.BLUE); add(Tile.GREEN); add(Tile.WHITE);add(Tile.GREEN);}};

        //case NotMyturnException

        Assertions.assertThrows(NotMyTurnException.class, () -> {
            cp.insertTiles(arr1,1);
        });

        //case NotConnectedException

        cp.setPlayerStatus(PlayerStatus.NOT_ONLINE);

        Assertions.assertThrows(NotConnectedException.class, () -> {
            cp.insertTiles(arr1,0);
        });

        //inserimento di stream_tiles di lunghezza non accettabile

        cp.setPlayerStatus(PlayerStatus.MY_TURN);

        Assertions.assertThrows(InvalidLenghtException.class, ()->{
            cp.insertTiles(arr0,1);
        });

        Assertions.assertThrows(InvalidLenghtException.class, ()->{
            cp.insertTiles(arrEx,0);
        });

        //caso colonna selezionata piena
        Assertions.assertThrows(NotEnoughSpaceException.class, ()->{
            cp.insertTiles(arr1,3);
            cp.insertTiles(arr1,3);
            cp.insertTiles(arr1,3);
        });


        //caso colonna di indice non valido
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> {
            cp.insertTiles(arr1,-1);
        });

        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> {
            cp.insertTiles(arr1,5);
        });

        //inserimento corretto
        assertTrue(cp.insertTiles(arr1,4));








    }

    @Test
    void catchTile() {
    }

    @Test
    void updateScore() {

    }
*/
}