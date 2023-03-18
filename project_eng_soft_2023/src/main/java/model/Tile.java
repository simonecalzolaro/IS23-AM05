package model;

public enum Tile{
    GREEN,
    BLUE,
    WHITE,
    YELLOW,
    PURPLE,
    LIGHT_BLUE;

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
