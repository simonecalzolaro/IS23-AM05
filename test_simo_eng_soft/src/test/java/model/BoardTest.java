package model;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class BoardTest  {

    private Board board;
    private Bag bag;

    @Test
    public void testInitializeBoard() {

        board = new Board();
        DeckCards deck= new DeckCards(4);
        board.initializeBoard(4);


        assertEquals(4, board.getnPlayers());
        bag=null;
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
    public void testSubTiles() throws NotAvailableTiles, NotEnoughSpace, NotInLine {
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

        PersonalGoalCard pgc= new PersonalGoalCard(2) ;

        Bookshelf bookshelf= new Bookshelf(board);
        board.setTile(6,5,Tile.LIGHTBLUE);
        board.setTile(6,4,Tile.LIGHTBLUE);
        board.setTile(6,3,Tile.LIGHTBLUE);

        board.updateBoard();
        List<Tile> temp=new ArrayList<>();
        temp=board.subTiles(6,5,6,4,6,3, bookshelf);

        int i = 6;
        for(int j=3; j<=5; j++){
            assertEquals(board.getBoard()[i][j], Tile.EMPTY);
        }
        assertEquals(3, temp.size());
        board=null;
    }

    @Test
    public void testInLine() {
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
}