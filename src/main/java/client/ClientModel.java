package client;

import view.View;

import java.util.ArrayList;
import java.util.Map;

/**
 * this class contains all the details about the player an the game he is playing
 */
public class ClientModel {

    private boolean connectionType; //false --> RMI; true --> Socket
    private int GameID;
    private String nickname;
    private Matrix board;
    private Matrix myBookshelf;
    private Map<String, Matrix> otherPlayers;

    private int numOtherPlayers;

    private int myScore;

    //------------cards-------------
    private Matrix pgc;
    private int pgcNum;
    private CGC cgc1;
    private CGC cgc2;




    //-----------chat-----------
    private ClientChat myChat;

    /**
     * constructor
     */
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


    /**
     * method called to initialize all the matrixes
     * @param board: new board to instantiate
     * @param myBookshelf: new bookshelf to instantiate
     * @param otherPlayers: new other's player bookshelf to instantiate
     */
    public void  initializeMatrixes(Matrix board, Matrix myBookshelf, Map<String, Matrix> otherPlayers){

        this.board=board;
        this.myBookshelf=myBookshelf;
        this.otherPlayers=otherPlayers;
        this.numOtherPlayers = otherPlayers.size();
        System.out.println("...all shelf and board successfully initialized...");

    }

    /**
     * method called to initialize all the cards
     * @param pgc: new personal goal card matrix to instantiate
     * @param pgcNum: personal goal card ordinal num
     * @param cgc1Num: first common goal card ordinal number
     * @param cgc2Num: second common goal card ordinal number
     */
    public void initializeCards(Matrix pgc, int pgcNum, int cgc1Num, int cgc2Num){

        this.pgc=pgc;
        this.pgcNum=pgcNum;

        this.cgc1=CGC.getCGC(cgc1Num);
        this.cgc2=CGC.getCGC(cgc2Num);

        System.out.println("...all cards successfully initialized...");

    }

    //-------------------- getter nickname ----------------------

    /**
     * @return player's nickname
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * @param nickname to set
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    //-------------------- getter and setter board ----------------------


    /**
     * @return player's board
     */
    public Matrix getBoard() {
        return board;
    }


    //-------------------- getter and setter myBookshelf ----------------------

    /**
     * @return player's bookshelf
     */
    public Matrix getMyBookshelf() {
        return myBookshelf;
    }


    //-------------------- getter and setter otherPlayers ----------------------

    /**
     * @return other players' nickname and their bookshelf
     */
    public Map<String, Matrix> getOtherPlayers() {
        return otherPlayers;
    }

    /**
     * to add a new player
     * @param nick: other player's nickname
     * @param matr: other player's bookshelf
     */
    public void addOtherPlayers(String nick, Matrix matr) {
        otherPlayers.put(nick, matr);
    }


    //--------------------getter and setter CGC---------------
    /**
     * @return my personal goal card
     */
    public Matrix getPgc() {
        return pgc;
    }

    /**
     * @return my personal goal card ordinal number
     */
    public int getPgcNum() {
        return pgcNum;
    }


    //--------------------getter PGC---------------
    /**
     * @return my first common goal card
     */
    public CGC getCgc1() {
        return cgc1;
    }

    /**
     * @return my second common goal card
     */
    public CGC getCgc2() {
        return cgc2;
    }


    //--------------------getter and setter GameID--------------------
    /**
     * @return the ID of the game I'm playing
     */
    public int getGameID() {
        return GameID;
    }

    /**
     * set the ID of the game I'm playing
     * @param gameID: int ID of the game
     */
    public void setGameID(int gameID) {
        GameID = gameID;
    }


    //--------------------getter and setter myScore--------------------
    /**
     * @return my current score
     */
    public int getMyScore() {
        return myScore;
    }

    /**
     * set the current score
     * @param myScore: int
     */
    public void setMyScore(int myScore) {
        this.myScore = myScore;
    }

    //------------------------getter chat---------------------------
    /**
     * @return my chat object
     */
    public ClientChat getMyChat() {
        return myChat;
    }

    public void setConnectionType(boolean connectionType){

        this.connectionType = connectionType;

    }

    public void setNumOtherPlayers(int numOtherPlayers) {
        this.numOtherPlayers = numOtherPlayers;
    }

    public boolean getConnectionType(){
        return connectionType;
    }

    public int getNumOtherPlayers() {
        return numOtherPlayers;
    }


}
