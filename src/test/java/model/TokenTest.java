package model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class TokenTest {
    @Test
    public void checkTokenEndOfGame(){
         Token token = Token.EndOfGame;

         assertSame(Token.EndOfGame, token);
         assertEquals(1, token.getScoreToken());
    }

    @Test
    public void checkTokenST2(){
        Token token = Token.ST2;

        assertSame(Token.ST2, token);
        assertEquals(2, token.getScoreToken());
    }

    @Test
    public void checkTokenST4(){
        Token token = Token.ST4;

        assertSame(Token.ST4, token);
        assertEquals(4 ,token.getScoreToken());
    }

    @Test
    public void checkTokenST6(){
        Token token = Token.ST6;

        assertSame(Token.ST6, token);
        assertEquals(6, token.getScoreToken());
    }

    @Test
    public void checkTokenST8(){
        Token token = Token.ST8;

        assertSame(Token.ST8, token);
        assertEquals(8, token.getScoreToken());
    }
}