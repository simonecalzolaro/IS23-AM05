package model;

import java.util.Stack;

/**
 * generic representation of a CommonGoalCard
 */
public abstract class CommonGoalCard implements Card{

    Stack<Token> stackToken = new Stack<>();

    /**
     * @param np number of players
     */
    public CommonGoalCard(int np){

        stackToken.push(Token.ST2);
        stackToken.push(Token.ST4);
        if(np>2){
            stackToken.push(Token.ST6);
            if(np>3) stackToken.push(Token.ST8);
        }
    }


    public Token getTopStack(){
        return stackToken.pop();
    }


}