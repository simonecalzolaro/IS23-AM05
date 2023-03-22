package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CGC10Test {

    @Test
    public void checkGoal_empty() {

        CGC10 cgc=new CGC10(4);

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
    void checkGoal_allBlue() {
        CGC10 cgc=new CGC10(4);

        Tile matr[][]=
                {{Tile.BLUE   ,Tile.BLUE   ,Tile.BLUE   ,Tile.BLUE   ,Tile.BLUE   },
                {Tile.BLUE   ,Tile.BLUE   ,Tile.BLUE   ,Tile.BLUE   ,Tile.BLUE   },
                {Tile.BLUE   ,Tile.BLUE   ,Tile.BLUE   ,Tile.BLUE   ,Tile.BLUE   },
                {Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   },
                {Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   },
                {Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   }};

        assertFalse(cgc.checkGoal(matr));

    }


    @Test
    void checkGoal_4typeTiles() {
        CGC10 cgc=new CGC10(4);

        Tile matr[][]=
                {{Tile.BLUE   ,Tile.WHITE   ,Tile.GREEN   ,Tile.YELLOW   ,Tile.LIGHTBLUE   },
                {Tile.WHITE   ,Tile.BLUE   ,Tile.GREEN   ,Tile.YELLOW   ,Tile.BLUE   },
                {Tile.BLUE   ,Tile.BLUE   ,Tile.BLUE   ,Tile.BLUE   ,Tile.BLUE   },
                {Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   },
                {Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   },
                {Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   }};

        assertFalse(cgc.checkGoal(matr));

    }

    @Test
    void checkGoal_5typeTiles1() {
        CGC10 cgc=new CGC10(4);

        Tile matr[][]=
                {{Tile.BLUE   ,Tile.WHITE   ,Tile.GREEN   ,Tile.YELLOW   ,Tile.LIGHTBLUE   },
                {Tile.WHITE   ,Tile.LIGHTBLUE   ,Tile.GREEN   ,Tile.YELLOW   ,Tile.BLUE   },
                {Tile.BLUE   ,Tile.BLUE   ,Tile.BLUE   ,Tile.BLUE   ,Tile.BLUE   },
                {Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   },
                {Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   },
                {Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   }};

        assertTrue(cgc.checkGoal(matr));

    }

    @Test
    void checkGoal_5typeTiles2() {
        CGC10 cgc=new CGC10(4);

        Tile matr[][]=
                {{Tile.BLUE   ,Tile.BLUE   ,Tile.BLUE   ,Tile.BLUE   ,Tile.BLUE   },
                 {Tile.BLUE   ,Tile.WHITE   ,Tile.GREEN   ,Tile.YELLOW   ,Tile.LIGHTBLUE   },
                 {Tile.WHITE   ,Tile.LIGHTBLUE   ,Tile.GREEN   ,Tile.YELLOW   ,Tile.BLUE   },
                 {Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   },
                 {Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   },
                 {Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   }};

        assertTrue(cgc.checkGoal(matr));

    }

    @Test
    void checkGoal_5typeTiles3_fake() {
        CGC10 cgc=new CGC10(4);

        Tile matr[][]=
                {{Tile.BLUE   ,Tile.EMPTY   ,Tile.BLUE   ,Tile.BLUE   ,Tile.BLUE   },
                {Tile.BLUE   ,Tile.WHITE   ,Tile.GREEN   ,Tile.YELLOW   ,Tile.LIGHTBLUE   },
                {Tile.WHITE   ,Tile.LIGHTBLUE   ,Tile.GREEN   ,Tile.YELLOW   ,Tile.BLUE   },
                {Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   },
                {Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   },
                {Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   }};

        assertFalse(cgc.checkGoal(matr));

    }
}