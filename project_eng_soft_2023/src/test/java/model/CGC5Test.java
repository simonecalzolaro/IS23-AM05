package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CGC5Test {


    CommonGoalCard cgc = new CommonGoalCard(5, 4);

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
    void checkGoal_oneType() {

        Tile matr[][]=
                {{Tile.BLUE   ,Tile.EMPTY   ,Tile.BLUE   ,Tile.EMPTY   ,Tile.BLUE   },
                {Tile.BLUE   ,Tile.EMPTY   ,Tile.BLUE   ,Tile.EMPTY   ,Tile.BLUE   },
                {Tile.BLUE   ,Tile.EMPTY   ,Tile.BLUE   ,Tile.EMPTY   ,Tile.BLUE   },
                {Tile.BLUE   ,Tile.EMPTY   ,Tile.BLUE   ,Tile.EMPTY   ,Tile.BLUE   },
                {Tile.BLUE   ,Tile.EMPTY   ,Tile.BLUE   ,Tile.EMPTY   ,Tile.BLUE   },
                {Tile.BLUE   ,Tile.EMPTY   ,Tile.BLUE   ,Tile.EMPTY   ,Tile.BLUE   }};

        assertTrue(cgc.checkGoal(matr));

    }

    @Test
    void checkGoal_twoType() {

        Tile matr[][]=
                {{Tile.BLUE   ,Tile.EMPTY   ,Tile.BLUE   ,Tile.EMPTY   ,Tile.BLUE   },
                {Tile.PINK   ,Tile.EMPTY   ,Tile.BLUE   ,Tile.EMPTY   ,Tile.BLUE   },
                {Tile.BLUE   ,Tile.EMPTY   ,Tile.PINK   ,Tile.EMPTY   ,Tile.PINK   },
                {Tile.BLUE   ,Tile.EMPTY   ,Tile.BLUE   ,Tile.EMPTY   ,Tile.BLUE   },
                {Tile.BLUE   ,Tile.EMPTY   ,Tile.BLUE   ,Tile.EMPTY   ,Tile.BLUE   },
                {Tile.BLUE   ,Tile.EMPTY   ,Tile.BLUE   ,Tile.EMPTY   ,Tile.BLUE   }};

        assertTrue(cgc.checkGoal(matr));

    }

    @Test
    void checkGoal_threeType() {

        Tile matr[][]=
                {{Tile.BLUE   ,Tile.EMPTY   ,Tile.BLUE   ,Tile.EMPTY   ,Tile.BLUE   },
                {Tile.PINK   ,Tile.EMPTY   ,Tile.WHITE   ,Tile.EMPTY   ,Tile.BLUE   },
                {Tile.WHITE   ,Tile.EMPTY   ,Tile.PINK   ,Tile.EMPTY   ,Tile.PINK   },
                {Tile.BLUE   ,Tile.EMPTY   ,Tile.BLUE   ,Tile.EMPTY   ,Tile.WHITE   },
                {Tile.BLUE   ,Tile.EMPTY   ,Tile.BLUE   ,Tile.EMPTY   ,Tile.BLUE   },
                {Tile.BLUE   ,Tile.EMPTY   ,Tile.BLUE   ,Tile.EMPTY   ,Tile.BLUE   }};

        assertTrue(cgc.checkGoal(matr));

    }


    @Test
    void checkGoal_fourType() {

        Tile matr[][]=
                {{Tile.GREEN   ,Tile.EMPTY   ,Tile.BLUE   ,Tile.EMPTY   ,Tile.BLUE   },
                {Tile.PINK   ,Tile.EMPTY   ,Tile.WHITE   ,Tile.EMPTY   ,Tile.BLUE   },
                {Tile.WHITE   ,Tile.EMPTY   ,Tile.PINK   ,Tile.EMPTY   ,Tile.PINK   },
                {Tile.BLUE   ,Tile.EMPTY   ,Tile.BLUE   ,Tile.EMPTY   ,Tile.WHITE   },
                {Tile.BLUE   ,Tile.EMPTY   ,Tile.GREEN   ,Tile.EMPTY   ,Tile.BLUE   },
                {Tile.BLUE   ,Tile.EMPTY   ,Tile.BLUE   ,Tile.EMPTY   ,Tile.GREEN   }};

        assertFalse(cgc.checkGoal(matr));

    }

    @Test
    void checkGoal_twoCol() {
        
        Tile matr[][]=
                {{Tile.GREEN   ,Tile.EMPTY   ,Tile.BLUE   ,Tile.EMPTY   ,Tile.BLUE   },
                {Tile.PINK   ,Tile.EMPTY   ,Tile.WHITE   ,Tile.EMPTY   ,Tile.BLUE   },
                {Tile.WHITE   ,Tile.EMPTY   ,Tile.PINK   ,Tile.EMPTY   ,Tile.PINK   },
                {Tile.BLUE   ,Tile.EMPTY   ,Tile.BLUE   ,Tile.EMPTY   ,Tile.WHITE   },
                {Tile.BLUE   ,Tile.EMPTY   ,Tile.GREEN   ,Tile.EMPTY   ,Tile.BLUE   },
                {Tile.EMPTY   ,Tile.EMPTY   ,Tile.BLUE   ,Tile.EMPTY   ,Tile.GREEN   }};

        assertFalse(cgc.checkGoal(matr));

    }
}