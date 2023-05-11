package client;

public enum Tile {

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
            case 1 : return Tile.GREEN;
            case 2 : return Tile.WHITE;
            case 3 : return Tile.BLUE;
            case 4 : return Tile.LIGHTBLUE;
            case 5 : return Tile.PINK;
            case 6 : return Tile.YELLOW;
            case 7 : return Tile.EMPTY;
            case 8 : return Tile.NOTAVAILABLE;

            default: return Tile.EMPTY;
        }


    }
}
