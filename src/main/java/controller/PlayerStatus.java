package controller;

import java.io.Serializable;

/**
 * enum class representing all the possible state of a player on the server-side.
 * MY_TURN: is the player turn
 * NOT_MY_TURN:  is not the player turn
 * NOT_ONLINE: the player is offline, he is excluded from the game until he doesn't come back online (NOT_MY_TURN)
 * WAITING_ROOM: the player is currently inside the waiting room
 * nOfPlayerAsked: this player was asked to enter the number of players
 */
public enum PlayerStatus implements Serializable {
    MY_TURN,
    NOT_MY_TURN,
    NOT_ONLINE,
    WAITING_ROOM,
    nOfPlayerAsked;

}
