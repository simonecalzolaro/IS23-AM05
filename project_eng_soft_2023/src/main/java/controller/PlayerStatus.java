package controller;

import java.io.Serializable;

public enum PlayerStatus implements Serializable {
    MY_TURN,
    NOT_MY_TURN,
    NOT_ONLINE,
    WAITING_ROOM

}
