package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CGC7Test {

    @Test
    public void checkGoal_empty() {

        CGC7 cgc=new CGC7(4);

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

        CGC7 cgc=new CGC7(4);

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

        CGC7 cgc=new CGC7(4);

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

        CGC7 cgc=new CGC7(4);

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

        CGC7 cgc=new CGC7(4);

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