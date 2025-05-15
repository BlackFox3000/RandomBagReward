package fr.lataverne.randomreward;

import org.junit.jupiter.api.Test;

import static fr.lataverne.randomreward.EnvironmentDetector.log;


class RandomRewardTest {

    @Test
    public void myTest(){
        for(int i=0; i<100; i++)
            log("i: "+i+" max: "+(int) Math.ceil((double)i/7));
    }
}