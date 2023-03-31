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

        for(int i=0; i<10; i++){

             c = new CommonGoalCard(i%5, 4);

            assertTrue(Arrays.asList(Token.values())
                    .contains(c.getTopStack()));

        }
    }

    @Test
    void getTopStack_emptyStack() {

        CommonGoalCard c;

        for(int i=0; i<5; i++){

            c = new CommonGoalCard(1, i);

            System.out.println("i: "+ i);

            for(int w=0; w<=i+1; w++){

                System.out.println(c.getTopStack());

            }
        }
    }
}