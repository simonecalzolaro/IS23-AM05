package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CGC2Test {


    @Test
    public void checkGoal_empty() {

        CGC1 cgc1=new CGC1(4);

        Tile matr[][]=
                {{Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   },
                {Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   },
                {Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   },
                {Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   },
                {Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   },
                {Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   }};

        assertFalse(cgc1.checkGoal(matr));

    }

    @Test
    public void checkGoal_1() {

        CGC2 cgc2=new CGC2(4);

        Tile matr[][]=
                {{Tile.PINK   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.PINK   },
                {Tile.BLUE   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   },
                {Tile.BLUE   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   },
                {Tile.BLUE   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   },
                {Tile.BLUE   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   },
                {Tile.PINK   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   }};

        assertFalse(cgc2.checkGoal(matr));

    }

    @Test
    public void checkGoal_2() {

        CGC2 cgc2=new CGC2(4);

        Tile matr[][]=
                {{Tile.PINK   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.PINK   },
                {Tile.BLUE   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.BLUE   },
                {Tile.BLUE   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.BLUE   },
                {Tile.BLUE   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.BLUE   },
                {Tile.BLUE   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.BLUE   },
                {Tile.PINK   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.PINK   }};

        assertTrue(cgc2.checkGoal(matr));

    }

    @Test
    public void checkGoal_3() {

        CGC2 cgc2=new CGC2(4);

        Tile matr[][]=
                {{Tile.PINK   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.PINK   },
                {Tile.BLUE   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.BLUE   },
                {Tile.BLUE   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.BLUE   },
                {Tile.BLUE   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.BLUE   },
                {Tile.BLUE   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.BLUE   },
                {Tile.PINK   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.BLUE   }};

        assertFalse(cgc2.checkGoal(matr));

    }

    @Test
    public void checkGoal_4() {

        CGC2 cgc2=new CGC2(4);

        Tile matr[][]=
                {{Tile.PINK   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.PINK   },
                {Tile.BLUE   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   },
                {Tile.BLUE   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   },
                {Tile.BLUE   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   },
                {Tile.BLUE   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   },
                {Tile.PINK   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   }};

        assertFalse(cgc2.checkGoal(matr));

    }
}