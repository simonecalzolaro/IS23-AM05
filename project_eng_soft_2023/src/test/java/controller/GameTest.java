package controller;

import model.Board;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {


    @Test
    void startGame() throws IOException {

        ArrayList<ControlPlayer> players = new ArrayList<>();
        Board board = new Board();
        board.initializeBoard(4);
        players.add(new RMIControlPlayer("Ciro",board));
        players.add(new RMIControlPlayer("Milo",board));
        players.add(new RMIControlPlayer("Gino",board));
        players.add(new RMIControlPlayer("Luis",board));

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
    void endTurn() throws IOException {
        Board board = new Board();
        board.initializeBoard(4);
        ArrayList<ControlPlayer> players = new ArrayList<>();
        players.add(new RMIControlPlayer("Ciro",board));
        players.add(new RMIControlPlayer("Milo",board));
        players.add(new RMIControlPlayer("Gino",board));
        players.add(new RMIControlPlayer("Luis",board));
        Game game = new Game(players,board);

        Assertions.assertEquals(0,game.getCurrPlayer());
        game.endTurn();
        Assertions.assertEquals(1,game.getCurrPlayer());
        game.endTurn();
        Assertions.assertEquals(2,game.getCurrPlayer());
        game.endTurn();
        Assertions.assertEquals(3,game.getCurrPlayer());
        game.endTurn();
        Assertions.assertEquals(0,game.getCurrPlayer());
        game.getPlayers().get(1).setPlayerStatus(PlayerStatus.NOT_ONLINE);
        game.endTurn();
        Assertions.assertEquals(2,game.getCurrPlayer());
        game.getPlayers().get(1).setPlayerStatus(PlayerStatus.NOT_ONLINE);
        board.setEOG();
        game.endTurn();
        Assertions.assertEquals(3,game.getCurrPlayer());
        game.endTurn();
        Assertions.assertEquals(PlayerStatus.NOT_MY_TURN,game.getPlayers().get(game.getCurrPlayer()).getPlayerStatus());

    }

    @Test
    void getGameID() {

    }



}