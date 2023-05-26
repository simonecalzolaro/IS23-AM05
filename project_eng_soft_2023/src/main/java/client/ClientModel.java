package client;

import java.util.ArrayList;
import java.util.Map;

public class ClientModel {

    private int GameID;
    private String nickname;
    private Matrix board;
    private Matrix myBookshelf;
    private Map<String, Matrix> otherPlayers;

    private int myScore;

    //------------cards-------------
    private Matrix pgc;
    private int pgcNum;
    private CGC cgc1;
    private CGC cgc2;


    //-----------chat-----------
    private ClientChat myChat;

    public ClientModel() {

        this.nickname = "";
        this.board=null;
        this.myBookshelf=null;
        this.otherPlayers=null;
        this.pgc=null;
        this.pgcNum=-1;
        this.cgc1=null;
        this.cgc2=null;
        this.GameID=-1;
        myScore=0;
        myChat=new ClientChat();
    }


    public void  initializeMatrixes(Matrix board, Matrix myBookshelf, Map<String, Matrix> otherPlayers){

        this.board=board;
        this.myBookshelf=myBookshelf;
        this.otherPlayers=otherPlayers;
        System.out.println("...all shelf and board successfully initialized...");

    }

    public void initializeCards(Matrix pgc, int pgcNum, int cgc1Num, int cgc2Num){

        this.pgc=pgc;
        this.pgcNum=pgcNum;

        this.cgc1=CGC.getCGC(cgc1Num);
        this.cgc2=CGC.getCGC(cgc2Num);

        System.out.println("...all cards successfully initialized...");

    }

    //-------------------- getter nickname ----------------------

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    //-------------------- getter and setter board ----------------------


    public Matrix getBoard() {
        return board;
    }


    //-------------------- getter and setter myBookshelf ----------------------

    public Matrix getMyBookshelf() {
        return myBookshelf;
    }




    //-------------------- getter and setter otherPlayers ----------------------

    public Map<String, Matrix> getOtherPlayers() {
        return otherPlayers;
    }

    public void addOtherPlayers(String nick, Matrix matr) {
        otherPlayers.put(nick, matr);
    }


    //--------------------getter and setter CGC---------------

    public Matrix getPgc() {
        return pgc;
    }

    public int getPgcNum() {
        return pgcNum;
    }


    //--------------------getter PGC---------------
    public CGC getCgc1() {
        return cgc1;
    }

    public CGC getCgc2() {
        return cgc2;
    }


    //--------------------getter and setter GameID--------------------
    public int getGameID() {
        return GameID;
    }

    public void setGameID(int gameID) {
        GameID = gameID;
    }


    //--------------------getter and setter myScore--------------------
    public int getMyScore() {
        return myScore;
    }

    public void setMyScore(int myScore) {
        this.myScore = myScore;
    }

    //------------------------getter chat---------------------------
    public ClientChat getMyChat() {
        return myChat;
    }

}
