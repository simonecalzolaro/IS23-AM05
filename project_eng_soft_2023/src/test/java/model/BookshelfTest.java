package model;


import myShelfieException.InvalidChoiceException;
import myShelfieException.InvalidLenghtException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class BookshelfTest {


    /**
     * This test analyze all possible cases of inserting tiles into the shelf, here there are 4 main cases:
     * 1)method receive an array of invalid lenght (<0 or >3)
     * 2)method receive a columns with not enough space for the tiles
     * 3)method receive an index of ouf bounds
     * 4)method receives valid parameters so the insertion is valid
     * @throws InvalidLenghtException
     * @throws InvalidChoiceException
     */

    @Test
    void putTiles() throws InvalidLenghtException, InvalidChoiceException {
        Board board = new Board();
        board.initializeBoard(4);
        Bookshelf b = new Bookshelf(board);
        ArrayList<Tile> arr1= new ArrayList<>(){{add(Tile.BLUE); add(Tile.GREEN); add(Tile.WHITE);}};


        ArrayList<Tile> arr0= new ArrayList<>(){{}};
        ArrayList<Tile> arrEx= new ArrayList<>(){{add(Tile.BLUE); add(Tile.GREEN); add(Tile.WHITE);add(Tile.GREEN);}};

        //inserimento di stream_tiles di lunghezza non accettabile
        Assertions.assertThrows(InvalidLenghtException.class, ()->{
            b.putTiles(arr0,1);
        });

        Assertions.assertThrows(InvalidLenghtException.class, ()->{
            b.putTiles(arrEx,0);
        });

        //caso colonna selezionata piena
        Assertions.assertThrows(InvalidChoiceException.class, ()->{
            b.putTiles(arr1,3);
            b.putTiles(arr1,3);
            b.putTiles(arr1,3);
        });

        //caso colonna di indice non valido
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> {
            b.putTiles(arr1,-1);
        });

        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> {
            b.putTiles(arr1,5);
        });

        //inserimento corretto

        assertTrue(b.putTiles(arr1,4));

    }


    /**
     * This test analyzes 3 main cases:
     * 1)empty shelf
     * 2)full shelf
     * 3)randomly filled shelf
     * @throws InvalidLenghtException
     * @throws InvalidChoiceException
     */
    @Test
    void maxShelfSpace() throws InvalidLenghtException, InvalidChoiceException {
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

    /**
     * This test analyzes 3 main case:
     * 1)Scored points having an empty shelf
     * 2)Scored points having a full shelf with tiles with the same color
     * 3)Scored points having a randomly filled shelf
     * @throws InvalidLenghtException
     * @throws InvalidChoiceException
     */

    @Test
    void getScoreGroups() throws InvalidLenghtException, InvalidChoiceException {
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



        assertEquals(18,b1.getScoreGroups());

    }


    /**
     * This test check if the points scored by reaching the common goal cards goal
     */

    @Test
    void getScoreCGC() {
        Board board = new Board();
        board.initializeBoard(4);

        Bookshelf bookshelf = new Bookshelf(board);

        board.setCGC1(1,4);

        board.setCGC2(2,4);

        //caso shelf vuota
        assertEquals(0,bookshelf.getScoreCGC());

        //caso shelf 1
        Tile[][] matr=
                {{Tile.PINK, Tile.BLUE, Tile.GREEN, Tile.WHITE, Tile.BLUE},
                        {Tile.PINK, Tile.BLUE, Tile.GREEN, Tile.YELLOW, Tile.BLUE},
                        {Tile.WHITE, Tile.WHITE, Tile.EMPTY, Tile.GREEN, Tile.WHITE},
                        {Tile.EMPTY, Tile.WHITE, Tile.EMPTY, Tile.GREEN, Tile.WHITE},
                        {Tile.EMPTY, Tile.WHITE, Tile.EMPTY, Tile.EMPTY, Tile.EMPTY},
                        {Tile.EMPTY, Tile.EMPTY, Tile.EMPTY, Tile.EMPTY, Tile.EMPTY}};

        bookshelf.setShelf(matr);

        bookshelf.getShelf();


        assertEquals(8,bookshelf.getScoreCGC());

        Tile matr1[][]=
                {{Tile.PINK   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.PINK   },
                        {Tile.BLUE   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.BLUE   },
                        {Tile.BLUE   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.BLUE   },
                        {Tile.BLUE   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.BLUE   },
                        {Tile.BLUE   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.BLUE   },
                        {Tile.PINK   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.PINK   }};

        bookshelf.setShelf(matr1);

        assertEquals(16,bookshelf.getScoreCGC());







    }


    /**
     * NB: this test requires an additional method setPGC() on the reference code because otherwise there's no way
     * to assing a pre-established PersonalGoalCard
     * This test analyze all the cases for receiving points (from 0 to 12 points)
     * @throws InvalidLenghtException
     * @throws InvalidChoiceException
     */

    @Test
    void getScorePGC() throws InvalidLenghtException, InvalidChoiceException {
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
        //assertEquals(3,b.getScorePGC());

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

    /**
     * This test analyze 3 cases:
     * 1)Game's not ended
     * 2)Game's ended but current player doesn't win
     * 3)Game's ended and current player win
     * @throws InvalidLenghtException
     * @throws InvalidChoiceException
     */

    @Test
    void checkEOG() throws InvalidLenghtException, InvalidChoiceException {
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

    /**
     * Similar to the previous test
     * @throws InvalidLenghtException
     * @throws InvalidChoiceException
     */

    @Test
    void getScoreEOG() throws InvalidLenghtException, InvalidChoiceException {
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





}