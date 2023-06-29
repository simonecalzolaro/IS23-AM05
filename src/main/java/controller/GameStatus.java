package controller;

/**
 * enum class representing the current state of the game.
 * PLAYING: the game has started and the players are active
 * SUSPENDED: the game is suspended when there are less than 2 active players and the lobby is waiting for someone to reconnect
 * END_GAME: the game has ended
 */
public enum GameStatus {

    PLAYING,
    SUSPENDED,
    END_GAME;

}
