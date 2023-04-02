package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CGC8Test {

    @Test
    public void checkGoal_empty() {

        CGC8 cgc=new CGC8(4);

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
    public void checkGoal_oneType() {

        CGC8 cgc=new CGC8(4);

        Tile matr[][]=
                {{Tile.BLUE   ,Tile.BLUE   ,Tile.BLUE   ,Tile.BLUE   ,Tile.BLUE   },
                {Tile.PINK   ,Tile.PINK   ,Tile.PINK   ,Tile.PINK   ,Tile.PINK   },
                {Tile.WHITE   ,Tile.WHITE   ,Tile.WHITE   ,Tile.WHITE   ,Tile.WHITE   },
                {Tile.PINK   ,Tile.PINK   ,Tile.PINK   ,Tile.PINK   ,Tile.PINK   },
                {Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   },
                {Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   }};

        assertTrue(cgc.checkGoal(matr));

    }

    @Test
    public void checkGoal_twoType() {

        CGC8 cgc=new CGC8(4);

        Tile matr[][]=
                {{Tile.BLUE   ,Tile.BLUE   ,Tile.BLUE   ,Tile.BLUE   ,Tile.GREEN   },
                {Tile.PINK   ,Tile.PINK   ,Tile.PINK   ,Tile.PINK   ,Tile.GREEN   },
                {Tile.WHITE   ,Tile.WHITE   ,Tile.WHITE   ,Tile.WHITE   ,Tile.GREEN   },
                {Tile.PINK   ,Tile.PINK   ,Tile.PINK   ,Tile.PINK   ,Tile.GREEN   },
                {Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   },
                {Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   }};

        assertTrue(cgc.checkGoal(matr));

    }

    @Test
    public void checkGoal_threeType() {

        CGC8 cgc=new CGC8(4);

        Tile matr[][]=
                {{Tile.BLUE   ,Tile.BLUE   ,Tile.BLUE   ,Tile.YELLOW   ,Tile.GREEN   },
                        {Tile.PINK   ,Tile.PINK   ,Tile.PINK   ,Tile.YELLOW   ,Tile.GREEN   },
                        {Tile.WHITE   ,Tile.WHITE   ,Tile.WHITE   ,Tile.YELLOW   ,Tile.GREEN   },
                        {Tile.PINK   ,Tile.PINK   ,Tile.PINK   ,Tile.YELLOW   ,Tile.GREEN   },
                        {Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   },
                        {Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   }};

        assertTrue(cgc.checkGoal(matr));

    }

    @Test
    public void checkGoal_emptyCol() {

        CGC8 cgc=new CGC8(4);

        Tile matr[][]=
                {{Tile.BLUE   ,Tile.BLUE   ,Tile.BLUE   ,Tile.YELLOW   ,Tile.GREEN   },
                {Tile.EMPTY   ,Tile.PINK   ,Tile.PINK   ,Tile.YELLOW   ,Tile.GREEN   },
                {Tile.EMPTY   ,Tile.WHITE   ,Tile.WHITE   ,Tile.YELLOW   ,Tile.GREEN   },
                {Tile.EMPTY   ,Tile.PINK   ,Tile.PINK   ,Tile.YELLOW   ,Tile.GREEN   },
                {Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   },
                {Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   }};

        assertFalse(cgc.checkGoal(matr));

    }


}