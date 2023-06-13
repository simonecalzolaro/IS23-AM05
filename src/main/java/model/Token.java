package model;

import java.io.Serializable;

/**
 * generic representation of a Token
 * EnfOfGame is the token you get when you are the first to complete your Bookshelf
 * ST2, ST4, ST8 are the tokens you can get when you complete a CommonGoalCard ( in order )
 */
public enum Token implements Serializable {

    EndOfGame(1),
    ST2(2),
    ST4(4),
    ST6(6),
    ST8(8);

    final int scoreToken;

    /**
     *
     * @param scoreToken represents the value of every Token
     */
    Token(int scoreToken){
        this.scoreToken = scoreToken;
    }

    /**
     *
     * @return the value linked to that specific Token
     */
    public int getScoreToken(){
        return scoreToken;
    }
}