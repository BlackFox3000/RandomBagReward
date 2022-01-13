package fr.lataverne.randomreward.Stock;

import fr.lataverne.randomreward.Reward;
import org.junit.jupiter.api.Test;

class BagTest {

    @Test
    public void testUpdateBag(){
        System.out.println("1");
        Bag bag = new Bag();
        System.out.println("2");
        bag.updateBag("francis");
        String[] reward = {"minecraft", "book", "12", "3.4"};
        bag.add(new Reward(reward));
        bag.updateBag("francis");
    }

}