package model;

public class Bookshelf_tester {
    public static void main (String[] args){
        PersonalGoalCard pgc = new PG1();
        Board board = new Board();

        Bookshelf b = new Bookshelf();


        Tile[] arr = {Tile.YELLOW, Tile.YELLOW,Tile.BLUE,Tile.GREEN,Tile.LIGHT_BLUE,Tile.WHITE};
        Tile[] arr1 = {Tile.YELLOW,Tile.YELLOW,Tile.PURPLE,Tile.GREEN,Tile.WHITE,Tile.WHITE};
        Tile[] arr2 = {Tile.YELLOW,Tile.WHITE,Tile.WHITE,Tile.WHITE,Tile.PURPLE,Tile.WHITE};
        Tile[] arr3 = {Tile.YELLOW,Tile.WHITE,Tile.WHITE,Tile.PURPLE,Tile.PURPLE,Tile.WHITE};
        Tile[] arr4 = {Tile.BLUE,Tile.BLUE,Tile.BLUE,Tile.YELLOW,Tile.PURPLE,Tile.WHITE};

        b.putTiles(arr,0);
        b.putTiles(arr1,1);
        b.putTiles(arr2,2);
        b.putTiles(arr3,3);
        b.putTiles(arr4,4);

        System.out.println(b.getMyScore());




















    }
}
