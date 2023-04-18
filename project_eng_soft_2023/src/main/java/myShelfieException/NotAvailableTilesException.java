package myShelfieException;

public class NotAvailableTilesException extends Exception {

    public NotAvailableTilesException() {
        System.out.println("These tiles are not catchable");
    }
}