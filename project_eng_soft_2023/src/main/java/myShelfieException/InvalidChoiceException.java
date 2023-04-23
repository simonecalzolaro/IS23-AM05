package myShelfieException;

public class InvalidChoiceException extends Exception{
    public InvalidChoiceException(String s) {
        System.out.println(s);
    }
}
