package client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import model.Tile;

public class Matrix {


    private Tile[][] matr;
    private Map<Tile, Integer[]> map;


    public Matrix(Map<model.Tile, Integer[]> pgcMap) {

        map=new HashMap<>();
        ArrayList<Tile> keys= new ArrayList(pgcMap.keySet());

        for (Map.Entry<model.Tile, Integer[]> entry : pgcMap.entrySet()){

            map.put(Tile.getTile(entry.getKey().ordinal()), entry.getValue());

            /*
            System.out.println("Key = " + entry.getKey() +
                    ", Value = " + entry.getValue());

             */
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

    public Tile getTileByCoord(int row, int col){
        return matr[row][col];
    }

    private Map<Tile, Integer[]>  generateMapFromMatrix(Tile[][] matr){

        Map<Tile, Integer[]> map=new HashMap<>();

        for (int i=0; i< matr.length; i++){
            for (int j=0; j< matr[0].length; j++){

                map.put(matr[i][j], new Integer[]{j, i});

            }
        }

        return map;
    }

    private Tile[][] generateMatrixFromMap( Map<Tile, Integer[]>  map){

        Tile[][] matr= new Tile[][]{};

        for (int i=0; i< matr.length; i++){
            for (int j=0; j< matr[0].length; j++){
                matr[i][j]=Tile.EMPTY;
            }
        }


        for (Map.Entry<Tile, Integer[]> entry : map.entrySet()){
            matr[entry.getValue()[0]][entry.getValue()[1]] = entry.getKey() ;
        }

        return matr;
    }

    public Tile[][] getMatr() {
        return matr;
    }

    public Map<Tile, Integer[]> getMap() {
        return map;
    }


}
