package model;

public class NotEnoughSpace extends Exception{
    public NotEnoughSpace() {
        System.out.println("Not Enough Space in player's bookshelf");
    }
}
