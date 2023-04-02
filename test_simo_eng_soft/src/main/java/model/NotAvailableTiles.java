package model;

public class NotAvailableTiles extends Exception {

    public NotAvailableTiles() {
        System.out.println("These tiles are not catchable");
    }
}