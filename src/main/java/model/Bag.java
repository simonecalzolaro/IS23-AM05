package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.shuffle;

public class Bag implements Serializable {

    private List<Tile> tilesBag;

    /**
     * Constructor
     */
    public Bag() {
        tilesBag=new ArrayList<Tile>();

    }

    /**
     * set the correct tiles number and tiles type and shuffle the order of the tiles
     */
    public void initializeBag(){
        for(int i=0; i<132;i++){
            if(i<22){
                tilesBag.add(Tile.BLUE);
            } else if (i<44) {
                tilesBag.add(Tile.WHITE);
            } else if (i<66) {
                tilesBag.add(Tile.GREEN);
            } else if (i<88) {
                tilesBag.add(Tile.PINK);
            } else if (i<110) {
                tilesBag.add(Tile.LIGHTBLUE);
            } else {
                tilesBag.add(Tile.YELLOW);
            }
        }

        shuffle(tilesBag);
    }

    /**
     *
     * @return return random tile and remove it from the bag
     */
    public Tile getTile(){
        Tile temp;
        if(getTilesNum()!=0){
            temp=tilesBag.get(0);
            tilesBag.remove(0);
            return temp;
        }
        return Tile.EMPTY;
    }


    public List<Tile> getTilesBag() {
        return tilesBag;
    }

    public int getTilesNum(){
        return tilesBag.size();
    }



}