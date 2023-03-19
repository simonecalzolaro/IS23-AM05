package model;

/**
 * Token is an enum where we have the different scores and the token EndOfGame that you pick
 * when you complete your bookshelf ( end of game and last round to play)
 */
public enum Token {
    EndOfGame(1),
    ST2(2),
    ST4(4),
    ST6(6),
    ST8(8);

    final int scoreToken;

    private Token(int scoreToken){
        this.scoreToken = scoreToken;
    }

    public int getScoreToken(){
        return scoreToken;
    }
}
