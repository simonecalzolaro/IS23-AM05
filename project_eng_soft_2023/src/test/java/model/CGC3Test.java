package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CGC3Test {

    @BeforeEach
    void setUp() {
    }

    @Test
    public void checkGoal_empty() {

        CGC3 cgc=new CGC3(4);

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

        CGC3 cgc=new CGC3(4);

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

        CGC3 cgc=new CGC3(4);

        Tile matr[][]=
                {{Tile.BLUE   ,Tile.GREEN   ,Tile.GREEN   ,Tile.GREEN   ,Tile.GREEN   },
                {Tile.BLUE   ,Tile.BLUE   ,Tile.BLUE   ,Tile.GREEN   ,Tile.PINK   },
                {Tile.EMPTY   ,Tile.BLUE   ,Tile.BLUE   ,Tile.GREEN   ,Tile.PINK   },
                {Tile.EMPTY   ,Tile.GREEN   ,Tile.GREEN   ,Tile.GREEN   ,Tile.PINK   },
                {Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.PINK   },
                {Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   }};

        assertTrue(cgc.checkGoal(matr));

    }

    @Test
    void checkGoal_3() {

        CGC3 cgc=new CGC3(4);

        Tile matr[][]=
                {{Tile.BLUE   ,Tile.GREEN   ,Tile.GREEN   ,Tile.GREEN   ,Tile.WHITE   },
                {Tile.BLUE   ,Tile.BLUE   ,Tile.GREEN   ,Tile.WHITE   ,Tile.WHITE   },
                {Tile.BLUE   ,Tile.BLUE   ,Tile.BLUE   ,Tile.EMPTY   ,Tile.WHITE   },
                {Tile.BLUE   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   },
                {Tile.BLUE   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   },
                {Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   }};

        assertTrue(cgc.checkGoal(matr));

    }





}