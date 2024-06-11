package mock;

import org.junit.jupiter.api.Test;

class RandomRewardTest {

    @Test
    public void myTest(){
        for(int i=0; i<100; i++)
            System.out.println("i: "+i+" max: "+(int) Math.ceil((double)i/7));
    }
}