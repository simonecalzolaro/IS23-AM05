package myShelfieException;

public class FatalException extends Exception{

    private String nickname;
    private int ID;
    public FatalException(String nickname, int ID){
        super();
        this.nickname = nickname;
        this.ID = ID;

    }

    public int getID() {
        return ID;
    }

    public String getNickname(){
        return nickname;
    }
}
