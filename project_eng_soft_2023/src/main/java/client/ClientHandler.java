package client;

import model.Board;

public interface ClientHandler {

    int enterNUmberOfPlayers();
    boolean updateBoard();

    void theGameEnd();

    void statYourTurn();

    void endYourTurn();

}
