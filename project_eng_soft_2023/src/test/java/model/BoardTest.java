package model;

import junit.framework.TestCase;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;

import java.util.ArrayList;
import java.util.List;

public class BoardTest extends TestCase {

    private Board board;
    private Bag bag;

    @Before
    public void setUp(){

        bag = new Bag();
        bag.initializeBag();
        board = new Board();
    }

    @After
    public void tearDown(){
        bag=null;
        board=null;
    }
    public void testInitializeBoard() {
        DeckCards deck= new DeckCards(4);
        board.initializeBoard(4);


        Assert.assertEquals(4, board.getnPlayers());

    }

    public void testFill() {
        DeckCards deck= new DeckCards(4);
        board.initializeBoard(4);
        for(int i=0; i<9; i++){
            for (int j=0; j<9; j++){
                Assert.assertNotEquals(null, board.getBoard()[i][j]);
                Assert.assertNotEquals(Tile.EMPTY, board.getBoard()[i][j]);
            }
        }


    }


    public void testFill2() {
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
            bag.getTile();
        }

        int tile= bag.getTilesNum();
        board.fill();
        Assert.assertEquals(oldEmpty-tile, board.getEmptyTilesNum());
    }


    public void testEmptyBoard1() {
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

        Assert.assertTrue(board.emptyBoard());

    }

    public void testEmptyBoard2() {
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


        Assert.assertFalse(board.emptyBoard());

    }

    public void testSingleTile() {
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

        Assert.assertTrue(board.singleTile(0,3));
        Assert.assertTrue(board.singleTile(1,5));
        Assert.assertTrue(board.singleTile(3,2));
        Assert.assertFalse(board.singleTile(7,5));

    }

    public void testOkTiles() {
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

        Assert.assertTrue(board.okTile(0,3));
        Assert.assertTrue(board.okTile(1,5));
        Assert.assertTrue(board.okTile(3,2));

        Assert.assertTrue(board.okTile(7,5));
        Assert.assertTrue(board.okTile(7,4));
        Assert.assertTrue(board.okTile(6,5));
        Assert.assertTrue(board.okTile(8,5));

        Assert.assertFalse(board.okTile(6,4));
    }

    public void testSubTiles() throws NotAvailableTiles, NotEnoughSpace, NotInLine {

        DeckCards deck= new DeckCards(4);
        board.initializeBoard(4);
        for(int i=0; i<9; i++){
            for (int j=0; j<9; j++){
                if(board.getBoard()[i][j]!=Tile.NOTAVAILABLE){
                    board.setTile(i, j, Tile.EMPTY);
                }
            }
        }

        PersonalGoalCard pgc= new PG2() ;

        Bookshelf bookshelf= new Bookshelf(board, pgc);
        board.setTile(6,5,Tile.LIGHTBLUE);
        board.setTile(6,4,Tile.LIGHTBLUE);
        board.setTile(6,3,Tile.LIGHTBLUE);

        board.updateBoard();
        List<Tile> temp=new ArrayList<>();
        temp=board.subTiles(6,5,6,4,6,3, bookshelf);

        int i = 6;
        for(int j=3; j<=5; j++){
            Assert.assertEquals(board.getBoard()[i][j], Tile.EMPTY);
        }
        Assert.assertEquals(3, temp.size());
    }


    public void testInLine() {
        DeckCards deck= new DeckCards(4);
        board.initializeBoard(4);
        for(int i=0; i<9; i++){
            for (int j=0; j<9; j++){
                if(board.getBoard()[i][j]!=Tile.NOTAVAILABLE){
                    board.setTile(i, j, Tile.EMPTY);
                }
            }
        }


    }


    public void testInLine1() {

        Assert.assertTrue(board.inLine(6,5,6,4));
    }

    public void testInLine2() {
        Assert.assertFalse(board.inLine(6,5,5,4));
    }


    public void testInLine3() {
        Assert.assertTrue(board.inLine(6,5,6,4, 6,3));
    }
    public void testInLine4() {
        Assert.assertFalse(board.inLine(6,5,6,4, 6,2));
    }
}