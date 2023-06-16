package model;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

import myShelfieException.InvalidChoiceException;
import org.junit.jupiter.api.Test;

public class BoardTest  {

    private Board board;

    @Test
    public void testInitializeBoard1() {

        board = new Board();
        DeckCards deck= new DeckCards(4);
        board.initializeBoard(4);

        assertEquals(4, board.getNPlayers());
        Bag bag = null;
        board=null;

    }
    @Test
    public void testInitializeBoard2() {

        board = new Board();
        DeckCards deck= new DeckCards(3);
        board.initializeBoard(3);

        assertEquals(3, board.getNPlayers());
        Bag bag = null;
        board=null;

    }
    @Test
    public void testInitializeBoard3() {

        board = new Board();
        DeckCards deck= new DeckCards(2);
        board.initializeBoard(2);

        assertEquals(2, board.getNPlayers());
        Bag bag = null;
        board=null;

    }
    @Test
    public void testFill() {

        board = new Board();
        DeckCards deck= new DeckCards(4);
        board.initializeBoard(4);
        for(int i=0; i<9; i++){
            for (int j=0; j<9; j++){
                assertNotEquals(null, board.getBoard()[i][j]);
                assertNotEquals(Tile.EMPTY, board.getBoard()[i][j]);
            }
        }


        board=null;
    }

    @Test
    public void testFill2() {
        board = new Board();
        DeckCards deck= new DeckCards(4);
        board.initializeBoard(4);
        for(int i=0; i<9; i++){
            for (int j=0; j<9; j++){
                if(board.getBoard()[i][j]!=Tile.NOTAVAILABLE){
                    board.setTile(i, j, Tile.EMPTY);
                }
            }
        }

        board.setTile(1,5,Tile.LIGHTBLUE);
        board.setTile(3,2,Tile.LIGHTBLUE);
        board.setTile(4,0,Tile.LIGHTBLUE);
        board.setTile(4,5,Tile.LIGHTBLUE);
        board.setTile(5,3,Tile.LIGHTBLUE);
        board.setTile(7,5,Tile.LIGHTBLUE);

        int oldEmpty= board.getEmptyTilesNum();

        for(int i = 0; i<122; i++){
            board.getBag().getTile();
        }

        int tile= board.getBag().getTilesNum();
        board.fill();

        assertEquals(oldEmpty-tile, board.getEmptyTilesNum());
        board=null;



    }
    @Test
    public void testFill3() {
        board = new Board();
        DeckCards deck= new DeckCards(4);
        board.initializeBoard(4);
        for(int i=0; i<9; i++){
            for (int j=0; j<9; j++){
                if(board.getBoard()[i][j]!=Tile.NOTAVAILABLE){
                    board.setTile(i, j, Tile.EMPTY);
                }
            }
        }

        board.setTile(1,5,Tile.LIGHTBLUE);
        board.setTile(3,2,Tile.LIGHTBLUE);
        board.setTile(4,0,Tile.LIGHTBLUE);
        board.setTile(4,5,Tile.LIGHTBLUE);
        board.setTile(5,3,Tile.LIGHTBLUE);
        board.setTile(7,5,Tile.LIGHTBLUE);

        int oldEmpty= board.getEmptyTilesNum();

        for(int i = 0; i<60; i++){
            board.getBag().getTile();
        }

        int tile= board.getBag().getTilesNum();
        board.fill();
        assertEquals(oldEmpty-tile, board.getEmptyTilesNum());
        board=null;
    }

    @Test
    public void testEmptyBoard1() {
        board = new Board();
        DeckCards deck= new DeckCards(4);
        board.initializeBoard(4);
        for(int i=0; i<9; i++){
            for (int j=0; j<9; j++){
                if(board.getBoard()[i][j]!=Tile.NOTAVAILABLE){
                    board.setTile(i, j, Tile.EMPTY);
                }
            }
        }

        board.setTile(1,5,Tile.LIGHTBLUE);
        board.setTile(2,4,Tile.LIGHTBLUE);
        board.setTile(3,2,Tile.LIGHTBLUE);
        board.setTile(4,0,Tile.LIGHTBLUE);
        board.setTile(4,5,Tile.LIGHTBLUE);
        board.setTile(5,3,Tile.LIGHTBLUE);
        board.setTile(7,5,Tile.LIGHTBLUE);

        assertTrue(board.emptyBoard());
        board=null;
    }

    @Test
    public void testEmptyBoard2() {
        board = new Board();
        DeckCards deck= new DeckCards(4);
        board.initializeBoard(4);
        for(int i=0; i<9; i++){
            for (int j=0; j<9; j++){
                if(board.getBoard()[i][j]!=Tile.NOTAVAILABLE){
                    board.setTile(i, j, Tile.EMPTY);
                }
            }
        }

        board.setTile(1,5,Tile.LIGHTBLUE);
        board.setTile(1,4,Tile.LIGHTBLUE);


        assertFalse(board.emptyBoard());
        board=null;
    }

    @Test
    public void testSingleTile() {
        board = new Board();
        DeckCards deck= new DeckCards(4);
        board.initializeBoard(4);
        for(int i=0; i<9; i++){
            for (int j=0; j<9; j++){
                if(board.getBoard()[i][j]!=Tile.NOTAVAILABLE){
                    board.setTile(i, j, Tile.EMPTY);
                }
            }
        }
        board.setTile(0,3,Tile.LIGHTBLUE);
        board.setTile(1,5,Tile.LIGHTBLUE);
        board.setTile(3,2,Tile.LIGHTBLUE);
        board.setTile(7,5,Tile.LIGHTBLUE);
        board.setTile(6,5,Tile.LIGHTBLUE);



        assertTrue(board.singleTile(0,3));
        assertTrue(board.singleTile(1,5));
        assertTrue(board.singleTile(3,2));
        assertFalse(board.singleTile(7,5));

        for(int i=0; i<9; i++){
            for (int j=0; j<9; j++){
                if(board.getBoard()[i][j]!=Tile.NOTAVAILABLE){
                    board.setTile(i, j, Tile.EMPTY);
                }
            }
        }

        board.setTile(8, 4, Tile.LIGHTBLUE);
        assertTrue(board.singleTile(8,4));
        board.setTile(8, 4, Tile.EMPTY);

        board.setTile(4, 8, Tile.LIGHTBLUE);
        assertTrue(board.singleTile(4,8));


        board=null;
    }

    @Test
    public void testOkTiles() {
        board = new Board();
        DeckCards deck= new DeckCards(4);
        board.initializeBoard(4);
        for(int i=0; i<9; i++){
            for (int j=0; j<9; j++){
                if(board.getBoard()[i][j]!=Tile.NOTAVAILABLE){
                    board.setTile(i, j, Tile.EMPTY);
                }
            }
        }
        board.setTile(0,3,Tile.LIGHTBLUE);
        board.setTile(1,5,Tile.LIGHTBLUE);
        board.setTile(3,2,Tile.LIGHTBLUE);

        board.setTile(7,5,Tile.LIGHTBLUE);
        board.setTile(7,4,Tile.LIGHTBLUE);
        board.setTile(6,5,Tile.LIGHTBLUE);
        board.setTile(8,5,Tile.LIGHTBLUE);

        board.setTile(5,4,Tile.LIGHTBLUE);
        board.setTile(6,4,Tile.LIGHTBLUE);
        board.setTile(6,3,Tile.LIGHTBLUE);

        assertTrue(board.okTile(0,3));
        assertTrue(board.okTile(1,5));
        assertTrue(board.okTile(3,2));

        assertTrue(board.okTile(7,5));
        assertTrue(board.okTile(7,4));
        assertTrue(board.okTile(6,5));
        assertTrue(board.okTile(8,5));

        assertFalse(board.okTile(6,4));
        board=null;
    }
    @Test
    public void chooseOneTiles() throws InvalidChoiceException {
        board = new Board();
        DeckCards deck= new DeckCards(4);
        board.initializeBoard(4);

        assertThrows(InvalidChoiceException.class, ()->board.chooseTiles(0,0));

        board.chooseTiles(0,4);
    }

    @Test
    public void chooseTwoTiles() throws InvalidChoiceException {
        board = new Board();
        DeckCards deck= new DeckCards(4);
        board.initializeBoard(4);
        Bookshelf bookshelf= new Bookshelf(board);

        board.chooseTiles(0,3,0,4, bookshelf);

        assertThrows(InvalidChoiceException.class, ()-> board.chooseTiles(0,3,4,0, bookshelf));

        Tile[][] shelf=new Tile[][] { {Tile.BLUE, Tile.BLUE,Tile.BLUE,Tile.BLUE,Tile.BLUE},
                {Tile.BLUE, Tile.BLUE,Tile.BLUE,Tile.BLUE,Tile.BLUE},
                {Tile.BLUE, Tile.BLUE,Tile.BLUE,Tile.BLUE,Tile.BLUE},
                {Tile.BLUE, Tile.BLUE,Tile.BLUE,Tile.BLUE,Tile.BLUE},
                {Tile.BLUE, Tile.BLUE,Tile.BLUE,Tile.BLUE,Tile.BLUE},
                {Tile.BLUE, Tile.BLUE,Tile.BLUE,Tile.BLUE,Tile.BLUE},
        };

        bookshelf.setShelf(shelf);

        assertThrows(InvalidChoiceException.class, ()->board.chooseTiles(0,0,0,1,bookshelf));
        assertThrows(InvalidChoiceException.class, ()-> board.chooseTiles(0,3,0,4,bookshelf));
    }

    @Test
    public void chooseThreeTiles() throws InvalidChoiceException {
        board = new Board();
        DeckCards deck= new DeckCards(4);
        board.initializeBoard(4);
        Bookshelf bookshelf= new Bookshelf(board);

        assertThrows(InvalidChoiceException.class, ()-> board.chooseTiles(0,3,4,0, 0, 4, bookshelf));
        assertThrows(InvalidChoiceException.class, ()->board.chooseTiles(0,0,0,1,0,2,bookshelf));

        board.setTile(0,3, Tile.EMPTY);
        board.setTile(0,4,Tile.EMPTY);
        board.updateBoard();
        board.chooseTiles(1,3,1,4,1,5,bookshelf);

        Tile[][] shelf=new Tile[][] { {Tile.BLUE, Tile.BLUE,Tile.BLUE,Tile.BLUE,Tile.BLUE},
                {Tile.BLUE, Tile.BLUE,Tile.BLUE,Tile.BLUE,Tile.BLUE},
                {Tile.BLUE, Tile.BLUE,Tile.BLUE,Tile.BLUE,Tile.BLUE},
                {Tile.BLUE, Tile.BLUE,Tile.BLUE,Tile.BLUE,Tile.BLUE},
                {Tile.BLUE, Tile.BLUE,Tile.BLUE,Tile.BLUE,Tile.BLUE},
                {Tile.BLUE, Tile.BLUE,Tile.BLUE,Tile.BLUE,Tile.BLUE},
        };

        bookshelf.setShelf(shelf);


        assertThrows(InvalidChoiceException.class,()-> board.chooseTiles(1,3,1,4,1,5,bookshelf));
    }


        @Test
    public void testSubTiles() throws InvalidChoiceException{
        board = new Board();
        DeckCards deck= new DeckCards(4);
        board.initializeBoard(4);

        for(int i=0; i<9; i++){
            for (int j=0; j<9; j++){
                if(board.getBoard()[i][j]!=Tile.NOTAVAILABLE){
                    board.setTile(i, j, Tile.EMPTY);
                }
            }
        }

        Bookshelf bookshelf= new Bookshelf(board);
            List<Integer> coord=new ArrayList<>();

        board.setTile(6,5,Tile.LIGHTBLUE);
        board.setTile(6,4,Tile.LIGHTBLUE);
        board.setTile(6,3,Tile.LIGHTBLUE);

        board.updateBoard();
        List<Tile> temp;
        temp=board.chooseTiles(6,5,6,4,6,3, bookshelf);

        coord.add(6);
        coord.add(5);
        coord.add(6);
        coord.add(4);
        coord.add(6);
        coord.add(3);
        board.subTiles(coord);
        int i = 6;
        for(int j=3; j<=5; j++){
            assertEquals(board.getBoard()[i][j], Tile.EMPTY);
        }
        assertEquals(3, temp.size());

        coord.clear();
            board.setTile(6,5,Tile.LIGHTBLUE);
            board.setTile(6,4,Tile.LIGHTBLUE);


            board.updateBoard();

            temp=board.chooseTiles(6,5,6,4, bookshelf);

            coord.add(6);
            coord.add(5);
            coord.add(6);
            coord.add(4);
            board.subTiles(coord);
            i = 4;
            for(int j=3; j<=5; j++){
                assertEquals(board.getBoard()[i][j], Tile.EMPTY);
            }
            assertEquals(2, temp.size());

            coord.clear();
            board.setTile(6,5,Tile.LIGHTBLUE);

            board.updateBoard();



            coord.add(6);
            coord.add(5);
            board.subTiles(coord);


            coord.add(1);

            assertThrows(IllegalArgumentException.class, ()->board.subTiles(coord));
            board=null;
    }

    @Test
    public void testInLine1() {

        board = new Board();
        assertTrue(board.inLine(6,5,6,4));
    }

    @Test
    public void testInLine2() {

        board = new Board();
        assertFalse(board.inLine(6,5,5,4));
    }

    @Test
    public void testInLine3() {


        board = new Board();
        assertTrue(board.inLine(6,5,6,4, 6,3));
    }

    @Test
    public void testInLine4() {

        board = new Board();
        assertFalse(board.inLine(6,5,6,4, 6,2));
    }
    @Test
    public void testInLine5() {

        board = new Board();
        assertFalse(board.inLine(2,6,3,6, 3,6));
    }

    @Test
    public void set(){
        board = new Board();

        board.setEOG();
        board.setCGC1(1, 2);
        board.setCGC2(2,2);

    }
    @Test
    public void get(){
        board = new Board();

        board.getEOG();
        board.getBag();
        board.getDeck();
        board.getCommonGoalCard2();
        board.getCommonGoalCard1();
        board.getNPlayers();

    }

}