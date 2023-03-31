package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CGC10Test {

    CommonGoalCard cgc = new CommonGoalCard(10, 4);
    @Test
    public void checkGoal_empty() {

        Tile matr[][]=
                {{Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   },
                {Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   },
                {Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   },
                {Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   },
                {Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   },
                {Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   }};

        assertFalse(cgc.checkGoal(matr));

    }

    @Test
    void checkGoal_allBlue() {

        Tile matr[][]=
                {{Tile.BLUE   ,Tile.BLUE   ,Tile.BLUE   ,Tile.BLUE   ,Tile.BLUE   },
                {Tile.BLUE   ,Tile.BLUE   ,Tile.BLUE   ,Tile.BLUE   ,Tile.BLUE   },
                {Tile.BLUE   ,Tile.BLUE   ,Tile.BLUE   ,Tile.BLUE   ,Tile.BLUE   },
                {Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   },
                {Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   },
                {Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   }};

        assertFalse(cgc.checkGoal(matr));

    }


    @Test
    void checkGoal_4typeTiles() {

        Tile matr[][]=
                {{Tile.BLUE   ,Tile.WHITE   ,Tile.GREEN   ,Tile.YELLOW   ,Tile.LIGHTBLUE   },
                {Tile.WHITE   ,Tile.BLUE   ,Tile.GREEN   ,Tile.YELLOW   ,Tile.BLUE   },
                {Tile.BLUE   ,Tile.BLUE   ,Tile.BLUE   ,Tile.BLUE   ,Tile.BLUE   },
                {Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   },
                {Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   },
                {Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   }};

        assertFalse(cgc.checkGoal(matr));

    }

    @Test
    void checkGoal_5typeTiles1() {

        Tile matr[][]=
                {{Tile.BLUE   ,Tile.WHITE   ,Tile.GREEN   ,Tile.YELLOW   ,Tile.LIGHTBLUE   },
                {Tile.WHITE   ,Tile.LIGHTBLUE   ,Tile.GREEN   ,Tile.YELLOW   ,Tile.BLUE   },
                {Tile.BLUE   ,Tile.BLUE   ,Tile.BLUE   ,Tile.BLUE   ,Tile.BLUE   },
                {Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   },
                {Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   },
                {Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   }};

        assertTrue(cgc.checkGoal(matr));

    }

    @Test
    void checkGoal_5typeTiles2() {

        Tile matr[][]=
                {{Tile.BLUE   ,Tile.BLUE   ,Tile.BLUE   ,Tile.BLUE   ,Tile.BLUE   },
                 {Tile.BLUE   ,Tile.WHITE   ,Tile.GREEN   ,Tile.YELLOW   ,Tile.LIGHTBLUE   },
                 {Tile.WHITE   ,Tile.LIGHTBLUE   ,Tile.GREEN   ,Tile.YELLOW   ,Tile.BLUE   },
                 {Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   },
                 {Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   },
                 {Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   }};

        assertTrue(cgc.checkGoal(matr));

    }

    @Test
    void checkGoal_5typeTiles3_fake() {

        Tile matr[][]=
                {{Tile.BLUE   ,Tile.BLUE   ,Tile.BLUE   ,Tile.BLUE   ,Tile.BLUE   },
                {Tile.BLUE   ,Tile.WHITE   ,Tile.GREEN   ,Tile.YELLOW   ,Tile.LIGHTBLUE   },
                {Tile.WHITE   ,Tile.EMPTY   ,Tile.GREEN   ,Tile.YELLOW   ,Tile.BLUE   },
                {Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   },
                {Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   },
                {Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   }};

        assertFalse(cgc.checkGoal(matr));

    }
}