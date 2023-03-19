package model;

public class Tester {
    public static void main(String[] args){

        Tile[][] shelf = new Tile[6][5];

        for(int i=0; i< shelf.length; i++){
            for (int j=0; j<shelf[i].length; j++){
                shelf[i][j] = Tile.EMPTY;
            }
        }

        shelf[4][4] = Tile.GREEN;
        shelf[5][2] = Tile.BLUE;
        shelf[3][3] = Tile.WHITE;
        shelf[5][0] = Tile.PINK;
        shelf[2][1] = Tile.YELLOW;


        PersonalGoalCard p = new PG1();
        p.updateScore(shelf);
        System.out.println(p.getScore());
        System.out.println(p.checkGoal(shelf));



        shelf[0][2] = Tile.LIGHTBLUE;
        p.updateScore(shelf);
        System.out.println(p.getScore());
        System.out.println(p.checkGoal(shelf));

    }
}
