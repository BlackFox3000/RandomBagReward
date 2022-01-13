package fr.lataverne.randomreward;

import fr.lataverne.randomreward.Stock.Bag;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashMap;

class RandomRewardTest {

    @Test
    public void testGetSaveFile() throws IOException {
        HashMap<String, Bag> bags = RandomReward.getSaveFile();
        for(Bag bag : bags.values()){
            bag.print();
        }
    }

}