package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CGC12Test {

    CommonGoalCard cgc = new CommonGoalCard(12, 4);
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
    void checkGoal_diagAsc1() {

        Tile matr[][]=
                {{Tile.BLUE   ,Tile.PINK   ,Tile.PINK   ,Tile.PINK   ,Tile.PINK   },
                {Tile.EMPTY   ,Tile.BLUE   ,Tile.BLUE   ,Tile.PINK   ,Tile.PINK   },
                {Tile.EMPTY   ,Tile.EMPTY   ,Tile.PINK   ,Tile.BLUE   ,Tile.PINK   },
                {Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.BLUE   ,Tile.BLUE   },
                {Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.PINK   },
                {Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   }};

        assertTrue(cgc.checkGoal(matr));
    }

    @Test
    void checkGoal_diagAsc2() {

        Tile matr[][]=
                {{Tile.BLUE   ,Tile.PINK   ,Tile.PINK   ,Tile.PINK   ,Tile.PINK   },
                {Tile.WHITE   ,Tile.BLUE   ,Tile.BLUE   ,Tile.PINK   ,Tile.PINK   },
                {Tile.EMPTY   ,Tile.WHITE   ,Tile.PINK   ,Tile.BLUE   ,Tile.PINK   },
                {Tile.EMPTY   ,Tile.EMPTY   ,Tile.WHITE   ,Tile.BLUE   ,Tile.BLUE   },
                {Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.WHITE   ,Tile.PINK   },
                {Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.WHITE   }};

        assertTrue(cgc.checkGoal(matr));
    }

    @Test
    void checkGoal_diagDisc1() {

        Tile matr[][]=
                {{Tile.BLUE   ,Tile.PINK   ,Tile.BLUE   ,Tile.PINK   ,Tile.BLUE   },
                {Tile.BLUE   ,Tile.BLUE   ,Tile.PINK   ,Tile.BLUE   ,Tile.EMPTY   },
                {Tile.BLUE   ,Tile.BLUE   ,Tile.PINK   ,Tile.EMPTY   ,Tile.EMPTY   },
                {Tile.BLUE   ,Tile.PINK   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   },
                {Tile.PINK   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   },
                {Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   }};

        assertTrue(cgc.checkGoal(matr));
    }

    @Test
    void checkGoal_diagDisc2() {

        Tile matr[][]=
                {{Tile.BLUE   ,Tile.PINK   ,Tile.BLUE   ,Tile.PINK   ,Tile.BLUE   },
                {Tile.BLUE   ,Tile.BLUE   ,Tile.PINK   ,Tile.BLUE   ,Tile.WHITE   },
                {Tile.BLUE   ,Tile.BLUE   ,Tile.PINK   ,Tile.BLUE   ,Tile.EMPTY   },
                {Tile.BLUE   ,Tile.PINK   ,Tile.WHITE   ,Tile.EMPTY   ,Tile.EMPTY   },
                {Tile.PINK   ,Tile.PINK   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   },
                {Tile.WHITE   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   }};

        assertTrue(cgc.checkGoal(matr));
    }
}