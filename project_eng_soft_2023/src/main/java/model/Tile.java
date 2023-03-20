
package model;

public enum Tile {
    GREEN,
    WHITE,
    BLUE,
    LIGHTBLUE,
    PINK,
    YELLOW,
    EMPTY,
    NOTAVAILABLE;

    private int x;
    private int y;


    public void setX(int x){
        this.x = x;
    }

    public void setY(int y){
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}