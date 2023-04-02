package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CGC9Test {

    @Test
    public void checkGoal_empty() {

        CGC9 cgc=new CGC9(4);

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

        CGC9 cgc=new CGC9(4);

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

        CGC9 cgc=new CGC9(4);

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

        CGC9 cgc=new CGC9(4);

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

        CGC9 cgc=new CGC9(4);

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