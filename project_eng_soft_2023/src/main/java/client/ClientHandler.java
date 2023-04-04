package client;

import model.Board;

public interface ClientHandler {

    int enterNUmberOfPlayers();
    boolean updateBoard(Board board);

    void theGameEnd();

    void statYourTurn();

    void endYourTurn();

}
