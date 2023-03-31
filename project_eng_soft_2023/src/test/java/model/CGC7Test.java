package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CGC7Test {

    CommonGoalCard cgc = new CommonGoalCard(7, 4);
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
    public void checkGoal_diag1() {

        Tile matr[][]=
                {{Tile.BLUE   ,Tile.PINK   ,Tile.PINK   ,Tile.PINK   ,Tile.PINK   },
                {Tile.EMPTY   ,Tile.BLUE   ,Tile.PINK   ,Tile.PINK   ,Tile.PINK   },
                {Tile.EMPTY   ,Tile.EMPTY   ,Tile.BLUE   ,Tile.PINK   ,Tile.PINK   },
                {Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.BLUE   ,Tile.PINK   },
                {Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.BLUE   },
                {Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   }};

        assertTrue(cgc.checkGoal(matr));

    }

    @Test
    public void checkGoal_diag2() {

        Tile matr[][]=
                {{Tile.BLUE   ,Tile.PINK   ,Tile.PINK   ,Tile.PINK   ,Tile.BLUE   },
                {Tile.PINK   ,Tile.PINK   ,Tile.PINK   ,Tile.BLUE   ,Tile.EMPTY   },
                {Tile.PINK   ,Tile.PINK   ,Tile.BLUE   ,Tile.PINK   ,Tile.EMPTY   },
                {Tile.PINK   ,Tile.BLUE   ,Tile.EMPTY   ,Tile.BLUE   ,Tile.EMPTY   },
                {Tile.BLUE   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   },
                {Tile.PINK   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   }};

        assertTrue(cgc.checkGoal(matr));

    }

    @Test
    public void checkGoal_diag3() {

        Tile matr[][]=
                {{Tile.BLUE   ,Tile.PINK   ,Tile.PINK   ,Tile.PINK   ,Tile.PINK   },
                {Tile.PINK   ,Tile.BLUE   ,Tile.PINK   ,Tile.BLUE   ,Tile.PINK   },
                {Tile.EMPTY   ,Tile.PINK   ,Tile.BLUE   ,Tile.PINK   ,Tile.PINK   },
                {Tile.EMPTY   ,Tile.BLUE   ,Tile.PINK   ,Tile.PINK   ,Tile.PINK   },
                {Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.PINK   ,Tile.PINK   },
                {Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.PINK   }};

        assertTrue(cgc.checkGoal(matr));

    }

    @Test
    public void checkGoal_diag4() {


        Tile matr[][]=
                {{Tile.BLUE   ,Tile.PINK   ,Tile.PINK   ,Tile.PINK   ,Tile.BLUE   },
                {Tile.PINK   ,Tile.BLUE   ,Tile.PINK   ,Tile.BLUE   ,Tile.WHITE   },
                {Tile.WHITE   ,Tile.PINK   ,Tile.BLUE   ,Tile.WHITE   ,Tile.PINK   },
                {Tile.WHITE   ,Tile.BLUE   ,Tile.WHITE   ,Tile.BLUE   ,Tile.PINK   },
                {Tile.WHITE   ,Tile.WHITE   ,Tile.PINK   ,Tile.PINK   ,Tile.PINK   },
                {Tile.WHITE   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.PINK   }};

        assertTrue(cgc.checkGoal(matr));

    }



}