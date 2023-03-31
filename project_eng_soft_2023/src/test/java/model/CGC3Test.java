package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CGC3Test {

    CommonGoalCard cgc = new CommonGoalCard(3, 4);

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
    void checkGoal_1() {

        Tile matr[][]=
               {{Tile.BLUE   ,Tile.GREEN   ,Tile.BLUE   ,Tile.GREEN   ,Tile.EMPTY   },
                {Tile.BLUE   ,Tile.GREEN   ,Tile.BLUE   ,Tile.GREEN   ,Tile.EMPTY   },
                {Tile.BLUE   ,Tile.GREEN   ,Tile.BLUE   ,Tile.GREEN   ,Tile.EMPTY   },
                {Tile.BLUE   ,Tile.GREEN   ,Tile.BLUE   ,Tile.GREEN   ,Tile.EMPTY   },
                {Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   },
                {Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   }};

        assertTrue(cgc.checkGoal(matr));

    }

    @Test
    void checkGoal_2() {

        Tile matr[][]=
                {{Tile.BLUE   ,Tile.GREEN   ,Tile.GREEN   ,Tile.GREEN   ,Tile.GREEN   },
                {Tile.BLUE   ,Tile.BLUE   ,Tile.BLUE   ,Tile.GREEN   ,Tile.PINK   },
                {Tile.EMPTY   ,Tile.EMPTY   ,Tile.BLUE   ,Tile.GREEN   ,Tile.PINK   },
                {Tile.EMPTY   ,Tile.EMPTY   ,Tile.GREEN   ,Tile.GREEN   ,Tile.PINK   },
                {Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.PINK   },
                {Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   }};

        assertFalse(cgc.checkGoal(matr));

    }

    @Test
    void checkGoal_3() {

        Tile matr[][]=
                {{Tile.BLUE   ,Tile.GREEN   ,Tile.GREEN   ,Tile.GREEN   ,Tile.WHITE   },
                {Tile.BLUE   ,Tile.BLUE   ,Tile.GREEN   ,Tile.WHITE   ,Tile.WHITE   },
                {Tile.BLUE   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.PINK   ,Tile.WHITE   },
                {Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.PINK   ,Tile.PINK   },
                {Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.PINK   },
                {Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   }};

        assertTrue(cgc.checkGoal(matr));

    }
}