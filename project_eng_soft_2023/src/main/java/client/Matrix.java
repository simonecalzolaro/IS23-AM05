package client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Matrix {

    Tile[][] matr;
    Map<Tile, int[]> map;




    public Matrix(Map<model.Tile,int[]> pgcMap) {

        map=new HashMap<>();
        ArrayList<Tile> keys= new ArrayList(pgcMap.keySet());

        for(int i=0; i< keys.size(); i++){
            map.put( keys.get(i), pgcMap.get( keys.get(i)) ); //keys.get(i).ordinal()
        }



    }

    public Matrix(model.Tile[][] matr) {

        this.matr=new Tile[matr.length][matr[0].length];

        for(int i=0; i< matr.length; i++){
            for(int j=0; j< matr[0].length; j++){
                this.matr[i][j]=Tile.getTile(matr[i][j].ordinal());
            }
        }

    }
}
