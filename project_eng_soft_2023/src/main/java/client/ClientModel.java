package client;

import java.util.ArrayList;
import java.util.Map;

public class ClientModel {


    private String nickname;
    private Matrix board;
    private Matrix myBookshelf;
    private Map<String, Matrix> otherPlayers;

    private Matrix pgc;
    private int pgcNum;

    private CGC cgc1;

    private CGC cgc2;


    public ClientModel() {
        this.nickname = "";

    }


    public void  initializeMatrixes(Matrix board, Matrix myBookshelf, Map<String, Matrix> otherPlayers){

        this.board=board;
        this.myBookshelf=myBookshelf;
        this.otherPlayers=otherPlayers;

    }

    public void initializeCards(Matrix pgc, int pgcNum, int cgc1Num, int cgc2Num){

        this.pgc=pgc;
        this.pgcNum=pgcNum;

        this.cgc1=CGC.getCGC(cgc1Num);
        this.cgc2=CGC.getCGC(cgc1Num);


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


    //--------------------getter e setter CGC---------------

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



}
