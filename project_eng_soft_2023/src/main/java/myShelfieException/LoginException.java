package myShelfieException;

public class LoginException extends Exception{

    String description;

    public LoginException(String s) {

        description=s;
        System.out.println(s);

    }

    @Override
    public String toString() {
        return super.toString();
    }

    public String getMessage() {
        return description;
    }
}
