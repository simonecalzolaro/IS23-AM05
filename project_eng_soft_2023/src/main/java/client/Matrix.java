package client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Matrix {
    private Tile[][] matr;
    private Map<Tile, Integer[]> map;

    public Matrix(Map<model.Tile, Integer[]> pgcMap) {

        map=new HashMap<>();
        ArrayList<Tile> keys= new ArrayList(pgcMap.keySet());

        for (Map.Entry<model.Tile, Integer[]> entry : pgcMap.entrySet()){

            map.put(Tile.getTile(entry.getKey().ordinal()) , new Integer[] {entry.getValue()[0], entry.getValue()[1]});
            //System.out.println("key:"+Tile.getTile(entry.getKey().ordinal())+ " value: "+ entry.getValue()[0] + ","+entry.getValue()[1]);
        }

      matr=generateMatrixFromMap(map);


    }

    public Matrix(model.Tile[][] matr) {

        this.matr=new Tile[matr.length][matr[0].length];

        for(int i=0; i< matr.length; i++){
            for(int j=0; j< matr[0].length; j++){
                this.matr[i][j]=Tile.getTile(matr[i][j].ordinal());
            }
        }

       map=generateMapFromMatrix(this.matr);

    }

    public Tile getTileByCoord(int row, int col){
        return matr[row][col];
    }

    public Integer[] getTileFromMap(Tile tile){
        return map.get(tile);
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

        Tile[][] matr= new Tile[6][5];
        Integer[] vett=new Integer[2];

        for (int i=0; i < matr.length; i++){
            for (int j=0; j< matr[0].length; j++){
                matr[i][j]=Tile.EMPTY;
            }
        }

        for (Map.Entry<Tile, Integer[]> entry : map.entrySet()){
            vett[0]=entry.getValue()[0];
            vett[1]=entry.getValue()[1];
            matr[vett[0]][vett[1]] = entry.getKey() ;
        }

        return matr;
    }

    public Tile[][] getMatr() { return matr; }

    public Map<Tile, Integer[]> getMap() {
        return map;
    }


}
