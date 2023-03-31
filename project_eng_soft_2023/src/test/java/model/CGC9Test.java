package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CGC9Test {

    CommonGoalCard cgc = new CommonGoalCard(9, 4);
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
    void checkGoal_1ColOk() {

        Tile matr[][]=
                {{Tile.PINK   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   },
                {Tile.BLUE  ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   },
                {Tile.YELLOW   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   },
                {Tile.GREEN   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   },
                {Tile.LIGHTBLUE   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   },
                {Tile.WHITE   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   }};

        assertFalse(cgc.checkGoal(matr));

    }


    @Test
    void checkGoal_2ColOk() {

        Tile matr[][]=
                {{Tile.PINK   ,Tile.EMPTY   ,Tile.YELLOW   ,Tile.EMPTY   ,Tile.EMPTY   },
                {Tile.BLUE  ,Tile.EMPTY   ,Tile.PINK   ,Tile.EMPTY   ,Tile.EMPTY   },
                {Tile.YELLOW   ,Tile.EMPTY   ,Tile.BLUE   ,Tile.EMPTY   ,Tile.EMPTY   },
                {Tile.GREEN   ,Tile.EMPTY   ,Tile.WHITE   ,Tile.EMPTY   ,Tile.EMPTY   },
                {Tile.LIGHTBLUE   ,Tile.EMPTY   ,Tile.GREEN   ,Tile.EMPTY   ,Tile.EMPTY   },
                {Tile.WHITE   ,Tile.EMPTY   ,Tile.LIGHTBLUE   ,Tile.EMPTY   ,Tile.EMPTY   }};

        assertTrue(cgc.checkGoal(matr));

    }

    @Test
    void checkGoal_2ColOk_1fake() {

        Tile matr[][]=
                {{Tile.PINK   ,Tile.EMPTY   ,Tile.YELLOW   ,Tile.EMPTY   ,Tile.EMPTY   },
                {Tile.BLUE  ,Tile.EMPTY   ,Tile.PINK   ,Tile.EMPTY   ,Tile.EMPTY   },
                {Tile.YELLOW   ,Tile.EMPTY   ,Tile.BLUE   ,Tile.EMPTY   ,Tile.EMPTY   },
                {Tile.GREEN   ,Tile.EMPTY   ,Tile.WHITE   ,Tile.EMPTY   ,Tile.EMPTY   },
                {Tile.LIGHTBLUE   ,Tile.EMPTY   ,Tile.GREEN   ,Tile.EMPTY   ,Tile.EMPTY   },
                {Tile.WHITE   ,Tile.EMPTY   ,Tile.GREEN   ,Tile.EMPTY   ,Tile.EMPTY   }};

        assertFalse(cgc.checkGoal(matr));

    }

    @Test
    void checkGoal_2fake() {

        Tile matr[][]=
                {{Tile.WHITE   ,Tile.EMPTY   ,Tile.YELLOW   ,Tile.EMPTY   ,Tile.EMPTY   },
                {Tile.BLUE  ,Tile.EMPTY   ,Tile.PINK   ,Tile.EMPTY   ,Tile.EMPTY   },
                {Tile.YELLOW   ,Tile.EMPTY   ,Tile.BLUE   ,Tile.EMPTY   ,Tile.EMPTY   },
                {Tile.GREEN   ,Tile.EMPTY   ,Tile.WHITE   ,Tile.EMPTY   ,Tile.EMPTY   },
                {Tile.LIGHTBLUE   ,Tile.EMPTY   ,Tile.GREEN   ,Tile.EMPTY   ,Tile.EMPTY   },
                {Tile.WHITE   ,Tile.EMPTY   ,Tile.GREEN   ,Tile.EMPTY   ,Tile.EMPTY   }};

        assertFalse(cgc.checkGoal(matr));

    }




}