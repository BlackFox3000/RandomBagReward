package fr.lataverne.randomreward.api;

import fr.lataverne.randomreward.ConfigManager;
import fr.lataverne.randomreward.RandomReward;
import fr.lataverne.randomreward.commands.CommandManager;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import static fr.lataverne.randomreward.api.RequestPost.config;

public class ApiRequestManager {

    public static ConfigManager config;

    public ApiRequestManager() {
        config = RandomReward.getInstance().getConfigManager();
    }

    public static void getApiConnection() {
        //String url = RandomReward.getConfigManager().getApiUrl();

        //System.out.println(String.format("GET %s.", url));
    }

    public String getBag(String uuid) {
        return "{}"; //this.commandManager.getUrlStorage();
    }

    // ========== Notification and top vote count =============
    /**
     *
     * @param pseudo
     * @throws Exception
     */
    public void getCurrentMonthVoteForOnePlayer(String pseudo) throws Exception {
        // "http://localhost/ApiRandomReward/getVote.php?pseudo="
        RequestPost.sendGet(config.getUrlGetVote() + URLEncoder.encode(pseudo, StandardCharsets.UTF_8));
    }

    public void getCurrentMonthVoteForAll() throws Exception {
        // "http://localhost/ApiRandomReward/getVotes.php"
        RequestPost.sendGet(config.getUrlGetVotes());
    }

    public void getSelectedMonthVoteForOnePlayer(String pseudo, String nbMonth) throws Exception {
        // "http://localhost/ApiRandomReward/getVote.php?pseudo="
        RequestPost.sendGet(config.getUrlGetVote() + URLEncoder.encode(pseudo, StandardCharsets.UTF_8)
        +"/?nbMonth="+URLEncoder.encode(nbMonth, StandardCharsets.UTF_8));
    }

    public void getSelectedMonthVoteForAll(String nbMonth) throws Exception {
        // "http://localhost/ApiRandomReward/getVotes.php?nbMonth="
        RequestPost.sendGet(config.getUrlGetVotes() + URLEncoder.encode(nbMonth, StandardCharsets.UTF_8));
    }
}
