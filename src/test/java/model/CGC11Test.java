package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CGC11Test {

    CommonGoalCard cgc = new CommonGoalCard(11, 4);

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
    void checkGoal_3x3square() {

        Tile matr[][]=
                {{Tile.EMPTY   ,Tile.BLUE   ,Tile.BLUE   ,Tile.BLUE   ,Tile.EMPTY   },
                {Tile.EMPTY   ,Tile.BLUE   ,Tile.BLUE   ,Tile.BLUE   ,Tile.EMPTY   },
                {Tile.EMPTY   ,Tile.BLUE   ,Tile.BLUE   ,Tile.BLUE   ,Tile.EMPTY   },
                {Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   },
                {Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   },
                {Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   }};

        assertTrue(cgc.checkGoal(matr));

    }

    @Test
    void checkGoal_xShape1() {

        Tile matr[][]=
                {{Tile.EMPTY   ,Tile.BLUE   ,Tile.WHITE   ,Tile.BLUE   ,Tile.EMPTY   },
                {Tile.EMPTY   ,Tile.WHITE   ,Tile.BLUE   ,Tile.WHITE   ,Tile.EMPTY   },
                {Tile.EMPTY   ,Tile.BLUE   ,Tile.WHITE   ,Tile.BLUE   ,Tile.EMPTY   },
                {Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   },
                {Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   },
                {Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   }};

        assertTrue(cgc.checkGoal(matr));

    }

    @Test
    void checkGoal_xShape2() {

        Tile matr[][]=
                {{Tile.EMPTY   ,Tile.WHITE   ,Tile.BLUE   ,Tile.BLUE   ,Tile.EMPTY   },
                 {Tile.EMPTY   ,Tile.BLUE   ,Tile.BLUE   ,Tile.BLUE   ,Tile.EMPTY   },
                 {Tile.EMPTY   ,Tile.BLUE   ,Tile.BLUE   ,Tile.BLUE   ,Tile.EMPTY   },
                 {Tile.EMPTY   ,Tile.BLUE   ,Tile.EMPTY   ,Tile.BLUE   ,Tile.EMPTY   },
                 {Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   },
                 {Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   }};

        assertTrue(cgc.checkGoal(matr));

    }

}