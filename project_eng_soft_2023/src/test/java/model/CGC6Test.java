package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CGC6Test {

    CommonGoalCard cgc = new CommonGoalCard(6, 4);
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
    public void checkGoal_mixFalse7() {

        Tile matr[][]=
                {{Tile.BLUE   ,Tile.PINK   ,Tile.BLUE   ,Tile.PINK   ,Tile.BLUE   },
                {Tile.PINK   ,Tile.BLUE   ,Tile.PINK   ,Tile.BLUE   ,Tile.PINK   },
                {Tile.BLUE   ,Tile.PINK   ,Tile.BLUE   ,Tile.PINK   ,Tile.GREEN   },
                {Tile.LIGHTBLUE   ,Tile.LIGHTBLUE   ,Tile.LIGHTBLUE   ,Tile.GREEN   ,Tile.GREEN   },
                {Tile.EMPTY   ,Tile.LIGHTBLUE   ,Tile.LIGHTBLUE   ,Tile.GREEN   ,Tile.GREEN   },
                {Tile.EMPTY   ,Tile.LIGHTBLUE   ,Tile.LIGHTBLUE   ,Tile.GREEN   ,Tile.GREEN   }};

        assertFalse(cgc.checkGoal(matr));

    }

    @Test
    public void checkGoal_mixTrue8() {

        Tile matr[][]=
                {{Tile.BLUE   ,Tile.PINK   ,Tile.BLUE   ,Tile.PINK   ,Tile.BLUE   },
                {Tile.PINK   ,Tile.BLUE   ,Tile.PINK   ,Tile.BLUE   ,Tile.PINK   },
                {Tile.BLUE   ,Tile.PINK   ,Tile.BLUE   ,Tile.PINK   ,Tile.GREEN   },
                {Tile.LIGHTBLUE   ,Tile.LIGHTBLUE   ,Tile.LIGHTBLUE   ,Tile.GREEN   ,Tile.GREEN   },
                {Tile.BLUE   ,Tile.LIGHTBLUE   ,Tile.LIGHTBLUE   ,Tile.GREEN   ,Tile.GREEN   },
                {Tile.EMPTY   ,Tile.LIGHTBLUE   ,Tile.LIGHTBLUE   ,Tile.GREEN   ,Tile.GREEN   }};

        assertTrue(cgc.checkGoal(matr));

    }

}