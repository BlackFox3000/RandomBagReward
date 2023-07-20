package fr.lataverne.randomreward;

import org.bukkit.Bukkit;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class RandomBuilder {

    private final ArrayList<Reward> rewards = new ArrayList<>();
    private final HashMap<Integer,Reward> rewardHashMap = new HashMap<>();
    private int indexMax = 0;

    public RandomBuilder(File myObj) {
        try {
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                String[] words = data.split(" ");
                if(!Objects.equals(words[0], "#"))
                    this.rewards.add(new Reward(words));
            }
            myReader.close();
            this.initialiseIndexs();

        } catch (FileNotFoundException e) {
            Bukkit.getConsoleSender().sendMessage("An error occurred.");
            e.printStackTrace();
        }
    }

    private void initialiseIndexs() {
        int	indexTotal = 0;
        for (Reward reward : this.rewards){
            indexTotal += (int) (reward.getChance()*100);
            rewardHashMap.put(indexTotal,reward);
        }
        this.indexMax = indexTotal;
    }

    public void printRewards(){
        for (Integer index : rewardHashMap.keySet()){
            Bukkit.getConsoleSender().sendMessage("index:"+index+" ");
            rewardHashMap.get(index).print();
        }
    }

    /**
     * returne one random reward
     * @return Reward
     */
    public Reward getRandomReward(){
        int indexRandom = (int) Math.floor(Math.random() * indexMax);
        return get(indexRandom);
    }

    private Reward get(int indexRandom) {
        Object[] keys = this.rewardHashMap.keySet().toArray();
        Arrays.sort(keys);

        int lastIndex = -1;
        for(Object index : keys){
            if(indexRandom <= (int) index && lastIndex == -1) {
                lastIndex = (int) index;
            }
        }

        return rewardHashMap.get(lastIndex);
    }

    public ArrayList<String> getList() {
        ArrayList<String> strings = new ArrayList<>();
        for (Integer index : rewardHashMap.keySet()){
            String string = "index:"+index+" ";
            string += rewardHashMap.get(index).getString();
            strings.add(string);
        }
        return strings;
    }

    public static void main(String[] args){
        RandomBuilder randomBuilder= new RandomBuilder(  new File("src\\fr\\lataverne\\randomreward\\rewards.txt"));
        randomBuilder.printRewards();

        HashMap<String, Integer> stat = new HashMap<>();
        for(int i = 0; i<500 ; i++) {
            Reward reward = randomBuilder.getRandomReward();
            if(reward == null)
                //System.out.println("on est naze patron");
                Bukkit.getConsoleSender().sendMessage("on est naze patron");
            else
            if(!stat.containsKey(reward.getName()))
                stat.put(reward.getName(),1);
            else
                stat.put(reward.getName(),stat.get(reward.getName())+1);
        }

    }

}

