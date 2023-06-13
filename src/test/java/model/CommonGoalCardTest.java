package model;

import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.ToStringBuilder;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class CommonGoalCardTest {

    private Token t;

    /**
     * check if the .getTopStack always return a token
     */
    @Test
    void getTopStack() {

         CommonGoalCard c;

        for(int i=1; i<=12; i++){

             c = new CommonGoalCard(i, 4);

            assertTrue(Arrays.asList(Token.values())
                    .contains(c.getTopStack()));

        }
    }

    @Test
    void getTopStack_emptyStack() {

        CommonGoalCard c;

        for(int i=2; i<5; i++){

            c = new CommonGoalCard(1, i);

            System.out.println("i: "+ i);

            for(int w=0; w<=i+1; w++){

                System.out.println(c.getTopStack());

            }
        }
    }
}