package fr.lataverne.randomreward.controllers;

import fr.lataverne.randomreward.ConfigManager;
import fr.lataverne.randomreward.api.ApiRequestManager;
import fr.lataverne.randomreward.api.NotificationService;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;

import static fr.lataverne.randomreward.api.NotificationService.sendNotificationVotePost;

public class NotificationController {


    public static void getVote(CommandSender sender, ConfigManager config) throws Exception {
        Map<String, Integer> list =  NotificationService.getAllVotes(config);
    }

    public void sendNotification(String args, ConfigManager config) {
        try {
            sendNotificationVotePost("MonPseudo", config);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param sender Le receveur retour du message
     * @param playerVote Le joueur cible à qui on souhaite connaitre ses votes.
     * @throws Exception
     */
    public static void getCurrentMonthVoteForOnePlayer(CommandSender sender, Player playerVote) throws Exception {
        int nbVote = ApiRequestManager.getCurrentMonthVoteForOnePlayer(playerVote.getUniqueId().toString());
        if(sender instanceof Player player) {
            player.sendMessage("[RR] "+playerVote.getName()+" dispose de "+nbVote);
        }else{
            sender.sendMessage("[RR] "+playerVote.getName()+" dispose de "+nbVote);
        }
    }

    /**
     *
     * @param sender Le receveur retour du message
     * @param playerVote Le joueur cible à qui on souhaite connaitre ses votes.
     * @param date mois souhaité au format YYYYMM
     * @throws Exception
     */
    public static void getSelectedMonthVoteForOnePlayer(CommandSender sender, Player playerVote, String date) throws Exception {
        int nbVote = ApiRequestManager.getSelectedMonthVoteForOnePlayer(playerVote.getUniqueId().toString(),date);
        if(sender instanceof Player player) {
            player.sendMessage("[RR] "+playerVote.getName()+" dispose de "+nbVote);
        }else{
            sender.sendMessage("[RR] "+playerVote.getName()+" dispose de "+nbVote);
        }
    }

}
