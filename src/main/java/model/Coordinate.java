package model;

import java.io.Serializable;

public class Coordinate implements Serializable {
    private int x;
    private int y;

    public Coordinate(int x, int y){
        this.x=x;
        this.y=y;
    }

    public int getY() {
        return y;
    }

    public int getX() {
        return x;
    }
}