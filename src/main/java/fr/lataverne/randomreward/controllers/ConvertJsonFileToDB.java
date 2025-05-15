package fr.lataverne.randomreward.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.lataverne.randomreward.ConfigManager;
import fr.lataverne.randomreward.RandomReward;
import fr.lataverne.randomreward.api.RewardService;
import fr.lataverne.randomreward.models.Reward;
import fr.lataverne.randomreward.models.RewardDB;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

public class ConvertJsonFileToDB {
    public static ConfigManager config;

    public ConvertJsonFileToDB() {
        config = RandomReward.getInstance().getConfigManager();
    }

    /**
     * Récupère un tableau de Reward depuis un fichier JSON
     * @param file
     * @return
     */
    public static List<Reward> readRewardsFromFile(File file) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return Arrays.asList(mapper.readValue(file, Reward[].class));
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    /**
     * Converti une list de Reward en RewardBD
     * @param rewards
     * @param uuid
     * @return
     */
    public static List<RewardDB> convertRewardsToDB(List<Reward> rewards, String uuid) {
        List<RewardDB> converted = new ArrayList<>();
        String currentDate = LocalDate.now().toString(); // format YYYY-MM-DD

        for (Reward reward : rewards) {
            converted.add(new RewardDB(
                    uuid,
                    reward.getPlugin(),
                    reward.getName(),
                    reward.getCount(),
                    currentDate
            ));
        }
        return converted;
    }

    /**
     * Envoie une liste de RewardDB en basse de donnée
     * @param rewards
     */
    public static void insertRewardsInDB(List<RewardDB> rewards) throws Exception {
        for (RewardDB reward : rewards) {
            RewardService.addReward(reward, config);
        }
    }

    public static boolean mainTransefertJsonToDB() throws Exception {
        File folder = new File(config.getPathFilesJson());
        for (File file : Objects.requireNonNull(folder.listFiles())) {
            if (file.getName().endsWith(".json")) {
                String uuid = file.getName().replace(".json", "");
                List<Reward> rewards = readRewardsFromFile(file);
                List<RewardDB> rewardsDB = convertRewardsToDB(rewards, uuid);
                insertRewardsInDB(rewardsDB);
            }
        }
        return true;
    }
}
