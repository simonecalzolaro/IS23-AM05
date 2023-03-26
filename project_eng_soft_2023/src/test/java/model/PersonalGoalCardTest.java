package model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

class PersonalGoalCardTest {

    @Test
    public void checkPGC1(){
        PersonalGoalCard personalGoalCard = new PersonalGoalCard(1);

        Tile[][] matr = new Tile[6][5];

        for (Tile[] tiles : matr) {
            Arrays.fill(tiles, Tile.EMPTY);
        }

        matr[4][4] = Tile.GREEN;
        matr[5][2] = Tile.BLUE;
        matr[3][3] = Tile.WHITE;
        matr[5][0] = Tile.PINK;
        matr[2][1] = Tile.YELLOW;
        matr[0][2] = Tile.LIGHTBLUE;

        assertEquals(12, personalGoalCard.getScore(matr));
        assertTrue(personalGoalCard.checkGoal(matr));

    }

    @Test
    public void checkPGC1_2(){
        PersonalGoalCard personalGoalCard = new PersonalGoalCard(1);

        Tile[][] matr = new Tile[6][5];

        for (Tile[] tiles : matr) {
            Arrays.fill(tiles, Tile.EMPTY);
        }

        matr[4][4] = Tile.BLUE;
        matr[5][2] = Tile.BLUE;
        matr[3][3] = Tile.WHITE;
        matr[5][0] = Tile.PINK;
        matr[2][1] = Tile.YELLOW;
        matr[0][2] = Tile.LIGHTBLUE;

        assertEquals(9, personalGoalCard.getScore(matr));
        assertFalse(personalGoalCard.checkGoal(matr));
    }

    @Test
    public void checkPGC1_3(){
        PersonalGoalCard personalGoalCard = new PersonalGoalCard(1);

        Tile[][] matr = new Tile[6][5];

        for (Tile[] tiles : matr) {
            Arrays.fill(tiles, Tile.EMPTY);
        }

        matr[4][4] = Tile.BLUE;
        matr[5][2] = Tile.BLUE;
        matr[3][3] = Tile.BLUE;
        matr[5][0] = Tile.PINK;
        matr[2][1] = Tile.YELLOW;
        matr[0][2] = Tile.LIGHTBLUE;

        assertEquals(6, personalGoalCard.getScore(matr));
        assertFalse(personalGoalCard.checkGoal(matr));
    }

    @Test
    public void checkPGC1_4(){
        PersonalGoalCard personalGoalCard = new PersonalGoalCard(1);

        Tile[][] matr = new Tile[6][5];

        for (Tile[] tiles : matr) {
            Arrays.fill(tiles, Tile.EMPTY);
        }

        matr[4][4] = Tile.BLUE;
        matr[5][2] = Tile.BLUE;
        matr[3][3] = Tile.BLUE;
        matr[5][0] = Tile.BLUE;
        matr[2][1] = Tile.YELLOW;
        matr[0][2] = Tile.LIGHTBLUE;

        assertEquals(3, personalGoalCard.getScore(matr));
        assertFalse(personalGoalCard.checkGoal(matr));
    }

    @Test
    public void checkPGC1_5(){
        PersonalGoalCard personalGoalCard = new PersonalGoalCard(1);

        Tile[][] matr = new Tile[6][5];

        for (Tile[] tiles : matr) {
            Arrays.fill(tiles, Tile.EMPTY);
        }

        matr[4][4] = Tile.BLUE;
        matr[5][2] = Tile.BLUE;
        matr[3][3] = Tile.BLUE;
        matr[5][0] = Tile.BLUE;
        matr[2][1] = Tile.BLUE;
        matr[0][2] = Tile.LIGHTBLUE;

        assertEquals(2, personalGoalCard.getScore(matr));
        assertFalse(personalGoalCard.checkGoal(matr));
    }

    @Test
    public void checkPGC1_6(){
        PersonalGoalCard personalGoalCard = new PersonalGoalCard(1);

        Tile[][] matr = new Tile[6][5];

        for (Tile[] tiles : matr) {
            Arrays.fill(tiles, Tile.EMPTY);
        }

        matr[4][4] = Tile.BLUE;
        matr[5][2] = Tile.BLUE;
        matr[3][3] = Tile.BLUE;
        matr[5][0] = Tile.BLUE;
        matr[2][1] = Tile.BLUE;
        matr[0][2] = Tile.BLUE;

        assertEquals(1, personalGoalCard.getScore(matr));
        assertFalse(personalGoalCard.checkGoal(matr));
    }

    @Test
    public void checkPGC1_empty(){
        PersonalGoalCard personalGoalCard = new PersonalGoalCard(1);

        Tile[][] matr = new Tile[6][5];

        for (Tile[] tiles : matr) {
            Arrays.fill(tiles, Tile.EMPTY);
        }

        assertEquals(0, personalGoalCard.getScore(matr));
        assertFalse(personalGoalCard.checkGoal(matr));
    }
}