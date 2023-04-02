package model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CGC1Test {

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

        CGC1 cgc1=new CGC1(4);

        Tile matr[][]=
                {{Tile.BLUE   ,Tile.BLUE   ,Tile.BLUE   ,Tile.BLUE   ,Tile.BLUE   },
                {Tile.BLUE   ,Tile.BLUE   ,Tile.BLUE   ,Tile.BLUE   ,Tile.BLUE   },
                {Tile.BLUE   ,Tile.BLUE   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   },
                {Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   },
                {Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   },
                {Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   }};

        assertTrue(cgc1.checkGoal(matr));

    }

    @Test
    public void checkGoal_2() {

        CGC1 cgc1=new CGC1(4);

        Tile matr[][]=
                {{Tile.PINK   ,Tile.BLUE   ,Tile.BLUE   ,Tile.BLUE   ,Tile.BLUE   },
                {Tile.PINK   ,Tile.BLUE   ,Tile.BLUE   ,Tile.BLUE   ,Tile.BLUE   },
                {Tile.PINK   ,Tile.PINK   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   },
                {Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   },
                {Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   },
                {Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   }};

        assertTrue(cgc1.checkGoal(matr));

    }

    @Test
    public void checkGoal_3() {

        CGC1 cgc1=new CGC1(4);

        Tile matr[][]=
                {{Tile.PINK   ,Tile.BLUE   ,Tile.GREEN   ,Tile.BLUE   ,Tile.BLUE   },
                {Tile.PINK   ,Tile.BLUE   ,Tile.GREEN   ,Tile.BLUE   ,Tile.BLUE   },
                {Tile.PINK   ,Tile.PINK   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   },
                {Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   },
                {Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   },
                {Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   }};

        assertTrue(cgc1.checkGoal(matr));

    }


    @Test
    public void checkGoal_4() {

        CGC1 cgc1=new CGC1(4);

        Tile matr[][]=
                {{Tile.PINK   ,Tile.BLUE   ,Tile.GREEN   ,Tile.EMPTY   ,Tile.BLUE   },
                {Tile.PINK   ,Tile.BLUE   ,Tile.GREEN   ,Tile.EMPTY   ,Tile.BLUE   },
                {Tile.WHITE   ,Tile.WHITE   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   },
                {Tile.EMPTY   ,Tile.WHITE   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   },
                {Tile.EMPTY   ,Tile.WHITE   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   },
                {Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   }};

        assertTrue(cgc1.checkGoal(matr));

    }

    @Test
    public void checkGoal_5() {

        CGC1 cgc1=new CGC1(4);

        Tile matr[][]=
                {{Tile.PINK   ,Tile.WHITE   ,Tile.PINK   ,Tile.WHITE   ,Tile.PINK   },
                {Tile.WHITE   ,Tile.PINK   ,Tile.EMPTY   ,Tile.PINK   ,Tile.BLUE   },
                {Tile.WHITE   ,Tile.WHITE   ,Tile.EMPTY   ,Tile.GREEN   ,Tile.BLUE   },
                {Tile.EMPTY   ,Tile.WHITE   ,Tile.EMPTY   ,Tile.GREEN   ,Tile.BLUE   },
                {Tile.EMPTY   ,Tile.WHITE   ,Tile.EMPTY   ,Tile.GREEN   ,Tile.BLUE   },
                {Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.BLUE   ,Tile.BLUE   }};

        assertTrue(cgc1.checkGoal(matr));

    }

    @Test
    public void checkGoal_6() {

        CGC1 cgc1=new CGC1(4);

        Tile matr[][]=
                {{Tile.PINK   ,Tile.WHITE   ,Tile.PINK   ,Tile.WHITE   ,Tile.PINK   },
                {Tile.WHITE   ,Tile.PINK   ,Tile.EMPTY   ,Tile.PINK   ,Tile.BLUE   },
                {Tile.WHITE   ,Tile.WHITE   ,Tile.EMPTY   ,Tile.GREEN   ,Tile.BLUE   },
                {Tile.EMPTY   ,Tile.WHITE   ,Tile.EMPTY   ,Tile.GREEN   ,Tile.BLUE   },
                {Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.GREEN   ,Tile.BLUE   },
                {Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.BLUE   ,Tile.EMPTY   }};

        assertFalse(cgc1.checkGoal(matr));

    }


    @Test
    public void checkGoal_flippedShelf() {

        CGC1 cgc1=new CGC1(4);

        Tile matr[][]=
                {{Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   },
                {Tile.PINK   ,Tile.BLUE   ,Tile.GREEN   ,Tile.EMPTY   ,Tile.BLUE   },
                {Tile.WHITE   ,Tile.WHITE   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   },
                {Tile.EMPTY   ,Tile.WHITE   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   },
                {Tile.EMPTY   ,Tile.WHITE   ,Tile.EMPTY   ,Tile.EMPTY   ,Tile.EMPTY   },
                {Tile.PINK   ,Tile.BLUE   ,Tile.GREEN   ,Tile.EMPTY   ,Tile.BLUE   }};

        String str = null;
        assertThrows(IllegalArgumentException.class , () -> {Integer.valueOf(str);});
    }

}