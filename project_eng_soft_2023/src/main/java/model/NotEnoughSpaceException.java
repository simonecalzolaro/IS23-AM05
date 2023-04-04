package model;

public class NotEnoughSpaceException extends Exception{
    public NotEnoughSpaceException() {
        System.out.println("Not Enough Space in player's bookshelf");
    }
}