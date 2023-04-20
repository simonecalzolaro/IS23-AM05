package controller;

import model.Board;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {


    @Test
    void startGame() throws IOException {

        ArrayList<ControlPlayer> players = new ArrayList<>();
        Board board = new Board();
        board.initializeBoard(4);
        players.add(new ControlPlayer("Ciro",board));
        players.add(new ControlPlayer("Milo",board));
        players.add(new ControlPlayer("Gino",board));
        players.add(new ControlPlayer("Luis",board));

        Game game1 = new Game(players,board);

        assertEquals(1,game1.getGameID());

        assertTrue(game1.getGameStatus() == GameStatus.PLAYING);

        for(int i = 0; i< players.size();i++){
            if(i==0) assertTrue(players.get(i).getPlayerStatus() == PlayerStatus.MY_TURN);
            else{
                assertTrue(players.get(i).getPlayerStatus() == PlayerStatus.NOT_MY_TURN);
            }
        }

    }

    @Test
    void endTurn() {

    }

    @Test
    void getGameID() {

    }



}