package fr.lataverne.randomreward.api;

import fr.lataverne.randomreward.ConfigManager;
import fr.lataverne.randomreward.RandomReward;
import fr.lataverne.randomreward.models.RewardDB;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static fr.lataverne.randomreward.controllers.PlayerController.getPlayertoPseudo;

public class ApiRequestManager {

    public static ConfigManager config;

    public ApiRequestManager() {
        config = RandomReward.getInstance().getConfigManager();
    }

    public static void getApiConnection() {
        //String url = RandomReward.getConfigManager().getApiUrl();

        //System.out.println(String.format("GET %s.", url));
    }

    /**
     * Obtenir le bag associé à l'uuid
     * @param uuid du joueur cible
     * @return le bag du joueur
     * @throws Exception récupération du sac impossible.
     */
    public static List<RewardDB> getBag(String uuid) throws Exception {
        List<Map<String, Object>> rewardsMap = RewardService.getRewards(uuid, config);
        List<RewardDB> rewardDBList = new ArrayList<>();

        for (Map<String, Object> rewardMap : rewardsMap) {
            //System.out.println(" reward map "+rewardMap.toString());
            RewardDB rewardDB = new RewardDB();
            int id = Integer.parseInt((String) rewardMap.get("id"));
            int count = Integer.parseInt((String) rewardMap.get("count"));

            rewardDB.setId(id);
            rewardDB.setUuid(uuid);
            rewardDB.setPlugin((String) rewardMap.get("plugin"));
            rewardDB.setItem((String) rewardMap.get("item"));
            rewardDB.setCount(count);

            rewardDBList.add(rewardDB);
        }

        return rewardDBList;
    }

    public static RewardDB getReward(int indexItem) throws Exception {
        return RewardService.getReward(indexItem, config);
    }


    // ========== Notification and top vote count =============

    public static void sendNotificationVote(CommandSender sender, String pseudo) throws Exception {
        Player player = getPlayertoPseudo(sender, pseudo);
        if(player!=null) {
            NotificationService.sendNotificationVotePost(player.getUniqueId().toString(), config);
        }
    }

    /**
     * @param uuid Le joueur cible à qui on souhaite connaitre ses votes.
     * @throws Exception
     */
    public static int getCurrentMonthVoteForOnePlayer(String uuid) throws Exception {
        return NotificationService.getVotesForUuid(uuid, config);
    }

    public static Map<String, Integer> getCurrentMonthVoteForAll() throws Exception {
        return NotificationService.getAllVotes(config);
    }

    public static int getSelectedMonthVoteForOnePlayer(String uuid, String date) throws Exception {
        return NotificationService.getVotesForUuid(uuid,date,config);
    }

    public static Map<String, Integer> getSelectedMonthVoteForAll(String date) throws Exception {
        return NotificationService.getAllVotes(date, config);
    }

    public static void deleteReward(int indexItem) throws Exception {
        RewardService.deleteReward(indexItem, config);
    }
}
