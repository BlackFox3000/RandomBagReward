package fr.lataverne.randomreward.Stock;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.lataverne.randomreward.Reward;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

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

    @Test
    public void testReadBag() throws IOException {
        File folder = new File("src/main/java/fr/lataverne/randomreward/players/");
        HashMap<String, Bag> bags = new HashMap<>();

        if (folder.listFiles() == null) {
            System.out.println("[RandomReward] create folder 'player/'");
            Path path = Paths.get("plugins/RandomReward/players/");
            folder.mkdirs();
            Files.createDirectories(path);
            folder = new File("plugins/RandomReward/players/");
        }
        else
            System.out.println("[RandomRewar] Fichier '/player/' existant" );

        for (File fileEntry : Objects.requireNonNull(folder.listFiles())) {
            if (!fileEntry.isDirectory()) {
                FileReader fr = new FileReader(fileEntry);
                BufferedReader br = new BufferedReader(fr);
                String line;
                StringBuilder json = new StringBuilder();
                while ((line = br.readLine()) != null) {
                    json.append(line);
                }
                Bag bag = new Bag(json.toString());
                String name = fileEntry.getName().replace(".txt", "");
                bags.put(name, bag);
            }
        }
    }

    @Test
    public void TestDeserialize() throws JsonProcessingException {
        String json =
                "[" +
                        "{\"plugin\" : \"itemreward\" , \"nomItem\" : \"ULU\",\"count\":1,\"chance\":1.6,\"index\":0,\"isCustomItem\":true,\"otherArg\":[]}," +
                        "{\"plugin\":\"minecraft\",\"nomItem\":\"grass_block\",\"count\":64,\"chance\":3.4,\"index\":0,\"isCustomItem\":false,\"otherArg\":[]}," +
                        "{\"plugin\":\"minecraft\",\"nomItem\":\"grass_blocko\",\"count\":64,\"chance\":3.4,\"index\":0,\"isCustomItem\":false,\"otherArg\":[]}" +
                "]";
        ObjectMapper objectMapper = new ObjectMapper();


        System.out.println("json: "+json);

        //List<Reward> listReward
           List<Reward> listReward = objectMapper.readValue(json, new TypeReference<List<Reward>>(){});
        for (Reward reward:listReward
             ) {
            reward.printTest();
        }

    }

}