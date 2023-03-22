package model;

import java.util.EmptyStackException;
import java.util.Stack;

/**
 * generic representation of a CommonGoalCard
 */
public abstract class CommonGoalCard implements Card{

    Stack<Token> stackTiles;

    /**
     * @param np number of players
     */
    public CommonGoalCard(int np){

        if(np>=2) {
            stackTiles = new Stack<Token>();
            stackTiles.push(Token.ST2);
            stackTiles.push(Token.ST4);
            if (np > 2) {
                stackTiles.push(Token.ST6);
                if (np > 3) stackTiles.push(Token.ST8);
            }
        }else throw new IllegalArgumentException(" Number of players must be greater than 1 ");
    }


    public Token getTopStack() {
        try{
            return stackTiles.pop();
        }catch (EmptyStackException e){
            return Token.ST2;
        }

    }


}
