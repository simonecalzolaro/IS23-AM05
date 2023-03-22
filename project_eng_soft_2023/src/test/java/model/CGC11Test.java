package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CGC11Test {

    @Test
    public void checkGoal_empty() {

        CGC11 cgc=new CGC11(4);

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

        CGC11 cgc=new CGC11(4);

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

        CGC11 cgc=new CGC11(4);

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

        CGC11 cgc=new CGC11(4);

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