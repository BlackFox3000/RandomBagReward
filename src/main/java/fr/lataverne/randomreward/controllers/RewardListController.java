package fr.lataverne.randomreward.controllers;

import fr.lataverne.randomreward.models.Reward;
import java.io.IOException;
import java.util.*;
import java.nio.file.Files;
import java.nio.file.Path;
import static org.bukkit.Bukkit.getConsoleSender;

public class RewardListController {

    private final List<Reward> rewards = new ArrayList<>();
    private static final Map<Integer, Reward> rewardMap = new HashMap<>();
    private static int indexMax = 0;

    public RewardListController(String pathRewardFile) {
        try {
            List<String> lines = readFileLines(pathRewardFile);
            for (String line : lines) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;

                String[] parts = line.split("\\s+");
                if (parts.length < 4) {
                    getConsoleSender().sendMessage("Ligne invalide ignorée: " + line);
                    continue;
                }

                try {
                    rewards.add(new Reward(parts));
                } catch (Exception e) {
                    getConsoleSender().sendMessage("Erreur parsing reward: " + line);
                    e.printStackTrace();
                }
            }

            initialiseIndexes();
        } catch (IOException e) {
            getConsoleSender().sendMessage("Erreur lors de la lecture du fichier: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private List<String> readFileLines(String filePath) throws IOException {
        return Files.readAllLines(Path.of(filePath));
    }

    private void initialiseIndexes() {
        int totalIndex = 0;
        for (Reward reward : rewards) {
            totalIndex += (int) (reward.getChance() * 100);
            rewardMap.put(totalIndex, reward);
        }
        this.indexMax = totalIndex;
    }

    public void printRewards() {
        for (Map.Entry<Integer, Reward> entry : rewardMap.entrySet()) {
            getConsoleSender().sendMessage("Index: " + entry.getKey());
            entry.getValue().print();
        }
    }

    public static Reward getRandomReward() {
        int randomIndex = (int) (Math.random() * indexMax);
        return getRewardByIndex(randomIndex);
    }

    private static Reward getRewardByIndex(int index) {
        return rewardMap.entrySet().stream()
                .filter(entry -> index <= entry.getKey())
                .min(Comparator.comparingInt(Map.Entry::getKey))
                .map(Map.Entry::getValue)
                .orElse(null);
    }

    public List<String> getList() {
        List<String> list = new ArrayList<>();
        for (Map.Entry<Integer, Reward> entry : rewardMap.entrySet()) {
            list.add("index: " + entry.getKey() + " " + entry.getValue().getString());
        }
        return list;
    }

    public List<Reward> getRewardList() {
        return rewards;
    }
}
