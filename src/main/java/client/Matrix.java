package client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * generic class representing a matrix of Tiles
 */
public class Matrix {
    private Tile[][] matr;
    private Map<Tile, Integer[]> map;

    /**
     * constructor 1
     * @param pgcMap: map
     */
    public Matrix(Map<model.Tile, Integer[]> pgcMap) {

        map=new HashMap<>();
        ArrayList<Tile> keys= new ArrayList(pgcMap.keySet());

        for (Map.Entry<model.Tile, Integer[]> entry : pgcMap.entrySet()){

            map.put(Tile.getTile(entry.getKey().ordinal()) , new Integer[] {entry.getValue()[0], entry.getValue()[1]});
            //System.out.println("key:"+Tile.getTile(entry.getKey().ordinal())+ " value: "+ entry.getValue()[0] + ","+entry.getValue()[1]);
        }

      matr=generateMatrixFromMap(map);


    }

    /**
     * constructor 2
     * @param matr: Tile[][]
     */
    public Matrix(model.Tile[][] matr) {

        this.matr=new Tile[matr.length][matr[0].length];

        for(int i=0; i< matr.length; i++){
            for(int j=0; j< matr[0].length; j++){
                this.matr[i][j]=Tile.getTile(matr[i][j].ordinal());
            }
        }

       map=generateMapFromMatrix(this.matr);

    }

    /**
     * @param row: row
     * @param col: column
     * @return the tile inside this tile with coordinates [row][col]
     */
    public Tile getTileByCoord(int row, int col){
        return matr[row][col];
    }

    /**
     * @param tile: Tile
     * @return the coord of a given Tile
     */
    public Integer[] getTileFromMap(Tile tile){
        return map.get(tile);
    }

    /**
     * @param matr: matrix from witch generate the return
     * @return a Map
     */
    private Map<Tile, Integer[]>  generateMapFromMatrix(Tile[][] matr){

        Map<Tile, Integer[]> map=new HashMap<>();

        for (int i=0; i< matr.length; i++){
            for (int j=0; j< matr[0].length; j++){

                map.put(matr[i][j], new Integer[]{j, i});

            }
        }

        return map;
    }

    /**
     * @param map: Map from witch generate the return
     * @return a matrix
     */
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

    /**
     * @return the matrix of Tile
     */
    public Tile[][] getMatr() { return matr; }

    /**
     * @return the correspondent Map of Tiles
     */
    public Map<Tile, Integer[]> getMap() {
        return map;
    }


}
