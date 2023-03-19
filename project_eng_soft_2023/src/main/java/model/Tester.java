package model;

public class Tester {
    public static void main(String[] args){

        Tile[][] shelf = new Tile[6][5];

        for (int i=0; i< shelf.length; i++){
            for(int j=0; j< shelf[i].length; j++){
                shelf[i][j] = Tile.EMPTY;
            }
        }

        shelf[4][4] = Tile.GREEN;
        shelf[5][2] = Tile.BLUE;
        shelf[3][3] = Tile.WHITE;
        shelf[5][0] = Tile.PINK;
        shelf[2][1] = Tile.YELLOW;
        shelf[0][2] = Tile.LIGHTBLUE;

        DeckCards deck = new DeckCards(4);

        PersonalGoalCard card = deck.getRandPGC();

        card.updateScore(shelf);
        System.out.println(card.getScore());
        System.out.println(card.checkGoal(shelf));

    }
}
