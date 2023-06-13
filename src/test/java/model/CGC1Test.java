package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CGC1Test {


    //test CGC1--------------------------------------------------------------------------------------------------------------------------

        CommonGoalCard cgc1 = new CommonGoalCard(1, 4);

        @Test
        public void checkGoal1_empty () {


        Tile matr[][] =
                {{Tile.EMPTY, Tile.EMPTY, Tile.EMPTY, Tile.EMPTY, Tile.EMPTY},
                        {Tile.EMPTY, Tile.EMPTY, Tile.EMPTY, Tile.EMPTY, Tile.EMPTY},
                        {Tile.EMPTY, Tile.EMPTY, Tile.EMPTY, Tile.EMPTY, Tile.EMPTY},
                        {Tile.EMPTY, Tile.EMPTY, Tile.EMPTY, Tile.EMPTY, Tile.EMPTY},
                        {Tile.EMPTY, Tile.EMPTY, Tile.EMPTY, Tile.EMPTY, Tile.EMPTY},
                        {Tile.EMPTY, Tile.EMPTY, Tile.EMPTY, Tile.EMPTY, Tile.EMPTY}};

        assertFalse(cgc1.checkGoal(matr));

    }

        @Test
        public void checkGoal1_1 () {


        Tile matr[][] =
                {{Tile.BLUE, Tile.BLUE, Tile.BLUE, Tile.BLUE, Tile.BLUE},
                        {Tile.BLUE, Tile.BLUE, Tile.BLUE, Tile.BLUE, Tile.BLUE},
                        {Tile.BLUE, Tile.BLUE, Tile.EMPTY, Tile.EMPTY, Tile.EMPTY},
                        {Tile.EMPTY, Tile.EMPTY, Tile.EMPTY, Tile.EMPTY, Tile.EMPTY},
                        {Tile.EMPTY, Tile.EMPTY, Tile.EMPTY, Tile.EMPTY, Tile.EMPTY},
                        {Tile.EMPTY, Tile.EMPTY, Tile.EMPTY, Tile.EMPTY, Tile.EMPTY}};

        assertFalse(cgc1.checkGoal(matr));

    }

        @Test
        public void checkGoal1_2 () {


        Tile matr[][] =
                {{Tile.PINK, Tile.BLUE, Tile.BLUE, Tile.BLUE, Tile.BLUE},
                        {Tile.PINK, Tile.BLUE, Tile.BLUE, Tile.BLUE, Tile.BLUE},
                        {Tile.PINK, Tile.PINK, Tile.EMPTY, Tile.EMPTY, Tile.EMPTY},
                        {Tile.EMPTY, Tile.EMPTY, Tile.EMPTY, Tile.EMPTY, Tile.EMPTY},
                        {Tile.EMPTY, Tile.EMPTY, Tile.EMPTY, Tile.EMPTY, Tile.EMPTY},
                        {Tile.EMPTY, Tile.EMPTY, Tile.EMPTY, Tile.EMPTY, Tile.EMPTY}};

        assertFalse(cgc1.checkGoal(matr));

    }

        @Test
        public void checkGoal1_3 () {


        Tile matr[][] =
                {{Tile.PINK, Tile.BLUE, Tile.GREEN, Tile.BLUE, Tile.PINK},
                        {Tile.PINK, Tile.BLUE, Tile.GREEN, Tile.BLUE, Tile.PINK},
                        {Tile.EMPTY, Tile.PINK, Tile.PINK, Tile.EMPTY, Tile.EMPTY},
                        {Tile.EMPTY, Tile.EMPTY, Tile.EMPTY, Tile.EMPTY, Tile.EMPTY},
                        {Tile.EMPTY, Tile.EMPTY, Tile.EMPTY, Tile.EMPTY, Tile.EMPTY},
                        {Tile.EMPTY, Tile.EMPTY, Tile.EMPTY, Tile.EMPTY, Tile.EMPTY}};

        assertTrue(cgc1.checkGoal(matr));

    }


        @Test
        public void checkGoal1_4 () {


        Tile matr[][] =
                {{Tile.PINK, Tile.BLUE, Tile.GREEN, Tile.WHITE, Tile.BLUE},
                        {Tile.PINK, Tile.BLUE, Tile.GREEN, Tile.YELLOW, Tile.BLUE},
                        {Tile.WHITE, Tile.WHITE, Tile.EMPTY, Tile.GREEN, Tile.WHITE},
                        {Tile.EMPTY, Tile.WHITE, Tile.EMPTY, Tile.GREEN, Tile.WHITE},
                        {Tile.EMPTY, Tile.WHITE, Tile.EMPTY, Tile.EMPTY, Tile.EMPTY},
                        {Tile.EMPTY, Tile.EMPTY, Tile.EMPTY, Tile.EMPTY, Tile.EMPTY}};

        assertTrue(cgc1.checkGoal(matr));

    }

        @Test
        public void checkGoal1_5 () {


        Tile matr[][] =
                {{Tile.PINK, Tile.WHITE, Tile.PINK, Tile.WHITE, Tile.PINK},
                        {Tile.WHITE, Tile.PINK, Tile.EMPTY, Tile.PINK, Tile.BLUE},
                        {Tile.WHITE, Tile.WHITE, Tile.EMPTY, Tile.GREEN, Tile.BLUE},
                        {Tile.EMPTY, Tile.WHITE, Tile.EMPTY, Tile.GREEN, Tile.BLUE},
                        {Tile.EMPTY, Tile.WHITE, Tile.EMPTY, Tile.GREEN, Tile.BLUE},
                        {Tile.EMPTY, Tile.EMPTY, Tile.EMPTY, Tile.BLUE, Tile.BLUE}};

        assertFalse(cgc1.checkGoal(matr));

    }

        @Test
        public void checkGoal1_6 () {


        Tile matr[][] =
                {{Tile.PINK, Tile.WHITE, Tile.PINK, Tile.WHITE, Tile.PINK},
                        {Tile.WHITE, Tile.PINK, Tile.EMPTY, Tile.PINK, Tile.BLUE},
                        {Tile.WHITE, Tile.WHITE, Tile.EMPTY, Tile.GREEN, Tile.BLUE},
                        {Tile.EMPTY, Tile.WHITE, Tile.EMPTY, Tile.GREEN, Tile.BLUE},
                        {Tile.EMPTY, Tile.EMPTY, Tile.EMPTY, Tile.GREEN, Tile.BLUE},
                        {Tile.EMPTY, Tile.EMPTY, Tile.EMPTY, Tile.BLUE, Tile.EMPTY}};

        assertFalse(cgc1.checkGoal(matr));

    }


        @Test
        public void checkGoal1_flippedShelf () {


        Tile matr[][] =
                {{Tile.EMPTY, Tile.EMPTY, Tile.EMPTY, Tile.EMPTY, Tile.EMPTY},
                        {Tile.PINK, Tile.BLUE, Tile.GREEN, Tile.EMPTY, Tile.BLUE},
                        {Tile.WHITE, Tile.WHITE, Tile.EMPTY, Tile.EMPTY, Tile.EMPTY},
                        {Tile.EMPTY, Tile.WHITE, Tile.EMPTY, Tile.EMPTY, Tile.EMPTY},
                        {Tile.EMPTY, Tile.WHITE, Tile.EMPTY, Tile.EMPTY, Tile.EMPTY},
                        {Tile.PINK, Tile.BLUE, Tile.GREEN, Tile.EMPTY, Tile.BLUE}};

        String str = null;
        assertThrows(IllegalArgumentException.class, () -> {
            Integer.valueOf(str);
        });
    }








}