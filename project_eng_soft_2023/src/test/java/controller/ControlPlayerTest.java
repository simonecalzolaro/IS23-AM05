package controller;

import client.ClientHandler;
import client.RMIClient;
import model.Board;
import model.Tile;
import myShelfieException.InvalidChoiceException;
import myShelfieException.InvalidLenghtException;
import myShelfieException.NotConnectedException;
import myShelfieException.NotMyTurnException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.rmi.RemoteException;
import java.util.ArrayList;

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
    void insertTiles() throws NotConnectedException, InvalidChoiceException, NotMyTurnException, InvalidLenghtException, RemoteException {
        Board board = new Board();
        board.initializeBoard(4);
        ControlPlayer cp = new RMIControlPlayer("Tony", board, ch);

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
        Assertions.assertThrows(InvalidChoiceException.class, ()->{
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