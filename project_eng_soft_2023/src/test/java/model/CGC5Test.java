package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CGC5Test {

    @Test
    public void checkGoal_empty() {

        CGC5 cgc=new CGC5(4);

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

        CGC5 cgc=new CGC5(4);

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

        CGC5 cgc=new CGC5(4);

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

        CGC5 cgc=new CGC5(4);

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

        CGC5 cgc=new CGC5(4);

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

        CGC5 cgc=new CGC5(4);

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