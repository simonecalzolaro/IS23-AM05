package model;

import java.util.Stack;

/**
 * generic representation of a CommonGoalCard
 */
public abstract class CommonGoalCard implements Card{

    Stack<Tile> stackTiles;

    /**
     * @param np number of players
     */
    public CommonGoalCard(int np){

        stackTiles.push(Tile.ST2);
        stackTiles.push(Tile.ST4);
        if(np>2){
            stackTiles.push(Tile.ST6);
            if(np>3) stackTiles.push(Tile.ST8);
        }
    }


    public Tile getTopStack(){
        return stackTiles.pop();
    }


}
