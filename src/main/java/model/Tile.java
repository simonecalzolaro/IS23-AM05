
package model;

import java.io.Serializable;

public enum Tile implements Serializable {

    GREEN,
    WHITE,
    BLUE,
    LIGHTBLUE,
    PINK,
    YELLOW,
    EMPTY,
    NOTAVAILABLE;

    public static Tile getTile(int tileNumb){

        switch (tileNumb % 12) {
            case 0 : return Tile.GREEN;
            case 1 : return Tile.WHITE;
            case 2 : return Tile.BLUE;
            case 3 : return Tile.LIGHTBLUE;
            case 4 : return Tile.PINK;
            case 5 : return Tile.YELLOW;
            case 6 : return Tile.EMPTY;
            case 7 : return Tile.NOTAVAILABLE;

            default: return Tile.EMPTY;
        }


    }

}