package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CGC4Test {


    @Test
    public void checkGoal_empty() {

        CGC4 cgc=new CGC4(4);

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

        CGC4 cgc=new CGC4(4);

        Tile matr[][]=
                {{Tile.BLUE   ,Tile.BLUE   ,Tile.WHITE   ,Tile.WHITE   ,Tile.EMPTY   },
                {Tile.BLUE   ,Tile.BLUE   ,Tile.WHITE   ,Tile.WHITE   ,Tile.EMPTY   },
                {Tile.GREEN   ,Tile.GREEN   ,Tile.PINK   ,Tile.PINK   ,Tile.EMPTY   },
                {Tile.GREEN   ,Tile.GREEN   ,Tile.PINK   ,Tile.PINK   ,Tile.EMPTY   },
                {Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   },
                {Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   }};

        assertFalse(cgc.checkGoal(matr));
    }

    @Test
    void checkGoal_2() {

        CGC4 cgc=new CGC4(4);

        Tile matr[][]=
                {{Tile.BLUE   ,Tile.BLUE   ,Tile.WHITE   ,Tile.WHITE   ,Tile.EMPTY   },
                {Tile.BLUE   ,Tile.BLUE   ,Tile.WHITE   ,Tile.WHITE   ,Tile.EMPTY   },
                {Tile.GREEN   ,Tile.GREEN   ,Tile.PINK   ,Tile.PINK   ,Tile.EMPTY   },
                {Tile.GREEN   ,Tile.GREEN   ,Tile.PINK   ,Tile.PINK   ,Tile.EMPTY   },
                {Tile.BLUE   ,Tile.BLUE   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   },
                {Tile.BLUE   ,Tile.BLUE   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   }};

        assertTrue(cgc.checkGoal(matr));
    }

    @Test
    void checkGoal_3() {

        CGC4 cgc=new CGC4(4);

        Tile matr[][]=
                {{Tile.BLUE   ,Tile.BLUE   ,Tile.WHITE   ,Tile.WHITE   ,Tile.EMPTY   },
                {Tile.BLUE   ,Tile.BLUE   ,Tile.WHITE   ,Tile.WHITE   ,Tile.EMPTY   },
                {Tile.BLUE   ,Tile.BLUE   ,Tile.PINK   ,Tile.PINK   ,Tile.EMPTY   },
                {Tile.GREEN   ,Tile.GREEN   ,Tile.PINK   ,Tile.PINK   ,Tile.EMPTY   },
                {Tile.BLUE   ,Tile.BLUE   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   },
                {Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   }};

        assertFalse(cgc.checkGoal(matr));
    }
}