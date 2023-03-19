package model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static java.util.Collections.shuffle;

public class Bag {
    private List<Tile> tilesBag;

    public Bag() {
        tilesBag=new ArrayList<Tile>();

    }

    public void inizializeBag(){
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
    public Tile getTile(){
        Tile temp;
        temp=tilesBag.get(0);
        tilesBag.remove(0);
        return temp;
    }

    public List<Tile> getTilesBag() {
        return tilesBag;
    }

    public int getTilesNum(){
        return tilesBag.size();
    }


}