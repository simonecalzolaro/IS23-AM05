package model;

import java.util.EmptyStackException;
import java.util.Stack;
import java.util.stream.Stream;

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

    /**
     * @return the top Token on the stack, if EmptyStackException occurs it returns always the smallest token
     */
    public Token getTopStack() {
        try{
            return stackTiles.pop();
        }catch (EmptyStackException e){
            return Token.ST2;
        }

    }


    /**
     * check if the shelf is correctly filled, it means that is impossible to have "floating" Tiles
     * @param shelf
     */
    public void goodShelf(Tile[][] shelf){

        //for each column I check that "EMPTY" tiles are ONLY at the end;

        boolean emptyTilesFound;

        for(int col=0; col< shelf[0].length; col++){//columns

            emptyTilesFound=false;

            for(int row=0; row< shelf.length; row++){//columns

                if (shelf[row][col].equals(Tile.EMPTY)) emptyTilesFound=true ;
                else{
                    if ( emptyTilesFound ) throw new IllegalArgumentException(" The shelf is not filled properly ");;
                }
            }
        }
    }


}
