package model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class BookshelfTest {





    @Test
    void putTiles() {
        Board board = new Board();
        board.initializeBoard(4);
        Bookshelf b = new Bookshelf(board);
        ArrayList<Tile> arr1= new ArrayList<>(){{add(Tile.BLUE); add(Tile.GREEN); add(Tile.WHITE);}};


        ArrayList<Tile> arr0= new ArrayList<>(){{}};
        ArrayList<Tile> arrEx= new ArrayList<>(){{add(Tile.BLUE); add(Tile.GREEN); add(Tile.WHITE);add(Tile.GREEN);}};

        //inserimento di stream_tiles di lunghezza non accettabile
        assertEquals(-2,b.putTiles(arr0,1));
        assertEquals(-2,b.putTiles(arrEx,1));

        //inserimento corretto di tiles
        assertEquals(0,b.putTiles(arr1,2));
        b.putTiles(arr1,3);
        assertEquals(0,b.putTiles(arr1,3));

        //inserimento in colonna senza abbastanza spazio
        b.putTiles(arr1,3);
        assertEquals(-1,b.putTiles(arr1,3));

        //inserimento in colonna non accettabile
        assertEquals(-3,b.putTiles(arr1,5));
        assertEquals(-3,b.putTiles(arr1,-1));








    }



    @Test
    void maxShelfSpace() {
        Board board = new Board();
        board.initializeBoard(4);
        Bookshelf b = new Bookshelf(board);
        ArrayList<Tile> arr1= new ArrayList<>(){{add(Tile.BLUE); add(Tile.GREEN); add(Tile.WHITE);}};

        //caso shelf vuota
        assertEquals(6,b.maxShelfSpace());

        //caso shelf piena
        b.putTiles(arr1,0);
        b.putTiles(arr1,0);
        b.putTiles(arr1,1);
        b.putTiles(arr1,1);
        b.putTiles(arr1,2);
        b.putTiles(arr1,2);
        b.putTiles(arr1,3);
        b.putTiles(arr1,3);
        b.putTiles(arr1,4);
        b.putTiles(arr1,4);

        assertEquals(0,b.maxShelfSpace());

        //caso shelf casuale
        Bookshelf c = new Bookshelf(board);
        ArrayList<Tile> arrX= new ArrayList<>(){{add(Tile.BLUE);}};
        c.putTiles(arrX,0);
        c.putTiles(arr1,0);
        c.putTiles(arr1,1);
        c.putTiles(arr1,1);
        c.putTiles(arr1,2);
        c.putTiles(arr1,2);
        c.putTiles(arrX,3);
        c.putTiles(arr1,3);
        c.putTiles(arr1,4);
        c.putTiles(arrX,4);

        assertEquals(2,c.maxShelfSpace());


    }

    @Test
    void getMyScore() {
    }

    @Test
    void getScoreGroups() {
        Board board = new Board();
        board.initializeBoard(4);
        Bookshelf b = new Bookshelf(board);

        //caso shelf vuota
        assertEquals(0,b.getScoreGroups());

        //caso tutte di un colore + tabella piena
        ArrayList<Tile> arr3= new ArrayList<>(){{add(Tile.BLUE);add(Tile.BLUE);add(Tile.BLUE);}};
        b.putTiles(arr3,0);
        b.putTiles(arr3,0);
        b.putTiles(arr3,1);
        b.putTiles(arr3,1);
        b.putTiles(arr3,2);
        b.putTiles(arr3,2);
        b.putTiles(arr3,3);
        b.putTiles(arr3,3);
        b.putTiles(arr3,4);
        b.putTiles(arr3,4);
        assertEquals(8,b.getScoreGroups());

        //caso disposizione particolare

        Board board1 = new Board();
        board1.initializeBoard(4);
        Bookshelf b1 = new Bookshelf(board1);
        ArrayList<Tile> arrA= new ArrayList<>(){{add(Tile.LIGHTBLUE);add(Tile.LIGHTBLUE);add(Tile.LIGHTBLUE);}};
        ArrayList<Tile> arrB= new ArrayList<>(){{add(Tile.PINK);}};
        ArrayList<Tile> arrC= new ArrayList<>(){{add(Tile.GREEN);add(Tile.LIGHTBLUE);add(Tile.LIGHTBLUE);}};
        ArrayList<Tile> arrD= new ArrayList<>(){{add(Tile.PINK);add(Tile.PINK);add(Tile.PINK);}};
        ArrayList<Tile> arrE= new ArrayList<>(){{add(Tile.GREEN);add(Tile.GREEN);add(Tile.GREEN);}};
        ArrayList<Tile> arrF= new ArrayList<>(){{add(Tile.BLUE);add(Tile.BLUE);add(Tile.GREEN);}};
        ArrayList<Tile> arrG= new ArrayList<>(){{add(Tile.PINK);add(Tile.BLUE);add(Tile.BLUE);}};

        b1.putTiles(arrA,0);
        b1.putTiles(arrB,0);
        b1.putTiles(arrC,1);
        b1.putTiles(arrD,1);
        b1.putTiles(arrE,2);
        b1.putTiles(arrB,2);
        b1.putTiles(arrF,3);
        b1.putTiles(arrG,4);

        b1.getBookshelf();

        assertEquals(18,b1.getScoreGroups());











    }




    @Test
    void getScoreCGC() {
    }

    @Test
    void getScorePGC() {
        Board board = new Board();
        board.initializeBoard(4);
        Bookshelf b = new Bookshelf(board);
        PersonalGoalCard pgc = new PersonalGoalCard(1);
        b.setPGC(pgc);

        //caso shelf vuota
        assertEquals(0,b.getScorePGC());

        //caso 1 punto
        ArrayList<Tile> arr1= new ArrayList<>(){{add(Tile.PINK);}};
        ArrayList<Tile> arr2= new ArrayList<>(){{add(Tile.BLUE); add(Tile.GREEN);}};

        ArrayList<Tile> arr3= new ArrayList<>(){{add(Tile.BLUE); add(Tile.GREEN); add(Tile.WHITE);}};
        b.putTiles(arr3,0);
        b.putTiles(arr2,0);
        b.putTiles(arr1,0);


        assertEquals(1,b.getScorePGC());

        //caso 2 punti
        ArrayList<Tile> arr1A= new ArrayList<>(){{add(Tile.YELLOW); }};
        b.putTiles(arr2,1);
        b.putTiles(arr1A,1);
        assertEquals(2,b.getScorePGC());

        //caso 3 punti
        ArrayList<Tile> arr1B= new ArrayList<>(){{add(Tile.LIGHTBLUE);}};
        b.putTiles(arr1B,2);
        assertEquals(3,b.getScorePGC());

        //caso 6 punti
        ArrayList<Tile> arr1C= new ArrayList<>(){{add(Tile.WHITE);}};
        b.putTiles(arr3,3);
        b.putTiles(arr1C,3);
        assertEquals(6,b.getScorePGC());

        //caso 9 punti
        ArrayList<Tile> arr1D= new ArrayList<>(){{add(Tile.BLUE);}};
        b.putTiles(arr3,2);
        b.putTiles(arr1D,2);
        b.putTiles(arr1D,2);
        assertEquals(9,b.getScorePGC());

        //caso 12 punti
        ArrayList<Tile> arr1E= new ArrayList<>(){{add(Tile.GREEN);}};
        b.putTiles(arr3,4);
        b.putTiles(arr1E,4);
        b.putTiles(arr1E,4);
        assertEquals(12,b.getScorePGC());



    }

    @Test
    void checkEOG() {
        Board board = new Board();
        board.initializeBoard(4);
        Bookshelf b = new Bookshelf(board);

        //caso partita non è finita
        assertFalse(b.checkEOG());



        //caso partita finita ma non ho vinto
        board.setEOG();
        assertTrue(b.checkEOG());

        //caso finisco per primo ,vinco e termina la partita
        Board board1 = new Board();
        board1.initializeBoard(4);
        Bookshelf b1 = new Bookshelf(board1);

        ArrayList<Tile> arr1= new ArrayList<>(){{add(Tile.BLUE); add(Tile.GREEN); add(Tile.WHITE);}};
        b1.putTiles(arr1,0);
        b1.putTiles(arr1,0);
        b1.putTiles(arr1,1);
        b1.putTiles(arr1,1);
        b1.putTiles(arr1,2);
        b1.putTiles(arr1,2);
        b1.putTiles(arr1,3);
        b1.putTiles(arr1,3);
        b1.putTiles(arr1,4);
        b1.putTiles(arr1,4);
        assertTrue(b1.checkEOG());



    }

    @Test
    void getScoreEOG() {
        Board board = new Board();
        board.initializeBoard(4);
        Bookshelf b = new Bookshelf(board);

        //caso partita non è finita
        assertEquals(0,b.getScoreEOG());

        //caso partita finita ma non ho vinto
        board.setEOG();
        assertEquals(0,b.getScoreEOG());

        //caso finisco per primo, vinco e termino la partita
        Board board1 = new Board();
        board1.initializeBoard(4);
        Bookshelf b1 = new Bookshelf(board1);

        ArrayList<Tile> arr1= new ArrayList<>(){{add(Tile.BLUE); add(Tile.GREEN); add(Tile.WHITE);}};
        b1.putTiles(arr1,0);
        b1.putTiles(arr1,0);
        b1.putTiles(arr1,1);
        b1.putTiles(arr1,1);
        b1.putTiles(arr1,2);
        b1.putTiles(arr1,2);
        b1.putTiles(arr1,3);
        b1.putTiles(arr1,3);
        b1.putTiles(arr1,4);
        b1.putTiles(arr1,4);
        b1.checkEOG();
        assertEquals(1,b1.getScoreEOG());


    }

    @Test
    void checkCG1(){

    }

    @Test
    void checkCG2(){

    }
}